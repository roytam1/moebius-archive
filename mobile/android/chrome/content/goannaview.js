/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
"use strict";

var Cc = Components.classes;
var Ci = Components.interfaces;
var Cu = Components.utils;
var Cr = Components.results;

Cu.import("resource://gre/modules/AppConstants.jsm");
Cu.import("resource://gre/modules/XPCOMUtils.jsm");

XPCOMUtils.defineLazyModuleGetter(this, "Log",
 "resource://gre/modules/AndroidLog.jsm", "AndroidLog");

XPCOMUtils.defineLazyModuleGetter(this, "Services",
 "resource://gre/modules/Services.jsm", "Services");

function dump(msg) {
  Log.d("View", msg);
}

function startup() {
    dump("zerdatime " + Date.now() + " - goannaview chrome startup finished.");
}
