package com.example.asteroids;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class AsteroidsMainActivity extends AppCompatActivity {

    private MaterialButton asteroids_BTN_right;
    private MaterialButton asteroids_BTN_left;
    private MaterialTextView asteroids_txt_speed;

    private Timer timer;
    long startTime = 0;

    private ShapeableImageView[][] board;
    private AsteroidsGameManager gameManager;
    private ShapeableImageView[] hearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find views and set listeners
        findViews();
        buttons();

        // create the game manager
        gameManager = new AsteroidsGameManager(board.length, board[0].length);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // start the game
        startTimer();
    }


    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
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

    }


    /**
     * what happened every second
     */
    @SuppressLint("DefaultLocale")
    private void gameFrame() {
        // count the time
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        //print the time
        asteroids_txt_speed.setText(String.format("%02d:%02d", minutes, seconds));

        resetBoard();
        if (!gameManager.moveAsteroidsDown(board.length)) {
            moveShip(Constants.Direction.MIDDLE.value);
            updateBoard();

            // add new asteroid every 2 seconds
            if (seconds % Constants.ASTEROID_TIME_CREATION == 0) {
                gameManager.addNewAsteroid();
            }
        } else {
            // toast
            toastMaker();

            // vibrate
            vibration();

            // remove life
            if (gameManager.getShip().getLife() > 0) {
                hearts[gameManager.getShip().getLife() - 1].setVisibility(View.INVISIBLE);
            }

            // reset the board and clear the logic board from asteroids
            resetBoard();
            gameManager.clearAsteroids();

            // change the image of the ship to explosion
            loadImage(gameManager.getShip().getEXPLODE(), board[gameManager.getShip().getY()][gameManager.getShip().getX()]);

            // remove life from the ship
            gameManager.getShip().setLife(gameManager.getShip().getLife() - 1);

            // reset the game
            stopTimer();
            startTimer();
        }


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
        int gameSpeed = Constants.GAME_SPEED;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> gameFrame());
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
     * toast maker
     */
    private void toastMaker() {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Collision", duration);
        toast.show();
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
        asteroids_BTN_right.setOnClickListener(v -> moveShip(Constants.Direction.RIGHT.value));
        asteroids_BTN_left.setOnClickListener(v -> moveShip(Constants.Direction.LEFT.value));
    }
}