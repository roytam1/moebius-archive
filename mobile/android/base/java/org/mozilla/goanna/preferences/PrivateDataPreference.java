/* -*- Mode: Java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.preferences;

import org.mozilla.goanna.GoannaAppShell;
import org.mozilla.goanna.Telemetry;
import org.mozilla.goanna.TelemetryContract;

import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.goanna.icons.storage.DiskStorage;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

class PrivateDataPreference extends MultiPrefMultiChoicePreference {
    private static final String LOGTAG = "GoannaPrivateDataPreference";
    private static final String PREF_KEY_PREFIX = "private.data.";

    public PrivateDataPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) {
            return;
        }

        Telemetry.sendUIEvent(TelemetryContract.Event.SANITIZE, TelemetryContract.Method.DIALOG, "settings");

        final Set<String> values = getValues();
        final JSONObject json = new JSONObject();

        for (String value : values) {
            // Privacy pref checkbox values are stored in Android prefs to
            // remember their check states. The key names are private.data.X,
            // where X is a string from Goanna sanitization. This prefix is
            // removed here so we can send the values to Goanna, which then does
            // the sanitization for each key.
            final String key = value.substring(PREF_KEY_PREFIX.length());
            try {
                json.put(key, true);
            } catch (JSONException e) {
                Log.e(LOGTAG, "JSON error", e);
            }
        }

        if (values.contains("private.data.offlineApps")) {
            // Remove all icons from storage if removing "Offline website data" was selected.
            DiskStorage.get(getContext()).evictAll();
        }

        // clear private data in goanna
        GoannaAppShell.notifyObservers("Sanitize:ClearData", json.toString());
    }
}
