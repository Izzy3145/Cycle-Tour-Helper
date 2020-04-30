package com.example.cycle_tour_helper.di.network;

import com.example.cycle_tour_helper.models.WeatherForecast;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    //daily?lat=35&lon=139&cnt=10
    @GET("daily")
    Flowable<WeatherForecast> getWeatherData(
            @Query("lat") int lat,
            @Query("lon") int lon,
            @Query("cnt") int count
    );
}
