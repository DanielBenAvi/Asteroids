package com.example.asteroids;

public class Constants {
    public static final int SHIP_Y = 8;
    public static final int ASTEROID_TIME_CREATION = 2;
    public static final int SHIP_LIFE = 3;
    public static final int GAME_SPEED = 500;
    public static final int VIBRATION_SPEED = 500;


    enum Direction {
        LEFT(-1), MIDDLE(0), RIGHT(1);

        int value;

        Direction(int i) {
            this.value = i;
        }
    }



}
