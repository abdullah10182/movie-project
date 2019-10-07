package com.example.popularmoviesapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewDetailActivity extends AppCompatActivity {

    TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        mContent = findViewById(R.id.tv_review_detail_content);

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        mContent.setText(content);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Review: " + intent.getStringExtra("author"));

        System.out.println(intent.getStringExtra("movieTitle"));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
