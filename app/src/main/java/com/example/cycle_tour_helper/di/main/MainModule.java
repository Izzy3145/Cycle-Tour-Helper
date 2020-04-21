package com.example.cycle_tour_helper.di.main;

import com.example.cycle_tour_helper.ui.main.RouteAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    static RouteAdapter provideRoutesAdapter(){
        return new RouteAdapter();
    }

}
