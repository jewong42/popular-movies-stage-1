package com.jewong.popularmovies.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.jewong.popularmovies.data.Movie;

@SuppressWarnings("unused")
@Entity(tableName = "favoriteMovies")
@TypeConverters({DataConverter.class})
public class FavoriteMovieEntry {

    @PrimaryKey()
    private int id;
    private Movie movie;

    public FavoriteMovieEntry(Movie movie) {
        this.movie = movie;
        this.id = movie.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

}
