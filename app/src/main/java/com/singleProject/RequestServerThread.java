package com.singleProject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Handler;

import static java.lang.System.out;

public class RequestServerThread implements Runnable {

    private String number;
    Context context;

    RequestServerThread(String number, Context context){
        this.number = number;
        this.context = context;
    }
    @Override
    public void run() {
        String sentence;
        String modifiedSentence;
        PrintWriter out;

        try{

            Socket clientSocket = new Socket("se2-isys.aau.at",53212);

            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

            BufferedReader inFromServer = new BufferedReader(
                   new InputStreamReader(clientSocket.getInputStream()));
            out.println(number);
            modifiedSentence = inFromServer.readLine();
            Log.d("ServerOutput",modifiedSentence);

            clientSocket.close();
            Intent i = new Intent(MainActivity.ACTION_SHOW_TEXT);
            i.putExtra("response", modifiedSentence);
            LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(i);

        }catch (Exception e){
            Log.e("ServerOutput", e.getMessage());
        }


    }
}
