package com.example.popularmoviesapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmoviesapp.adapters.RecyclerViewReviewsAdapter;
import com.example.popularmoviesapp.adapters.RecyclerViewTrailersAdapter;
import com.example.popularmoviesapp.model.MovieItem;
import com.example.popularmoviesapp.model.Review;
import com.example.popularmoviesapp.model.Trailer;
import com.example.popularmoviesapp.utilities.MovieDbJsonUtils;
import com.example.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ImageView backdropImage;
    private TextView description;
    private TextView userRating;
    private TextView title;
    private TextView releaseDate;
    private RecyclerView mRecyclerViewReviews;
    private RecyclerView mRecyclerViewTrailers;
    private RecyclerViewReviewsAdapter adapterReviews;
    private RecyclerViewTrailersAdapter adapterTrailers;
    private ProgressBar mProgressBar;
    private int mMovieId;
    private Context mContext;

    ArrayList<Review> mReviews = new ArrayList<>();
    ArrayList<Trailer> mTrailers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.tv_title_under);
        backdropImage = findViewById(R.id.iv_backdrop_image);
        description = findViewById(R.id.tv_description_text);
        userRating = findViewById(R.id.tv_user_rating);
        releaseDate = findViewById(R.id.tv_release_date);
        mContext = this;
        mProgressBar = findViewById(R.id.pb_reviews);
        mRecyclerViewReviews = findViewById(R.id.rv_reviews);
        mRecyclerViewTrailers = findViewById(R.id.rv_trailers);

        populateDetailUi();

        RecyclerView.LayoutManager layoutManagerReviews = new GridLayoutManager(DetailActivity.this,2);
        mRecyclerViewReviews.setLayoutManager(layoutManagerReviews);
        adapterReviews = new RecyclerViewReviewsAdapter(DetailActivity.this, mReviews);
        mRecyclerViewReviews.setAdapter(adapterReviews);

        RecyclerView.LayoutManager layoutManagerTrailers = new LinearLayoutManager(DetailActivity.this);
        mRecyclerViewTrailers.setLayoutManager(layoutManagerTrailers);
        adapterTrailers = new RecyclerViewTrailersAdapter(DetailActivity.this, mTrailers);
        mRecyclerViewTrailers.setAdapter(adapterTrailers);


    }

    private void populateDetailUi() {
        Intent mIntent = getIntent();
        MovieItem movieItem = (MovieItem) mIntent.getSerializableExtra("movieItem");

        title.setText(movieItem.getTitle());
        description.setText(movieItem.getDescription());
        userRating.append(movieItem.getUserRating());
        releaseDate.append(movieItem.getReleaseDate());
        Picasso.get().load(movieItem.getBackdropImage()).fit().centerCrop().into(backdropImage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(movieItem.getTitle());

        mMovieId = movieItem.getId();
        fetchMovieDetails(mMovieId);
    }

    private void fetchMovieDetails(int id){
        URL movieReviewsEndpointUrl = NetworkUtils.buildDetailPageUrls(this, id, "reviews");
        URL movieTrailersEndpointUrl = NetworkUtils.buildDetailPageUrls(this, id, "trailers");
        if(NetworkUtils.isNetworkAvailable(DetailActivity.this)){
            new MovieDbQueryTask().execute(movieReviewsEndpointUrl);
            new MovieDbQueryTaskTrailers().execute(movieTrailersEndpointUrl);
        } else {
            //fail
        }

    }

    private void displayTrailersAndReviewsIfReady() {
        if(mReviews.size() > 0 && mTrailers.size() > 0){
            mRecyclerViewReviews.setVisibility(View.VISIBLE);
            mRecyclerViewTrailers.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    class MovieDbQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            try {
                String movieReviews = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                return movieReviews;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if(jsonString != null && !jsonString.equals("")) {
                try {
                   mReviews = MovieDbJsonUtils.getArrayListMovieReviews(jsonString, (String) title.getText());
                   adapterReviews.setReviewsList(mReviews);
                   adapterReviews.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayTrailersAndReviewsIfReady();
            } else {
                //fail
            }
        }
    }

    class MovieDbQueryTaskTrailers extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            try {
                String movieReviews = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                return movieReviews;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if(jsonString != null && !jsonString.equals("")) {
                try {
                    mTrailers = MovieDbJsonUtils.getArrayListMovieTrailers(jsonString);
                    adapterTrailers.setTrailersList(mTrailers);
                    adapterTrailers.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayTrailersAndReviewsIfReady();
            } else {
                //fail
            }
        }
    }
}
