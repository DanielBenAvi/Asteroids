package com.example.asteroids;

public class Asteroid {
    private int x;
    private int y;
    private int image;

    public Asteroid() {
        image = R.drawable.asteroid;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIMAGE() {
        return image;
    }

    public Asteroid setX(int x) {
        this.x = x;
        return this;
    }

    public Asteroid setY(int y) {
        this.y = y;
        return this;
    }
}
