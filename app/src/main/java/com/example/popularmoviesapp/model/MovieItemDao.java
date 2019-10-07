package com.example.popularmoviesapp.model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieItemDao {

    @Insert
    void insert(MovieItem movieItem);

    @Update
    void update(MovieItem movieItem);

    @Delete
    void delete(MovieItem movieItem);

    @Query("DELETE FROM movie_item_table")
    void deleteAllMovieItems();

    @Query("SELECT * FROM movie_item_table ORDER BY title")
    LiveData<List<MovieItem>> getAllMovieItems();

}
