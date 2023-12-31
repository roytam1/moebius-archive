/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna;

public class GoannaViewContent implements GoannaView.ContentDelegate {
    /**
    * A View has started loading content from the network.
    * @param view The GoannaView that initiated the callback.
    * @param url The resource being loaded.
    */
    @Override
    public void onPageStart(GoannaView view, String url) {}

    /**
    * A View has finished loading content from the network.
    * @param view The GoannaView that initiated the callback.
    * @param success Whether the page loaded successfully or an error occurred.
    */
    @Override
    public void onPageStop(GoannaView view, boolean success) {}

    /**
    * A View is displaying content. This page could have been loaded via
    * network or from the session history.
    * @param view The GoannaView that initiated the callback.
    */
    @Override
    public void onPageShow(GoannaView view) {}

    /**
    * A page title was discovered in the content or updated after the content
    * loaded.
    * @param view The GoannaView that initiated the callback.
    * @param title The title sent from the content.
    */
    @Override
    public void onReceivedTitle(GoannaView view, String title) {}

    /**
    * A link element was discovered in the content or updated after the content
    * loaded that specifies a favicon.
    * @param view The GoannaView that initiated the callback.
    * @param url The href of the link element specifying the favicon.
    * @param size The maximum size specified for the favicon, or -1 for any size.
    */
    @Override
    public void onReceivedFavicon(GoannaView view, String url, int size) {}
}
