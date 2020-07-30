package com.jewong.popularmovies.rest;

import com.jewong.popularmovies.data.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("3/discover/movie?")
    Call<MovieList> getMovieByDiscover(@Query("api_key") String apiKey,
                                       @Query("sort_by") String sortBy);

}
