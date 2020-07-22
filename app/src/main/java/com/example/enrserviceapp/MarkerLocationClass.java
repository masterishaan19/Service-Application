package com.example.enrserviceapp;

class MarkerLocationClass{
    String key;
    double latitude, longitude, radius;

    public MarkerLocationClass(String key, double latitude, double longitude, double radius) {
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getKey() {
        return key;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }
}
