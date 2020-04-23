package com.example.cycle_tour_helper.models;

public class Route {

    private String routeTitle;
    private String routeFileName;

    public Route(String routeTitle, String routeFileName) {
        this.routeTitle = routeTitle;
        this.routeFileName = routeFileName;
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
}
