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
    RecyclerView lvText;
    ArrayList<String> movieTitles = new ArrayList<>();
    String[] moviePosters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        lvText =  (RecyclerView) findViewById(R.id.rv_listview);
        button = (Button) findViewById(R.id.button1);

        fetchMovieData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMovieData();
            }
        });
    }

    private void fetchMovieData() {
        URL githubSearchUrl = NetworkUtils.buildUrl(this,"popularity.desc");
        System.out.println(githubSearchUrl);
        new MovieDbQueryTask().execute(githubSearchUrl);
    }


    class MovieDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            button.setText("loading...");
            lvText.setVisibility(View.INVISIBLE);
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
                button.setText("Button");
                lvText.setVisibility(View.VISIBLE);
                try {
                    movieTitles = MovieDbJsonUtils.getArrayStringFromJson(MainActivity.this, jsonString, "original_title");
                   // moviePosters = MovieDbJsonUtils.getArrayStringFromJson(MainActivity.this, jsonString, "poster_path");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, movieTitles);
//                ListView listView = (ListView) findViewById(R.id.listview1);
//                listView.setAdapter(adapter);

//                MyAdpater adapter = new MyAdpater(MainActivity.this, movieTitles, moviePosters);
//                ListView listView = findViewById(R.id.listview1);
//                listView.setAdapter(adapter);

                RecyclerView recyclerView = findViewById(R.id.rv_listview);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, movieTitles);
                recyclerView.setAdapter(adapter);
                //recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
                recyclerView.setLayoutManager(layoutManager);

            }
        }
    }

    class MyAdpater extends ArrayAdapter<String> {
        Context context;
        String rMovieTitles[];
        String rMoviePosters[];


        public MyAdpater(Context c, String movieTitles[], String moviePosters[]) {
            super(c, R.layout.list_item, R.id.tv_label, movieTitles);
            this.context = c;
            this.rMovieTitles = movieTitles;
            this.rMoviePosters = moviePosters;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_item, parent, false);
            TextView myTitle = row.findViewById(R.id.tv_label);
            TextView myPoster = row.findViewById(R.id.tv_poster);

            myTitle.setText(rMovieTitles[position]);
            myPoster.setText(rMoviePosters[position]);

            return row;
        }
    }

}