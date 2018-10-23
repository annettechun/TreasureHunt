package com.example.hojun.treasurehunt;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Leaderboard extends AppCompatActivity {

    private TextView data;
    private TextView data2;
    private TextView data3;
    private TextView data4;
    private TextView data5;
    private TextView data6;
    private TextView data7;
    private TextView data8;
    private TextView data9;
    private TextView data10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Button btnHit = (Button)findViewById(R.id.btnHit);
        data = (TextView)findViewById(R.id.jsonItem);
        data2 = (TextView)findViewById(R.id.jsonItem2);
        data3 = (TextView)findViewById(R.id.jsonItem3);
        data4 = (TextView)findViewById(R.id.jsonItem4);
        data5 = (TextView)findViewById(R.id.jsonItem5);
        data6 = (TextView)findViewById(R.id.jsonItem6);
        data7 = (TextView)findViewById(R.id.jsonItem7);
        data8 = (TextView)findViewById(R.id.jsonItem8);
        data9 = (TextView)findViewById(R.id.jsonItem9);
        data10 = (TextView)findViewById(R.id.jsonItem10);

        new JSONTask().execute("http://plato.cs.virginia.edu/~amc4sq/");
        //btnHit.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {}
        //});
    }

    public class JSONTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params){
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                String result = "";
                try{
                    JSONArray array = new JSONArray(finalJson);
                    for(int i=0;i<array.length();i++){
                        JSONObject e = array.getJSONObject(i);
                        result+= e.getString("name") + " " + e.getString("score") + " ";
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

                //return buffer.toString();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            String[] results = result.split(" ");
            data.setText(results[0]);
            data.setTextColor(Color.parseColor("#000000"));
            data2.setText(results[1]);
            data2.setTextColor(Color.parseColor("#000000"));
            data3.setText(results[2]);
            data3.setTextColor(Color.parseColor("#000000"));
            data4.setText(results[3]);
            data4.setTextColor(Color.parseColor("#000000"));
            data5.setText(results[4]);
            data5.setTextColor(Color.parseColor("#000000"));
            data6.setText(results[5]);
            data6.setTextColor(Color.parseColor("#000000"));
            data7.setText(results[6]);
            data7.setTextColor(Color.parseColor("#000000"));
            data8.setText(results[7]);
            data8.setTextColor(Color.parseColor("#000000"));
            data9.setText(results[8]);
            data9.setTextColor(Color.parseColor("#000000"));
            data10.setText(results[9]);
            data10.setTextColor(Color.parseColor("#000000"));
        }
    }
}
