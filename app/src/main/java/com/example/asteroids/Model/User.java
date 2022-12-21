package com.example.asteroids.Model;

public class User {
    private String name = "";
    private int score = 0;
    private double latitude = 0.0;
    private double longitude = 0.0;

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

    public double getLatitude() {
        return latitude;
    }

    public User setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public User setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
