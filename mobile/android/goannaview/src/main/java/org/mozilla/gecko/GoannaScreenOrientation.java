/* -*- Mode: Java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import org.mozilla.goanna.annotation.WrapForJNI;

import java.util.Arrays;
import java.util.List;

/*
 * Updates, locks and unlocks the screen orientation.
 *
 * Note: Replaces the OnOrientationChangeListener to avoid redundant rotation
 * event handling.
 */
public class GoannaScreenOrientation {
    private static final String LOGTAG = "GoannaScreenOrientation";

    // Make sure that any change in dom/base/ScreenOrientation.h happens here too.
    public enum ScreenOrientation {
        NONE(0),
        PORTRAIT_PRIMARY(1 << 0),
        PORTRAIT_SECONDARY(1 << 1),
        PORTRAIT(PORTRAIT_PRIMARY.value | PORTRAIT_SECONDARY.value),
        LANDSCAPE_PRIMARY(1 << 2),
        LANDSCAPE_SECONDARY(1 << 3),
        LANDSCAPE(LANDSCAPE_PRIMARY.value | LANDSCAPE_SECONDARY.value),
        DEFAULT(1 << 4);

        public final short value;

        private ScreenOrientation(int value) {
            this.value = (short)value;
        }

        private final static ScreenOrientation[] sValues = ScreenOrientation.values();

        public static ScreenOrientation get(int value) {
            for (ScreenOrientation orient: sValues) {
                if (orient.value == value) {
                    return orient;
                }
            }
            return NONE;
        }
    }

    // Singleton instance.
    private static GoannaScreenOrientation sInstance;
    // Default rotation, used when device rotation is unknown.
    private static final int DEFAULT_ROTATION = Surface.ROTATION_0;
    // Last updated screen orientation.
    private ScreenOrientation mScreenOrientation;
    // Whether the update should notify Goanna about screen orientation changes.
    private boolean mShouldNotify = true;

    public static GoannaScreenOrientation getInstance() {
        if (sInstance == null) {
            sInstance = new GoannaScreenOrientation();
        }
        return sInstance;
    }

    /*
     * Enable Goanna screen orientation events on update.
     */
    public void enableNotifications() {
        update();
        mShouldNotify = true;
    }

    /*
     * Disable Goanna screen orientation events on update.
     */
    public void disableNotifications() {
        mShouldNotify = false;
    }

    /*
     * Update screen orientation.
     * Retrieve orientation and rotation via GoannaAppShell.
     *
     * @return Whether the screen orientation has changed.
     */
    public boolean update() {
        final Context appContext = GoannaAppShell.getApplicationContext();
        if (appContext == null) {
            return false;
        }
        Configuration config = appContext.getResources().getConfiguration();
        return update(config.orientation);
    }

    /*
     * Update screen orientation given the android orientation.
     * Retrieve rotation via GoannaAppShell.
     *
     * @param aAndroidOrientation
     *        Android screen orientation from Configuration.orientation.
     *
     * @return Whether the screen orientation has changed.
     */
    public boolean update(int aAndroidOrientation) {
        return update(getScreenOrientation(aAndroidOrientation, getRotation()));
    }

    @WrapForJNI(dispatchTo = "goanna")
    private static native void onOrientationChange(short screenOrientation, short angle);

