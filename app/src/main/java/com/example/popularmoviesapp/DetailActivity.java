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
    private TextView userRating;
    private TextView title;
    private TextView releaseDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.tv_title_under);
        backdropImage = findViewById(R.id.iv_backdrop_image);
        description = findViewById(R.id.tv_description_text);
        userRating = findViewById(R.id.tv_user_rating);
        releaseDate = findViewById(R.id.tv_release_date);

        Intent mIntent = getIntent();

        title.setText(mIntent.getStringExtra("movieTitle"));
        description.setText(mIntent.getStringExtra("description"));
        userRating.append(mIntent.getStringExtra("rating"));
        releaseDate.append(mIntent.getStringExtra("releaseDate"));
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
