/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.goanna.AboutPages;
import org.mozilla.goanna.AppConstants;
import org.mozilla.goanna.EventDispatcher;
import org.mozilla.goanna.R;
import org.mozilla.goanna.GoannaAppShell;
import org.mozilla.goanna.SiteIdentity;
import org.mozilla.goanna.SiteIdentity.SecurityMode;
import org.mozilla.goanna.SiteIdentity.MixedMode;
import org.mozilla.goanna.SiteIdentity.TrackingMode;
import org.mozilla.goanna.SnackbarBuilder;
import org.mozilla.goanna.Tab;
import org.mozilla.goanna.Tabs;
import org.mozilla.goanna.util.GoannaBundle;
import org.mozilla.goanna.util.GoannaEventListener;
import org.mozilla.goanna.util.ThreadUtils;
import org.mozilla.goanna.widget.AnchoredPopup;
import org.mozilla.goanna.widget.DoorHanger;
import org.mozilla.goanna.widget.DoorHanger.OnButtonClickListener;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.mozilla.goanna.widget.DoorhangerConfig;
import org.mozilla.goanna.widget.SiteLogins;

/**
 * SiteIdentityPopup is a singleton class that displays site identity data in
 * an arrow panel popup hanging from the lock icon in the browser toolbar.
 *
 * A site identity icon may be displayed in the url, and is set in <code>ToolbarDisplayLayout</code>.
 */
public class SiteIdentityPopup extends AnchoredPopup implements GoannaEventListener {

    public static enum ButtonType { DISABLE, ENABLE, KEEP_BLOCKING, CANCEL, COPY }

    private static final String LOGTAG = "GoannaSiteIdentityPopup";

    private static final String MIXED_CONTENT_SUPPORT_URL =
        "https://support.mozilla.org/kb/how-does-insecure-content-affect-safety-android";
    private static final String TRACKING_CONTENT_SUPPORT_URL =
        "https://support.mozilla.org/kb/firefox-android-tracking-protection";

    // Placeholder string.
    private final static String FORMAT_S = "%s";

    private final Resources mResources;
    private SiteIdentity mSiteIdentity;

    private LinearLayout mIdentity;

    private LinearLayout mIdentityKnownContainer;

    private ImageView mIcon;
    private TextView mTitle;
    private TextView mSecurityState;
    private TextView mMixedContentActivity;
    private TextView mOwner;
    private TextView mOwnerSupplemental;
    private TextView mVerifier;
    private TextView mLink;
    private TextView mSiteSettingsLink;

    private View mDivider;

    private DoorHanger mTrackingContentNotification;
    private DoorHanger mSelectLoginDoorhanger;

    private final OnButtonClickListener mContentButtonClickListener;

    public SiteIdentityPopup(Context context) {
        super(context);

        mResources = mContext.getResources();

        mContentButtonClickListener = new ContentNotificationButtonListener();
    }

    void registerListeners() {
        EventDispatcher.getInstance().registerGoannaThreadListener(this,
                                                                  "Doorhanger:Logins",
                                                                  "Permissions:CheckResult");
    }

