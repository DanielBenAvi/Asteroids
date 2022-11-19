package com.example.asteroids;

import java.util.ArrayList;

public class AsteroidsGameManager {

    private ArrayList<Asteroid> asteroids;
    private Ship ship;

    /**
     * Constructor
     */
    public AsteroidsGameManager() {
        int SHIP_Y = 8;
        ship = new Ship().setX(1).setY(SHIP_Y).setLife(3);
        asteroids = new ArrayList<>();
    }

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
    public void moveAsteroidsDown(int boardLength) {
        // remove the first one if got to the end
        if (asteroids.size() == boardLength) { //TODO remove hard code
            asteroids.remove(asteroids.get(0));
        }

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
    public void moveShip(int direction) {
        int temp_x;
        temp_x = ship.getX() + direction;
        if (temp_x > 2) {
            temp_x = 0;
        } else if (temp_x < 0) {
            temp_x = 2;
        }
        ship.setX(temp_x);
    }
}
