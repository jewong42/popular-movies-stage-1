package com.jewong.popularmovies.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.jewong.popularmovies.AppExecutors;
import com.jewong.popularmovies.data.Movie;
import com.jewong.popularmovies.data.MovieList;
import com.jewong.popularmovies.model.AppDatabase;
import com.jewong.popularmovies.rest.MovieAPIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListViewModel extends AndroidViewModel {


    MediatorLiveData<Boolean> mIsLoading = new MediatorLiveData<>();
    MutableLiveData<Boolean> mHasFailedToLoad = new MutableLiveData<>();
    MutableLiveData<List<Movie>> mMovieList = new MutableLiveData<>();


    AppDatabase mDatabase;
    MovieAPIClient mMovieAPIClient = new MovieAPIClient();


    public MovieListViewModel(@NonNull Application application) {
        super(application);
        mDatabase = AppDatabase.getInstance(application);
        mIsLoading.addSource(mMovieList, value ->
                mIsLoading.setValue(isLoading()));
    }

    private Boolean isLoading() {
        return mMovieList.getValue() == null;
    }

    public void loadData(String sortBy) {
        mMovieAPIClient.getMovies(sortBy, new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                if (response.body() != null && response.body().getResults() != null) {
                    mMovieList.setValue(response.body().getResults());
                } else {
                    mHasFailedToLoad.setValue(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                mHasFailedToLoad.setValue(true);
            }
        });
    }


    public void getFavorites() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Movie> favorites = mDatabase.favoriteMovieDao().loadAllMovies();
            AppExecutors.getInstance().mainThread().execute(() ->
                    mMovieList.setValue(favorites));
        });
    }
}
