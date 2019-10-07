package com.example.popularmoviesapp.utilities;

import com.example.popularmoviesapp.model.MovieItem;
import com.example.popularmoviesapp.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDbJsonUtils {

    public static ArrayList<MovieItem> getArrayListMovieItems(String jsonStr) throws JSONException {
        ArrayList<MovieItem> movieItems = new ArrayList<>();
        String prefixPosterUrl = "https://image.tmdb.org/t/p/w185/";
        String prefixBackdropImageUrl = "https://image.tmdb.org/t/p/w500/";

        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray resultsArray = jsonObj.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject resultObject = resultsArray.getJSONObject(i);
            String title = resultObject.getString("original_title");
            String poster = prefixPosterUrl + resultObject.getString("poster_path");
            String description = resultObject.getString("overview");
            String backdropImage = prefixBackdropImageUrl + resultObject.getString("backdrop_path");
            String userRating = resultObject.getString("vote_average");
            String releaseDate = resultObject.getString("release_date");
            String id = resultObject.getString("id");
            MovieItem movieItem = new MovieItem(title, poster, description, backdropImage, userRating, releaseDate, id);
            movieItems.add(movieItem);
        }
        return movieItems;
    }

    public static ArrayList<Review> getArrayListMovieReviews(String jsonStr, String movieTitle) throws JSONException {
        ArrayList<Review> reviews = new ArrayList<>();

        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray resultsArray = jsonObj.getJSONArray("results");

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject resultObject = resultsArray.getJSONObject(i);
            String author = resultObject.getString("author");
            String content = resultObject.getString("content");
            String url = resultObject.getString("url");
            String id = resultObject.getString("id");
            Review review = new Review(author, content, url, id);
            review.setMovieTitle(movieTitle);
            reviews.add(review);
        }
        return reviews;
    }

}


