package com.example.enrserviceapp;

import com.google.android.gms.maps.model.LatLng;

class MarkerObject {
    private double latitude, longitude;
    private long radius;
    MarkerObject(double latitude, double longitude, long radius){
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public long getRadius() {
        return radius;
    }
}
