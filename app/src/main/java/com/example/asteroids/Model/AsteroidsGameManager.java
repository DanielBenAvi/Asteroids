package com.example.asteroids.Model;


import android.util.Log;

import com.example.asteroids.Model.GameObjects.Asteroid;
import com.example.asteroids.Model.GameObjects.Object;
import com.example.asteroids.Model.GameObjects.Ship;
import com.example.asteroids.Model.GameObjects.Star;
import com.example.asteroids.Other.Constants;

import java.util.Random;

public class AsteroidsGameManager {

    Random rand = new Random();

    private Ship ship;
    private Object[][] logicBoard;

    /**
     * Constructor
     */
    public AsteroidsGameManager(int rows, int columns) {
        // create the logic board
        logicBoard = new Object[rows][columns];

        // create the ship x - always in the middle
        ship = (Ship) new Ship().setX(logicBoard[0].length / 2);
        ship.setLife(Constants.SHIP_LIFE);
    }


    /**
     * clear all the asteroids from the logicBoard
     */
    public void clearAsteroids() {
        for (int i = 0; i < logicBoard.length; i++) {
            for (int j = 0; j < logicBoard[i].length; j++) {
                if (logicBoard[i][j] instanceof Asteroid) logicBoard[i][j] = null;
            }
        }
    }


    public Ship getShip() {
        return ship;
    }


    /**
     * move all the asteroids down
     *
     * @param boardLength the length of the board
     */
    public void moveAsteroidsAndFuelsDown(int boardLength) {
        // move all asteroids down;
        for (int i = logicBoard.length - 1; i >= 0; i--) {
            for (int j = logicBoard[i].length - 1; j >= 0; j--) {
                if (logicBoard[i][j] instanceof Asteroid) {
                    // remove the asteroid from the board if it reached the bottom
                    if (i != boardLength - 1) {
                        // update the asteroid's y position
                        int tempY = logicBoard[i][j].getY();
                        logicBoard[i][j].setY(tempY + 1);

                        // move the asteroid down in the logic board
                        logicBoard[i + 1][j] = logicBoard[i][j];
                    }
                    logicBoard[i][j] = null;
                }
                // check if the ship is hit by an Fuel
                else if (logicBoard[i][j] instanceof Star) {
                    // remove the asteroid from the board if it reached the bottom
                    if (i != boardLength - 1) {
                        // update the asteroid's y position
                        int tempY = logicBoard[i][j].getY();

                        logicBoard[i][j].setY(tempY + 1);

                        // move the asteroid down in the logic board
                        logicBoard[i + 1][j] = logicBoard[i][j];
                    }
                    logicBoard[i][j] = null;
                }
            }
        }
    }


    /**
     * move the ship
     *
     * @param direction left (-1), right = (1)
     */
    public void moveShip(int direction) {
        // move the ship
        int temp_x;
        temp_x = ship.getX() + direction;
        if (temp_x > logicBoard[0].length - 1) temp_x = 0;
        else if (temp_x < 0) temp_x = logicBoard[0].length - 1;
        ship.setX(temp_x);
    }

    /**
     * get the logic board
     *
     * @return logic board
     */
    public Object[][] getLogicBoard() {
        return logicBoard;
    }

    /**
     * add new asteroid
     */
    public void addNewAsteroid() {
        int i = rand.nextInt(logicBoard[0].length);
        int j = 0;
        Asteroid tempAsteroid = (Asteroid) new Asteroid().setX(i).setY(j);
        getLogicBoard()[j][i] = tempAsteroid;
    }

    public void addNewFuel() {
        int i = rand.nextInt(logicBoard[0].length);
        int j = 0;
        Star tempFuel = (Star) new Star().setX(i).setY(j);
        getLogicBoard()[j][i] = tempFuel;
    }

    public void clearFuel() {
        for (int i = 0; i < logicBoard.length; i++) {
            for (int j = 0; j < logicBoard[i].length; j++) {
                if (logicBoard[i][j] instanceof Star) logicBoard[i][j] = null;
            }
        }
    }

    public int checkCollision() {
        // check if the ship is hit by an asteroid
        if (logicBoard[ship.getY()][ship.getX()] instanceof Asteroid) {
            logicBoard[ship.getY()][ship.getX()] = null;
            return -1;
        }

        // check if the ship is hit by an Fuel
        else if (logicBoard[ship.getY()][ship.getX()] instanceof Star) {
            logicBoard[ship.getY()][ship.getX()] = null;
            return +1;
        }
        return 0;
    }


    public void addUser(User user) {
        // add the user to the database
        MyDB myDB = MyDB.loadFromSP();

        // add the user to the database
        myDB.addUser(user); // add the user to the database

        // save the database to the shared preferences
        Log.d("addUser", myDB.toString());
        MySP.getInstance().saveToSP(myDB);
    }
}
