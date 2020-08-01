package com.jewong.popularmovies.rest;

import com.jewong.popularmovies.data.MovieList;
import com.jewong.popularmovies.data.ReviewList;
import com.jewong.popularmovies.data.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {

    @GET("3/movie/{sort_by}/")
    Call<MovieList> getMovies(
            @Path("sort_by") String sortBy,
            @Query("api_key") String apiKey);

    @GET("3/movie/{movie_id}/videos")
    Call<VideoList> getVideos(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey);

    @GET("3/movie/{movie_id}/reviews")
    Call<ReviewList> getReviews(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey);

}
