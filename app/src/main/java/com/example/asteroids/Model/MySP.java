package com.example.asteroids.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.asteroids.Other.Constants;


public class MySP {
    private static MySP mySharedPreferences = null;
    private SharedPreferences sharedPreferences;


    /**
     * singleton constructor pattern
     *
     * @param context the context
     */
    private MySP(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SP_KEY, Context.MODE_PRIVATE);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MySP getInstance() {
        return mySharedPreferences;
    }

    /**
     * Initialize the shared preferences
     *
     * @param context the context
     */
    public static void initSharedPreferences(Context context) {
        if (mySharedPreferences == null) {
            mySharedPreferences = new MySP(context);
        }
    }


    /**
     * put string to shared preferences
     *
     * @param key   the key
     * @param value the value
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * get string from shared preferences
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }


}


