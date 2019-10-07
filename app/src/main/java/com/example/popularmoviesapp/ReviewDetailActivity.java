package com.example.popularmoviesapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewDetailActivity extends AppCompatActivity {

    TextView mContent;
    TextView mReviewBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        mContent = findViewById(R.id.tv_review_detail_content);
        mReviewBy = findViewById(R.id.tv_review_by);

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        mContent.setText(content);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Review: " + intent.getStringExtra("movieTitle"));

        mReviewBy.setText("Reviewed by: " + intent.getStringExtra("author"));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
