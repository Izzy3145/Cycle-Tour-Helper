package com.example.cycle_tour_helper.viewmodels;

import android.content.pm.PackageManager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cycle_tour_helper.models.WeatherForecast;
import com.example.cycle_tour_helper.repository.WeatherRepository;

import javax.inject.Inject;

public class WeatherViewModel extends ViewModel {

    private static final String TAG = "WeatherViewModel";
    private WeatherRepository weatherRepository;
    private MutableLiveData<WeatherForecast> returnedForecast;


    @Inject
    public WeatherViewModel(WeatherRepository weatherRepository){
        this.weatherRepository = weatherRepository;
        init();
    }

    private void init(){
        if(returnedForecast == null){
            returnedForecast = new MutableLiveData<>();
        }
    }
}
