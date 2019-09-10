package com.example.popularmoviesapp.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.example.popularmoviesapp.MainActivity;
import com.example.popularmoviesapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static private String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/discover/movie";


    public static URL buildUrl(Context context, String sortBy) {
        //https://api.themoviedb.org/3/discover/movie?api_key=1794dee38856c7c24f577c0cae2e2b15&language=en-US&sort_by=popularity.desc&page=1
        //popularity.desc, popularity.asc, vote_count.desc, vote_count.asc
        URL url = null;
        String apiKey = context.getResources().getString(R.string.the_moviedb_api_key);

        if(sortBy == null || sortBy.equals("")){
            sortBy = "popularity.desc";
        }

        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("sort_by", sortBy)
                .appendQueryParameter("page", "1")
                .build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
