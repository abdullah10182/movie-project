package com.example.popularmoviesapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesapp.DetailActivity;
import com.example.popularmoviesapp.MainActivity;
import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mTitles;
    private ArrayList<String> mPosters;
    private List<MovieItem> mMovieItems;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<MovieItem> movieItems) {
//        this.mTitles = mTitles;
//        this.mPosters = mPosters;
        this.mMovieItems = movieItems;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(mMovieItems.get(position).getTitle());
        Picasso.get().load(mMovieItems.get(position).getPoster()).fit().centerInside().into(holder.poster);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
               // Log.d(TAG, "onClick: " + mMovieItems.get(position).getDescription());
               // Toast.makeText(mContext, mMovieItems.get(position).getDescription(), Toast.LENGTH_SHORT).show();
                launchDetailActivity(mMovieItems.get(position));
            }
        });
    }

    public void launchDetailActivity(MovieItem movieData) {
        System.out.println(movieData.getTitle());
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("movieBackdropImage", movieData.getBackdropImage());
        intent.putExtra("movieTitle", movieData.getTitle());
        intent.putExtra("description", movieData.getDescription());
        intent.putExtra("rating", movieData.getUserRating());
        intent.putExtra("releaseDate", movieData.getReleaseDate());
        mContext.startActivity(intent);
    }

    public void setMovieList(List<MovieItem> movieList) {
        this.mMovieItems = movieList;
    }


    @Override
    public int getItemCount() {
        return mMovieItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView poster;
        CardView parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_label);
            poster = itemView.findViewById(R.id.iv_poster_image);
            parentLayout = itemView.findViewById(R.id.card_layout);
        }
    }
}
