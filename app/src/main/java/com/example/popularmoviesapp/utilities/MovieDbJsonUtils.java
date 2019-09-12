package com.example.popularmoviesapp.utilities;

import android.content.Context;

import com.example.popularmoviesapp.model.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDbJsonUtils {

    public static  ArrayList<MovieItem> getArrayListMovieItems(Context context, String jsonStr) throws  JSONException {
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
            MovieItem movieItem = new MovieItem(title, poster, description, backdropImage, userRating);
            movieItems.add(movieItem);
        }

        return movieItems;
    }

    public static ArrayList<String> getArrayListStringFromJson(Context context, String jsonStr, String objKey) throws JSONException {

        ArrayList<String> parsedStringArray = null;

        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray resultsArray = jsonObj.getJSONArray("results");

        parsedStringArray = new ArrayList<>(resultsArray.length());

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject resultObject = resultsArray.getJSONObject(i);
            parsedStringArray.add(resultObject.getString(objKey));
        }

        return parsedStringArray;
    }

    public static ArrayList<String> createMoviePosterUrls(ArrayList<String> partialMovieUrls) {
        ArrayList<String> fullMovieUrls = new ArrayList<>(partialMovieUrls.size());
        String prefixPosterUrl = "https://image.tmdb.org/t/p/w185/";

        for (int i = 0; i < partialMovieUrls.size(); i++) {
            fullMovieUrls.add(prefixPosterUrl + partialMovieUrls.get(i));
        }
        return fullMovieUrls;
    }

    //create method for images
    //https://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
}


