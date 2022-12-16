package com.example.asteroids.Model;

import java.util.ArrayList;

public class MyDB {

    private ArrayList<User> users = new ArrayList<>();
    private static MyDB instance;

    // singleton


    private MyDB() {
    }


    public static MyDB getInstance() {
        if (instance == null) {
            instance = new MyDB();
        }
        return instance;
    }

    public MyDB getDB() {
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public MyDB setUsers(ArrayList<User> users) {
        this.users = users;
        return this;
    }

}
