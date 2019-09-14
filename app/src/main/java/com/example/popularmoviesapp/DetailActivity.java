package com.example.popularmoviesapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView backdropImage;
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backdropImage = findViewById(R.id.iv_backdrop_image);
        description = findViewById(R.id.tv_description_text);

        Intent mIntent = getIntent();

//        private String title;
//        private String poster;
//        private String description;
//        private String backdropImage;
//        private String userRating;


        description.setText(mIntent.getStringExtra("description"));
        Picasso.get().load(mIntent.getStringExtra("movieBackdropImage")).fit().centerCrop().into(backdropImage);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mIntent.getStringExtra("movieTitle"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
