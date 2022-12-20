package com.example.asteroids;

import android.app.Application;
import android.util.Log;

import com.example.asteroids.Model.MySP;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySP.init(this);
        Log.d("TAG", "onCreate: MyApp");
    }

}
