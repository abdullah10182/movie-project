package com.example.popularmoviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.popularmoviesapp.model.MovieItem;
import com.example.popularmoviesapp.utilities.MovieDbJsonUtils;
import com.example.popularmoviesapp.utilities.NetworkUtils;
import com.example.popularmoviesapp.utilities.RecyclerViewAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<MovieItem> mMovieItems = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mRecyclerView = findViewById(R.id.rv_listview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(MainActivity.this, mMovieItems);
        mRecyclerView.setAdapter(adapter);

        //initialize data fetching
        fetchMovieData("popularity.desc");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                fetchMovieData("popularity.desc");
                item.setChecked(true);
                return true;
            case R.id.action_sort_top_rated:
                fetchMovieData("vote_count.desc");
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void fetchMovieData(String sortBy) {
        URL githubSearchUrl = NetworkUtils.buildUrl(this, sortBy);
        new MovieDbQueryTask().execute(githubSearchUrl);
    }

    class MovieDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                mRecyclerView.setVisibility(View.VISIBLE);
                try {
                    mMovieItems = MovieDbJsonUtils.getArrayListMovieItems(MainActivity.this, jsonString);
                    adapter.setMovieList(mMovieItems);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}