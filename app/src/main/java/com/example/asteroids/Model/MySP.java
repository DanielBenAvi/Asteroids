package com.example.asteroids.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MySP {
    private static final String DB_FILE = "DB_FILE";

    private static MySP instance = null;
    private SharedPreferences preferences;

    private MySP(Context context) {
        preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) instance = new MySP(context);
    }

    public static MySP getInstance() {
        return instance;
    }


    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }


}


