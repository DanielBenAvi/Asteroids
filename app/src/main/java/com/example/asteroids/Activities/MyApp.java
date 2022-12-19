package com.example.asteroids.Activities;

import android.app.Application;

import com.example.asteroids.Model.MySP;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySP.initSharedPreferences(this);
    }
}
