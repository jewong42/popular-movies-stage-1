package com.jewong.popularmovies.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jewong.popularmovies.data.MovieList;
import com.jewong.popularmovies.data.ReviewList;
import com.jewong.popularmovies.data.VideoList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieAPIClient {

    // TODO: Replace API_KEY. Removed prior to submission per instructions.
    private static final String API_KEY = "baecc17a5e904cabd97067cacadb8603";
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private MovieAPI mMovieAPI;

    public MovieAPIClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.mMovieAPI = retrofit.create(MovieAPI.class);
    }

    public void getMovies(String sortBy, Callback<MovieList> callback) {
        mMovieAPI.getMovies(sortBy, API_KEY).enqueue(callback);
    }

    public void getVideos(int movieId, Callback<VideoList> callback) {
        mMovieAPI.getVideos(movieId, API_KEY).enqueue(callback);
    }

    public void getReviews(int movieId, Callback<ReviewList> callback) {
        mMovieAPI.getReviews(movieId, API_KEY).enqueue(callback);
    }

}
