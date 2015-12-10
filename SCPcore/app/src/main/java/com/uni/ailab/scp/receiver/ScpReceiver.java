package com.uni.ailab.scp.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.uni.ailab.scp.gui.ReceiverChoiceActivity;
import com.uni.ailab.scp.log.Logger;


public class ScpReceiver extends BroadcastReceiver
{
    public static final String SCP_MODE = "scp.mode";
    public static final String SCP_CALLER = "scp.caller";
    public static final String SCP_METHOD = "scp.method";
    public static final String SCP_INTENT = "scp.intent";
    public static final String REQUESTCODE = "requestCode";

    private static SQLiteHelper dbHelper;
    
	public ScpReceiver()
    { }

	@Override
	public void onReceive(Context context, Intent intent)
	{
        /*
        TODO: should check intent authenticity
         */

        /*
        Open DB if not yet initialized
         */
        if (dbHelper == null)
            dbHelper = new SQLiteHelper(context);

        /*
        Parse the request
         */
        //String mode = intent.getStringExtra(SCP_MODE);
        String caller = intent.getStringExtra(SCP_CALLER);
        String method = intent.getStringExtra(SCP_METHOD);
        int requestCode = -1;
        if (method.equals("startActivityForResult"))
            requestCode = Integer.parseInt(intent.getStringExtra(REQUESTCODE));
        String type = inferType(method);
        Intent wrappedIntent = intent.getParcelableExtra(SCP_INTENT);

        String action = wrappedIntent.getAction();
        ComponentName cn = wrappedIntent.getComponent();
        if (cn != null) {
            String componentFqdn = cn.getClassName();
        }


        if (true) {
            Intent intentWrapper = new Intent();
            intentWrapper.setAction("it.unige.scp.action.answer");
            intentWrapper.putExtra(SCP_CALLER, caller);
            intentWrapper.putExtra(SCP_METHOD, method);
            intentWrapper.putExtra(SCP_INTENT, wrappedIntent);
            if (requestCode != -1) {
                intentWrapper.putExtra(REQUESTCODE, "" + requestCode);
            }
            context.sendBroadcast(intentWrapper);
            return;

        }



        Logger.log("*** New request received");
        Logger.log("Component: " + caller);
        Logger.log("Type: " + type);
        //Logger.log("Mode: " + mode);
        Logger.log("");
        // Retrieve list and ask user if needed
        String query = dbHelper.getQuery(type, null, action);
        Intent i = new Intent(context, ReceiverChoiceActivity.class);
        i.setAction(Intent.ACTION_VIEW);
        i.putExtra("scp.query", query);
        i.putExtra("scp.caller", caller);
        i.putExtra("scp.type", type);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


    }

    private static String inferType(String methodName) {
        final String activity = "Activity";
        final String service = "Service";
        if (methodName.contains(activity))
            return activity;
        if (methodName.contains(service))
            return service;
        return null;
    }
}
