package com.example.cycle_tour_helper.di;

import androidx.lifecycle.ViewModelProvider;

import com.example.cycle_tour_helper.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

        @Binds
        public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

}
