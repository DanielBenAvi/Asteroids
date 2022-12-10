package com.example.asteroids.Model;

public class User {
    private String name= "";
    private int score = 0;
    private float latitude = 0.0f;
    private float longitude = 0.0f;

    public User() {
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public User setScore(int score) {
        this.score = score;
        return this;
    }

    public float getLatitude() {
        return latitude;
    }

    public User setLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    public float getLongitude() {
        return longitude;
    }

    public User setLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }
}
