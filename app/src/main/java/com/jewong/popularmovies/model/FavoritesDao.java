package com.jewong.popularmovies.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.jewong.popularmovies.data.Movie;

import java.util.List;

@Dao
@TypeConverters({DataConverter.class})
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    List<Movie> loadAllMovies();

    @Query("SELECT * FROM favorites WHERE id = :movieId ")
    Movie loadMovie(int movieId);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movieId);

}
