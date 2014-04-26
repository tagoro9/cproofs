package com.scytl.cproofs.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DummyService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.scytl.cproofs.test.service.action.FOO";
    private static final String ACTION_BAZ = "com.scytl.cproofs.test.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.scytl.cproofs.test.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.scytl.cproofs.test.service.extra.PARAM2";

    public DummyService() {
        super("DummyService");
    }

    public String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