    /*
     * Update screen orientation given the screen orientation.
     *
     * @param aScreenOrientation
     *        Goanna screen orientation based on android orientation and rotation.
     *
     * @return Whether the screen orientation has changed.
     */
    public boolean update(ScreenOrientation aScreenOrientation) {
        if (mScreenOrientation == aScreenOrientation) {
            return false;
        }
        mScreenOrientation = aScreenOrientation;
        Log.d(LOGTAG, "updating to new orientation " + mScreenOrientation);
        if (mShouldNotify) {
            // Goanna expects a definite screen orientation, so we default to the
            // primary orientations.
            if (aScreenOrientation == ScreenOrientation.PORTRAIT) {
                aScreenOrientation = ScreenOrientation.PORTRAIT_PRIMARY;
            } else if (aScreenOrientation == ScreenOrientation.LANDSCAPE) {
                aScreenOrientation = ScreenOrientation.LANDSCAPE_PRIMARY;
            }

            if (GoannaThread.isRunning()) {
                onOrientationChange(aScreenOrientation.value, getAngle());
            } else {
                GoannaThread.queueNativeCall(GoannaScreenOrientation.class, "onOrientationChange",
                                            aScreenOrientation.value, getAngle());
            }
        }
        GoannaAppShell.resetScreenSize();
        return true;
    }

    /*
     * @return The Android orientation (Configuration.orientation).
     */
    public int getAndroidOrientation() {
        return screenOrientationToAndroidOrientation(getScreenOrientation());
    }

    /*
     * @return The Goanna screen orientation derived from Android orientation and
     *         rotation.
     */
    public ScreenOrientation getScreenOrientation() {
        return mScreenOrientation;
    }

    /**
     * Lock screen orientation given the Goanna screen orientation.
     *
     * @param aGoannaOrientation
     *        The Goanna orientation provided.
     */
    public void lock(int aGoannaOrientation) {
        lock(ScreenOrientation.get(aGoannaOrientation));
    }

