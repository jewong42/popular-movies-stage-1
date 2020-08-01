package com.jewong.popularmovies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jewong.popularmovies.R;
import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.data.Review;
import com.jewong.popularmovies.data.Video;
import com.jewong.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.jewong.popularmovies.ui.adapter.ReviewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "com.jewong.popularmovies.MOVIE";
    public static final String BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String SIZE = "w780";
    ActivityMovieDetailsBinding mBinding;
    MovieDetailsViewModel mMovieDetailsViewModel;
    ReviewAdapter mReviewAdapter = new ReviewAdapter(null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        mMovieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        initializeViews();
        initializeObservers();
        initializeDetails();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initializeViews() {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.reviewRecyclerView.setAdapter(mReviewAdapter);
        mBinding.favorite.setOnClickListener(v -> mMovieDetailsViewModel.onFavoriteClick());
    }

    private void initializeObservers() {
        mMovieDetailsViewModel.mVideoList.observe(this, this::renderTrailer);
        mMovieDetailsViewModel.mReviewList.observe(this, this::renderReviews);
        mMovieDetailsViewModel.mIsFavorite.observe(this, this::renderFavorite);
        mMovieDetailsViewModel.mHasFailedToLoad.observe(this, hasFailedToLoad -> {
            int progressBarVisibility = hasFailedToLoad ? View.GONE : View.VISIBLE;
            if (hasFailedToLoad) showErrorToast();
            mBinding.progressBarContainer.setVisibility(progressBarVisibility);
        });
        mMovieDetailsViewModel.mHasDetailsLoaded.observe(this, hasLoaded -> {
            int progressBarVisibility = hasLoaded ? View.GONE : View.VISIBLE;
            if (hasLoaded) renderDetails(mMovieDetailsViewModel.mMovie);
            mBinding.progressBarContainer.setVisibility(progressBarVisibility);
        });
    }

    private void initializeDetails() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(EXTRA_MOVIE) != null) {
            Movie movie = (Movie) getIntent().getExtras().getSerializable(EXTRA_MOVIE);
            mMovieDetailsViewModel.loadDetails(movie);
        }
    }

    private void renderDetails(Movie movie) {
        final String posterPath = movie.getPosterPath();
        final String imageUri = String.format("%s%s%s", BASE_URL, SIZE, posterPath);
        final String voterAverage = movie.getVoteAverage() != null ?
                movie.getVoteAverage().toString() : "";
        Picasso.get().load(imageUri).into(mBinding.poster);
        mBinding.title.setText(movie.getTitle());
        mBinding.releaseDate.setText(movie.getReleaseDate());
        mBinding.voteAverage.setText(voterAverage);
        mBinding.plot.setText(movie.getOverview());
    }

    private void renderFavorite(Boolean isFavorite) {
        int stringId = isFavorite ? R.string.favorite : R.string.unfavorite;
        mBinding.favorite.setText(getString(stringId));
    }

    private void renderReviews(List<Review> reviews) {
        if (reviews == null) return;
        mReviewAdapter.setData(reviews);
    }

    private void renderTrailer(List<Video> videos) {
        if (videos == null) return;
        for (Video video : videos) {
            if (video.getType().equals("Trailer")) {
                mBinding.trailer.setVisibility(View.VISIBLE);
                mBinding.trailer.setOnClickListener(v -> playTrailer(video));
                break;
            }
        }
    }

    private void playTrailer(Video trailer) {
        Intent appIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + trailer.getId()));
        Intent webIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getId()));
        try {
            getBaseContext().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getBaseContext().startActivity(webIntent);
        }
    }

    private void showErrorToast() {
        Toast.makeText(this, getString(R.string.data_error), Toast.LENGTH_LONG).show();
    }

}

