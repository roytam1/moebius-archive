/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna;

import org.mozilla.goanna.mozglue.GoannaLoader;

import android.content.Context;
import org.mozilla.goanna.annotation.RobocopTarget;

public class NSSBridge {
    private static final String LOGTAG = "NSSBridge";

    private static native String nativeEncrypt(String aDb, String aValue);
    private static native String nativeDecrypt(String aDb, String aValue);

    @RobocopTarget
    static public String encrypt(Context context, String aValue)
      throws Exception {
        String resourcePath = context.getPackageResourcePath();
        GoannaLoader.loadNSSLibs(context, resourcePath);

        String path = GoannaProfile.get(context).getDir().toString();
        return nativeEncrypt(path, aValue);
    }

    @RobocopTarget
    static public String encrypt(Context context, String profilePath, String aValue)
      throws Exception {
        String resourcePath = context.getPackageResourcePath();
        GoannaLoader.loadNSSLibs(context, resourcePath);

        return nativeEncrypt(profilePath, aValue);
    }

    @RobocopTarget
    static public String decrypt(Context context, String aValue)
      throws Exception {
        String resourcePath = context.getPackageResourcePath();
        GoannaLoader.loadNSSLibs(context, resourcePath);

        String path = GoannaProfile.get(context).getDir().toString();
        return nativeDecrypt(path, aValue);
    }

    @RobocopTarget
    static public String decrypt(Context context, String profilePath, String aValue)
      throws Exception {
        String resourcePath = context.getPackageResourcePath();
        GoannaLoader.loadNSSLibs(context, resourcePath);

        return nativeDecrypt(profilePath, aValue);
    }
}
