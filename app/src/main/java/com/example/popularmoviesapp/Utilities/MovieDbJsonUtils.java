package com.example.popularmoviesapp.Utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDbJsonUtils {

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

//        public static ArrayList<String> getArrayListMoviePosterUrls(Context context, String jsonStr) throws JSONException{
//            JSONObject jsonObj = new JSONObject(jsonStr);
//            JSONArray resultsArray = jsonObj.getJSONArray("results");
//            ArrayList<String> posterUrls = new ArrayList<>(resultsArray.length());
//
//            for (int i = 0; i < resultsArray.length(); i++) {
//                JSONObject resultObject = resultsArray.getJSONObject(i);
//                posterUrls.add(resultObject.getString("poster_path"));
//            }
//
//            return posterUrls;
//        }

        //create method for images
        //https://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
}


