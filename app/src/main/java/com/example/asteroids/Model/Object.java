package com.example.asteroids.Model;

public class Object {
    protected int x;
    protected int y;
    protected int image;


    public Object() {
    }


    public int getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Object setX(int x) {
        this.x = x;
        return this;
    }

    public Object setY(int y) {
        this.y = y;
        return this;
    }


}
