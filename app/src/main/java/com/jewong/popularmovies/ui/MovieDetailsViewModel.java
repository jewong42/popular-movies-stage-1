package com.jewong.popularmovies.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.jewong.popularmovies.AppExecutors;
import com.jewong.popularmovies.State;
import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.data.Review;
import com.jewong.popularmovies.data.ReviewList;
import com.jewong.popularmovies.data.Video;
import com.jewong.popularmovies.data.VideoList;
import com.jewong.popularmovies.model.AppDatabase;
import com.jewong.popularmovies.model.FavoriteMovieEntry;
import com.jewong.popularmovies.rest.MovieAPIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsViewModel extends AndroidViewModel {

    MutableLiveData<List<Video>> mVideoList = new MutableLiveData<>();
    MutableLiveData<List<Review>> mReviewList = new MutableLiveData<>();
    MutableLiveData<Boolean> mIsFavorite = new MutableLiveData<>();
    MutableLiveData<Boolean> mHasFailedToLoad = new MutableLiveData<>();
    MediatorLiveData<Boolean> mHasDetailsLoaded = new MediatorLiveData<>();
    AppDatabase mDatabase;
    MovieAPIClient mMovieAPIClient = new MovieAPIClient();
    Movie mMovie;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        mDatabase = AppDatabase.getInstance(application);
        mHasDetailsLoaded.addSource(mVideoList, value ->
                mHasDetailsLoaded.setValue(hasDetailsLoaded()));
        mHasDetailsLoaded.addSource(mReviewList, value ->
                mHasDetailsLoaded.setValue(hasDetailsLoaded()));
    }

    public void loadDetails(Movie movie) {
        mMovie = movie;
        int movieID = mMovie.getId();
        configureIsFavorite(movieID);
        loadVideos(movieID);
        loadReviews(movieID);
    }

    public void onFavoriteClick() {
        int movieID = mMovie.getId();
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (mIsFavorite.getValue() != null) {
                if (mIsFavorite.getValue()) {
                    FavoriteMovieEntry favorite = new FavoriteMovieEntry(mMovie);
                    mDatabase.favoriteMovieDao().insertMovie(favorite);
                } else {
                    mDatabase.favoriteMovieDao().deleteMovie(movieID);
                }
                configureIsFavorite(movieID);
            }
        });
    }

    private void configureIsFavorite(int movieID) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Movie favorite = mDatabase.favoriteMovieDao().loadMovie(movieID);
            AppExecutors.getInstance().mainThread().execute(() -> {
                boolean isFavorite = favorite == null;
                mIsFavorite.setValue(isFavorite);
            });
        });
    }

    private void loadVideos(int movieID) {
        mMovieAPIClient.getVideos(movieID, new Callback<VideoList>() {
            @Override
            public void onResponse(@NonNull Call<VideoList> call,
                                   @NonNull Response<VideoList> response) {
                if (response.body() != null) mVideoList.setValue(response.body().getResults());
                else mHasFailedToLoad.setValue(true);
            }

            @Override
            public void onFailure(@NonNull Call<VideoList> call, @NonNull Throwable t) {
                mHasFailedToLoad.setValue(true);
            }
        });
    }

    private void loadReviews(int movieId) {
        mMovieAPIClient.getReviews(movieId, new Callback<ReviewList>() {
            @Override
            public void onResponse(@NonNull Call<ReviewList> call,
                                   @NonNull Response<ReviewList> response) {
                if (response.body() != null) mReviewList.setValue(response.body().getResults());
                else mHasFailedToLoad.setValue(true);
            }

            @Override
            public void onFailure(@NonNull Call<ReviewList> call, @NonNull Throwable t) {
                mHasFailedToLoad.setValue(true);
            }
        });
    }

    private boolean hasDetailsLoaded() {
        return mVideoList != null && mReviewList != null;
    }

}