package com.example.asteroids;

public class Ship {
    private int x;
    private int y;
    private int life;
    private final int IMAGE = R.drawable.ship;
    private final int EXPLODE = R.drawable.explode;

    public Ship() {
    }

    public int getEXPLODE() {
        return EXPLODE;
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

    public int getIMAGE() {
        return IMAGE;
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
