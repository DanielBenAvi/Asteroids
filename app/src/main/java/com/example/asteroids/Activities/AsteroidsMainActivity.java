package com.example.asteroids.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.asteroids.Model.AsteroidsGameManager;
import com.example.asteroids.Model.User;
import com.example.asteroids.Other.Constants;
import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class AsteroidsMainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;

    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    public static final String KEY_LATITUDE = "KEY_LATITUDE";
    private String userName = "";


    private MaterialButton asteroids_BTN_right;
    private MaterialButton asteroids_BTN_left;
    private MaterialTextView asteroids_txt_speed;

    private Timer timer;
    long startTime = 0;
    long timeStemp = 0;

    private ShapeableImageView[][] board;
    private AsteroidsGameManager gameManager;
    private ShapeableImageView[] hearts;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views and set listeners
        findViews();
        buttons();

        // get user name
        userName = getIntent().getStringExtra(KEY_NAME);


        if (Constants.gameOption == Constants.GameOptions.ACCELEROMETER.value) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // create the game manager
        gameManager = new AsteroidsGameManager(board.length, board[0].length);

        Constants.difficulty = Constants.DIFFICULTY_DEFAULT;

        // Move detector
    }


    @Override
    protected void onResume() {
        super.onResume();
        // start the game
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        mediaPlayer.release();
    }


    /**
     * move the ship
     *
     * @param direction (1 = right, -1 = left)
     */
    private void moveShip(int direction) {
        // 1) clear the board
        resetBoard();
        // 2) move the ship
        gameManager.moveShip(direction);
        // 3) update the board
        updateBoard();
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
     * load all asteroids
     */
    private void updateBoard() {
        // load the objects to the board from the logic board
        for (int i = 0; i < gameManager.getLogicBoard().length; i++) {
            for (int j = 0; j < gameManager.getLogicBoard()[i].length; j++) {
                // only load the objects that are not null
                if (gameManager.getLogicBoard()[i][j] != null) {
                    loadImage(gameManager.getLogicBoard()[i][j].getImage(), board[i][j]);
                }
            }
        }

        loadImage(gameManager.getShip().getImage(), board[gameManager.getShip().getY()][gameManager.getShip().getX()]);

    }


    /**
     * what happened every second
     */
    @SuppressLint("DefaultLocale")
    private void gameFrame() {
        moveShip(Constants.Direction.MIDDLE.value);


        // count the time
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        seconds %= 60;

        //print the time
        asteroids_txt_speed.setText(String.format("SCORE: %02d", Constants.gameScore));

        resetBoard();
        // check if the ship is alive
        int result = gameManager.checkCollision();

        // move the asteroids and fuels down
        gameManager.moveAsteroidsAndFuelsDown(board.length);


        // collision handling
        if (result >= 0) {
            Constants.gameScore += result;
            if (result > 0) {
                makeSound(R.raw.ding_sound);
            }
            regularMovement(seconds);
        } else {
            collisionMovement();
        }

    }

    /**
     * what happened when there is a collision
     */
    private void collisionMovement() {

        Constants.difficulty = Constants.DIFFICULTY_DEFAULT;

        Constants.toast(this, "COLLISION!");

        // vibrate
        vibration();

        // remove life
        if (gameManager.getShip().getLife() > 1) {
            makeSound(R.raw.crash_sound);
            hearts[gameManager.getShip().getLife() - 1].setVisibility(View.INVISIBLE);
        } else {
            makeSound(R.raw.game_over);
            Constants.gameOption = Constants.GameOptions.BUTTONS.value;
            openScoreActivity(Constants.gameScore);
        }
        // reset the board and clear the logic board from asteroids
        resetBoard();
        gameManager.clearAsteroids();
        gameManager.clearFuel();

        // change the image of the ship to explosion
        loadImage(gameManager.getShip().getEXPLODE(), board[gameManager.getShip().getY()][gameManager.getShip().getX()]);

        // remove life from the ship
        gameManager.getShip().setLife(gameManager.getShip().getLife() - 1);

        // reset the game
        stopTimer();
        startTimer();
    }


    /**
     * what happened when there is no collision
     *
     * @param seconds the time in seconds
     */
    private void regularMovement(int seconds) {

        moveShip(Constants.Direction.MIDDLE.value);

        updateBoard();

        // add new asteroid every 2 seconds
        if (seconds % Constants.ASTEROID_TIME_CREATION == 0) {
            int random = (int) (Math.random() * (board[0].length - Constants.difficulty)) + 1;
            for (int i = 0; i < random; i++) {
                gameManager.addNewAsteroid();
            }
        }

        // add new Fuel every 3 seconds
        if (seconds % Constants.FUEL_TIME_CREATION == 0) {
            gameManager.addNewFuel();
        }
    }


    /**
     * open the score activity
     *
     * @param s the score
     */
    private void openScoreActivity(int s) {
        addUser();
        Constants.gameScore = 0;
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        startActivity(scoreIntent);
        mediaPlayer.release();
        finish();
    }

    /**
     * stop the timer
     */
    private void stopTimer() {
        timer.cancel();
    }

    /**
     * start the timer
     */
    private void startTimer() {
        timer = new Timer();
        int gameSpeed = Constants.gameSpeed;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> gameFrame());
            }
        }, Constants.gameSpeed, Constants.gameSpeed);
        startTime = System.currentTimeMillis();
    }


    /**
     * All find by id
     */
    private void findViews() {
        asteroids_BTN_right = findViewById(R.id.asteroids_BTN_right);
        asteroids_BTN_left = findViewById(R.id.asteroids_BTN_left);
        asteroids_txt_speed = findViewById(R.id.asteroids_txt_speed);

        board = new ShapeableImageView[][]{{findViewById(R.id.asteroids_IMG_00), findViewById(R.id.asteroids_IMG_01), findViewById(R.id.asteroids_IMG_02), findViewById(R.id.asteroids_IMG_03), findViewById(R.id.asteroids_IMG_04)}, {findViewById(R.id.asteroids_IMG_10), findViewById(R.id.asteroids_IMG_11), findViewById(R.id.asteroids_IMG_12), findViewById(R.id.asteroids_IMG_13), findViewById(R.id.asteroids_IMG_14)}, {findViewById(R.id.asteroids_IMG_20), findViewById(R.id.asteroids_IMG_21), findViewById(R.id.asteroids_IMG_22), findViewById(R.id.asteroids_IMG_23), findViewById(R.id.asteroids_IMG_24)}, {findViewById(R.id.asteroids_IMG_30), findViewById(R.id.asteroids_IMG_31), findViewById(R.id.asteroids_IMG_32), findViewById(R.id.asteroids_IMG_33), findViewById(R.id.asteroids_IMG_34)}, {findViewById(R.id.asteroids_IMG_40), findViewById(R.id.asteroids_IMG_41), findViewById(R.id.asteroids_IMG_42), findViewById(R.id.asteroids_IMG_43), findViewById(R.id.asteroids_IMG_44)}, {findViewById(R.id.asteroids_IMG_50), findViewById(R.id.asteroids_IMG_51), findViewById(R.id.asteroids_IMG_52), findViewById(R.id.asteroids_IMG_53), findViewById(R.id.asteroids_IMG_54)}, {findViewById(R.id.asteroids_IMG_60), findViewById(R.id.asteroids_IMG_61), findViewById(R.id.asteroids_IMG_62), findViewById(R.id.asteroids_IMG_63), findViewById(R.id.asteroids_IMG_64)}, {findViewById(R.id.asteroids_IMG_70), findViewById(R.id.asteroids_IMG_71), findViewById(R.id.asteroids_IMG_72), findViewById(R.id.asteroids_IMG_73), findViewById(R.id.asteroids_IMG_74)}, {findViewById(R.id.asteroids_IMG_80), findViewById(R.id.asteroids_IMG_81), findViewById(R.id.asteroids_IMG_82), findViewById(R.id.asteroids_IMG_83), findViewById(R.id.asteroids_IMG_84)}, {findViewById(R.id.asteroids_IMG_90), findViewById(R.id.asteroids_IMG_91), findViewById(R.id.asteroids_IMG_92), findViewById(R.id.asteroids_IMG_93), findViewById(R.id.asteroids_IMG_94)}};

        hearts = new ShapeableImageView[]{findViewById(R.id.asteroids_IMG_h0), findViewById(R.id.asteroids_IMG_h1), findViewById(R.id.asteroids_IMG_h2)};

        if (Constants.gameOption == Constants.GameOptions.BUTTONS.value) {
            asteroids_BTN_right.setVisibility(View.VISIBLE);
            asteroids_BTN_left.setVisibility(View.VISIBLE);
        } else {
            asteroids_BTN_right.setVisibility(View.INVISIBLE);
            asteroids_BTN_left.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Make sound
     */
    private void makeSound(int sound) {
        mediaPlayer = MediaPlayer.create(this, sound);
        mediaPlayer.setVolume(100, 100);
        mediaPlayer.start();
    }


    /**
     * vibration
     */
    @SuppressLint("ObsoleteSdkInt")
    private void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(Constants.VIBRATION_SPEED, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(Constants.VIBRATION_SPEED);
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
        if (Constants.gameOption == Constants.GameOptions.BUTTONS.value) {
            asteroids_BTN_right.setOnClickListener(v -> moveShip(Constants.Direction.RIGHT.value));
            asteroids_BTN_left.setOnClickListener(v -> moveShip(Constants.Direction.LEFT.value));
        }
    }

    /**
     * Move ship
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (Constants.gameOption != Constants.GameOptions.ACCELEROMETER.value) {
            return;
        }
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis - timeStemp < 500) {
            return;
        }

        timeStemp = currentTimeMillis;
        float x = event.values[0];
        if (x > Constants.MOVEMENT_VALUE) {
            moveShip(Constants.Direction.LEFT.value);
        } else if (x < -Constants.MOVEMENT_VALUE) {
            moveShip(Constants.Direction.RIGHT.value);
        }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private void addUser() {
        Intent intent = getIntent();
        User user = new User();
        user.setName(intent.getStringExtra(KEY_NAME));
        user.setScore(Constants.gameScore);
        user.setLatitude(intent.getDoubleExtra(KEY_LATITUDE, 0));
        user.setLongitude(intent.getDoubleExtra(KEY_LONGITUDE, 0));

        gameManager.addUser(user);


    }


}