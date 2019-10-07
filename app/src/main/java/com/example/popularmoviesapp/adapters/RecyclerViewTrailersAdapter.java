package com.example.popularmoviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.Trailer;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTrailersAdapter extends RecyclerView.Adapter<RecyclerViewTrailersAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewTrailersAda";

    private ArrayList<String> source;
//    private ArrayList<String> content;
//    private ArrayList<String> url;
    private List<Trailer> mTrailers;
    private Context mContext;

    public RecyclerViewTrailersAdapter(Context mContext, ArrayList<Trailer> trailers) {
        this.mTrailers = trailers;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println("------>" + mTrailers.get(position).getMovieTitle());
        holder.source.setText(mTrailers.get(position).getSource());
//        holder.source.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//            launchTrailerDetailActivity(mTrailers.get(position));
//            }
//        });

    }


    public void setTrailersList(List<Trailer> trailers) {
        this.mTrailers = trailers;
    }

    private void launchTrailerDetailActivity(Trailer trailer) {
        System.out.println(trailer.getMovieTitle());
//        Intent intent = new Intent(mContext, ReviewDetailActivity.class);
//        intent.putExtra("content", trailer.getContent());
//        intent.putExtra("author", trailer.getAuthor());
//        intent.putExtra("movieTitle", trailer.getMovieTitle());
//        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView source;

        public ViewHolder(View itemView) {
            super(itemView);
            System.out.println("viewholder");
            source = itemView.findViewById(R.id.tv_trailer_source);

        }
    }
}
