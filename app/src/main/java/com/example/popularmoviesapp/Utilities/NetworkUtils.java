package com.example.popularmoviesapp.Utilities;

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

    //final static String GITHUB_BASE_URL = "https://api.github.com/search/repositories";
    static String apiKey = null;
    //final static String GITHUB_BASE_URL = "https://api.themoviedb.org/3/movie/popular?api_key="+ apiKey +"&language=en-US&page=1";


    final static String PARAM_QUERY = "q";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_SORT = "sort";
    final static String sortBy = "stars";


    public static URL buildUrl(String apiKey) {
//        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
//                .appendQueryParameter(PARAM_QUERY, githubSearchQuery)
//                .appendQueryParameter(PARAM_SORT, sortBy)
//                .build();
        String urlString = "https://api.themoviedb.org/3/movie/popular?api_key="+ apiKey +"&language=en-US&page=1";
        Uri builtUri = Uri.parse(urlString).buildUpon()
                .build();

        URL url = null;
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
