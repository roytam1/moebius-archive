/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.tests.browser.junit3;

import android.test.InstrumentationTestCase;
import org.mozilla.goanna.AppConstants;
import org.mozilla.goanna.menu.GoannaMenu;
import org.mozilla.goanna.util.ThreadUtils;

public class TestGoannaMenu extends InstrumentationTestCase {

    private volatile Exception exception;
    private void setException(Exception e) {
        this.exception = e;
    }

    public void testMenuThreading() throws InterruptedException {
        final GoannaMenu menu = new GoannaMenu(getInstrumentation().getTargetContext());
        final Object semaphore = new Object();

        ThreadUtils.postToUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    menu.add("test1");
                } catch (Exception e) {
                    setException(e);
                }

                synchronized (semaphore) {
                    semaphore.notify();
                }
            }
        });
        synchronized (semaphore) {
            semaphore.wait();
        }

        // No exception thrown if called on UI thread.
        assertNull(exception);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    menu.add("test2");
                } catch (Exception e) {
                    setException(e);
                }

                synchronized (semaphore) {
                    semaphore.notify();
                }
            }
        }).start();

        synchronized (semaphore) {
            semaphore.wait();
        }

        if (AppConstants.RELEASE_OR_BETA) {
            // No exception thrown: release build.
            assertNull(exception);
            return;
        }

        assertNotNull(exception);
        assertEquals(exception.getClass(), IllegalThreadStateException.class);
    }
}
