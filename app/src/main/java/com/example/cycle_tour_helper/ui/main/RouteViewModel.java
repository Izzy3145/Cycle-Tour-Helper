package com.example.cycle_tour_helper.ui.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cycle_tour_helper.models.Route;

import javax.inject.Inject;

public class RouteViewModel extends ViewModel {

    private static final String TAG = "RouteViewModel";

    private MutableLiveData<Route> selectedRoute;

    @Inject
    public RouteViewModel(){
        init();
        Log.i(TAG, "View model is ready");
    }

    void init(){
        if(selectedRoute == null){
            selectedRoute = new MutableLiveData<>();
        }
    }

    public MutableLiveData<Route> getSelectedRoute(){
        return selectedRoute;
    }

    public void setSelectedRoute(Route route){
        selectedRoute.setValue(route);
    }
}
