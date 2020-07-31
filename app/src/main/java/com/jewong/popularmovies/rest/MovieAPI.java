package com.jewong.popularmovies.rest;

import com.jewong.popularmovies.data.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("3/movie/{sort_by}/")
    Call<MovieList> getMovieByDiscover(
            @Path("sort_by") String sortBy,
            @Query("api_key") String apiKey);

}
