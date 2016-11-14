package com.example.hirakchandravanshi.downloadapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.lang.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button but;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but = (Button)findViewById(R.id.but);
        txt = (TextView)findViewById(R.id.txt);
        //works on click of download button.
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "https://www.iiitd.ac.in/about";
                ConnectivityManager cm =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null && ni.isConnected()) {
                    new DownloadData().execute(str);
                } else {
                    System.out.println("Error in connection");
                    txt.setText("Error in connection");
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        String  s = txt.getText().toString();
        savedInstanceState.putString("text", s);
        super.onSaveInstanceState(savedInstanceState);
    }
    // function to restore state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String s2 = savedInstanceState.getString("text");
        txt.setText(s2);
    }
    //AsyncTask Runs in background and downloads the data from website
    private class DownloadData extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... uurl)
        {
            try{
                return down(uurl[0]);
            }catch(IOException e){
                return "Unable to process the request";
            }
        }
        @Override
        protected void onPostExecute(String DisplayData){
            System.out.println(DisplayData);
            txt.setText(DisplayData);
        }
    }

    //Function to download data from site and returning it as a string
    private String down(String uurrl) throws IOException{
        InputStream i = null;
        try {
            URL url = new URL(uurrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            i = urlConnection.getInputStream();
            String DataAsString = data(i);
            return DataAsString;
        } finally {
            if (i != null) {
                i.close();
            }
        }
    }
    // Function to convert inputstream to string.
    public String data(InputStream s) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        StringBuilder Build = new StringBuilder();
        String l;
        //String l = null;
        try {
            while ((l = br.readLine()) != null) {
                Build.append(l).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Build.toString();
    }
}
