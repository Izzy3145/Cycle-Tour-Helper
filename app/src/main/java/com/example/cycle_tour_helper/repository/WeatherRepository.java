package com.example.cycle_tour_helper.repository;

import androidx.annotation.NonNull;

import com.example.cycle_tour_helper.db.WeatherDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WeatherRepository {

    private static final String TAG = "WeatherRepository";

    private static WeatherRepository instance;

    @NonNull
    private final WeatherDao weatherDao;

    @Inject
    public WeatherRepository(@NonNull WeatherDao weatherDao){
        this.weatherDao = weatherDao;
    }


}
