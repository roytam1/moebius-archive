/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.goanna.media;

// Non-default types used in interface.
import android.os.Bundle;
import android.view.Surface;
import org.mozilla.goanna.media.FormatParam;
import org.mozilla.goanna.media.ICodecCallbacks;
import org.mozilla.goanna.media.Sample;

interface ICodec {
    void setCallbacks(in ICodecCallbacks callbacks);
    boolean configure(in FormatParam format, inout Surface surface, int flags, in String drmStubId);
    boolean isAdaptivePlaybackSupported();
    void start();
    void stop();
    void flush();
    void release();

    Sample dequeueInput(int size);
    oneway void queueInput(in Sample sample);

    oneway void releaseOutput(in Sample sample, in boolean render);
}
