package com.jewong.popularmovies.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jewong.popularmovies.data.MovieList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieAPIClient {

    // TODO: Replace API_KEY. Removed prior to submission per instructions.
    private static final String API_KEY = "";
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

    public void getMovieByDiscover(String sortBy, Callback<MovieList> callback) {
        mMovieAPI.getMovieByDiscover(sortBy, API_KEY)
                .enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        callback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

}
