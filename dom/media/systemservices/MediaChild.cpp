/* -*- Mode: C++; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* vim: set sw=2 ts=8 et ft=cpp : */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

#include "MediaChild.h"
#include "MediaParent.h"

#include "nsGlobalWindow.h"
#include "mozilla/MediaManager.h"
#include "mozilla/Logging.h"
#include "nsQueryObject.h"

#undef LOG
mozilla::LazyLogModule gMediaChildLog("MediaChild");
#define LOG(args) MOZ_LOG(gMediaChildLog, mozilla::LogLevel::Debug, args)

namespace mozilla {
namespace media {

already_AddRefed<Pledge<nsCString>>
GetPrincipalKey(const ipc::PrincipalInfo& aPrincipalInfo, bool aPersist)
{
  RefPtr<MediaManager> mgr = MediaManager::GetInstance();
  MOZ_ASSERT(mgr);

  RefPtr<Pledge<nsCString>> p = new Pledge<nsCString>();
  uint32_t id = mgr->mGetPrincipalKeyPledges.Append(*p);

  if (XRE_GetProcessType() == GoannaProcessType_Default) {
    mgr->GetNonE10sParent()->RecvGetPrincipalKey(id, aPrincipalInfo, aPersist);
  } else {
    Child::Get()->SendGetPrincipalKey(id, aPrincipalInfo, aPersist);
  }
  return p.forget();
}

void
SanitizeOriginKeys(const uint64_t& aSinceWhen, bool aOnlyPrivateBrowsing)
{
  LOG(("SanitizeOriginKeys since %llu %s", aSinceWhen,
       (aOnlyPrivateBrowsing? "in Private Browsing." : ".")));

  if (XRE_GetProcessType() == GoannaProcessType_Default) {
    // Avoid opening MediaManager in this case, since this is called by
    // sanitize.js when cookies are cleared, which can happen on startup.
    RefPtr<Parent<NonE10s>> tmpParent = new Parent<NonE10s>();
    tmpParent->RecvSanitizeOriginKeys(aSinceWhen, aOnlyPrivateBrowsing);
  } else {
    Child::Get()->SendSanitizeOriginKeys(aSinceWhen, aOnlyPrivateBrowsing);
  }
}

static Child* sChild;

Child* Child::Get()
{
  MOZ_ASSERT(XRE_GetProcessType() == GoannaProcessType_Content);
  MOZ_ASSERT(NS_IsMainThread());
  if (!sChild) {
    sChild = static_cast<Child*>(dom::ContentChild::GetSingleton()->SendPMediaConstructor());
  }
  return sChild;
}

Child::Child()
  : mActorDestroyed(false)
{
  LOG(("media::Child: %p", this));
  MOZ_COUNT_CTOR(Child);
}

Child::~Child()
{
  LOG(("~media::Child: %p", this));
  sChild = nullptr;
  MOZ_COUNT_DTOR(Child);
}

void Child::ActorDestroy(ActorDestroyReason aWhy)
{
  mActorDestroyed = true;
}

mozilla::ipc::IPCResult
Child::RecvGetPrincipalKeyResponse(const uint32_t& aRequestId,
                                   const nsCString& aKey)
{
  RefPtr<MediaManager> mgr = MediaManager::GetInstance();
  if (!mgr) {
    return IPC_FAIL_NO_REASON(this);
  }
  RefPtr<Pledge<nsCString>> pledge =
    mgr->mGetPrincipalKeyPledges.Remove(aRequestId);
  if (pledge) {
    pledge->Resolve(aKey);
  }
  return IPC_OK();
}

PMediaChild*
AllocPMediaChild()
{
  return new Child();
}

bool
DeallocPMediaChild(media::PMediaChild *aActor)
{
  delete static_cast<Child*>(aActor);
  return true;
}

} // namespace media
} // namespace mozilla
