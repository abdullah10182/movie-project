package com.example.popularmoviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesapp.DetailActivity;
import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.ReviewDetailActivity;
import com.example.popularmoviesapp.model.MovieItem;
import com.example.popularmoviesapp.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewReviewsAdapter extends RecyclerView.Adapter<RecyclerViewReviewsAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewReviewsAdap";

    private ArrayList<String> author;
    private ArrayList<String> content;
    private ArrayList<String> url;
    private List<Review> mReviews;
    private Context mContext;

    public RecyclerViewReviewsAdapter(Context mContext, ArrayList<Review> reviews) {
        this.mReviews = reviews;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.author.setText(mReviews.get(position).getAuthor());
        holder.content.setText(mReviews.get(position).getContent());

        holder.reviewCard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                launchReviewDetailActivity(mReviews.get(position));
            }
        });

    }


    public void setReviewsList(List<Review> reviews) {
        this.mReviews = reviews;
    }

    private void launchReviewDetailActivity(Review review) {
        Intent intent = new Intent(mContext, ReviewDetailActivity.class);
        intent.putExtra("content", review.getContent());
        intent.putExtra("author", review.getAuthor());
        intent.putExtra("movieTitle", review.getMovieTitle());
        mContext.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView author;
        TextView content;
        CardView reviewCard;

        public ViewHolder(View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.tv_review_author);
            content = itemView.findViewById(R.id.tv_review_content);
            reviewCard = itemView.findViewById(R.id.card_layout_review_item);
        }
    }
}
