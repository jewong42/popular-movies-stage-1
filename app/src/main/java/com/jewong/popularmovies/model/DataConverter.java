package com.jewong.popularmovies.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String writeIntegerList(List<Integer> list) {
        if (list == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public List<Integer> readIntegerList(String json) {
        if (json == null) return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}
