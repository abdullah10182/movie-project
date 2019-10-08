package com.example.popularmoviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;
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
    private static final String TAG = "MainActivity";

    ArrayList<MovieItem> mMovieItems = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerViewMovieItemsAdapter adapter;
    ProgressBar mProgressBar;
    Button mRetryBtn;
    String mSortOrder;
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
        if(savedInstanceState != null && savedInstanceState.getString("sort_order") != null){
            String sortOrder = savedInstanceState.getString("sort_order");
            mSortOrder = sortOrder;
            fetchMoviesBySortOrder(sortOrder);
        } else {
            System.out.println("no savedInstanceState action_sort_popular");
            mSortOrder = "action_sort_popular";
            fetchMovieData("popular");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sort_order", mSortOrder);
    }

    public void fetchMoviesBySortOrder(String sortOrder) {
        switch (sortOrder) {
            case "action_sort_popular":
                fetchMovieData("popular");
                return;
            case "action_sort_top_rated":
                fetchMovieData("top_rated");
                return;
            case "action_sort_favourite":
                fetchFavouriteMovies();
                return;
        }
    }

    public void fetchFavouriteMovies(){

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovieItems().observe(this, new Observer<List<MovieItem>>() {
            @Override
            public void onChanged(List<MovieItem> movieItems) {
                Log.d(TAG, "updating data from LiveData in ViewModel");
                adapter.setMovieList(movieItems);
                adapter.notifyDataSetChanged();
            }
        });

        if(!NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRetryBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int resID = getResources().getIdentifier(mSortOrder, "id", getPackageName());
        menu.findItem(resID).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mSortOrder = getResources().getResourceName(item.getItemId()).split("\\/")[1];
        switch (mSortOrder) {
            case "action_sort_popular":
                fetchMovieData("popular");
                item.setChecked(true);
                return true;
            case "action_sort_top_rated":
                fetchMovieData("top_rated");
                item.setChecked(true);
                return true;
            case "action_sort_favourite":
                mRecyclerView.setVisibility(View.VISIBLE);
                fetchFavouriteMovies();
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchMovieData(String sortBy) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRetryBtn.setVisibility(View.INVISIBLE);
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
        //fetchMovieData("popular");
        fetchMoviesBySortOrder(mSortOrder);
    }

    public void connectionFailed() {
        Toast.makeText(getApplicationContext(), "No connection to the internet", Toast.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.INVISIBLE);
        if(mSortOrder == "action_sort_favourite"){
            mRecyclerView.setVisibility(View.VISIBLE);
            mRetryBtn.setVisibility(View.INVISIBLE);
        } else{
            mRetryBtn.setVisibility(View.VISIBLE);
        }
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