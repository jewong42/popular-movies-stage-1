package com.jewong.popularmovies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jewong.popularmovies.R;
import com.jewong.popularmovies.data.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> mDataset = new ArrayList<>();

    public ReviewAdapter(ArrayList<Review> dataSet) {
        if (dataSet != null) setData(dataSet);
    }

    @Override
    @NonNull
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_review,
                parent,
                false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mDataset.get(position);
        holder.name.setText(review.getAuthor());
        holder.review.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setData(List<Review> results) {
        mDataset.clear();
        mDataset.addAll(results);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView review;
        public TextView name;


        public ViewHolder(View v) {
            super(v);
            review = v.findViewById(R.id.review_text_view);
            name = v.findViewById(R.id.name_text_view);
        }
    }

}
