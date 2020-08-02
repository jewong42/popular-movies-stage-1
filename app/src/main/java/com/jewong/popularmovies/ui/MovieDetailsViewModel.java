package com.jewong.popularmovies.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.jewong.popularmovies.common.AppExecutors;
import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.data.Review;
import com.jewong.popularmovies.data.ReviewList;
import com.jewong.popularmovies.data.Video;
import com.jewong.popularmovies.data.VideoList;
import com.jewong.popularmovies.model.AppDatabase;
import com.jewong.popularmovies.api.MovieAPIClient;
import com.jewong.popularmovies.model.FavoritesDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsViewModel extends AndroidViewModel {

    MutableLiveData<List<Video>> mVideoList = new MutableLiveData<>();
    MutableLiveData<List<Review>> mReviewList = new MutableLiveData<>();
    MutableLiveData<Boolean> mIsFavorite = new MutableLiveData<>();
    MutableLiveData<Movie> mMovie = new MutableLiveData<>();
    MutableLiveData<Boolean> mHasFailedToLoad = new MutableLiveData<>();
    MediatorLiveData<Boolean> mHasLoaded = new MediatorLiveData<>();
    FavoritesDao mFavoritesDao;
    MovieAPIClient mMovieAPIClient = new MovieAPIClient();

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        mFavoritesDao = AppDatabase.getInstance(application).favoritesDao();
        mHasLoaded.addSource(mVideoList, value ->
                mHasLoaded.setValue(hasLoaded()));
        mHasLoaded.addSource(mReviewList, value ->
                mHasLoaded.setValue(hasLoaded()));
        mHasLoaded.addSource(mIsFavorite, value ->
                mHasLoaded.setValue(hasLoaded()));
        mHasLoaded.addSource(mMovie, value ->
                mHasLoaded.setValue(hasLoaded()));
    }

    public void loadDetails(Movie movie) {
        mMovie.setValue(movie);
        if (mMovie.getValue() != null) {
            int movieID = mMovie.getValue().getId();
            configureIsFavorite(movieID);
            loadVideos(movieID);
            loadReviews(movieID);
        } else {
            mHasFailedToLoad.setValue(true);
        }
    }

    public void onFavoriteClick() {
        if (mMovie.getValue() == null) return;
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (mIsFavorite.getValue() != null) {
                if (mIsFavorite.getValue()) mFavoritesDao.insertMovie(mMovie.getValue());
                else mFavoritesDao.deleteMovie(mMovie.getValue());
            }
            configureIsFavorite(mMovie.getValue().getId());
        });
    }

    private void configureIsFavorite(int movieID) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            Movie favorite = mFavoritesDao.loadMovie(movieID);
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

    private boolean hasLoaded() {
        Boolean hasFavoritesLoaded = mMovie.getValue() != null
                && mIsFavorite.getValue() != null
                && mIsFavorite.getValue();
        Boolean hasLoaded = mVideoList.getValue() != null
                && mReviewList.getValue() != null
                && mIsFavorite.getValue() != null
                && mMovie.getValue() != null;
        return hasFavoritesLoaded || hasLoaded;
    }

}