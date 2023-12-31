/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.notifications;

import org.mozilla.goanna.GoannaApp;
import org.mozilla.goanna.GoannaAppShell;
import org.mozilla.goanna.GoannaThread;
import org.mozilla.goanna.mozglue.SafeIntent;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 *  Broadcast receiver for Notifications. Will forward them to GoannaApp (and start Goanna) if they're clicked.
 *  If they're being dismissed, it will not start Goanna, but may forward them to JS if Goanna is running.
 *  This is also the only entry point for notification intents.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String LOGTAG = "GoannaNotificationReceiver";

    public void onReceive(Context context, Intent intent) {
        final Uri data = intent.getData();
        if (data == null) {
            Log.e(LOGTAG, "handleNotificationEvent: empty data");
            return;
        }

        final String action = intent.getAction();
        if (NotificationClient.CLICK_ACTION.equals(action) ||
                NotificationClient.CLOSE_ACTION.equals(action)) {
            onNotificationClientAction(context, action, data, intent);
            return;
        }

        final String notificationType = data.getQueryParameter(NotificationHelper.EVENT_TYPE_ATTR);
        if (notificationType == null) {
            return;
        }

        // In case the user swiped out the notification, we empty the id set.
        if (NotificationHelper.CLEARED_EVENT.equals(notificationType)) {
            // If Goanna isn't running, we throw away events where the notification was cancelled.
            // i.e. Don't bug the user if they're just closing a bunch of notifications.
            if (GoannaThread.isRunning()) {
                NotificationHelper.getArgsAndSendNotificationIntent(new SafeIntent(intent));
            }

            final NotificationClient client = (NotificationClient)
                    GoannaAppShell.getNotificationListener();
            client.onNotificationClose(data.getQueryParameter(NotificationHelper.ID_ATTR));
            return;
        }

        forwardMessageToActivity(intent, context);
    }

    private void forwardMessageToActivity(final Intent intent, final Context context) {
        final ComponentName name = intent.getExtras().getParcelable(NotificationHelper.ORIGINAL_EXTRA_COMPONENT);
        intent.setComponent(name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void onNotificationClientAction(final Context context, final String action,
                                            final Uri data, final Intent intent) {
        final String name = data.getQueryParameter("name");
        final String cookie = data.getQueryParameter("cookie");
        final Intent persistentIntent = (Intent)
                intent.getParcelableExtra(NotificationClient.PERSISTENT_INTENT_EXTRA);

        if (persistentIntent != null) {
            // Go through GoannaService for persistent notifications.
            context.startService(persistentIntent);
        }

        if (NotificationClient.CLICK_ACTION.equals(action)) {
            GoannaAppShell.onNotificationClick(name, cookie);

            if (persistentIntent != null) {
                // Don't launch GoannaApp if it's a background persistent notification.
                return;
            }

            final Intent appIntent = new Intent(GoannaApp.ACTION_ALERT_CALLBACK);
            appIntent.setComponent(new ComponentName(
                    data.getAuthority(), data.getPath().substring(1))); // exclude leading slash.
            appIntent.setData(data);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);

        } else if (NotificationClient.CLOSE_ACTION.equals(action)) {
            GoannaAppShell.onNotificationClose(name, cookie);

            final NotificationClient client = (NotificationClient)
                    GoannaAppShell.getNotificationListener();
            client.onNotificationClose(name);
        }
    }
}
