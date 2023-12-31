/* -*- Mode: c++; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

#ifndef GoannaBatteryManager_h
#define GoannaBatteryManager_h

#include "GeneratedJNINatives.h"
#include "nsAppShell.h"

#include "mozilla/Hal.h"

namespace mozilla {

class GoannaBatteryManager final
    : public java::GoannaBatteryManager::Natives<GoannaBatteryManager>
{
public:
    static void
    OnBatteryChange(double aLevel, bool aCharging, double aRemainingTime)
    {
        hal::NotifyBatteryChange(
                hal::BatteryInformation(aLevel, aCharging, aRemainingTime));
    }
};

} // namespace mozilla

#endif // GoannaBatteryManager_h
