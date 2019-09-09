package com.example.popularmoviesapp.Utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class MovieDbJsonUtils {
        public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr) throws JSONException {

            /* String array to hold each day's weather String */
            String[] parsedWeatherData = null;

            JSONObject forecastJson = new JSONObject(forecastJsonStr);


            JSONArray weatherArray = forecastJson.getJSONArray("results");

            parsedWeatherData = new String[weatherArray.length()];

            for (int i = 0; i < weatherArray.length(); i++) {
                String title;
                String posterUrl;

                /* Get the JSON object representing the day */
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                System.out.println(dayForecast.getString("original_title"));

                parsedWeatherData[i] = dayForecast.getString("original_title");
            }

            return parsedWeatherData;
        }
}
