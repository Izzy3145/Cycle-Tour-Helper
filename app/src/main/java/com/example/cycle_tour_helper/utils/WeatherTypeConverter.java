package com.example.cycle_tour_helper.utils;

import androidx.room.TypeConverter;

import com.example.cycle_tour_helper.models.WeatherForecast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class WeatherTypeConverter {

        @TypeConverter
        public static WeatherForecast.City fromJson(String json) {
            Gson gson = new Gson();
            return gson.fromJson(json, WeatherForecast.City.class);
        }

        @TypeConverter
        public static String cityObjToCity(WeatherForecast.City weatherCity) {
            Gson gson = new Gson();
            return gson.toJson(weatherCity);
        }

    @TypeConverter
    public static List<WeatherForecast.Day> storedStringToDayObjects(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<WeatherForecast.Day>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String sayObjectsToStoredString(List<WeatherForecast.Day> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }

}
