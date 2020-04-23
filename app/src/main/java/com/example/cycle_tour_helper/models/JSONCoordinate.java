package com.example.cycle_tour_helper.models;

public class JSONCoordinate {

    private double lat;
    private double lng;
    private double altitude;

    public JSONCoordinate(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public JSONCoordinate(double lat, double lng, double altitude) {
        this.lat = lat;
        this.lng = lng;
        this.altitude = altitude;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
