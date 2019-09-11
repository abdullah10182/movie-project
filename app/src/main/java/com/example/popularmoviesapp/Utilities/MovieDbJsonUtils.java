package com.example.popularmoviesapp.Utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MovieDbJsonUtils {
        public static ArrayList<String> getArrayStringFromJson(Context context, String jsonStr, String objKey) throws JSONException {

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
}