    @Override
    protected void init() {
        super.init();

        // Make the popup focusable so it doesn't inadvertently trigger click events elsewhere
        // which may reshow the popup (see bug 785156)
        setFocusable(true);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mIdentity = (LinearLayout) inflater.inflate(R.layout.site_identity, null);
        mContent.addView(mIdentity);

        mIdentityKnownContainer =
                (LinearLayout) mIdentity.findViewById(R.id.site_identity_known_container);

        mIcon = (ImageView) mIdentity.findViewById(R.id.site_identity_icon);
        mTitle = (TextView) mIdentity.findViewById(R.id.site_identity_title);
        mSecurityState = (TextView) mIdentity.findViewById(R.id.site_identity_state);
        mMixedContentActivity = (TextView) mIdentity.findViewById(R.id.mixed_content_activity);

        mOwner = (TextView) mIdentityKnownContainer.findViewById(R.id.owner);
        mOwnerSupplemental = (TextView) mIdentityKnownContainer.findViewById(R.id.owner_supplemental);
        mVerifier = (TextView) mIdentityKnownContainer.findViewById(R.id.verifier);
        mDivider = mIdentity.findViewById(R.id.divider_doorhanger);

        mLink = (TextView) mIdentity.findViewById(R.id.site_identity_link);
        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tabs.getInstance().loadUrlInTab(MIXED_CONTENT_SUPPORT_URL);
            }
        });

        mSiteSettingsLink = (TextView) mIdentity.findViewById(R.id.site_settings_link);
    }

    private void updateIdentity(final SiteIdentity siteIdentity) {
        if (!mInflated) {
            init();
        }

        final boolean isIdentityKnown = (siteIdentity.getSecurityMode() == SecurityMode.IDENTIFIED ||
                                         siteIdentity.getSecurityMode() == SecurityMode.VERIFIED);
        updateConnectionState(siteIdentity);
        toggleIdentityKnownContainerVisibility(isIdentityKnown);

        if (isIdentityKnown) {
            updateIdentityInformation(siteIdentity);
        }

        GoannaAppShell.notifyObservers("Permissions:Check", null);
    }

    @Override
    public void handleMessage(String event, JSONObject goannaObject) {
        if ("Doorhanger:Logins".equals(event)) {
            try {
                final Tab selectedTab = Tabs.getInstance().getSelectedTab();
                if (selectedTab != null) {
                    final JSONObject data = goannaObject.getJSONObject("data");
                    addLoginsToTab(data);
                }
                if (isShowing()) {
                    addSelectLoginDoorhanger(selectedTab);
                }
            } catch (JSONException e) {
                Log.e(LOGTAG, "Error accessing logins in Doorhanger:Logins message", e);
            }
        } else if ("Permissions:CheckResult".equals(event)) {
            final boolean hasPermissions = goannaObject.optBoolean("hasPermissions", false);
            if (hasPermissions) {
                mSiteSettingsLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GoannaAppShell.notifyObservers("Permissions:Get", null);
                        dismiss();
                    }
                });
            }

            ThreadUtils.postToUiThread(new Runnable() {
                @Override
                public void run() {
                    mSiteSettingsLink.setVisibility(hasPermissions ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    private void addLoginsToTab(JSONObject data) throws JSONException {
        final JSONArray logins = data.getJSONArray("logins");

        final SiteLogins siteLogins = new SiteLogins(logins);
        Tabs.getInstance().getSelectedTab().setSiteLogins(siteLogins);
    }

    private void addSelectLoginDoorhanger(Tab tab) throws JSONException {
        final SiteLogins siteLogins = tab.getSiteLogins();
        if (siteLogins == null) {
            return;
        }

        final JSONArray logins = siteLogins.getLogins();
        if (logins.length() == 0) {
            return;
        }

        final JSONObject login = (JSONObject) logins.get(0);

        // Create button click listener for copying a password to the clipboard.
        final OnButtonClickListener buttonClickListener = new OnButtonClickListener() {
            Activity activity = (Activity) mContext;
            @Override
            public void onButtonClick(JSONObject response, DoorHanger doorhanger) {
                try {
                    final int buttonId = response.getInt("callback");
                    if (buttonId == ButtonType.COPY.ordinal()) {
                        final ClipboardManager manager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        String password;
                        if (response.has("password")) {
                            // Click listener being called from List Dialog.
                            password = response.optString("password");
                        } else {
                            password = login.getString("password");
                        }

                        manager.setPrimaryClip(ClipData.newPlainText("password", password));

                        SnackbarBuilder.builder(activity)
                                .message(R.string.doorhanger_login_select_toast_copy)
                                .duration(Snackbar.LENGTH_SHORT)
                                .buildAndShow();
                    }
                    dismiss();
                } catch (JSONException e) {
                    Log.e(LOGTAG, "Error handling Select login button click", e);
                    SnackbarBuilder.builder(activity)
                            .message(R.string.doorhanger_login_select_toast_copy_error)
                            .duration(Snackbar.LENGTH_SHORT)
                            .buildAndShow();
                }
            }
        };

        final DoorhangerConfig config = new DoorhangerConfig(DoorHanger.Type.LOGIN, buttonClickListener);

        // Set buttons.
        config.setButton(mContext.getString(R.string.button_cancel), ButtonType.CANCEL.ordinal(), false);
        config.setButton(mContext.getString(R.string.button_copy), ButtonType.COPY.ordinal(), true);

        // Set message.
        String username = ((JSONObject) logins.get(0)).getString("username");
        if (TextUtils.isEmpty(username)) {
            username = mContext.getString(R.string.doorhanger_login_no_username);
        }

        final String message = mContext.getString(R.string.doorhanger_login_select_message).replace(FORMAT_S, username);
        config.setMessage(message);

        // Set options.
        final GoannaBundle options = new GoannaBundle();

        // Add action text only if there are other logins to select.
        if (logins.length() > 1) {
            final GoannaBundle actionText = new GoannaBundle();
            actionText.putString("type", "SELECT");

            final JSONObject bundle = new JSONObject();
            bundle.put("logins", logins);

            actionText.putBundle("bundle", GoannaBundle.fromJSONObject(bundle));
            options.putBundle("actionText", actionText);
        }

        config.setOptions(options);

        ThreadUtils.postToUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mInflated) {
                    init();
                }

                removeSelectLoginDoorhanger();

                mSelectLoginDoorhanger = DoorHanger.Get(mContext, config);
                mContent.addView(mSelectLoginDoorhanger);
                mDivider.setVisibility(View.VISIBLE);
            }
        });
    }

    private void removeSelectLoginDoorhanger() {
        if (mSelectLoginDoorhanger != null) {
            mContent.removeView(mSelectLoginDoorhanger);
            mSelectLoginDoorhanger = null;
        }
    }

    private void toggleIdentityKnownContainerVisibility(final boolean isIdentityKnown) {
        final int identityInfoVisibility = isIdentityKnown ? View.VISIBLE : View.GONE;
        mIdentityKnownContainer.setVisibility(identityInfoVisibility);
    }

    /**
     * Update the Site Identity content to reflect connection state.
     *
     * The connection state should reflect the combination of:
     * a) Connection encryption
     * b) Mixed Content state (Active/Display Mixed content, loaded, blocked, none, etc)
     * and update the icons and strings to inform the user of that state.
     *
     * @param siteIdentity SiteIdentity information about the connection.
     */
    private void updateConnectionState(final SiteIdentity siteIdentity) {
        if (siteIdentity.getSecurityMode() == SecurityMode.CHROMEUI) {
            mSecurityState.setText(R.string.identity_connection_chromeui);
            mSecurityState.setTextColor(ContextCompat.getColor(mContext, R.color.placeholder_active_grey));

            mIcon.setImageResource(R.drawable.icon);
            clearSecurityStateIcon();

            mMixedContentActivity.setVisibility(View.GONE);
            mLink.setVisibility(View.GONE);
        } else if (!siteIdentity.isSecure()) {
            if (siteIdentity.getMixedModeActive() == MixedMode.LOADED) {
                // Active Mixed Content loaded because user has disabled blocking.
                mIcon.setImageResource(R.drawable.lock_disabled);
                clearSecurityStateIcon();
                mMixedContentActivity.setVisibility(View.VISIBLE);
                mMixedContentActivity.setText(R.string.mixed_content_protection_disabled);

                mLink.setVisibility(View.VISIBLE);
            } else if (siteIdentity.getMixedModeDisplay() == MixedMode.LOADED) {
                // Passive Mixed Content loaded.
                mIcon.setImageResource(R.drawable.lock_inactive);
                setSecurityStateIcon(R.drawable.warning_major, 1);
                mMixedContentActivity.setVisibility(View.VISIBLE);
                if (siteIdentity.getMixedModeActive() == MixedMode.BLOCKED) {
                    mMixedContentActivity.setText(R.string.mixed_content_blocked_some);
                } else {
                    mMixedContentActivity.setText(R.string.mixed_content_display_loaded);
                }
                mLink.setVisibility(View.VISIBLE);

            } else {
                // Unencrypted connection with no mixed content.
                mIcon.setImageResource(R.drawable.globe_light);
                clearSecurityStateIcon();

                mMixedContentActivity.setVisibility(View.GONE);
                mLink.setVisibility(View.GONE);
            }

            mSecurityState.setText(R.string.identity_connection_insecure);
            mSecurityState.setTextColor(ContextCompat.getColor(mContext, R.color.placeholder_active_grey));

        } else if (siteIdentity.isSecurityException()) {

            mIcon.setImageResource(R.drawable.lock_inactive);
            setSecurityStateIcon(R.drawable.warning_major, 1);
            mSecurityState.setText(R.string.identity_connection_insecure);
            mSecurityState.setTextColor(ContextCompat.getColor(mContext, R.color.placeholder_active_grey));

        } else {
            // Connection is secure.
            mIcon.setImageResource(R.drawable.lock_secure);

            setSecurityStateIcon(R.drawable.img_check, 2);
            mSecurityState.setTextColor(ContextCompat.getColor(mContext, R.color.affirmative_green));
            mSecurityState.setText(R.string.identity_connection_secure);

            // Mixed content has been blocked, if present.
            if (siteIdentity.getMixedModeActive() == MixedMode.BLOCKED ||
                siteIdentity.getMixedModeDisplay() == MixedMode.BLOCKED) {
                mMixedContentActivity.setVisibility(View.VISIBLE);
                mMixedContentActivity.setText(R.string.mixed_content_blocked_all);
                mLink.setVisibility(View.VISIBLE);
            } else {
                mMixedContentActivity.setVisibility(View.GONE);
                mLink.setVisibility(View.GONE);
            }
        }
    }

    private void clearSecurityStateIcon() {
        mSecurityState.setCompoundDrawablePadding(0);
        TextViewCompat.setCompoundDrawablesRelative(mSecurityState, null, null, null, null);
    }

    private void setSecurityStateIcon(int resource, int factor) {
        final Drawable stateIcon = ContextCompat.getDrawable(mContext, resource);
        stateIcon.setBounds(0, 0, stateIcon.getIntrinsicWidth() / factor, stateIcon.getIntrinsicHeight() / factor);
        TextViewCompat.setCompoundDrawablesRelative(mSecurityState, stateIcon, null, null, null);
        mSecurityState.setCompoundDrawablePadding((int) mResources.getDimension(R.dimen.doorhanger_drawable_padding));
    }
    private void updateIdentityInformation(final SiteIdentity siteIdentity) {
        String owner = siteIdentity.getOwner();
        if (owner == null) {
            mOwner.setVisibility(View.GONE);
            mOwnerSupplemental.setVisibility(View.GONE);
        } else {
            mOwner.setVisibility(View.VISIBLE);
            mOwner.setText(owner);

            // Supplemental data is optional.
            final String supplemental = siteIdentity.getSupplemental();
            if (!TextUtils.isEmpty(supplemental)) {
                mOwnerSupplemental.setText(supplemental);
                mOwnerSupplemental.setVisibility(View.VISIBLE);
            } else {
                mOwnerSupplemental.setVisibility(View.GONE);
            }
        }

        final String verifier = siteIdentity.getVerifier();
        mVerifier.setText(verifier);
    }

    private void addTrackingContentNotification(boolean blocked) {
        // Remove any existing tracking content notification.
        removeTrackingContentNotification();

        final DoorhangerConfig config = new DoorhangerConfig(DoorHanger.Type.TRACKING, mContentButtonClickListener);

        final int icon = blocked ? R.drawable.shield_enabled : R.drawable.shield_disabled;

        final GoannaBundle options = new GoannaBundle();
        final GoannaBundle tracking = new GoannaBundle();
        tracking.putBoolean("enabled", blocked);
        options.putBundle("tracking_protection", tracking);
        config.setOptions(options);

        config.setLink(mContext.getString(R.string.learn_more), TRACKING_CONTENT_SUPPORT_URL);

        addNotificationButtons(config, blocked);

        mTrackingContentNotification = DoorHanger.Get(mContext, config);

        mTrackingContentNotification.setIcon(icon);

        mContent.addView(mTrackingContentNotification);
        mDivider.setVisibility(View.VISIBLE);
    }

    private void removeTrackingContentNotification() {
        if (mTrackingContentNotification != null) {
            mContent.removeView(mTrackingContentNotification);
            mTrackingContentNotification = null;
        }
    }

    private void addNotificationButtons(DoorhangerConfig config, boolean blocked) {
        if (blocked) {
            config.setButton(mContext.getString(R.string.disable_protection), ButtonType.DISABLE.ordinal(), false);
        } else {
            config.setButton(mContext.getString(R.string.enable_protection), ButtonType.ENABLE.ordinal(), true);
        }
    }

    /*
     * @param identityData A JSONObject that holds the current tab's identity data.
     */
    void setSiteIdentity(SiteIdentity siteIdentity) {
        mSiteIdentity = siteIdentity;
    }

    @Override
    public void show() {
        if (mSiteIdentity == null) {
            Log.e(LOGTAG, "Can't show site identity popup for undefined state");
            return;
        }

        // Verified about: pages have the CHROMEUI SiteIdentity, however there can also
        // be unverified about: pages for which  "This site's identity is unknown" or
        // "This is a secure Firefox page" are both misleading, so don't show a popup.
        final Tab selectedTab = Tabs.getInstance().getSelectedTab();
        if (selectedTab != null &&
                AboutPages.isAboutPage(selectedTab.getURL()) &&
                mSiteIdentity.getSecurityMode() != SecurityMode.CHROMEUI) {
            Log.d(LOGTAG, "We don't show site identity popups for unverified about: pages");
            return;
        }

        updateIdentity(mSiteIdentity);

        final TrackingMode trackingMode = mSiteIdentity.getTrackingMode();
        if (trackingMode != TrackingMode.UNKNOWN) {
            addTrackingContentNotification(trackingMode == TrackingMode.TRACKING_CONTENT_BLOCKED);
        }

        try {
            addSelectLoginDoorhanger(selectedTab);
        } catch (JSONException e) {
            Log.e(LOGTAG, "Error adding selectLogin doorhanger", e);
        }

        if (mSiteIdentity.getSecurityMode() == SecurityMode.CHROMEUI) {
            // For about: pages we display the product icon in place of the verified/globe
            // image, hence we don't also set the favicon (for most about pages the
            // favicon is the product icon, hence we'd be showing the same icon twice).
            mTitle.setText(R.string.moz_app_displayname);
        } else {
            mTitle.setText(selectedTab.getBaseDomain());

            final Bitmap favicon = selectedTab.getFavicon();
            if (favicon != null) {
                final Drawable faviconDrawable = new BitmapDrawable(mResources, favicon);
                final int dimen = (int) mResources.getDimension(R.dimen.browser_toolbar_favicon_size);
                faviconDrawable.setBounds(0, 0, dimen, dimen);

                TextViewCompat.setCompoundDrawablesRelative(mTitle, faviconDrawable, null, null, null);
                mTitle.setCompoundDrawablePadding((int) mContext.getResources().getDimension(R.dimen.doorhanger_drawable_padding));
            }
        }

        showDividers();

        super.show();
    }

    // Show the right dividers
    private void showDividers() {
        final int count = mContent.getChildCount();
        DoorHanger lastVisibleDoorHanger = null;

        for (int i = 0; i < count; i++) {
            final View child = mContent.getChildAt(i);

            if (!(child instanceof DoorHanger)) {
                continue;
            }

            DoorHanger dh = (DoorHanger) child;
            dh.showDivider();
            if (dh.getVisibility() == View.VISIBLE) {
                lastVisibleDoorHanger = dh;
            }
        }

        if (lastVisibleDoorHanger != null) {
            lastVisibleDoorHanger.hideDivider();
        }
    }

    void destroy() {
    }

    void unregisterListeners() {
        EventDispatcher.getInstance().unregisterGoannaThreadListener(this,
                                                                    "Doorhanger:Logins",
                                                                    "Permissions:CheckResult");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        removeTrackingContentNotification();
        removeSelectLoginDoorhanger();
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mTitle, null, null, null, null);
        mDivider.setVisibility(View.GONE);
    }

    private class ContentNotificationButtonListener implements OnButtonClickListener {
        @Override
        public void onButtonClick(JSONObject response, DoorHanger doorhanger) {
            GoannaAppShell.notifyObservers("Session:Reload", response.toString());
            dismiss();
        }
    }
}
