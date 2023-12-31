// GENERATED CODE
// Generated by the Java program at /build/annotationProcessors at compile time
// from annotations on Java methods. To update, change the annotations on the
// corresponding Java methods and rerun the build. Manually updating this file
// will cause your build to fail.

#ifndef GeneratedJNINatives_h
#define GeneratedJNINatives_h

#include "GeneratedJNIWrappers.h"
#include "mozilla/jni/Natives.h"

namespace mozilla {
namespace java {

template<class Impl>
class AlarmReceiver::Natives : public mozilla::jni::NativeImpl<AlarmReceiver, Impl>
{
public:
    static const JNINativeMethod methods[1];
};

template<class Impl>
const JNINativeMethod AlarmReceiver::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<AlarmReceiver::NotifyAlarmFired_t>(
            mozilla::jni::NativeStub<AlarmReceiver::NotifyAlarmFired_t, Impl>
            ::template Wrap<&Impl::NotifyAlarmFired>)
};

template<class Impl>
class AndroidGamepadManager::Natives : public mozilla::jni::NativeImpl<AndroidGamepadManager, Impl>
{
public:
    static const JNINativeMethod methods[3];
};

template<class Impl>
const JNINativeMethod AndroidGamepadManager::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<AndroidGamepadManager::OnAxisChange_t>(
            mozilla::jni::NativeStub<AndroidGamepadManager::OnAxisChange_t, Impl>
            ::template Wrap<&Impl::OnAxisChange>),

    mozilla::jni::MakeNativeMethod<AndroidGamepadManager::OnButtonChange_t>(
            mozilla::jni::NativeStub<AndroidGamepadManager::OnButtonChange_t, Impl>
            ::template Wrap<&Impl::OnButtonChange>),

    mozilla::jni::MakeNativeMethod<AndroidGamepadManager::OnGamepadChange_t>(
            mozilla::jni::NativeStub<AndroidGamepadManager::OnGamepadChange_t, Impl>
            ::template Wrap<&Impl::OnGamepadChange>)
};

template<class Impl>
class EventDispatcher::Natives : public mozilla::jni::NativeImpl<EventDispatcher, Impl>
{
public:
    static const JNINativeMethod methods[3];
};

template<class Impl>
const JNINativeMethod EventDispatcher::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<EventDispatcher::DispatchToGoanna_t>(
            mozilla::jni::NativeStub<EventDispatcher::DispatchToGoanna_t, Impl>
            ::template Wrap<&Impl::DispatchToGoanna>),

    mozilla::jni::MakeNativeMethod<EventDispatcher::DisposeNative_t>(
            mozilla::jni::NativeStub<EventDispatcher::DisposeNative_t, Impl>
            ::template Wrap<&Impl::DisposeNative>),

    mozilla::jni::MakeNativeMethod<EventDispatcher::HasGoannaListener_t>(
            mozilla::jni::NativeStub<EventDispatcher::HasGoannaListener_t, Impl>
            ::template Wrap<&Impl::HasGoannaListener>)
};

template<class Impl>
class EventDispatcher::NativeCallbackDelegate::Natives : public mozilla::jni::NativeImpl<NativeCallbackDelegate, Impl>
{
public:
    static const JNINativeMethod methods[3];
};

template<class Impl>
const JNINativeMethod EventDispatcher::NativeCallbackDelegate::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<EventDispatcher::NativeCallbackDelegate::Finalize_t>(
            mozilla::jni::NativeStub<EventDispatcher::NativeCallbackDelegate::Finalize_t, Impl>
            ::template Wrap<&Impl::Finalize>),

    mozilla::jni::MakeNativeMethod<EventDispatcher::NativeCallbackDelegate::SendError_t>(
            mozilla::jni::NativeStub<EventDispatcher::NativeCallbackDelegate::SendError_t, Impl>
            ::template Wrap<&Impl::SendError>),

    mozilla::jni::MakeNativeMethod<EventDispatcher::NativeCallbackDelegate::SendSuccess_t>(
            mozilla::jni::NativeStub<EventDispatcher::NativeCallbackDelegate::SendSuccess_t, Impl>
            ::template Wrap<&Impl::SendSuccess>)
};

