package com.example.cycle_tour_helper.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cycle_tour_helper.models.Route;
import com.example.cycle_tour_helper.repository.RouteRepository;
import com.example.cycle_tour_helper.utils.FirebaseCallback;

import java.io.InputStream;

import javax.inject.Inject;

public class RouteViewModel extends ViewModel implements FirebaseCallback {

    private static final String TAG = "RouteViewModel";

    private RouteRepository routeRepository;

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
        if(routeRepository == null){
            routeRepository = new RouteRepository(this);
        }
    }

    public MutableLiveData<Route> getSelectedRoute(){
        return selectedRoute;
    }

    public void setSelectedRoute(Route route){
       // routeRepository.getFileFromFirebase();
        selectedRoute.setValue(route);

    }

    @Override
    public void onSuccessFileFound(Route route) {
        Log.i(TAG, "onSuccessFileFound: " + route.getInputString());
    }

    @Override
    public void onFailedFileFound(Exception e) {
        Log.i(TAG, "onFailedFileFound: ");

    }
}
