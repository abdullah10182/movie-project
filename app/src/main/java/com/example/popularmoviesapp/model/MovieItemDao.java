package com.example.popularmoviesapp.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MovieItemDao {
    @Query("SELECT * FROM favourite_movies ORDER BY id")
    LiveData<List<MovieItem>> fetchAllMovies();

    @Insert
    void insertMovie(MovieItem favouriteMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieItem favouriteMovie);

    @Delete
    void deleteMovie(MovieItem FavouriteMovie);

    @Query("SELECT * FROM favourite_movies WHERE id = :id")
    LiveData<MovieItem> loadMoviesById(int id);
}
