package com.example.einzelabgabe;

import android.os.AsyncTask;
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
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textViewAntwort);


        final Button button = findViewById(R.id.buttonAbschicken);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                EditText editText = (EditText) findViewById(R.id.editText);
                String text = editText.getText().toString();

                Proccess myProccess = new Proccess();
                myProccess.execute(text);
                try {
                    text = myProccess.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                textView.setText(text);

            }
        });

        final Button buttonBerechne = findViewById(R.id.buttonBerechne);
        buttonBerechne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                EditText editText = (EditText) findViewById(R.id.editText);

                String text = editText.getText().toString();
                int finalNumber = 1;
                for(int i = 0; i<text.length(); i++){
                    int number = (text.charAt(i))-48;
                    if(number == 0){
                        number = 1;
                    }
                    if(number%2 == 0){
                        finalNumber = finalNumber * number;
                    }


                }

                if(finalNumber == 1){
                    finalNumber = 0;
                }

                textView.setText("Das Produkt ist: " + finalNumber);

            }
        });


    }
}


class Proccess extends AsyncTask<String, Void, String> {
    String modifiedSentence;

    @Override
    protected String doInBackground(String... arg0) {
       // final TextView textView = (TextView) findViewById(R.id.textViewAntwort);
        String text = arg0[0];
        System.out.print(text);
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("se2-isys.aau.at",53212);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.writeBytes( text+ '\n');

            modifiedSentence = inFromServer.readLine();

            System.out.println("From Server: " + modifiedSentence);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return modifiedSentence;
    }

}