package com.example.asteroids;

public class Asteroid extends Object {

    public Asteroid() {
        super();
        image = R.drawable.asteroid;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
