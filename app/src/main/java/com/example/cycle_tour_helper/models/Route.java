package com.example.cycle_tour_helper.models;

import androidx.annotation.Nullable;

public class Route {

    private String routeTitle;
    private String routeFileName;
    private String inputString;

    public Route() {
    }

    public Route(String routeTitle, String routeFileName, @Nullable String inputString) {
        this.routeTitle = routeTitle;
        this.routeFileName = routeFileName;
        this.inputString = inputString;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public void setRouteTitle(String routeTitle) {
        this.routeTitle = routeTitle;
    }

    public String getRouteFileName() {
        return routeFileName;
    }

    public void setRouteFileName(String routeFileName) {
        this.routeFileName = routeFileName;
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }
}