template<class Impl>
class GoannaAppShell::Natives : public mozilla::jni::NativeImpl<GoannaAppShell, Impl>
{
public:
    static const JNINativeMethod methods[9];
};

template<class Impl>
const JNINativeMethod GoannaAppShell::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaAppShell::NotifyObservers_t>(
            mozilla::jni::NativeStub<GoannaAppShell::NotifyObservers_t, Impl>
            ::template Wrap<&Impl::NotifyObservers>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::NotifyPushObservers_t>(
            mozilla::jni::NativeStub<GoannaAppShell::NotifyPushObservers_t, Impl>
            ::template Wrap<&Impl::NotifyPushObservers>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::NotifyAlertListener_t>(
            mozilla::jni::NativeStub<GoannaAppShell::NotifyAlertListener_t, Impl>
            ::template Wrap<&Impl::NotifyAlertListener>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::NotifyUriVisited_t>(
            mozilla::jni::NativeStub<GoannaAppShell::NotifyUriVisited_t, Impl>
            ::template Wrap<&Impl::NotifyUriVisited>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::OnFullScreenPluginHidden_t>(
            mozilla::jni::NativeStub<GoannaAppShell::OnFullScreenPluginHidden_t, Impl>
            ::template Wrap<&Impl::OnFullScreenPluginHidden>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::OnLocationChanged_t>(
            mozilla::jni::NativeStub<GoannaAppShell::OnLocationChanged_t, Impl>
            ::template Wrap<&Impl::OnLocationChanged>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::OnSensorChanged_t>(
            mozilla::jni::NativeStub<GoannaAppShell::OnSensorChanged_t, Impl>
            ::template Wrap<&Impl::OnSensorChanged>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::ReportJavaCrash_t>(
            mozilla::jni::NativeStub<GoannaAppShell::ReportJavaCrash_t, Impl>
            ::template Wrap<&Impl::ReportJavaCrash>),

    mozilla::jni::MakeNativeMethod<GoannaAppShell::SyncNotifyObservers_t>(
            mozilla::jni::NativeStub<GoannaAppShell::SyncNotifyObservers_t, Impl>
            ::template Wrap<&Impl::SyncNotifyObservers>)
};

template<class Impl>
class GoannaAppShell::CameraCallback::Natives : public mozilla::jni::NativeImpl<CameraCallback, Impl>
{
public:
    static const JNINativeMethod methods[1];
};

template<class Impl>
const JNINativeMethod GoannaAppShell::CameraCallback::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaAppShell::CameraCallback::OnFrameData_t>(
            mozilla::jni::NativeStub<GoannaAppShell::CameraCallback::OnFrameData_t, Impl>
            ::template Wrap<&Impl::OnFrameData>)
};

template<class Impl>
class GoannaBatteryManager::Natives : public mozilla::jni::NativeImpl<GoannaBatteryManager, Impl>
{
public:
    static const JNINativeMethod methods[1];
};

template<class Impl>
const JNINativeMethod GoannaBatteryManager::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaBatteryManager::OnBatteryChange_t>(
            mozilla::jni::NativeStub<GoannaBatteryManager::OnBatteryChange_t, Impl>
            ::template Wrap<&Impl::OnBatteryChange>)
};

template<class Impl>
class GoannaEditable::Natives : public mozilla::jni::NativeImpl<GoannaEditable, Impl>
{
public:
    static const JNINativeMethod methods[7];
};

template<class Impl>
const JNINativeMethod GoannaEditable::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaEditable::DisposeNative_t>(
            mozilla::jni::NativeStub<GoannaEditable::DisposeNative_t, Impl>
            ::template Wrap<&Impl::DisposeNative>),

    mozilla::jni::MakeNativeMethod<GoannaEditable::OnImeAddCompositionRange_t>(
            mozilla::jni::NativeStub<GoannaEditable::OnImeAddCompositionRange_t, Impl>
            ::template Wrap<&Impl::OnImeAddCompositionRange>),

    mozilla::jni::MakeNativeMethod<GoannaEditable::OnImeReplaceText_t>(
            mozilla::jni::NativeStub<GoannaEditable::OnImeReplaceText_t, Impl>
            ::template Wrap<&Impl::OnImeReplaceText>),

    mozilla::jni::MakeNativeMethod<GoannaEditable::OnImeRequestCursorUpdates_t>(
            mozilla::jni::NativeStub<GoannaEditable::OnImeRequestCursorUpdates_t, Impl>
            ::template Wrap<&Impl::OnImeRequestCursorUpdates>),

    mozilla::jni::MakeNativeMethod<GoannaEditable::OnImeSynchronize_t>(
            mozilla::jni::NativeStub<GoannaEditable::OnImeSynchronize_t, Impl>
            ::template Wrap<&Impl::OnImeSynchronize>),

    mozilla::jni::MakeNativeMethod<GoannaEditable::OnImeUpdateComposition_t>(
            mozilla::jni::NativeStub<GoannaEditable::OnImeUpdateComposition_t, Impl>
            ::template Wrap<&Impl::OnImeUpdateComposition>),

    mozilla::jni::MakeNativeMethod<GoannaEditable::OnKeyEvent_t>(
            mozilla::jni::NativeStub<GoannaEditable::OnKeyEvent_t, Impl>
            ::template Wrap<&Impl::OnKeyEvent>)
};

template<class Impl>
class GoannaNetworkManager::Natives : public mozilla::jni::NativeImpl<GoannaNetworkManager, Impl>
{
public:
    static const JNINativeMethod methods[2];
};

template<class Impl>
const JNINativeMethod GoannaNetworkManager::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaNetworkManager::OnConnectionChanged_t>(
            mozilla::jni::NativeStub<GoannaNetworkManager::OnConnectionChanged_t, Impl>
            ::template Wrap<&Impl::OnConnectionChanged>),

    mozilla::jni::MakeNativeMethod<GoannaNetworkManager::OnStatusChanged_t>(
            mozilla::jni::NativeStub<GoannaNetworkManager::OnStatusChanged_t, Impl>
            ::template Wrap<&Impl::OnStatusChanged>)
};

template<class Impl>
class GoannaScreenOrientation::Natives : public mozilla::jni::NativeImpl<GoannaScreenOrientation, Impl>
{
public:
    static const JNINativeMethod methods[1];
};

template<class Impl>
const JNINativeMethod GoannaScreenOrientation::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaScreenOrientation::OnOrientationChange_t>(
            mozilla::jni::NativeStub<GoannaScreenOrientation::OnOrientationChange_t, Impl>
            ::template Wrap<&Impl::OnOrientationChange>)
};

template<class Impl>
class GoannaThread::Natives : public mozilla::jni::NativeImpl<GoannaThread, Impl>
{
public:
    static const JNINativeMethod methods[6];
};

template<class Impl>
const JNINativeMethod GoannaThread::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaThread::CreateServices_t>(
            mozilla::jni::NativeStub<GoannaThread::CreateServices_t, Impl>
            ::template Wrap<&Impl::CreateServices>),

    mozilla::jni::MakeNativeMethod<GoannaThread::OnPause_t>(
            mozilla::jni::NativeStub<GoannaThread::OnPause_t, Impl>
            ::template Wrap<&Impl::OnPause>),

    mozilla::jni::MakeNativeMethod<GoannaThread::OnResume_t>(
            mozilla::jni::NativeStub<GoannaThread::OnResume_t, Impl>
            ::template Wrap<&Impl::OnResume>),

    mozilla::jni::MakeNativeMethod<GoannaThread::RunUiThreadCallback_t>(
            mozilla::jni::NativeStub<GoannaThread::RunUiThreadCallback_t, Impl>
            ::template Wrap<&Impl::RunUiThreadCallback>),

    mozilla::jni::MakeNativeMethod<GoannaThread::SpeculativeConnect_t>(
            mozilla::jni::NativeStub<GoannaThread::SpeculativeConnect_t, Impl>
            ::template Wrap<&Impl::SpeculativeConnect>),

    mozilla::jni::MakeNativeMethod<GoannaThread::WaitOnGoanna_t>(
            mozilla::jni::NativeStub<GoannaThread::WaitOnGoanna_t, Impl>
            ::template Wrap<&Impl::WaitOnGoanna>)
};

