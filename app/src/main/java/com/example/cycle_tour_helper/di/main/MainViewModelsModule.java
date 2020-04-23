package com.example.cycle_tour_helper.di.main;

import androidx.lifecycle.ViewModel;

import com.example.cycle_tour_helper.di.ViewModelKey;
import com.example.cycle_tour_helper.ui.main.RouteViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RouteViewModel.class)
    public abstract ViewModel bindRouteViewModel(RouteViewModel viewModel);

}
