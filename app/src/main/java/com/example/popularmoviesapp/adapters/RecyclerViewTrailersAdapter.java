package com.example.popularmoviesapp.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.Trailer;
import com.squareup.picasso.Picasso;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mTrailers.get(position).getName());
        Picasso.get().load(mTrailers.get(position).getThumbnail()).fit().centerInside().into(holder.trailerThumbnail);
        holder.trailerThumbnail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                launchTrailerDetailActivity(mTrailers.get(position));
            }
        });

    }


    public void setTrailersList(List<Trailer> trailers) {
        this.mTrailers = trailers;
    }

    private void launchTrailerDetailActivity(Trailer trailer) {
        System.out.println(trailer.getName());
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getSource()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getSource()));
        try {
            mContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mContext.startActivity(webIntent);
        }
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView trailerThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_trailer_name);
            trailerThumbnail = itemView.findViewById(R.id.iv_trailer_thumbnail);

        }
    }
}
