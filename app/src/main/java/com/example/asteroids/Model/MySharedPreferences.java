package com.example.asteroids.Model;

import android.content.Context;
import android.content.SharedPreferences;


public class MySharedPreferences {
    public static final String KEY_USERS = "my_prefs";
    private static MySharedPreferences instance;
    private final SharedPreferences sharedPreferences;


    /**
     * singleton constructor pattern
     *
     * @param context the context
     */
    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_USERS, Context.MODE_PRIVATE);
    }

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static MySharedPreferences getInstance(Context context) {
        return instance;
    }

    /**
     * Initialize the shared preferences
     *
     * @param context the context
     */
    public static void initSharedPreferences(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
        }
    }

    /**
     * getter
     *
     * @return the shared preferences
     */
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
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


