package com.example.asteroids;

public class Ship extends Object {
    private int life;

    public Ship() {
        super();
        y = 8;
        image = R.drawable.ship;
    }

    public int getEXPLODE() {
        return R.drawable.explode;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLife() {
        return life;
    }

    public Ship setX(int x) {
        this.x = x;
        return this;
    }

    public Ship setLife(int life) {
        this.life = life;
        return this;
    }

    public Ship setY(int y) {
        this.y = y;
        return this;
    }
}
