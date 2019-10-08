package com.example.popularmoviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.popularmoviesapp.model.FavouriteMovieDatabase;
import com.example.popularmoviesapp.model.MovieItem;
import com.example.popularmoviesapp.utilities.MovieDbJsonUtils;
import com.example.popularmoviesapp.utilities.NetworkUtils;
import com.example.popularmoviesapp.adapters.RecyclerViewMovieItemsAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<MovieItem> mMovieItems = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerViewMovieItemsAdapter adapter;
    ProgressBar mProgressBar;
    Button mRetryBtn;
    private FavouriteMovieDatabase mDb;

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
        mProgressBar = findViewById(R.id.pb_progressbar);
        mRetryBtn = findViewById(R.id.btn_retry);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewMovieItemsAdapter(MainActivity.this, mMovieItems);
        mRecyclerView.setAdapter(adapter);

        mDb = FavouriteMovieDatabase.getInstance(getApplicationContext());

        //initialize data fetching
        fetchMovieData("popular");

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void fetchFavouriteMovies(){
        System.out.println("===========query database=============");
        final LiveData<List<MovieItem>> favouriteMovieItems = mDb.favouriteMovieDao().fetchAllMovies();
        favouriteMovieItems.observe(this, new Observer<List<MovieItem>>() {
            @Override
            public void onChanged(List<MovieItem> movieItems) {
                adapter.setMovieList(movieItems);
                adapter.notifyDataSetChanged();

            }
        });
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
                fetchMovieData("popular");
                item.setChecked(true);
                return true;
            case R.id.action_sort_top_rated:
                fetchMovieData("top_rated");
                item.setChecked(true);
                return true;
            case R.id.action_sort_favourite:
                fetchFavouriteMovies();
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void fetchMovieData(String sortBy) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        URL movieDataEndpointUrl = NetworkUtils.buildUrl(this, sortBy);
        if(NetworkUtils.isNetworkAvailable(MainActivity.this)){
            new MovieDbQueryTask().execute(movieDataEndpointUrl);
        } else {
            connectionFailed();
        }
    }

    public void retryFetchData(View view) {
        mRetryBtn.setVisibility(View.INVISIBLE);
        fetchMovieData("popular");

    }

    public void connectionFailed() {
        Toast.makeText(getApplicationContext(), "No connection to the internet", Toast.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.INVISIBLE);
        mRetryBtn.setVisibility(View.VISIBLE);
    }

    class MovieDbQueryTask extends AsyncTask<URL, Void, String> {

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
                mProgressBar.setVisibility(View.INVISIBLE);
                try {
                    mMovieItems = MovieDbJsonUtils.getArrayListMovieItems(jsonString);
                    adapter.setMovieList(mMovieItems);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                connectionFailed();
            }
        }
    }
}