package it.unige.cseclab;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashMap;


public class SCPlibProxy extends BroadcastReceiver {

    public static final String ACTION_ASK = "it.unige.scp.action.ask";
    public static final String SCP_MODE = "scp.mode";
    public static final String SCP_CALLER = "scp.caller";
    public static final String SCP_METHOD = "scp.method";
    public static final String SCP_INTENT = "scp.intent";
    public static final String REQUESTCODE = "requestCode";

    public static final String startActivity = "startActivity";
    public static final String startService = "startService";
    public static final String startActivityForResult = "startActivityForResult";

    private static final HashMap<String, Activity> canonNameToActivity = new HashMap<>();

    public static void ScpStartService(Activity activity, Intent wrappedIntent) {
        setupAndSend(activity, new Intent(), wrappedIntent, startService);
    }

    public static void ScpStartActivity(Activity activity, Intent wrappedIntent) {
        setupAndSend(activity, new Intent(), wrappedIntent, startActivity);
    }

    public static void ScpStartActivityForResult(Activity activity, Intent wrappedIntent, int requestCode) {
        Intent intentWrapper = new Intent();
        intentWrapper.putExtra(REQUESTCODE, "" + requestCode);
        setupAndSend(activity, intentWrapper, wrappedIntent, startActivityForResult);
    }


    public static void setupAndSend(Activity activity, Intent intentWrapper, Intent wrappedIntent, String method) {
        String canonicalName = activity.getClass().getCanonicalName();
        intentWrapper.setAction(ACTION_ASK);
        intentWrapper.putExtra(SCP_CALLER, canonicalName);
        intentWrapper.putExtra(SCP_METHOD, method);
        intentWrapper.putExtra(SCP_INTENT, wrappedIntent);
        canonNameToActivity.put(canonicalName, activity);
        activity.getApplicationContext().sendBroadcast(intentWrapper);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent wrappedIntent = intent.getParcelableExtra(SCP_INTENT);
        String caller = intent.getStringExtra(SCP_CALLER);
        if (!canonNameToActivity.containsKey(caller))
            return;
        Activity activity = canonNameToActivity.get(caller);
        String method = intent.getStringExtra(SCP_METHOD);
        if (method == null)
            method = "";
        switch (method) {
            case startActivity:
                wrappedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(wrappedIntent);
                break;
            case startActivityForResult:
                wrappedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                int rc = Integer.parseInt(intent.getStringExtra(REQUESTCODE));
                activity.startActivityForResult(wrappedIntent, rc);
                break;
            case startService:
                activity.startService(wrappedIntent);
                break;
            default:
                Toast.makeText(context, "ERROR: UNKNOWN METHOD", Toast.LENGTH_LONG).show();
        }
    }
}
