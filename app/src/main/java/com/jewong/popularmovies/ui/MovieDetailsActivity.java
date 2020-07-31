package com.jewong.popularmovies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.jewong.popularmovies.R;
import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "com.jewong.popularmovies.MOVIE";
    public static final String BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String SIZE = "w780";
    ActivityMovieDetailsBinding mBinding;
    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        setMovie();
        initializeViews();
    }

    private void setMovie() {
        if (getIntent().getExtras() != null) {
            mMovie = (Movie) getIntent().getExtras().getSerializable(EXTRA_MOVIE);
        }
    }

    private void initializeViews() {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String posterPath = mMovie.getPosterPath();
        final String imageUri = String.format("%s%s%s", BASE_URL, SIZE, posterPath);
        final String voterAverage = mMovie.getVoteAverage() != null ?
                mMovie.getVoteAverage().toString() : "";
        Picasso.get().load(imageUri).into(mBinding.poster);
        mBinding.title.setText(mMovie.getTitle());
        mBinding.releaseDate.setText(mMovie.getReleaseDate());
        mBinding.voteAverage.setText(voterAverage);
        mBinding.plot.setText(mMovie.getOverview());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}