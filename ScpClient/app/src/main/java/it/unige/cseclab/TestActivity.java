package it.unige.cseclab;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simo_test);
        Toast.makeText(getApplicationContext(), "it works!!!", Toast.LENGTH_LONG).show();
        Button b = (Button) findViewById(R.id.buttonBack);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                EditText editText = (EditText) findViewById(R.id.editText);
                String value = editText.getText().toString();
                returnIntent.putExtra("result", value);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
