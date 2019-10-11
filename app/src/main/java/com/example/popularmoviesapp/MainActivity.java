package com.example.popularmoviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ArrayList<MovieItem> mMovieItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerViewMovieItemsAdapter adapter;
    private ProgressBar mProgressBar;
    private Button mRetryBtn;
    private String mSortOrder;
    //private Bundle mSavedInstanceState;
    private GridLayoutManager mLayoutManager;

    private boolean mScrollReachedBottom = false;
    private int mCurrentPage = 1;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private FavouriteMovieDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("-------- onCreate ---------");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mRecyclerView = findViewById(R.id.rv_listview);
        mProgressBar = findViewById(R.id.pb_progressbar);
        mRetryBtn = findViewById(R.id.btn_retry);
        mLayoutManager = new GridLayoutManager(MainActivity.this,2);
        //mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new RecyclerViewMovieItemsAdapter(MainActivity.this, mMovieItems);
        mRecyclerView.setAdapter(adapter);


        mDb = FavouriteMovieDatabase.getInstance(getApplicationContext());

        //initialize data fetching
        if(savedInstanceState != null && savedInstanceState.getString("sort_order") != null){
            //mSavedInstanceState = savedInstanceState;
            String sortOrder = savedInstanceState.getString("sort_order");
            System.out.println(sortOrder);
            String movieItemsString = savedInstanceState.getString("movie_items");
            mSortOrder = sortOrder;

            if(!sortOrder.equals("action_sort_favourite")) {
                try{
                    mMovieItems = MovieDbJsonUtils.getArrayListMovieItemsSavedState(movieItemsString);
                    adapter.setMovieList(mMovieItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (sortOrder.equals("action_sort_favourite")) {
                fetchFavouriteMovies();
            }
            //fetchMoviesBySortOrder(sortOrder);
        } else {
            System.out.println("no savedInstanceState action_sort_popular");
            mSortOrder = "action_sort_popular";
            fetchMovieData("popular");
        }

        scrollListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sort_order", mSortOrder);
        //outState.putParcelableArrayList("movies_list", mMovieItems);
        //outState.putParcelable("ListState", mRecyclerView.getLayoutManager().onSaveInstanceState());
        Gson gson = new Gson();
        outState.putString("movie_items", gson.toJson(mMovieItems));
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
                if(mSortOrder.equals("action_sort_favourite")) {
                    adapter.setMovieList(movieItems);
                    adapter.notifyDataSetChanged();
                    //adapter.notifyItemInserted(mMovieItems.size() - 1);
                }
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
        mRecyclerView.smoothScrollToPosition(0);
        mScrollReachedBottom = false;
        mCurrentPage = 1;
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
        URL movieDataEndpointUrl = NetworkUtils.buildUrl(this, sortBy, 1);
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

    public String getShortSortName(String longSortName) {
        String shortSortName = "";
        if(longSortName.equals("action_sort_popular"))
            shortSortName = "popular";
        else if (longSortName.equals("action_sort_top_rated"))
            shortSortName = "top_rated";
        return shortSortName;
    }

    public void scrollListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!mScrollReachedBottom) {
                        if ( (visibleItemCount + pastVisibleItems) >= totalItemCount && !mSortOrder.equals("action_sort_favourite")) {
                            URL movieDataEndpointUrl = NetworkUtils.buildUrl(MainActivity.this, getShortSortName(mSortOrder), mCurrentPage);
                            mScrollReachedBottom = true;
                            mProgressBar.setVisibility(View.VISIBLE);
                            if(NetworkUtils.isNetworkAvailable(MainActivity.this)){
                                new MovieDbQueryTask().execute(movieDataEndpointUrl);
                            } else {
                                connectionFailed();
                            }
                        }
                    }
                }
            }
        });
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
                    if(mCurrentPage == 1)
                        adapter.setMovieList(mMovieItems);
                    else
                        adapter.addToMoviesList(mMovieItems);

                    adapter.notifyDataSetChanged();
                    mScrollReachedBottom = false;
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mCurrentPage++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                connectionFailed();
            }
        }
    }
}