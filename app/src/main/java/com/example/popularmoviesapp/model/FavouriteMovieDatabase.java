package com.example.popularmoviesapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavouriteMovie.class}, version = 1, exportSchema = false)
public abstract class FavouriteMovieDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavouriteMovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favouriteMoviesList";
    private static FavouriteMovieDatabase sInstance;

    public static FavouriteMovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouriteMovieDatabase.class, FavouriteMovieDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()///
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavouriteMovieDao favouriteMovieDao();
}
