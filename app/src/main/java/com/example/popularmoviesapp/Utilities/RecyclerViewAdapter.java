package com.example.popularmoviesapp.Utilities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mPosters = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mTitles, ArrayList<String> mPosters) {
        this.mTitles = mTitles;
        this.mPosters = mPosters;
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

        System.out.println(mPosters.get(position));

        holder.title.setText(mTitles.get(position));
        Picasso.get().load(mPosters.get(position)).fit().centerInside().into(holder.poster);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + mTitles.get(position));
                Toast.makeText(mContext, mTitles.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView poster;
        LinearLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_label);
            poster = itemView.findViewById(R.id.iv_poster_image);
            parentLayout = itemView.findViewById(R.id.card_layout);
        }
    }
}
