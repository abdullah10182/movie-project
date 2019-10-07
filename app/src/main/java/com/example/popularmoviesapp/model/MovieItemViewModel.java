package com.example.popularmoviesapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieItemViewModel extends AndroidViewModel {
    private MovieItemRepository repository;
    private LiveData<List<MovieItem>> allMovieItems;

    public MovieItemViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieItemRepository(application);
        allMovieItems = repository.getAllMovieItems();
    }

    public void insert(MovieItem movieItem) {
        repository.insert(movieItem);
    }

    public void update(MovieItem movieItem) {
        repository.update(movieItem);
    }

    public void delete(MovieItem movieItem) {
        repository.delete(movieItem);
    }

    public void deleteAllMovieItems() {
        repository.deleteAllMovieItems();
    }

    public LiveData<List<MovieItem>> getAllMovieItems() {
        return getAllMovieItems();
    }
}
