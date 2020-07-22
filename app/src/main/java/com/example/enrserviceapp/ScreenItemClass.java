package com.example.enrserviceapp;

import android.util.Log;

class ScreenItemClass{
    String description, title;
    int imageUrl;
    String TAG = "Demo text";
    public ScreenItemClass(){
        Log.d(TAG, "ScreenItemClass: created a screen Item object for testing..");
    }
    public ScreenItemClass(String description, String title, int imageUrl) {
        this.description = description;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}
