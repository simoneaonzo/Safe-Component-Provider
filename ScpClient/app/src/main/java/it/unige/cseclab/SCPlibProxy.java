package it.unige.cseclab;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class SCPlibProxy extends BroadcastReceiver {
    public static final String ACTION_ASK = "it.unige.scp.action.ask";
    public static final String SCP_MODE = "scp.mode";
    public static final String SCP_CALLER = "scp.caller";
    public static final String SCP_METHOD = "scp.method";
    public static final String SCP_INTENT = "scp.intent";

    public static void ScpStartActivity(Activity a, Intent i) {
        Intent intentWrapper = new Intent();
        intentWrapper.setAction(ACTION_ASK);
        intentWrapper.putExtra(SCP_MODE, "broadcast");
        intentWrapper.putExtra(SCP_CALLER, a.getClass().getCanonicalName());
        intentWrapper.putExtra(SCP_METHOD, "startActivity");
        intentWrapper.putExtra(SCP_INTENT, i);
        a.getApplicationContext().sendBroadcast(intentWrapper);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent wrappedIntent = intent.getParcelableExtra(SCP_INTENT);
        String method = intent.getStringExtra(SCP_METHOD);
        if (method == null)
            method = "";
        switch (method) {
            case "startActivity":
                wrappedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(wrappedIntent);
                break;
            default:
                Toast.makeText(context, "ERROR: UNKNOWN METHOD", Toast.LENGTH_LONG).show();
        }
    }
}
