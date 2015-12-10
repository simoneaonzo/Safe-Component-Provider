package it.unige.cseclab;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestService extends IntentService {

    public TestService() {
        super("TestService");
    }

    public TestService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("***", "***service started***");
    }
}
