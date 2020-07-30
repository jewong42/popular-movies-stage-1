package com.jewong.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jewong.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "com.jewong.popularmovies.MOVIE";
    public static final String BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String SIZE = "w780";
    Movie mMovie;
    ImageView mPoster;
    TextView mTitle, mReleaseDate, mVoteAverage, mPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindMovie();
        bindViews();
        initializeViews();
    }

    private void bindMovie() {
        Bundle data = getIntent().getExtras();
        mMovie = (Movie) data.getSerializable(EXTRA_MOVIE);
    }

    private void bindViews() {
        mPoster = (ImageView) findViewById(R.id.poster);
        mTitle = (TextView) findViewById(R.id.title);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mVoteAverage = (TextView) findViewById(R.id.vote_average);
        mPlot = (TextView) findViewById(R.id.plot);
    }

    private void initializeViews() {
        String posterPath = mMovie.getPosterPath();
        String imageUri = String.format("%s%s%s", BASE_URL, SIZE, posterPath);
        Picasso.get().load(imageUri).into(mPoster);
        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteAverage.setText(mMovie.getVoteAverage().toString());
        mPlot.setText(mMovie.getOverview());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}