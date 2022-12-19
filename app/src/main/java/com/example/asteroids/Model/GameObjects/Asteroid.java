package com.example.asteroids.Model.GameObjects;

import com.example.asteroids.Other.Constants;

public class Asteroid extends Object {

    public Asteroid() {
        super();
        image = Constants.asteroids[getRandomNumber()];

    }

    // get random number between 0 and 5
    public int getRandomNumber() {
        return (int) (Math.random() * 6) ;
    }
}
