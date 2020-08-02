package com.jewong.popularmovies.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
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
        mBinding.reviewsLayout.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.reviewsLayout.reviewRecyclerView.setAdapter(new ReviewAdapter());
        mBinding.detailsLayout.favorite.setOnClickListener(v -> mMovieDetailsViewModel.onFavoriteClick());
    }

    private void initializeObservers() {
        mMovieDetailsViewModel.mVideoList.observe(this, this::renderTrailer);
        mMovieDetailsViewModel.mReviewList.observe(this, this::renderReviews);
        mMovieDetailsViewModel.mIsFavorite.observe(this, this::renderFavorite);
        mMovieDetailsViewModel.mMovie.observe(this, this::renderDetails);
        mMovieDetailsViewModel.mHasLoaded.observe(this, this::hideProgressBar);
        mMovieDetailsViewModel.mHasFailedToLoad.observe(this, hasFailedToLoad -> {
            hideProgressBar(hasFailedToLoad);
            if (hasFailedToLoad) showErrorToast();
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
        final float rating = (movie.getVoteAverage().floatValue() / 2.0f) * 1.0f;
        Picasso.get().load(imageUri).into(mBinding.poster);
        mBinding.detailsLayout.title.setText(movie.getTitle());
        mBinding.detailsLayout.releaseDate.setText(movie.getReleaseDate());
        mBinding.detailsLayout.plot.setText(movie.getOverview());
        mBinding.detailsLayout.rating.setRating(rating);
    }

    private void renderFavorite(Boolean isFavorite) {
        int stringId = isFavorite ? R.string.favorite : R.string.unfavorite;
        mBinding.detailsLayout.favorite.setText(getString(stringId));
    }

    private void renderReviews(List<Review> reviews) {
        ReviewAdapter adapter = (ReviewAdapter) mBinding.reviewsLayout.reviewRecyclerView.getAdapter();
        String title = String.format(getString(R.string.reviews), reviews.size());
        if (adapter != null) {
            adapter.setData(reviews);
            mBinding.reviewsLayout.title.setText(title);
            if (reviews.size() != 0) mBinding.reviewsLayout.noReviews.setVisibility(View.GONE);
        }
    }

    private void renderTrailer(List<Video> videos) {
        for (Video video : videos) {
            if (video.getType().equals("Trailer")) {
                mBinding.detailsLayout.trailer.setVisibility(View.VISIBLE);
                mBinding.detailsLayout.trailer.setOnClickListener(v -> playTrailer(video));
                break;
            }
        }
    }

    private void hideProgressBar(Boolean hideProgressBar) {
        int visibility = hideProgressBar ? View.GONE : View.VISIBLE;
        mBinding.progressBar.setVisibility(visibility);
    }

    private void playTrailer(Video trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("vnd.youtube:" + trailer.getId()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getId()));
        try {
            startActivity(appIntent);
        } catch (Exception e) {
            startActivity(webIntent);
        }
    }

    private void showErrorToast() {
        Toast.makeText(this, getString(R.string.data_error), Toast.LENGTH_LONG).show();
    }

}

