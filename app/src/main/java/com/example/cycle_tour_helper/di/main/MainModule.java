package com.example.cycle_tour_helper.di.main;

import android.app.Application;

import androidx.room.Room;

import com.example.cycle_tour_helper.db.WeatherDao;
import com.example.cycle_tour_helper.db.WeatherDatabase;
import com.example.cycle_tour_helper.repository.WeatherRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.cycle_tour_helper.db.WeatherDatabase.DATABASE_NAME;

@Module
public class MainModule {

    @Provides
    static WeatherDatabase provideWeatherDatabase(Application application) {
        return Room.databaseBuilder(
                application,
                WeatherDatabase.class,
                DATABASE_NAME
        ).build();
    }

    @Provides
    static WeatherDao provideWeatherDao(WeatherDatabase weatherDatabase) {
        return weatherDatabase.getWeatherDao();
    }

    @Provides
    static WeatherRepository provideWeatherRepository(WeatherDao weatherDao){
        return new WeatherRepository(weatherDao);
    }

}
