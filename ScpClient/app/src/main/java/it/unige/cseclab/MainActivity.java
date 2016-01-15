package it.unige.cseclab;

import com.uni.ailab.scp.scplib.ScpContext;
import com.uni.ailab.scp.scplib.ScpIntent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int CODE = 88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startSA = (Button) findViewById(R.id.buttonActivity);
        Button startSP = (Button) findViewById(R.id.buttonProvider);
        Button startSR = (Button) findViewById(R.id.buttonReceiver);
        Button startSS = (Button) findViewById(R.id.buttonService);
        Button startTA = (Button) findViewById(R.id.buttonTestActivity);
        Button startTS = (Button) findViewById(R.id.buttonTestService);
        Button startTB = (Button) findViewById(R.id.buttonTestBrowser);
        Button startTP = (Button) findViewById(R.id.buttonTestProvider);

        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {

                final Spinner componentSpinner = (Spinner) findViewById(R.id.spinner1);

                String component = componentSpinner.getSelectedItem().toString();
                if (component.compareTo(getString(R.string.none)) == 0)
                    component = "";

                switch (v.getId()) {
                    case R.id.buttonActivity: {
                        startScpActivity(v, component);
                        break;
                    }
                    case R.id.buttonProvider: {
                        startScpProvider(v, component);
                        break;
                    }
                    case R.id.buttonReceiver: {
                        startScpReceiver(v, component);
                        break;
                    }
                    case R.id.buttonService: {
                        startScpService(v, component);
                        break;
                    }
                    case R.id.buttonTestActivity: {
                        startTestActivity(v, component);
                        break;
                    }
                    case R.id.buttonTestService: {
                        startTestService(v, component);
                        break;
                    }
                    case R.id.buttonTestBrowser: {
                        startTestBrowser(v, component);
                        break;
                    }
                    case R.id.buttonTestProvider: {
                        startTestContentResolver(v, component);
                        break;
                    }
                }

            }
        };

        startSA.setOnClickListener(listener);
        startSP.setOnClickListener(listener);
        startSR.setOnClickListener(listener);
        startSS.setOnClickListener(listener);
        startTA.setOnClickListener(listener);
        startTS.setOnClickListener(listener);
        startTB.setOnClickListener(listener);
        startTP.setOnClickListener(listener);
    }

    /*
    sendBroadcast
    startActivity
    startActivityForResult
    startService
     */
    public void startScpActivity(View v, String cmp) {
        ScpIntent intent = new ScpIntent();
        intent.setAction(ScpIntent.ACTION_SCP);
        intent.putExtra("scp.caller", cmp);
        intent.putExtra("scp.type", "Activity");
        ScpContext c = new ScpContext(v.getContext());
        c.sendBroadcast(intent);
    }

    public void startScpProvider(View v, String cmp) {
        ScpIntent intent = new ScpIntent();
        intent.setAction(ScpIntent.ACTION_SCP);
        intent.putExtra("scp.caller", cmp);
        intent.putExtra("scp.type", "Provider");
        ScpContext c = new ScpContext(v.getContext());
        c.sendBroadcast(intent);
    }

    public void startScpReceiver(View v, String cmp) {
        ScpIntent intent = new ScpIntent();
        intent.setAction(ScpIntent.ACTION_SCP);
        intent.putExtra("scp.caller", cmp);
        intent.putExtra("scp.type", "Receiver");
        ScpContext c = new ScpContext(v.getContext());
        c.sendBroadcast(intent);
    }

    public void startScpService(View v, String cmp) {
        ScpIntent intent = new ScpIntent();
        intent.setAction(ScpIntent.ACTION_SCP);
        intent.putExtra("scp.caller", cmp);
        intent.putExtra("scp.type", "Service");
        ScpContext c = new ScpContext(v.getContext());
        c.sendBroadcast(intent);
    }

    //----------------------------------------------------------------------------------------------
    public void startTestActivity(View v, String cmp) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivityForResult(intent, CODE);
        SCPlibProxy.ScpStartActivityForResult(this, intent, CODE);
    }


    public void startTestService(View v, String cmp) {
        Intent intent = new Intent(this, TestService.class);
        startService(intent);
        SCPlibProxy.ScpStartService(this, intent);
    }

    public void startTestBrowser(View v, String cmp) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sixthevicious.wordpress.com/"));
        startActivity(browserIntent);
        SCPlibProxy.ScpStartActivity(this, browserIntent);
    }

    public void startSendBroadcast(View v, String cmp) {
        Intent intent = new Intent();
        intent.setAction("com.example.Broadcast");
        intent.putExtra("HighScore", 1000);
        sendBroadcast(intent);
        SCPlibProxy.ScpSendBroadcast(this, intent);
    }


    public void startTestContentResolver(View v, String cmp) {
        String AUTHORITIES = "com.uni.ailab.scp.secureManifest";
        Uri CONTENT_URI = Uri.parse("content://"+AUTHORITIES+"/components");
        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ciao", "bau");
        Uri res =SCPlibProxy.ScpCRinsert(this,contentResolver, CONTENT_URI, contentValues);
        assert (res!=null);
        /*
        contentResolver.insert(CONTENT_URI, contentValues);
        contentResolver.delete(CONTENT_URI, "", new String[]{});
        contentResolver.query(CONTENT_URI, new String[]{}, "", new String[]{}, "");
        contentResolver.update(CONTENT_URI, contentValues, "", new String[]{});
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("result");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
