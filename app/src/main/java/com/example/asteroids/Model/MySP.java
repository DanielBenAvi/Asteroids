package com.example.asteroids.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class MySP {
    private static final String DB_FILE = "DB_FILE";

    private static MySP instance = null;
    private final SharedPreferences preferences;

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


    public void saveToSP(MyDB db) {
        String recordsString = new Gson().toJson(db);
        putString("db", recordsString);

    }

    public MyDB loadFromSP() {
        String importGson = getString("db", "");

        MyDB db = new Gson().fromJson(importGson, MyDB.class);

        if (db == null) {
            db = new MyDB();
        }

        return db;
    }


}


