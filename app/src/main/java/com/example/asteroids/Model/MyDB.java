package com.example.asteroids.Model;

import java.util.ArrayList;

public class MyDB {

    private static ArrayList<User> users;
    private static MyDB instance;


    /**
     * singleton constructor pattern
     */
    private MyDB() {
        users = new ArrayList<>();
    }


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MyDB getInstance() {
        return instance;
    }

    /**
     * Initialize the database
     */
    public static void initDB() {
        if (instance == null) {
            users = new ArrayList<>();
            instance = new MyDB();
        }
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


        if (getUsers().size() < 10) {
            return;
        }
        // get the top 10 users
        ArrayList<User> tmp = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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
}