template<class Impl>
class GoannaView::Window::Natives : public mozilla::jni::NativeImpl<Window, Impl>
{
public:
    static const JNINativeMethod methods[5];
};

template<class Impl>
const JNINativeMethod GoannaView::Window::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<GoannaView::Window::Close_t>(
            mozilla::jni::NativeStub<GoannaView::Window::Close_t, Impl>
            ::template Wrap<&Impl::Close>),

    mozilla::jni::MakeNativeMethod<GoannaView::Window::DisposeNative_t>(
            mozilla::jni::NativeStub<GoannaView::Window::DisposeNative_t, Impl>
            ::template Wrap<&Impl::DisposeNative>),

    mozilla::jni::MakeNativeMethod<GoannaView::Window::LoadUri_t>(
            mozilla::jni::NativeStub<GoannaView::Window::LoadUri_t, Impl>
            ::template Wrap<&Impl::LoadUri>),

    mozilla::jni::MakeNativeMethod<GoannaView::Window::Open_t>(
            mozilla::jni::NativeStub<GoannaView::Window::Open_t, Impl>
            ::template Wrap<&Impl::Open>),

    mozilla::jni::MakeNativeMethod<GoannaView::Window::Reattach_t>(
            mozilla::jni::NativeStub<GoannaView::Window::Reattach_t, Impl>
            ::template Wrap<&Impl::Reattach>)
};

