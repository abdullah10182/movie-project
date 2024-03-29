package com.example.popularmoviesapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesapp.adapters.RecyclerViewReviewsAdapter;
import com.example.popularmoviesapp.adapters.RecyclerViewTrailersAdapter;
import com.example.popularmoviesapp.model.FavouriteMovieDatabase;
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

import static java.lang.Integer.parseInt;

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
    private boolean reviewsLoaded = false;
    private boolean trailersLoaded = false;
    private FavouriteMovieDatabase mDb;
    MovieItem mCurrentMovieItem;
    private boolean movieIsFavourite;
    private Button mFavouriteButton;
    private View dividerTrailers;
    private View dividerReviews;
    private TextView tvTitleTrailers;
    private TextView tvTitleReviews;
    private Button btnRetryAdditionalData;

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
        mFavouriteButton = findViewById(R.id.btn_favourite);
        dividerReviews = findViewById(R.id.divider_reviews);
        dividerTrailers = findViewById(R.id.divider_trailers);
        tvTitleReviews = findViewById(R.id.tv_title_reviews);
        tvTitleTrailers = findViewById(R.id.tv_title_trailers);
        btnRetryAdditionalData = findViewById(R.id.btn_retry_additional_data);

        populateDetailUi();

        RecyclerView.LayoutManager layoutManagerReviews = new GridLayoutManager(DetailActivity.this,2);
        mRecyclerViewReviews.setLayoutManager(layoutManagerReviews);
        adapterReviews = new RecyclerViewReviewsAdapter(DetailActivity.this, mReviews);
        mRecyclerViewReviews.setAdapter(adapterReviews);

        RecyclerView.LayoutManager layoutManagerTrailers = new GridLayoutManager(DetailActivity.this,2);
        mRecyclerViewTrailers.setLayoutManager(layoutManagerTrailers);
        adapterTrailers = new RecyclerViewTrailersAdapter(DetailActivity.this, mTrailers);
        mRecyclerViewTrailers.setAdapter(adapterTrailers);

        mDb = FavouriteMovieDatabase.getInstance(getApplicationContext());
        movieIsFavourite();

    }

    public void movieIsFavourite(){
        final Drawable heartBorder = getResources().getDrawable( R.drawable.ic_favorite_border_black_24dp );
        final Drawable heartFilled = getResources().getDrawable( R.drawable.ic_favorite_orange_24dp );

        final LiveData<MovieItem> favouriteMovieDb = mDb.favouriteMovieDao().loadMoviesById(mCurrentMovieItem.getId());
        favouriteMovieDb.observe(this, new Observer<MovieItem>() {
            @Override
            public void onChanged(MovieItem movieItem) {
                if(movieItem == null ){
                    movieIsFavourite = false;
                    //mFavouriteButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mFavouriteButton.setCompoundDrawablesWithIntrinsicBounds( heartBorder, null, null, null );
                    mFavouriteButton.setText("Add to \n favourites");
                } else {
                    movieIsFavourite = true;
                    mFavouriteButton.setCompoundDrawablesWithIntrinsicBounds( heartFilled, null, null, null );
                    //mFavouriteButton.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                    mFavouriteButton.setText("Favourite");
                }
            }
        });

    }

    public void saveFavouriteMovie(View v){
        final MovieItem favouriteMovie = new MovieItem(
                mCurrentMovieItem.getId(),
                mCurrentMovieItem.getTitle(),
                mCurrentMovieItem.getPoster(),
                mCurrentMovieItem.getDescription(),
                mCurrentMovieItem.getBackdropImage(),
                mCurrentMovieItem.getUserRating(),
                mCurrentMovieItem.getReleaseDate()
        );


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<MovieItem> favouriteMovieDb = mDb.favouriteMovieDao().loadMoviesById(mCurrentMovieItem.getId());
                if(movieIsFavourite){
                    System.out.println("removed from favs");
                    mDb.favouriteMovieDao().deleteMovie(favouriteMovie);
                } else {
                    mDb.favouriteMovieDao().insertMovie(favouriteMovie);
                    System.out.println("added to favs");
                }
            }
        });
    }

    private void populateDetailUi() {
        Intent mIntent = getIntent();
        MovieItem movieItem = (MovieItem) mIntent.getSerializableExtra("movieItem");
        mCurrentMovieItem = movieItem;

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
            errorLoadingData();
        }

    }

    private void displayTrailersAndReviewsIfReady() {
        if(trailersLoaded && reviewsLoaded){
            hideProgressLoader();
        }
    }

    public void hideProgressLoader(){
        mRecyclerViewReviews.setVisibility(View.VISIBLE);
        mRecyclerViewTrailers.setVisibility(View.VISIBLE);
        dividerReviews.setVisibility(View.VISIBLE);
        //dividerTrailers.setVisibility(View.VISIBLE);
        tvTitleReviews.setVisibility(View.VISIBLE);
        tvTitleTrailers.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void errorLoadingData(){
        mProgressBar.setVisibility(View.INVISIBLE);
        btnRetryAdditionalData.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), "No internet connection, cannot retrieve additional movie data", Toast.LENGTH_LONG).show();
    }

    public void retryFetchData(View view) {
        btnRetryAdditionalData.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        fetchMovieDetails(mCurrentMovieItem.getId());
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
                reviewsLoaded = true;
                displayTrailersAndReviewsIfReady();
            } else {
                hideProgressLoader();
                errorLoadingData();
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
                trailersLoaded = true;
                displayTrailersAndReviewsIfReady();
            } else {
                hideProgressLoader();
                errorLoadingData();
            }
        }
    }
}
