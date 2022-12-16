package com.example.asteroids.Model.GameObjects;

import com.example.asteroids.Other.App;
import com.example.asteroids.R;

public class Ship extends Object {
    private int life;

    public Ship() {
        super();
        y = App.SHIP_Y;
        image = R.drawable.ship;
    }

    public int getEXPLODE() {
        return R.drawable.explode;
    }

    public int getLife() {
        return life;
    }

    public Ship setLife(int life) {
        this.life = life;
        return this;
    }

}
