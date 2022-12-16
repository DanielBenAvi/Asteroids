package com.example.asteroids.Other;

import android.app.Application;
import android.content.Context;

import com.example.asteroids.Model.MyDB;
import com.example.asteroids.Model.MySharedPreferences;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;

public class App extends Application {

    public static MyDB myDB;
    public static MySharedPreferences mySharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Database
        myDB.initDB();
        // Initialize SharedPreferences
        MySharedPreferences.initSharedPreferences(this);


    }




    /**
     * Game Finale Settings
     */
    public static final int SHIP_Y = 8;
    public static final int ASTEROID_TIME_CREATION = 2;
    public static final int FUEL_TIME_CREATION = 3;
    public static final int DIFFICULTY_DEFAULT = 4;
    public static final int SHIP_LIFE = 3;
    public static final int VIBRATION_SPEED = 500;
    public static final float MOVEMENT_VALUE = 4.0f;

    /**
     * Game Settings
     */
    public static int gameSpeed = 500;
    public static int difficulty = 4;
    public static int gameScore = 0;
    public static int gameOption = GameOptions.BUTTONS.value;


    /**
     * Enum for Game Options
     */
    public enum GameOptions {
        BUTTONS(0), ACCELEROMETER(1);
        public final int value;

        GameOptions(int i) {
            this.value = i;
        }
    }


    /**
     * Direction Enum
     */
    public enum Direction {
        LEFT(-1), MIDDLE(0), RIGHT(1);
        public final int value;

        Direction(int i) {
            this.value = i;
        }
    }


}
