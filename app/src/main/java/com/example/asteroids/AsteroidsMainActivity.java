package com.example.asteroids;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AsteroidsMainActivity extends AppCompatActivity {

    private MaterialButton asteroids_BTN_right;
    private MaterialButton asteroids_BTN_left;
    private MaterialTextView asteroids_txt_speed;

    Random rand = new Random();

    private Timer timer;
    long startTime = 0;
    private int gameSpeed = 1000;

    private ShapeableImageView[][] board;
    private AsteroidsGameManager gameManager;

    private ShapeableImageView[] hearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViews();
        buttons();

        /*
           write from here only!!!!
         */
        // ship
        gameManager = new AsteroidsGameManager();
//        loadImage(gameManager.getShip().getIMAGE(), board[gameManager.getShip().getY()][gameManager.getShip().getX()]);


        startTimer();

    }


    private void moveLeft() {
        moveShip(-1);
    }


    private void moveRight() {
        moveShip(1);
    }

    /**
     * move the ship
     *
     * @param direction direction – left = (-1), right = (1)
     */
    public void moveShip(int direction) {
        Glide.with(this).clear(board[gameManager.getShip().getY()][gameManager.getShip().getX()]);
        gameManager.moveShip(direction);
        Glide.with(this).load(gameManager.getShip().getIMAGE()).into(board[gameManager.getShip().getY()][gameManager.getShip().getX()]);
    }

    /**
     * Iterate ove all the cells in board and reset the,
     */
    private void resetBoard() {
        for (ShapeableImageView[] imageViews : board) {
            for (ShapeableImageView imageView : imageViews) {
                Glide.with(this).clear(imageView);
            }
        }
    }

    /**
     * Move all the asteroids one block down
     */
    private void moveAsteroids() {
        // move all asteroids down
        gameManager.moveAsteroidsDown(board.length);

        // load all asteroids to the board
        loadAsteroids();
    }

    /**
     * load all asteroids
     */
    private void loadAsteroids() {
        for (Asteroid asteroid : gameManager.getAsteroids()) {
            if (asteroid.getY() < board.length) {
                loadImage(asteroid.getIMAGE(), board[asteroid.getY()][asteroid.getX()]);
            }
        }
    }


    /**
     * Replace Glide
     *
     * @param image     the image
     * @param imageView where to put the image
     */
    private void loadImage(int image, ShapeableImageView imageView) {
        Glide.with(this).load(image).into(imageView);
    }

    /**
     * all the buttons
     */
    private void buttons() {
        asteroids_BTN_right.setOnClickListener(v -> moveRight());
        asteroids_BTN_left.setOnClickListener(v -> moveLeft());
    }

    /**
     * frames
     */
    @SuppressLint("DefaultLocale")
    private void updateTimerUI() {
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        asteroids_txt_speed.setText(String.format("%02d:%02d", minutes, seconds));


        resetBoard();
        moveAsteroids();
        moveShip(0);

        if (seconds % 2 == 0) {
            gameManager.getAsteroids().add(new Asteroid().setX(rand.nextInt(board[0].length)).setY(0));
        }

    }

    /**
     * stop the timer
     */
    private void stopTimer() {
        timer.cancel();
    }


    /**
     * (1) start the timer.
     * (2) can reset after die.
     * (3) update the game every frame.
     */
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (!gameManager.checkCollision()) {
                        updateTimerUI();
                    } else {
                        if (gameManager.getShip().getLife() > 0) {
                            hearts[gameManager.getShip().getLife() - 1].setVisibility(View.INVISIBLE);
                        }
                        vibration();
                        resetBoard();
                        gameManager.getAsteroids().clear();
                        loadImage(gameManager.getShip().getEXPLODE(), board[gameManager.getShip().getY()][gameManager.getShip().getX()]);

                        gameManager.getShip().setLife(gameManager.getShip().getLife() - 1);


                        stopTimer();
                        startTimer();
                    }
                });
            }
        }, gameSpeed, gameSpeed);
        startTime = System.currentTimeMillis();

    }


    /**
     * All find by id
     */
    private void findViews() {
        asteroids_BTN_right = findViewById(R.id.asteroids_BTN_right);
        asteroids_BTN_left = findViewById(R.id.asteroids_BTN_left);
        asteroids_txt_speed = findViewById(R.id.asteroids_txt_speed);

        board = new ShapeableImageView[][]{{findViewById(R.id.asteroids_IMG_00), findViewById(R.id.asteroids_IMG_01), findViewById(R.id.asteroids_IMG_02)}, {findViewById(R.id.asteroids_IMG_10), findViewById(R.id.asteroids_IMG_11), findViewById(R.id.asteroids_IMG_12)}, {findViewById(R.id.asteroids_IMG_20), findViewById(R.id.asteroids_IMG_21), findViewById(R.id.asteroids_IMG_22)}, {findViewById(R.id.asteroids_IMG_30), findViewById(R.id.asteroids_IMG_31), findViewById(R.id.asteroids_IMG_32)}, {findViewById(R.id.asteroids_IMG_40), findViewById(R.id.asteroids_IMG_51), findViewById(R.id.asteroids_IMG_42)}, {findViewById(R.id.asteroids_IMG_50), findViewById(R.id.asteroids_IMG_51), findViewById(R.id.asteroids_IMG_62)}, {findViewById(R.id.asteroids_IMG_60), findViewById(R.id.asteroids_IMG_61), findViewById(R.id.asteroids_IMG_62)}, {findViewById(R.id.asteroids_IMG_70), findViewById(R.id.asteroids_IMG_71), findViewById(R.id.asteroids_IMG_72)}, {findViewById(R.id.asteroids_IMG_80), findViewById(R.id.asteroids_IMG_81), findViewById(R.id.asteroids_IMG_82)}, {findViewById(R.id.asteroids_IMG_90), findViewById(R.id.asteroids_IMG_91), findViewById(R.id.asteroids_IMG_92)}};
        hearts = new ShapeableImageView[]{findViewById(R.id.asteroids_IMG_h0), findViewById(R.id.asteroids_IMG_h1), findViewById(R.id.asteroids_IMG_h2)};
    }


    private void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}