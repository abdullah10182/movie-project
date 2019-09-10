package com.example.popularmoviesapp.Utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class MovieDbJsonUtils {
        public static String[] getArrayStringFromJson(Context context, String jsonStr, String objKey) throws JSONException {

            /* String array to hold each day's weather String */
            String[] parsedStringArray = null;

            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray resultsArray = jsonObj.getJSONArray("results");

            parsedStringArray = new String[resultsArray.length()];

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObject = resultsArray.getJSONObject(i);
                parsedStringArray[i] = resultObject.getString(objKey);
            }

            return parsedStringArray;
        }
}
