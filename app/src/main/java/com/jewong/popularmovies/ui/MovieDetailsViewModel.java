package com.jewong.popularmovies.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.data.Review;
import com.jewong.popularmovies.data.ReviewList;
import com.jewong.popularmovies.data.Video;
import com.jewong.popularmovies.data.VideoList;
import com.jewong.popularmovies.rest.MovieAPIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsViewModel extends AndroidViewModel {

    MutableLiveData<List<Video>> mVideoList = new MutableLiveData<>();
    MutableLiveData<List<Review>> mReviewList = new MutableLiveData<>();
    MutableLiveData<Boolean> mHasFailedToLoad = new MutableLiveData<>();
    MediatorLiveData<Boolean> hasLoaded = new MediatorLiveData<>();
    MovieAPIClient mMovieAPIClient = new MovieAPIClient();
    Movie mMovie;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        hasLoaded.addSource(mVideoList, value -> hasLoaded.setValue(hasDataLoaded()));
        hasLoaded.addSource(mReviewList, value -> hasLoaded.setValue(hasDataLoaded()));
    }

    public void loadMovieDetails(Movie movie) {
        mMovie = movie;
        loadVideos(mMovie.getId().toString());
        loadReviews(mMovie.getId().toString());
    }

    public void loadVideos(String movieId) {
        mMovieAPIClient.getVideos(movieId, new Callback<VideoList>() {
            @Override
            public void onResponse(@NonNull Call<VideoList> call, @NonNull Response<VideoList> response) {
                if (response.body() != null) mVideoList.setValue(response.body().getResults());
                else mHasFailedToLoad.setValue(true);
            }

            @Override
            public void onFailure(@NonNull Call<VideoList> call, @NonNull Throwable t) {
                mHasFailedToLoad.setValue(true);
            }
        });
    }

    public void loadReviews(String movieId) {
        mMovieAPIClient.getReviews(movieId, new Callback<ReviewList>() {
            @Override
            public void onResponse(@NonNull Call<ReviewList> call, @NonNull Response<ReviewList> response) {
                if (response.body() != null) mReviewList.setValue(response.body().getResults());
                else mHasFailedToLoad.setValue(true);
            }

            @Override
            public void onFailure(@NonNull Call<ReviewList> call, @NonNull Throwable t) {
                mHasFailedToLoad.setValue(true);
            }
        });
    }

    private boolean hasDataLoaded() {
        return mVideoList.getValue() != null && mReviewList.getValue() != null;
    }

}