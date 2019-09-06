package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.popularmoviesapp.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button button;
    ListView tvText;

    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X","Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tvText = findViewById(R.id.listview1);

        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tvText.setText(test());
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, mobileArray);

                ListView listView = (ListView) findViewById(R.id.listview1);
                listView.setAdapter(adapter);
            }
        });
    }

    private String test() {
        String apiKey = getString(R.string.the_moviedb_api_key);
        String githubQuery = "ractive backbone";

        URL githubSearchUrl = NetworkUtils.buildUrl(apiKey);
        System.out.println(githubSearchUrl);
        String githubSearchResults = null;
        try {
            githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubSearchUrl);
            return githubSearchResults;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
