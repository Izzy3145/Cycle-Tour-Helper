package com.example.cycle_tour_helper.di;

import android.app.Application;

import com.example.cycle_tour_helper.BaseApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                ViewModelFactoryModule.class,
                AppModule.class })

public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder{

        //@BindsInstance can be used when we want to bind an instance of an object to
        //the component at the time of construction
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
