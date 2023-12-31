/* -*- Mode: Java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.icons;

import android.content.Context;
import android.support.annotation.CheckResult;

import org.mozilla.goanna.GoannaAppShell;

import java.util.TreeSet;

import ch.boye.httpclientandroidlib.util.TextUtils;

/**
 * Builder for creating a request to load an icon.
 */
public class IconRequestBuilder {
    private final IconRequest internal;

    /* package-private */ IconRequestBuilder(Context context) {
        internal = new IconRequest(context);
    }

    /* package-private */ IconRequestBuilder(IconRequest request) {
        internal = request;
    }

    /**
     * Set the URL of the page for which the icon should be loaded.
     */
    @CheckResult
    public IconRequestBuilder pageUrl(String pageUrl) {
        internal.pageUrl = pageUrl;
        return this;
    }

    /**
     * Set whether this request is allowed to load icons from non http(s) URLs (e.g. the omni.ja).
     *
     * For example web content referencing internal URLs should not lead to us loading icons from
     * internal data structures like the omni.ja.
     */
    @CheckResult
    public IconRequestBuilder privileged(boolean privileged) {
        internal.privileged = privileged;
        return this;
    }

    /**
     * Add an icon descriptor describing the location and properties of an icon. All descriptors
     * will be ranked and tried in order of their rank. Executing the request will modify the list
     * of icons (filter or add additional descriptors).
     */
    @CheckResult
    public IconRequestBuilder icon(IconDescriptor descriptor) {
        internal.icons.add(descriptor);
        return this;
    }

    /**
     * Skip the network and do not load an icon from a network connection.
     */
    @CheckResult
    public IconRequestBuilder skipNetwork() {
        internal.skipNetwork = true;
        return this;
    }

    /**
     * If shouldSkipNetwork is true then do not load icon from a network connection.
     */
    @CheckResult
    public IconRequestBuilder skipNetworkIf(boolean shouldSkipNetwork) {
        internal.skipNetwork = shouldSkipNetwork;
        return this;
    }

    /**
     * Skip the disk cache and do not load an icon from disk.
     */
    @CheckResult
    public IconRequestBuilder skipDisk() {
        internal.skipDisk = true;
        return this;
    }

    /**
     * Skip the memory cache and do not return a previously loaded icon.
     */
    @CheckResult
    public IconRequestBuilder skipMemory() {
        internal.skipMemory = true;
        return this;
    }

    /**
     * The icon will be used as (Android) launcher icon. The loaded icon will be scaled to the
     * preferred Android launcher icon size.
     */
    public IconRequestBuilder forLauncherIcon() {
        internal.targetSize = GoannaAppShell.getPreferredIconSize();
        return this;
    }

    /**
     * Execute the callback on the background thread. By default the callback is always executed on
     * the UI thread in order to add the loaded icon to a view easily.
     */
    @CheckResult
    public IconRequestBuilder executeCallbackOnBackgroundThread() {
        internal.backgroundThread = true;
        return this;
    }

    /**
     * When executing the request then only prepare executing it but do not actually load an icon.
     * This mode is only used for some legacy code that uses the icon URL and therefore needs to
     * perform a lookup of the URL but doesn't want to load the icon yet.
     */
    public IconRequestBuilder prepareOnly() {
        internal.prepareOnly = true;
        return this;
    }

    /**
     * Return the request built with this builder.
     */
    @CheckResult
    public IconRequest build() {
        if (TextUtils.isEmpty(internal.pageUrl)) {
            throw new IllegalStateException("Page URL is required");
        }

        IconRequest request = new IconRequest(internal.getContext());
        request.pageUrl = internal.pageUrl;
        request.privileged = internal.privileged;
        request.icons = new TreeSet<>(internal.icons);
        request.skipNetwork = internal.skipNetwork;
        request.backgroundThread = internal.backgroundThread;
        request.skipDisk = internal.skipDisk;
        request.skipMemory = internal.skipMemory;
        request.targetSize = internal.targetSize;
        request.prepareOnly = internal.prepareOnly;
        return request;
    }

    /**
     * This is a no-op method.
     *
     * All builder methods are annotated with @CheckResult to denote that the
     * methods return the builder object and that it is typically an error to not call another method
     * on it until eventually calling build().
     *
     * However in some situations code can keep a reference
     * to the builder object and call methods only when a specific event occurs. To make this explicit
     * and avoid lint errors this method can be called.
     */
    public void deferBuild() {
        // No op
    }
}
