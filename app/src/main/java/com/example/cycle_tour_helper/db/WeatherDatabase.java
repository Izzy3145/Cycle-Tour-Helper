package com.example.cycle_tour_helper.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cycle_tour_helper.models.WeatherForecast;
import com.example.cycle_tour_helper.utils.WeatherTypeConverter;


@Database(entities = {WeatherForecast.class}, version = 1)
@TypeConverters({WeatherTypeConverter.class})
public abstract class WeatherDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "weather_db";

    public abstract WeatherDao getWeatherDao();
}
