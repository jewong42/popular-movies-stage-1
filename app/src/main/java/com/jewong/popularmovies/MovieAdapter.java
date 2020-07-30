package com.jewong.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jewong.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w500";
    private ArrayList<Movie> mDataset = new ArrayList<>();
    private MovieAdapterCallback mMovieAdapterCallback;

    public MovieAdapter(ArrayList<Movie> dataSet, MovieAdapterCallback callback) {
        if (dataSet != null) setData(dataSet);
        mMovieAdapterCallback = callback;
    }

    @Override
    @NonNull
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_movie_adapter,
                parent,
                false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mDataset.get(position);
        String posterPath = movie.getPosterPath();
        String imageUri = String.format("%s%s%s", BASE_URL, SIZE, posterPath);
        Picasso.get().load(imageUri).into(holder.imageView);
        holder.imageView.setOnClickListener(v1 -> mMovieAdapterCallback.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setData(List<Movie> results) {
        mDataset.clear();
        mDataset.addAll(results);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.movie_poster);
        }
    }

    interface MovieAdapterCallback {
        void onMovieClick(Movie movie);
    }

}
