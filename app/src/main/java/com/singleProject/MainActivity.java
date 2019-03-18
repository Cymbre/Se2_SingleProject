package com.singleProject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    Button buttonSend;
    TextView textViewNumber;
    EditText editTextInputNumber;
    TextView textViewResponse;

    Boolean threadIsRunning;

    public static final String ACTION_SHOW_TEXT= "showText";

    BroadcastReceiver  mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textViewResponse.setText(
                    intent.getStringExtra("response")
            );
            threadIsRunning = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = findViewById(R.id.sendToServer);
        textViewNumber = findViewById(R.id.textNumber);
        editTextInputNumber = findViewById(R.id.inputNumber);
        textViewResponse = findViewById(R.id.serverResponse);

        threadIsRunning = false;

        buttonSend.setText(R.string.button_send);
        textViewNumber.setText(R.string.text_mtrknr);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!threadIsRunning) {
                    RequestServerThread requestServerThread =
                            new RequestServerThread(
                                    editTextInputNumber.getText().toString(),getApplicationContext()
                            );
                    new Thread(requestServerThread).start();
                    threadIsRunning = true;
                }
                multiplyEvenIndices();
            }
        });

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, new IntentFilter(ACTION_SHOW_TEXT));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    /**
     * 51813425 mod 7 = 6
     *
     */
    private void multiplyEvenIndices(){
        char[] number = editTextInputNumber.getText().toString().toCharArray();
        int erg = 1;
        for (int i = 0; i < number.length; i++) {
            if(Integer.valueOf(number[i])%2==0?true:false){
                    erg*=i;
            }
        }
        Toast.makeText(getApplicationContext(),"Erg: "+ erg,Toast.LENGTH_LONG).show();
        Log.d("ClientOutput",erg + "");
    }
}


