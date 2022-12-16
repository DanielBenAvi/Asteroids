package com.example.asteroids.Model;

import android.content.Context;
import android.content.SharedPreferences;


public class MySharedPreferences {
    public static final String KEY_USERS = "my_prefs";
    private static MySharedPreferences instance;
    private SharedPreferences sharedPreferences;

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_USERS, Context.MODE_PRIVATE);
    }

    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
        }
        return instance;
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }


}


