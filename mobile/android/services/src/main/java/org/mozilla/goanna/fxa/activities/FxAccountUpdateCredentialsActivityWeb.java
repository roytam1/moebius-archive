/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.fxa.activities;

public class FxAccountUpdateCredentialsActivityWeb extends FxAccountWebFlowActivity {
  public FxAccountUpdateCredentialsActivityWeb() {
    super(CANNOT_RESUME_WHEN_NO_ACCOUNTS_EXIST, "force_auth");
  }
}
