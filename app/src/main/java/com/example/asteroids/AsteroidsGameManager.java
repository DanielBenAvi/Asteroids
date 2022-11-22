package com.example.asteroids;

import java.util.ArrayList;

public class AsteroidsGameManager {

    private ArrayList<Asteroid> asteroids; // old asteroids TODO: remove
    private Ship ship;
    private Object[][] logicBoard;

    /**
     * Constructor
     */
    public AsteroidsGameManager(int rows, int columns) {
        // create the logic board
        logicBoard = new Object[rows][columns];
        // create the ship y - always in the one before the last row
        int SHIP_Y = 8;
        // create the ship x - always in the middle
        ship = new Ship().setX(1).setY(SHIP_Y).setLife(3);
        // add the ship to the logic board
        logicBoard[SHIP_Y][1] = ship;
        // create the asteroids TODO: remove this
        asteroids = new ArrayList<>();
    }

    // TODO: remove this
    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public Ship getShip() {
        return ship;
    }

    /**
     * check if asteroid collided with space ship
     *
     * @return true (collided), false (not collided)
     */
    // TODO: fix the collision detection
    public boolean checkCollision() {
        for (Asteroid asteroid : asteroids) {
            if (asteroid.getY() == ship.getY() && ship.getX() == asteroid.getX()) {
                return true;
            }
        }
        return false;
    }

    /**
     * move all the asteroids down
     *
     * @param boardLength the length of the board
     */
    // TODO: add collision detection
    public void moveAsteroidsDown(int boardLength) {
        // move all asteroids down;
        // if an asteroid reached the bottom of the board, remove it
        // the check is done from the bottom to the top
        for (int i = logicBoard.length - 1; i >= 0; i--) {
            for (int j = logicBoard[i].length - 1; j >= 0; j--) {
                if (logicBoard[i][j] instanceof Asteroid) {
                    // remove the asteroid from the board if it reached the bottom
                    if (i != boardLength - 1) {
                        // move the asteroid down
                        logicBoard[i + 1][j] = logicBoard[i][j];
                    }
                    logicBoard[i][j] = null;
                }
            }
        }

        // old way TODO: remove
        // remove the first one if got to the end
        if (asteroids.size() == boardLength) {
            asteroids.remove(asteroids.get(0));
        }
        // old way TODO: remove
        // iterate over the asteroids and move all of them down
        for (int i = asteroids.size() - 1; i >= 0; i--) {
            int temp_y = asteroids.get(i).getY() + 1;
            asteroids.get(i).setY(temp_y);
        }
    }

    /**
     * move the ship
     *
     * @param direction left (-1), right = (1)
     */
    // TODO: add collision detection
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
}
