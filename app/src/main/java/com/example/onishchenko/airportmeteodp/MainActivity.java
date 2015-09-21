package com.example.onishchenko.airportmeteodp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String toast;
    TextView temperature,
             dewPoint,
             humidity,
             press,
             wind,
             changes,
             weatherTime;
    ProgressBar progressBar;
    ImageButton refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        temperature = (TextView) findViewById(R.id.temperature);
        dewPoint = (TextView) findViewById(R.id.dewPoint);
        humidity = (TextView) findViewById(R.id.humidity);
        press = (TextView) findViewById(R.id.press);
        wind = (TextView) findViewById(R.id.wind);
        changes = (TextView) findViewById(R.id.changes);
        weatherTime = (TextView) findViewById(R.id.weatherTime);
        refreshButton = (ImageButton) findViewById(R.id.refreshButton);
        toast = getResources().getString(R.string.toast);
        refreshClick(refreshButton);
    }

    public void refreshClick(View v) {
        RefreshTask refreshTask = new RefreshTask();
        refreshTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    class RefreshTask extends AsyncTask<Void, String, ArrayList> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            refreshButton.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            String errMsg = progress[0];
            Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            ArrayList<String> values = new ArrayList<>();
            try {
                String url = "http://meteopost.com/weather/dnepropetrovsk/";
                Document document = Jsoup.connect(url).timeout(4000).get();
                if (document != null) {
                    Elements nextTurns = document.select("table table table table tr td:eq(1)");
                    for (Element nextTurn : nextTurns) {
                        values.add(nextTurn.text());
                    }
                }
                //HTML to strings parsing

            }  catch (Exception e) {
                e.printStackTrace();
                publishProgress(toast);
            }

            return values;
        }

        @Override
        protected void onPostExecute(ArrayList values) {
            super.onPostExecute(values);
            if (!values.isEmpty()) {
                temperature.setText(values.get(1).toString());
                dewPoint.setText(values.get(3).toString());
                humidity.setText(values.get(4).toString());
                press.setText(values.get(5).toString());
                wind.setText(values.get(8).toString());
                changes.setText(values.get(12).toString());
                weatherTime.setText(getString(R.string.city) +
                        ", погода на " + values.get(0).toString());
            }

            refreshButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }



}
