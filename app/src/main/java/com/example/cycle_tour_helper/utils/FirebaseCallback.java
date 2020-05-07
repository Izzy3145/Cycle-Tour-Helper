package com.example.cycle_tour_helper.utils;

import com.example.cycle_tour_helper.models.Route;

public interface FirebaseCallback {

    void onSuccessFileFound(Route route);
    void onFailedFileFound(Exception e);

}
