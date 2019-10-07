package com.example.popularmoviesapp.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieItemRepository {
    private MovieItemDao movieItemDao;
    private LiveData<List<MovieItem>> allMovieItems;

    public MovieItemRepository(Application application) {
        MovieItemDatabase database = MovieItemDatabase.getInstance(application);
        movieItemDao = database.movieItemDao();
        allMovieItems = movieItemDao.getAllMovieItems();
    }

    public void insert(MovieItem movieItem) {
        new InsertMovieItemAsyncTask(movieItemDao).execute(movieItem);
    }

    public void update(MovieItem movieItem) {
        new UpdateMovieItemAsyncTask(movieItemDao).execute(movieItem);
    }

    public void delete(MovieItem movieItem) {
        new DeleteMovieItemAsyncTask(movieItemDao).execute(movieItem);
    }

    public void deleteAllMovieItems() {
        new DeleteAllMovieItemsAsyncTask(movieItemDao).execute();
    }

    public LiveData<List<MovieItem>> getAllMovieItems() {
        return  allMovieItems;
    }

    private static class InsertMovieItemAsyncTask extends AsyncTask<MovieItem, Void, Void> {
        private MovieItemDao movieItemDao;

        private InsertMovieItemAsyncTask(MovieItemDao movieItemDao) {
            this.movieItemDao  = movieItemDao;
        }

        @Override
        protected Void doInBackground(MovieItem... movieItems) {
            System.out.println("do in backgorund DAO------" + movieItems[0]);
            movieItemDao.insert(movieItems[0]);
            return null;
        }
    }

    private static class UpdateMovieItemAsyncTask extends AsyncTask<MovieItem, Void, Void> {
        private MovieItemDao movieItemDao;

        private UpdateMovieItemAsyncTask(MovieItemDao movieItemDao) {
            this.movieItemDao  = movieItemDao;
        }

        @Override
        protected Void doInBackground(MovieItem... movieItems) {
            movieItemDao.update(movieItems[0]);
            return null;
        }
    }

    private static class DeleteMovieItemAsyncTask extends AsyncTask<MovieItem, Void, Void> {
        private MovieItemDao movieItemDao;

        private DeleteMovieItemAsyncTask(MovieItemDao movieItemDao) {
            this.movieItemDao  = movieItemDao;
        }

        @Override
        protected Void doInBackground(MovieItem... movieItems) {
            movieItemDao.delete(movieItems[0]);
            return null;
        }
    }

    private static class DeleteAllMovieItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieItemDao movieItemDao;

        private DeleteAllMovieItemsAsyncTask(MovieItemDao movieItemDao) {
            this.movieItemDao  = movieItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieItemDao.deleteAllMovieItems();
            return null;
        }
    }
}
