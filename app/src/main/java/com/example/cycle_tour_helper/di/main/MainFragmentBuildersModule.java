package com.example.cycle_tour_helper.di.main;

import com.example.cycle_tour_helper.ui.ListFragment;
import com.example.cycle_tour_helper.ui.MapFragment;
import com.example.cycle_tour_helper.ui.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ListFragment contributeListFragment();

    @ContributesAndroidInjector
    abstract MapFragment contributeMapFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

}