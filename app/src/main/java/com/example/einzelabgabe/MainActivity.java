package com.example.einzelabgabe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity  {

    String masage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textViewAntwort);
        final EditText editText = (EditText) findViewById(R.id.editText);



        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                EditText editText = (EditText) findViewById(R.id.editText);
                textView.setText(editText.getText().toString());

                String sentence;
                String modifiedSentence;

               // BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

                NetworkThread nt = new NetworkThread();

                nt.start();

                try {
                    nt.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}

class NetworkThread extends Thread{

    String modifiedSentence;
    public void run(){
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("se2-isys.aau.at",53212);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //   sentence = inFromUser.readLine();

            outToServer.writeBytes("1626008" + '\n');

            modifiedSentence = inFromServer.readLine();

            System.out.println("From Server: " + modifiedSentence);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}