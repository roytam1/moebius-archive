/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.fxa.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.mozilla.goanna.background.fxa.FxAccountUtils;
import org.mozilla.goanna.browserid.BrowserIDKeyPair;
import org.mozilla.goanna.browserid.JSONWebTokenUtils;
import org.mozilla.goanna.fxa.login.FxAccountLoginStateMachine.ExecuteDelegate;
import org.mozilla.goanna.fxa.login.FxAccountLoginTransition.LogMessage;
import org.mozilla.goanna.sync.ExtendedJSONObject;
import org.mozilla.goanna.sync.NonObjectJSONException;
import org.mozilla.goanna.sync.Utils;
import org.mozilla.goanna.sync.crypto.KeyBundle;

public class Married extends TokensAndKeysState {
  private static final String LOG_TAG = Married.class.getSimpleName();

  protected final String certificate;
  protected final String clientState;

  public Married(String email, String uid, byte[] sessionToken, byte[] kA, byte[] kB, BrowserIDKeyPair keyPair, String certificate) {
    super(StateLabel.Married, email, uid, sessionToken, kA, kB, keyPair);
    Utils.throwIfNull(certificate);
    this.certificate = certificate;
    try {
      this.clientState = FxAccountUtils.computeClientState(kB);
    } catch (NoSuchAlgorithmException e) {
      // This should never occur.
      throw new IllegalStateException("Unable to compute client state from kB.");
    }
  }

  @Override
  public ExtendedJSONObject toJSONObject() {
    ExtendedJSONObject o = super.toJSONObject();
    // Fields are non-null by constructor.
    o.put("certificate", certificate);
    return o;
  }

  @Override
  public void execute(final ExecuteDelegate delegate) {
    delegate.handleTransition(new LogMessage("staying married"), this);
  }

  public String generateAssertion(String audience, String issuer) throws NonObjectJSONException, IOException, GeneralSecurityException {
    // We generate assertions with no iat and an exp after 2050 to avoid
    // invalid-timestamp errors from the token server.
    final long expiresAt = JSONWebTokenUtils.DEFAULT_FUTURE_EXPIRES_AT_IN_MILLISECONDS;
    String assertion = JSONWebTokenUtils.createAssertion(keyPair.getPrivate(), certificate, audience, issuer, null, expiresAt);
    if (!FxAccountUtils.LOG_PERSONAL_INFORMATION) {
      return assertion;
    }

    try {
      FxAccountUtils.pii(LOG_TAG, "Generated assertion: " + assertion);
      ExtendedJSONObject a = JSONWebTokenUtils.parseAssertion(assertion);
      if (a != null) {
        FxAccountUtils.pii(LOG_TAG, "aHeader   : " + a.getObject("header"));
        FxAccountUtils.pii(LOG_TAG, "aPayload  : " + a.getObject("payload"));
        FxAccountUtils.pii(LOG_TAG, "aSignature: " + a.getString("signature"));
        String certificate = a.getString("certificate");
        if (certificate != null) {
          ExtendedJSONObject c = JSONWebTokenUtils.parseCertificate(certificate);
          FxAccountUtils.pii(LOG_TAG, "cHeader   : " + c.getObject("header"));
          FxAccountUtils.pii(LOG_TAG, "cPayload  : " + c.getObject("payload"));
          FxAccountUtils.pii(LOG_TAG, "cSignature: " + c.getString("signature"));
          // Print the relevant timestamps in sorted order with labels.
          HashMap<Long, String> map = new HashMap<Long, String>();
          map.put(a.getObject("payload").getLong("iat"), "aiat");
          map.put(a.getObject("payload").getLong("exp"), "aexp");
          map.put(c.getObject("payload").getLong("iat"), "ciat");
          map.put(c.getObject("payload").getLong("exp"), "cexp");
          ArrayList<Long> values = new ArrayList<Long>(map.keySet());
          Collections.sort(values);
          for (Long value : values) {
            FxAccountUtils.pii(LOG_TAG, map.get(value) + ": " + value);
          }
        } else {
          FxAccountUtils.pii(LOG_TAG, "Could not parse certificate!");
        }
      } else {
        FxAccountUtils.pii(LOG_TAG, "Could not parse assertion!");
      }
    } catch (Exception e) {
      FxAccountUtils.pii(LOG_TAG, "Got exception dumping assertion debug info.");
    }
    return assertion;
  }

  public KeyBundle getSyncKeyBundle() throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
    // TODO Document this choice for deriving from kB.
    return FxAccountUtils.generateSyncKeyBundle(kB);
  }

  public String getClientState() {
    if (FxAccountUtils.LOG_PERSONAL_INFORMATION) {
      FxAccountUtils.pii(LOG_TAG, "Client state: " + this.clientState);
    }
    return this.clientState;
  }

  public Cohabiting makeCohabitingState() {
    return new Cohabiting(email, uid, sessionToken, kA, kB, keyPair);
  }
}
