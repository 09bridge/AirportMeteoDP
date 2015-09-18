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

    RefreshTask refreshtask;
    String toast;
    TextView temper,temperR,vlazh,pres,wind,changes,weathertime;
    ProgressBar progressBar;
    ImageButton refrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        temper = (TextView) findViewById(R.id.temper);
        temperR = (TextView) findViewById(R.id.temperR);
        vlazh = (TextView) findViewById(R.id.vlazh);
        pres = (TextView) findViewById(R.id.pres);
        wind = (TextView) findViewById(R.id.wind);
        changes = (TextView) findViewById(R.id.changes);
        weathertime = (TextView) findViewById(R.id.weathertime);
        refrButton = (ImageButton) findViewById(R.id.refrButton);
        toast = getResources().getString(R.string.toast);
        onClick(refrButton);
    }

    public void onClick(View v) {
        refreshtask = new RefreshTask();
        refreshtask.execute();
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
            refrButton.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            String errMsg = progress[0];
            Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            ArrayList<String> values = null;
            String url;
            try {
                values = new ArrayList<>();
                url = "http://meteopost.com/weather/dnepropetrovsk/";
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
            if (values.size() > 0) {
                temper.setText(values.get(1).toString());
                temperR.setText(values.get(3).toString());
                vlazh.setText(values.get(4).toString());
                pres.setText(values.get(5).toString());
                wind.setText(values.get(8).toString());
                changes.setText(values.get(12).toString());
                weathertime.setText(getString(R.string.city) +
                        ", погода на " + values.get(0).toString());
            }

            refrButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }



}
