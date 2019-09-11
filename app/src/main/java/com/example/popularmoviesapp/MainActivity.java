package com.example.popularmoviesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.popularmoviesapp.Utilities.MovieDbJsonUtils;
import com.example.popularmoviesapp.Utilities.NetworkUtils;
import com.example.popularmoviesapp.Utilities.RecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    ArrayList<String> movieTitles = new ArrayList<>();
    ArrayList<String> moviePosters = new ArrayList<>();
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        button = (Button) findViewById(R.id.button1);

        mRecyclerView = findViewById(R.id.rv_listview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        //to avoid error: recyclerview No adapter attached; skipping layout
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, movieTitles, moviePosters);
        mRecyclerView.setAdapter(adapter);

        fetchMovieData("popularity.desc");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMovieData("vote_count.desc");
            }
        });
    }

    private void fetchMovieData(String sortBy) {
        URL githubSearchUrl = NetworkUtils.buildUrl(this, sortBy);
        System.out.println(githubSearchUrl);
        new MovieDbQueryTask().execute(githubSearchUrl);
    }


    class MovieDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            button.setText("loading...");
            mRecyclerView.setVisibility(View.INVISIBLE);
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
        protected void onPostExecute(String jsonString) {
            if(jsonString != null && !jsonString.equals("")) {
                ArrayList<String> moviePostersPartialUrl = new ArrayList<>();
                button.setText("Button");
                mRecyclerView.setVisibility(View.VISIBLE);
                try {
                    movieTitles = MovieDbJsonUtils.getArrayListStringFromJson(MainActivity.this, jsonString, "original_title");
                    moviePostersPartialUrl = MovieDbJsonUtils.getArrayListStringFromJson(MainActivity.this, jsonString, "poster_path");
                    moviePosters = MovieDbJsonUtils.createMoviePosterUrls(moviePostersPartialUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, movieTitles, moviePosters);
                mRecyclerView.setAdapter(adapter);

            }
        }
    }
}