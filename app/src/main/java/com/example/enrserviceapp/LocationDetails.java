package com.example.enrserviceapp;

import com.google.android.gms.maps.model.LatLng;

class LocationDetails{
    private LatLng latLng;
    private double radius;

    public LocationDetails(LatLng latLng, double radius) {
        this.latLng = latLng;
        this.radius = radius;
    }
    public LatLng getLatLng() {
        return latLng;
    }
    public double getRadius() {
        return radius;
    }
}
