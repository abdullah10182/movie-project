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
public interface FavouriteMovieDao {
    @Query("SELECT * FROM favourite_movies ORDER BY id")
    List<FavouriteMovie> fetchAllMovies();

    @Insert
    void insertMovie(FavouriteMovie favouriteMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavouriteMovie favouriteMovie);

    @Delete
    void deleteMovie(FavouriteMovie FavouriteMovie);

    @Query("SELECT * FROM favourite_movies WHERE id = :id")
    FavouriteMovie loadMoviesById(int id);
}
