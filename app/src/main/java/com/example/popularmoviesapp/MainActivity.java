package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.popularmoviesapp.Utilities.MovieDbJsonUtils;
import com.example.popularmoviesapp.Utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button button;
    ListView tvText;

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    String[] movieTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tvText = findViewById(R.id.listview1);

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();

//                JSONObject jsonObj;
//                try {
//                    jsonObj = new JSONObject(jsonString);
//                    movieTitles = MovieDbJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonString);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, movieTitles);
//
//                ListView listView = (ListView) findViewById(R.id.listview1);
//                listView.setAdapter(adapter);
            }
        });
    }

    private void test() {
        String apiKey = getResources().getString(R.string.the_moviedb_api_key);

        URL githubSearchUrl = NetworkUtils.buildUrl(apiKey);
        System.out.println(githubSearchUrl);
        //String githubSearchResults = null;
        new MovieDbQueryTask().execute(githubSearchUrl);

    }


    class MovieDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            button.setText("loading...");
        }

        @Override
        protected String doInBackground(URL... urls) {
            try {
                String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                return githubSearchResults;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null && !s.equals("")) {
                button.setText("Button");
                System.out.println(s);
                String jsonString = s;
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(jsonString);
                    movieTitles = MovieDbJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, movieTitles);

                ListView listView = (ListView) findViewById(R.id.listview1);
                listView.setAdapter(adapter);

            }
        }
    }

}