    /**
     * Lock screen orientation given the Goanna screen orientation.
     *
     * @param aScreenOrientation
     *        Goanna screen orientation derived from Android orientation and
     *        rotation.
     *
     * @return Whether the locking was successful.
     */
    public boolean lock(ScreenOrientation aScreenOrientation) {
        Log.d(LOGTAG, "locking to " + aScreenOrientation);
        final ScreenOrientationDelegate delegate = GoannaAppShell.getScreenOrientationDelegate();
        final int activityInfoOrientation = screenOrientationToActivityInfoOrientation(aScreenOrientation);
        if (delegate.setRequestedOrientationForCurrentActivity(activityInfoOrientation)) {
            update(aScreenOrientation);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Unlock and update screen orientation.
     *
     * @return Whether the unlocking was successful.
     */
    public boolean unlock() {
        Log.d(LOGTAG, "unlocking");
        final ScreenOrientationDelegate delegate = GoannaAppShell.getScreenOrientationDelegate();
        final int activityInfoOrientation = screenOrientationToActivityInfoOrientation(ScreenOrientation.DEFAULT);
        if (delegate.setRequestedOrientationForCurrentActivity(activityInfoOrientation)) {
            update();
            return true;
        } else {
            return false;
        }
    }

    /*
     * Combine the Android orientation and rotation to the Goanna orientation.
     *
     * @param aAndroidOrientation
     *        Android orientation from Configuration.orientation.
     * @param aRotation
     *        Device rotation from Display.getRotation().
     *
     * @return Goanna screen orientation.
     */
    private ScreenOrientation getScreenOrientation(int aAndroidOrientation, int aRotation) {
        boolean isPrimary = aRotation == Surface.ROTATION_0 || aRotation == Surface.ROTATION_90;
        if (aAndroidOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isPrimary) {
                // Non-rotated portrait device or landscape device rotated
                // to primary portrait mode counter-clockwise.
                return ScreenOrientation.PORTRAIT_PRIMARY;
            }
            return ScreenOrientation.PORTRAIT_SECONDARY;
        }
        if (aAndroidOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (isPrimary) {
                // Non-rotated landscape device or portrait device rotated
                // to primary landscape mode counter-clockwise.
                return ScreenOrientation.LANDSCAPE_PRIMARY;
            }
            return ScreenOrientation.LANDSCAPE_SECONDARY;
        }
        return ScreenOrientation.NONE;
    }

    /*
     * @return Device rotation converted to an angle.
     */
    public short getAngle() {
        switch (getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                Log.w(LOGTAG, "getAngle: unexpected rotation value");
                return 0;
        }
    }

    /*
     * @return Device rotation.
     */
    private int getRotation() {
        final Context appContext = GoannaAppShell.getApplicationContext();
        if (appContext == null) {
            return DEFAULT_ROTATION;
        }
        final WindowManager windowManager =
                (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getRotation();
    }

    /*
     * Retrieve the screen orientation from an array string.
     *
     * @param aArray
     *        String containing comma-delimited strings.
     *
     * @return First parsed Goanna screen orientation.
     */
    public static ScreenOrientation screenOrientationFromArrayString(String aArray) {
        List<String> orientations = Arrays.asList(aArray.split(","));
        if ("".equals(aArray) || orientations.size() == 0) {
            // If nothing is listed, return default.
            Log.w(LOGTAG, "screenOrientationFromArrayString: no orientation in string");
            return ScreenOrientation.DEFAULT;
        }

        // We don't support multiple orientations yet. To avoid developer
        // confusion, just take the first one listed.
        return screenOrientationFromString(orientations.get(0));
    }

    /*
     * Retrieve the screen orientation from a string.
     *
     * @param aStr
     *        String hopefully containing a screen orientation name.
     * @return Goanna screen orientation if matched, DEFAULT_SCREEN_ORIENTATION
     *         otherwise.
     */
    public static ScreenOrientation screenOrientationFromString(String aStr) {
        switch (aStr) {
            case "portrait":
                return ScreenOrientation.PORTRAIT;
            case "landscape":
                return ScreenOrientation.LANDSCAPE;
            case "portrait-primary":
                return ScreenOrientation.PORTRAIT_PRIMARY;
            case "portrait-secondary":
                return ScreenOrientation.PORTRAIT_SECONDARY;
            case "landscape-primary":
                return ScreenOrientation.LANDSCAPE_PRIMARY;
            case "landscape-secondary":
                return ScreenOrientation.LANDSCAPE_SECONDARY;
        }

        Log.w(LOGTAG, "screenOrientationFromString: unknown orientation string: " + aStr);
        return ScreenOrientation.DEFAULT;
    }

    /*
     * Convert Goanna screen orientation to Android orientation.
     *
     * @param aScreenOrientation
     *        Goanna screen orientation.
     * @return Android orientation. This conversion is lossy, the Android
     *         orientation does not differentiate between primary and secondary
     *         orientations.
     */
    public static int screenOrientationToAndroidOrientation(ScreenOrientation aScreenOrientation) {
        switch (aScreenOrientation) {
            case PORTRAIT:
            case PORTRAIT_PRIMARY:
            case PORTRAIT_SECONDARY:
                return Configuration.ORIENTATION_PORTRAIT;
            case LANDSCAPE:
            case LANDSCAPE_PRIMARY:
            case LANDSCAPE_SECONDARY:
                return Configuration.ORIENTATION_LANDSCAPE;
            case NONE:
            case DEFAULT:
            default:
                return Configuration.ORIENTATION_UNDEFINED;
        }
    }

    /*
     * Convert Goanna screen orientation to Android ActivityInfo orientation.
     * This is yet another orientation used by Android, but it's more detailed
     * than the Android orientation.
     * It is required for screen orientation locking and unlocking.
     *
     * @param aScreenOrientation
     *        Goanna screen orientation.
     * @return Android ActivityInfo orientation.
     */
    public static int screenOrientationToActivityInfoOrientation(ScreenOrientation aScreenOrientation) {
        switch (aScreenOrientation) {
            case PORTRAIT:
            case PORTRAIT_PRIMARY:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            case PORTRAIT_SECONDARY:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            case LANDSCAPE:
            case LANDSCAPE_PRIMARY:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            case LANDSCAPE_SECONDARY:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            case DEFAULT:
            case NONE:
                return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            default:
                return ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;
        }
    }
}
