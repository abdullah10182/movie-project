package com.example.popularmoviesapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.popularmoviesapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static private String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    //http://api.themoviedb.org/3/movie/popular?api_key=1794dee38856c7c24f577c0cae2e2b15


    public static URL buildUrl(Context context, String sortBy) {
        //popularity.desc, popularity.asc, vote_count.desc, vote_count.asc
        URL url = null;
        String apiKey = context.getResources().getString(R.string.the_moviedb_api_key);

        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter("api_key", apiKey)
                .build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildDetailPageUrls(Context context, int movieId, String type) {
        URL url = null;
        String apiKey = context.getResources().getString(R.string.the_moviedb_api_key);
        String movieIdString = Integer.toString(movieId);

        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(movieIdString)
                .appendPath(type)
                .appendQueryParameter("api_key", apiKey)
                .build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            System.out.println("network call: " + url);
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


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void connectionFailed(Context context) {
        Toast.makeText(context, "No connection to the internet", Toast.LENGTH_LONG).show();
    }

}
