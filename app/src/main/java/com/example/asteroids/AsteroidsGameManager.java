package com.example.asteroids;


import java.util.Random;

public class AsteroidsGameManager {

    Random rand = new Random();

    private final Ship ship;
    private final Object[][] logicBoard;

    /**
     * Constructor
     */
    public AsteroidsGameManager(int rows, int columns) {
        // create the logic board
        logicBoard = new Object[rows][columns];

        // create the ship x - always in the middle
        ship = new Ship().setX(1).setLife(3);

        // add the ship to the logic board
        logicBoard[ship.getY()][1] = ship;

    }


    /**
     * clear all the asteroids from the logicBoard
     */
    public void clearAsteroids() {
        for (int i = 0; i < logicBoard.length; i++) {
            for (int j = 0; j < logicBoard[i].length; j++) {
                if (logicBoard[i][j] instanceof Asteroid) {
                    logicBoard[i][j] = null;
                }
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
    public boolean moveAsteroidsDown(int boardLength) {
        // move all asteroids down;
        // if an asteroid reached the bottom of the board, remove it
        // the check is done from the bottom to the top
        for (int i = logicBoard.length - 1; i >= 0; i--) {
            for (int j = logicBoard[i].length - 1; j >= 0; j--) {
                if (logicBoard[i][j] instanceof Asteroid) {
                    // remove the asteroid from the board if it reached the bottom
                    if (i != boardLength - 1) {
                        // update the asteroid's y position
                        int tempY = logicBoard[i][j].getY();
                        // check collision
                        if (tempY + 1 == ship.getY() && j == ship.getX()) {
                            return true;
                        }
                        logicBoard[i][j].setY(tempY + 1);

                        // move the asteroid down in the logic board
                        logicBoard[i + 1][j] = logicBoard[i][j];
                    }

                    logicBoard[i][j] = null;
                }
            }
        }
        return false;
    }

    /**
     * move the ship
     *
     * @param direction left (-1), right = (1)
     */
    public void moveShip(int direction) {
        // remove the ship from the board
        logicBoard[ship.getY()][ship.getX()] = null;

        // move the ship
        int temp_x;
        temp_x = ship.getX() + direction;
        if (temp_x > 2) {
            temp_x = 0;
        } else if (temp_x < 0) {
            temp_x = 2;
        }
        ship.setX(temp_x);

        //load ship to the board
        logicBoard[ship.getY()][ship.getX()] = ship;
    }

    /**
     * get the logic board
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
        Asteroid tempAsteroid = new Asteroid().setX(i).setY(j);
        getLogicBoard()[j][i] = tempAsteroid;
    }
}
