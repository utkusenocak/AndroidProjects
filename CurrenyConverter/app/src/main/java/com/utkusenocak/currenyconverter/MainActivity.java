package com.utkusenocak.currenyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.beans.IndexedPropertyChangeEvent;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView cadTextView;
    private TextView usdTextView;
    private TextView jpyTextView;
    private TextView tryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cadTextView = findViewById(R.id.cadText);
        usdTextView = findViewById(R.id.usdText);
        jpyTextView = findViewById(R.id.jpyText);
        tryTextView = findViewById(R.id.tryText);
    }

    public void getRates(View view){

        DownloadData downloadData = new DownloadData();

        try {
            String url = "http://data.fixer.io/api/latest?access_key=8b04d88793146d51c56f40a3a7bcea7e&format=1";
            downloadData.execute(url);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0){
                    char character = (char) data;
                    result += character;

                    data = inputStreamReader.read();
                }

                return result;


            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String base = jsonObject.getString("base");
                String rates = jsonObject.getString("rates");
                JSONObject jsonObject1 = new JSONObject(rates);
                String turkishLira = jsonObject1.getString("TRY");
                String cad = jsonObject1.getString("CAD");
                String usd = jsonObject1.getString("USD");
                String jpy = jsonObject1.getString("JPY");

                cadTextView.setText("CAD: " + cad);
                usdTextView.setText("USD: " + usd);
                jpyTextView.setText("JPY: " + jpy);
                tryTextView.setText("TRY: " + turkishLira);
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
