/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import org.mozilla.goanna.util.ActivityResultHandler;
import org.mozilla.goanna.util.EventCallback;
import org.mozilla.goanna.util.InputOptionsUtils;

/**
 * Supports the DevTools WiFi debugging authentication flow by invoking a QR decoder.
 */
public class DevToolsAuthHelper {

    private static final String LOGTAG = "GoannaDevToolsAuthHelper";

    public static void scan(Context context, final EventCallback callback) {
        final Intent intent = InputOptionsUtils.createQRCodeReaderIntent();

        intent.putExtra("PROMPT_MESSAGE", context.getString(R.string.devtools_auth_scan_header));

        // Check ahead of time if an activity exists for the intent.  This
        // avoids a case where we get both an ActivityNotFoundException *and*
        // an activity result when the activity is missing.
        PackageManager pm = context.getPackageManager();
        if (pm.resolveActivity(intent, 0) == null) {
            Log.w(LOGTAG, "PackageManager can't resolve the activity.");
            callback.sendError("PackageManager can't resolve the activity.");
            return;
        }

        ActivityHandlerHelper.startIntent(intent, new ActivityResultHandler() {
            @Override
            public void onActivityResult(int resultCode, Intent intent) {
                if (resultCode == Activity.RESULT_OK) {
                    String text = intent.getStringExtra("SCAN_RESULT");
                    callback.sendSuccess(text);
                } else {
                    callback.sendError(resultCode);
                }
            }
        });
    }

}