template<class Impl>
class PrefsHelper::Natives : public mozilla::jni::NativeImpl<PrefsHelper, Impl>
{
public:
    static const JNINativeMethod methods[4];
};

template<class Impl>
const JNINativeMethod PrefsHelper::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<PrefsHelper::AddObserver_t>(
            mozilla::jni::NativeStub<PrefsHelper::AddObserver_t, Impl>
            ::template Wrap<&Impl::AddObserver>),

    mozilla::jni::MakeNativeMethod<PrefsHelper::GetPrefs_t>(
            mozilla::jni::NativeStub<PrefsHelper::GetPrefs_t, Impl>
            ::template Wrap<&Impl::GetPrefs>),

    mozilla::jni::MakeNativeMethod<PrefsHelper::RemoveObserver_t>(
            mozilla::jni::NativeStub<PrefsHelper::RemoveObserver_t, Impl>
            ::template Wrap<&Impl::RemoveObserver>),

    mozilla::jni::MakeNativeMethod<PrefsHelper::SetPref_t>(
            mozilla::jni::NativeStub<PrefsHelper::SetPref_t, Impl>
            ::template Wrap<&Impl::SetPref>)
};

template<class Impl>
class ScreenManagerHelper::Natives : public mozilla::jni::NativeImpl<ScreenManagerHelper, Impl>
{
public:
    static const JNINativeMethod methods[2];
};

template<class Impl>
const JNINativeMethod ScreenManagerHelper::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<ScreenManagerHelper::AddDisplay_t>(
            mozilla::jni::NativeStub<ScreenManagerHelper::AddDisplay_t, Impl>
            ::template Wrap<&Impl::AddDisplay>),

    mozilla::jni::MakeNativeMethod<ScreenManagerHelper::RemoveDisplay_t>(
            mozilla::jni::NativeStub<ScreenManagerHelper::RemoveDisplay_t, Impl>
            ::template Wrap<&Impl::RemoveDisplay>)
};

template<class Impl>
class SurfaceTextureListener::Natives : public mozilla::jni::NativeImpl<SurfaceTextureListener, Impl>
{
public:
    static const JNINativeMethod methods[1];
};

template<class Impl>
const JNINativeMethod SurfaceTextureListener::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<SurfaceTextureListener::OnFrameAvailable_t>(
            mozilla::jni::NativeStub<SurfaceTextureListener::OnFrameAvailable_t, Impl>
            ::template Wrap<&Impl::OnFrameAvailable>)
};

template<class Impl>
class LayerView::Compositor::Natives : public mozilla::jni::NativeImpl<Compositor, Impl>
{
public:
    static const JNINativeMethod methods[7];
};

