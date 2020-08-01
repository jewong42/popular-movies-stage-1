package com.jewong.popularmovies.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.jewong.popularmovies.data.Movie;

import java.util.List;

@Dao
@TypeConverters({DataConverter.class})
public interface FavoriteMovieDao {

    @Query("SELECT movie FROM favoriteMovies")
    List<Movie> loadAllMovies();

    @Query("SELECT movie FROM favoriteMovies WHERE id = :movieId ")
    Movie loadMovie(int movieId);

    @Insert
    void insertMovie(FavoriteMovieEntry movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteMovieEntry movie);

    @Query("DELETE FROM favoriteMovies WHERE id = :movieId")
    void deleteMovie(int movieId);

}
