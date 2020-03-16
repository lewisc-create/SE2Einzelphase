package com.example.tcp_test2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.tcp_test2.TcpClient;


public class MainActivity extends AppCompatActivity {

    Button btn_send;
    TcpClient mTcpClient;
    EditText et_matrNr;
    TextView tv_sreply;
    Button btn_Berechnung;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        btn_send = (Button)findViewById(R.id.btn_SendServer);
        et_matrNr = findViewById(R.id.etn_MatrikelNr);
        tv_sreply = findViewById(R.id.tv_serverResult);
        btn_Berechnung = (Button)findViewById(R.id.btn_buttonBerechnung);


        new connectTask().execute("");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = et_matrNr.getText().toString();

                //add the text in the arrayList


                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                btn_send.setEnabled(false);

            }
        });
        btn_Berechnung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message2 = et_matrNr.getText().toString();
                int messeage2InInteger = Integer.parseInt(message2);
                int quersummeWert=quersumme(messeage2InInteger); //Aufruf methode quersumme Rekusiv
                String ausgabeDisplygeradeUngerade="";
                if (quersummeWert%2==0){
                    ausgabeDisplygeradeUngerade="gerade";
                }else{
                    ausgabeDisplygeradeUngerade="UNgerade";
                }
                String quersummeWertString= Integer.toString(quersummeWert);
                tv_sreply.setText(ausgabeDisplygeradeUngerade);
            }
        });

    }


    public class connectTask extends AsyncTask<String,String,TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {
            //Objet TCPcliente und methodenaufruf ProgressUdate
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //Meassage von Server
                public void messageReceived(String message) {
                    //Methoden aufruf ProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //in Array List die Enthaltene Message von Server
            String test=values[0];
            // notiz an Adapter das neue Message erhalten
            tv_sreply.setText(test);
            //mTcpClient.stopClient();
        }
    }

    public static int quersumme(int zahl){
        if (zahl<=9)return zahl ;
        return zahl%10+quersumme(zahl/10);
    }

}