template<class Impl>
const JNINativeMethod LayerView::Compositor::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<LayerView::Compositor::AttachToJava_t>(
            mozilla::jni::NativeStub<LayerView::Compositor::AttachToJava_t, Impl>
            ::template Wrap<&Impl::AttachToJava>),

    mozilla::jni::MakeNativeMethod<LayerView::Compositor::CreateCompositor_t>(
            mozilla::jni::NativeStub<LayerView::Compositor::CreateCompositor_t, Impl>
            ::template Wrap<&Impl::CreateCompositor>),

    mozilla::jni::MakeNativeMethod<LayerView::Compositor::DisposeNative_t>(
            mozilla::jni::NativeStub<LayerView::Compositor::DisposeNative_t, Impl>
            ::template Wrap<&Impl::DisposeNative>),

    mozilla::jni::MakeNativeMethod<LayerView::Compositor::OnSizeChanged_t>(
            mozilla::jni::NativeStub<LayerView::Compositor::OnSizeChanged_t, Impl>
            ::template Wrap<&Impl::OnSizeChanged>),

    mozilla::jni::MakeNativeMethod<LayerView::Compositor::SyncInvalidateAndScheduleComposite_t>(
            mozilla::jni::NativeStub<LayerView::Compositor::SyncInvalidateAndScheduleComposite_t, Impl>
            ::template Wrap<&Impl::SyncInvalidateAndScheduleComposite>),

    mozilla::jni::MakeNativeMethod<LayerView::Compositor::SyncPauseCompositor_t>(
            mozilla::jni::NativeStub<LayerView::Compositor::SyncPauseCompositor_t, Impl>
            ::template Wrap<&Impl::SyncPauseCompositor>),

    mozilla::jni::MakeNativeMethod<LayerView::Compositor::SyncResumeResizeCompositor_t>(
            mozilla::jni::NativeStub<LayerView::Compositor::SyncResumeResizeCompositor_t, Impl>
            ::template Wrap<&Impl::SyncResumeResizeCompositor>)
};

template<class Impl>
class NativePanZoomController::Natives : public mozilla::jni::NativeImpl<NativePanZoomController, Impl>
{
public:
    static const JNINativeMethod methods[7];
};

template<class Impl>
const JNINativeMethod NativePanZoomController::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<NativePanZoomController::AdjustScrollForSurfaceShift_t>(
            mozilla::jni::NativeStub<NativePanZoomController::AdjustScrollForSurfaceShift_t, Impl>
            ::template Wrap<&Impl::AdjustScrollForSurfaceShift>),

    mozilla::jni::MakeNativeMethod<NativePanZoomController::DisposeNative_t>(
            mozilla::jni::NativeStub<NativePanZoomController::DisposeNative_t, Impl>
            ::template Wrap<&Impl::DisposeNative>),

    mozilla::jni::MakeNativeMethod<NativePanZoomController::HandleMotionEvent_t>(
            mozilla::jni::NativeStub<NativePanZoomController::HandleMotionEvent_t, Impl>
            ::template Wrap<&Impl::HandleMotionEvent>),

    mozilla::jni::MakeNativeMethod<NativePanZoomController::HandleMotionEventVelocity_t>(
            mozilla::jni::NativeStub<NativePanZoomController::HandleMotionEventVelocity_t, Impl>
            ::template Wrap<&Impl::HandleMotionEventVelocity>),

    mozilla::jni::MakeNativeMethod<NativePanZoomController::HandleMouseEvent_t>(
            mozilla::jni::NativeStub<NativePanZoomController::HandleMouseEvent_t, Impl>
            ::template Wrap<&Impl::HandleMouseEvent>),

    mozilla::jni::MakeNativeMethod<NativePanZoomController::HandleScrollEvent_t>(
            mozilla::jni::NativeStub<NativePanZoomController::HandleScrollEvent_t, Impl>
            ::template Wrap<&Impl::HandleScrollEvent>),

    mozilla::jni::MakeNativeMethod<NativePanZoomController::SetIsLongpressEnabled_t>(
            mozilla::jni::NativeStub<NativePanZoomController::SetIsLongpressEnabled_t, Impl>
            ::template Wrap<&Impl::SetIsLongpressEnabled>)
};

template<class Impl>
class VsyncSource::Natives : public mozilla::jni::NativeImpl<VsyncSource, Impl>
{
public:
    static const JNINativeMethod methods[1];
};

template<class Impl>
const JNINativeMethod VsyncSource::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<VsyncSource::NotifyVsync_t>(
            mozilla::jni::NativeStub<VsyncSource::NotifyVsync_t, Impl>
            ::template Wrap<&Impl::NotifyVsync>)
};

