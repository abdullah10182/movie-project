package com.example.popularmoviesapp;

import android.app.Application;
import android.util.Log;

import com.example.popularmoviesapp.model.FavouriteMovieDatabase;
import com.example.popularmoviesapp.model.MovieItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    private LiveData<List<MovieItem>> movieItems;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FavouriteMovieDatabase favouriteMovieDatabase = FavouriteMovieDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Retrieving data from database");
        movieItems = favouriteMovieDatabase.favouriteMovieDao().fetchAllMovies();
    }

    public LiveData<List<MovieItem>> getMovieItems() {
        return movieItems;
    }
}
