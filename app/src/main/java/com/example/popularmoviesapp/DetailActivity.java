package com.example.popularmoviesapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmoviesapp.adapters.RecyclerViewReviewsAdapter;
import com.example.popularmoviesapp.model.MovieItem;
import com.example.popularmoviesapp.model.Review;
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
    private RecyclerView mRecyclerView;
    private RecyclerViewReviewsAdapter adapter;
    private ProgressBar mProgressBar;
    private int mMovieId;
    private Context mContext;

    ArrayList<Review> mReviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.tv_title_under);
        backdropImage = findViewById(R.id.iv_backdrop_image);
        description = findViewById(R.id.tv_description_text);
        userRating = findViewById(R.id.tv_user_rating);
        releaseDate = findViewById(R.id.tv_release_date);

        populateDetailUi();

        mRecyclerView = findViewById(R.id.rv_reviews);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DetailActivity.this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewReviewsAdapter(DetailActivity.this, mReviews);
        mRecyclerView.setAdapter(adapter);
        mProgressBar = findViewById(R.id.pb_reviews);
        mContext = this;

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
        if(NetworkUtils.isNetworkAvailable(DetailActivity.this)){
            new MovieDbQueryTask().execute(movieReviewsEndpointUrl);
        } else {
            //fail
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
//                mRecyclerView.setVisibility(View.VISIBLE);
//                mProgressBar.setVisibility(View.INVISIBLE);
                try {
                   mReviews = MovieDbJsonUtils.getArrayListMovieReviews(jsonString, (String) title.getText());
                   adapter.setReviewsList(mReviews);
                   adapter.notifyDataSetChanged();
                   URL movieTrailersEndpointUrl = NetworkUtils.buildDetailPageUrls(mContext, mMovieId, "trailers");
                   new MovieDbQueryTaskTrailers().execute(movieTrailersEndpointUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                System.out.println(jsonString);
//                try {
////                    mReviews = MovieDbJsonUtils.getArrayListMovieReviews(jsonString, (String) title.getText());
////                    adapter.setReviewsList(mReviews);
////                    adapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            } else {
                //fail
            }
        }
    }
}
