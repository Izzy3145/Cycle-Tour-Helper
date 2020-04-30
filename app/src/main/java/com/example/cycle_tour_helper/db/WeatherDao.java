package com.example.cycle_tour_helper.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cycle_tour_helper.models.WeatherForecast;


import io.reactivex.Single;

@Dao
public interface WeatherDao {

    @Insert
    Single<Long> insertWeatherData(WeatherForecast weatherForecast) throws Exception;

    @Update
    Single<Integer> updateWeatherData(WeatherForecast weatherForecast) throws Exception;

    //the @Delete annotation deletes the entities passed in as parameters in the db
    @Delete
    Single<Integer> deleteNote(WeatherForecast weatherForecast) throws Exception;

    //@Query allows read/write operations on the db. Each @Query method is verified at compile time
    //Room also verifies the return type if the fields in the returned object don't match the entity column names
    @Query("SELECT * FROM weather")
    LiveData<WeatherForecast> getWeatherForecast();

}