template<class Impl>
class NativeJSContainer::Natives : public mozilla::jni::NativeImpl<NativeJSContainer, Impl>
{
public:
    static const JNINativeMethod methods[2];
};

template<class Impl>
const JNINativeMethod NativeJSContainer::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<NativeJSContainer::Clone2_t>(
            mozilla::jni::NativeStub<NativeJSContainer::Clone2_t, Impl>
            ::template Wrap<&Impl::Clone>),

    mozilla::jni::MakeNativeMethod<NativeJSContainer::DisposeNative_t>(
            mozilla::jni::NativeStub<NativeJSContainer::DisposeNative_t, Impl>
            ::template Wrap<&Impl::DisposeNative>)
};

template<class Impl>
class NativeJSObject::Natives : public mozilla::jni::NativeImpl<NativeJSObject, Impl>
{
public:
    static const JNINativeMethod methods[27];
};

template<class Impl>
const JNINativeMethod NativeJSObject::Natives<Impl>::methods[] = {

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetBoolean_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetBoolean_t, Impl>
            ::template Wrap<&Impl::GetBoolean>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetBooleanArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetBooleanArray_t, Impl>
            ::template Wrap<&Impl::GetBooleanArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetBundle_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetBundle_t, Impl>
            ::template Wrap<&Impl::GetBundle>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetBundleArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetBundleArray_t, Impl>
            ::template Wrap<&Impl::GetBundleArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetDouble_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetDouble_t, Impl>
            ::template Wrap<&Impl::GetDouble>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetDoubleArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetDoubleArray_t, Impl>
            ::template Wrap<&Impl::GetDoubleArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetInt_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetInt_t, Impl>
            ::template Wrap<&Impl::GetInt>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetIntArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetIntArray_t, Impl>
            ::template Wrap<&Impl::GetIntArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetObject_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetObject_t, Impl>
            ::template Wrap<&Impl::GetObject>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetObjectArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetObjectArray_t, Impl>
            ::template Wrap<&Impl::GetObjectArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetString_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetString_t, Impl>
            ::template Wrap<&Impl::GetString>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::GetStringArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::GetStringArray_t, Impl>
            ::template Wrap<&Impl::GetStringArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::Has_t>(
            mozilla::jni::NativeStub<NativeJSObject::Has_t, Impl>
            ::template Wrap<&Impl::Has>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptBoolean_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptBoolean_t, Impl>
            ::template Wrap<&Impl::OptBoolean>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptBooleanArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptBooleanArray_t, Impl>
            ::template Wrap<&Impl::OptBooleanArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptBundle_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptBundle_t, Impl>
            ::template Wrap<&Impl::OptBundle>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptBundleArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptBundleArray_t, Impl>
            ::template Wrap<&Impl::OptBundleArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptDouble_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptDouble_t, Impl>
            ::template Wrap<&Impl::OptDouble>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptDoubleArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptDoubleArray_t, Impl>
            ::template Wrap<&Impl::OptDoubleArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptInt_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptInt_t, Impl>
            ::template Wrap<&Impl::OptInt>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptIntArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptIntArray_t, Impl>
            ::template Wrap<&Impl::OptIntArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptObject_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptObject_t, Impl>
            ::template Wrap<&Impl::OptObject>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptObjectArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptObjectArray_t, Impl>
            ::template Wrap<&Impl::OptObjectArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptString_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptString_t, Impl>
            ::template Wrap<&Impl::OptString>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::OptStringArray_t>(
            mozilla::jni::NativeStub<NativeJSObject::OptStringArray_t, Impl>
            ::template Wrap<&Impl::OptStringArray>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::ToBundle_t>(
            mozilla::jni::NativeStub<NativeJSObject::ToBundle_t, Impl>
            ::template Wrap<&Impl::ToBundle>),

    mozilla::jni::MakeNativeMethod<NativeJSObject::ToString_t>(
            mozilla::jni::NativeStub<NativeJSObject::ToString_t, Impl>
            ::template Wrap<&Impl::ToString>)
};

} /* java */
} /* mozilla */
#endif // GeneratedJNINatives_h
