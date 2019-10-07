package com.example.popularmoviesapp.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = MovieItem.class, version = 1, exportSchema = false)
public abstract class MovieItemDatabase extends RoomDatabase {

    private static MovieItemDatabase instance;
    public abstract MovieItemDao movieItemDao();

    public static synchronized MovieItemDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieItemDatabase.class, "movie_item_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static  RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDbAsyncTask(instance).execute();

        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieItemDao movieItemDao;

        private PopulateDbAsyncTask(MovieItemDatabase db) {
            movieItemDao = db.movieItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieItemDao.insert(new MovieItem("test", "testposter", "test desc", "backimage test", "user rating test", "release date", "1"));
            return null;
        }
    }
}
