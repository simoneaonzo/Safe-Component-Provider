package it.unige.cseclab;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class SimoTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simo_test);
        Toast.makeText(getApplicationContext(), "it works!!!", Toast.LENGTH_LONG).show();
    }
}
