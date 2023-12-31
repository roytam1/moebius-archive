/* -*- Mode: Java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.customtabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsService;
import android.support.customtabs.CustomTabsSessionToken;
import android.util.Log;

import org.mozilla.goanna.GoannaProfile;
import org.mozilla.goanna.GoannaService;
import org.mozilla.goanna.GoannaThread;

import java.util.List;

/**
 * Custom tabs service external, third-party apps connect to.
 */
public class GoannaCustomTabsService extends CustomTabsService {
    private static final String LOGTAG = "GoannaCustomTabsService";
    private static final boolean DEBUG = false;
    private static final int MAX_SPECULATIVE_URLS = 50;

    @Override
    protected boolean updateVisuals(CustomTabsSessionToken sessionToken, Bundle bundle) {
        Log.v(LOGTAG, "updateVisuals()");

        return false;
    }

    @Override
    protected boolean warmup(long flags) {
        if (DEBUG) {
            Log.v(LOGTAG, "warming up...");
        }

        GoannaService.startGoanna(GoannaProfile.initFromArgs(this, null), null, getApplicationContext());

        return true;
    }

    @Override
    protected boolean newSession(CustomTabsSessionToken sessionToken) {
        Log.v(LOGTAG, "newSession()");

        // Pretend session has been started
        return true;
    }

    @Override
    protected boolean mayLaunchUrl(CustomTabsSessionToken sessionToken, Uri uri, Bundle bundle, List<Bundle> list) {
        if (DEBUG) {
            Log.v(LOGTAG, "opening speculative connections...");
        }

        if (uri == null) {
            return false;
        }

        GoannaThread.speculativeConnect(uri.toString());

        if (list == null) {
            return true;
        }

        for (int i = 0; i < list.size() && i < MAX_SPECULATIVE_URLS; i++) {
            Bundle listItem = list.get(i);
            if (listItem == null) {
                continue;
            }

            Uri listUri = listItem.getParcelable(KEY_URL);
            if (listUri == null) {
                continue;
            }

            GoannaThread.speculativeConnect(listUri.toString());
        }

        return true;
    }

    @Override
    protected Bundle extraCommand(String commandName, Bundle bundle) {
        Log.v(LOGTAG, "extraCommand()");

        return null;
    }
}
