/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.mozilla.goanna.mozglue.GoannaLoader;

/**
 * This broadcast receiver receives ACTION_MY_PACKAGE_REPLACED broadcasts and
 * starts procedures that should run after the APK has been updated.
 */
public class PackageReplacedReceiver extends BroadcastReceiver {
    public static final String ACTION_MY_PACKAGE_REPLACED = "android.intent.action.MY_PACKAGE_REPLACED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
            // This is not the broadcast we are looking for.
            return;
        }

        // Extract Goanna libs to allow them to be loaded from cache on startup.
        extractGoannaLibs(context);
    }

    private static void extractGoannaLibs(final Context context) {
        final String resourcePath = context.getPackageResourcePath();
        GoannaLoader.loadMozGlue(context);
        GoannaLoader.extractGoannaLibs(context, resourcePath);
    }
}
