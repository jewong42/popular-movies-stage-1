package com.jewong.popularmovies.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jewong.popularmovies.data.Movie;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String writeMovie(Movie movie) {
        if (movie == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<Movie>() {}.getType();
        String json = gson.toJson(movie, type);
        return json;
    }

    @TypeConverter
    public Movie readMovie(String json) {
        if (json == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<Movie>() {}.getType();
        Movie movie = gson.fromJson(json, type);
        return movie;
    }

    @TypeConverter
    public String writeIntegerList(List<Integer> list) {
        if (list == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public List<Integer> readIntegerList(String json) {
        if (json == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> list = gson.fromJson(json, type);
        return list;
    }

}
