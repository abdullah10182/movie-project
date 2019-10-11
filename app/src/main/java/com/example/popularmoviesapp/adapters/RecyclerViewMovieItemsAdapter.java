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
import com.example.popularmoviesapp.model.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMovieItemsAdapter extends RecyclerView.Adapter<RecyclerViewMovieItemsAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewMovieItemsAdapter";

    private ArrayList<String> mTitles;
    private ArrayList<String> mPosters;
    private List<MovieItem> mMovieItems;
    private Context mContext;

    public RecyclerViewMovieItemsAdapter(Context mContext, ArrayList<MovieItem> movieItems) {
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
        //Log.d(TAG, "onBindViewHolder: called.");

        holder.title.setText(mMovieItems.get(position).getTitle());
        Picasso.get().load(mMovieItems.get(position).getPoster()).fit().centerInside().into(holder.poster);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                launchDetailActivity(mMovieItems.get(position));
            }
        });
    }

    public void launchDetailActivity(MovieItem movieData) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("movieItem", movieData);
        mContext.startActivity(intent);
    }

    public void addToMoviesList(List<MovieItem> movieList) {
        this.mMovieItems.addAll(movieList);
    }

    public void setMovieList(List<MovieItem> movieList) {
        this.mMovieItems= movieList;
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
