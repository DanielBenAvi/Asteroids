package com.example.asteroids.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class MyDB {

    private static ArrayList<User> users = new ArrayList<>();


    public MyDB() {
    }


    /**
     * add user to the database
     *
     * @param user the user
     */
    public void addUser(User user) {
        // add the user to the database
        users.add(user);

        // sort the users by score descending
        users.sort((o1, o2) -> o2.getScore() - o1.getScore());

        int flag = users.size();
        if (flag > 10) flag = 10;

        ArrayList<User> tmp = new ArrayList<>();
        // get the top 10 users
        for (int i = 0; i < flag; i++) {
            tmp.add(users.get(i));
        }
        users = tmp;
    }

    /**
     * remove user from the database
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * setter
     *
     * @param users the user
     */
    public static void setUsers(ArrayList<User> users) {
        MyDB.users = users;
    }

    @NonNull
    @Override
    public String toString() {
        return Arrays.toString(users.toArray());
    }

    public static MyDB loadFromSP() {
        String importGson = MySP.getInstance().getString("db", "");
        Log.d("importGson", importGson);
        MyDB db = new Gson().fromJson(importGson, MyDB.class);

        if (db == null) {
            db = new MyDB();
        }

        return db;
    }
}
