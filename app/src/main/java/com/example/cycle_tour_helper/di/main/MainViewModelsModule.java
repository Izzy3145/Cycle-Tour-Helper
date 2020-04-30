package com.example.cycle_tour_helper.di.main;

import androidx.lifecycle.ViewModel;

import com.example.cycle_tour_helper.di.ViewModelKey;
import com.example.cycle_tour_helper.viewmodels.RouteViewModel;
import com.example.cycle_tour_helper.viewmodels.WeatherViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RouteViewModel.class)
    public abstract ViewModel bindRouteViewModel(RouteViewModel routeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel.class)
    public abstract ViewModel bindWeatherViewModel(WeatherViewModel weatherViewModel);

}
