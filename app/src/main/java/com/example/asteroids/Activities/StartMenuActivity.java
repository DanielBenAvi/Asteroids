package com.example.asteroids.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.asteroids.Other.Constants;
import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;

public class StartMenuActivity extends AppCompatActivity {

    MaterialButton startMenu_BTN_startGame;
    MaterialButton startMenu_BTN_scores;
    SwitchCompat startMenu_SWITCH_gameOptions;
    SwitchCompat startMenu_SWITCH_gameSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        findViews();
        buttonsListeners();
    }


    /**
     * open the game activity
     */
    private void openMainActivity() {
        Intent gameIntent = new Intent(this, UserName.class);
        startActivity(gameIntent);
        finish();
    }

    /**
     * open the scores activity
     */
    private void openScoreActivity() {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        startActivity(scoreIntent);
        finish();
    }

    /**
     * buttons listeners
     */
    private void buttonsListeners() {
        // start game button
        startMenu_BTN_startGame.setOnClickListener(v -> openMainActivity());
        // score button
        startMenu_BTN_scores.setOnClickListener(v -> openScoreActivity());

        // game options switch
        startMenu_SWITCH_gameOptions.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // if the switch is on - the game will be with accelerometer
            String mode = isChecked ? "Accelerometer" : "Buttons";
            Constants.gameOption = isChecked ? Constants.GameOptions.ACCELEROMETER.value : Constants.GameOptions.BUTTONS.value;
            Constants.toast(StartMenuActivity.this, "Game mode: " + mode);
        });

        // game speed switch
        startMenu_SWITCH_gameSpeed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // if the switch is on - the game will be with fast speed
            String speed = isChecked ? "Fast" : "Slow";
            Constants.gameSpeed = isChecked ? 300 : 1000;
            Constants.toast(StartMenuActivity.this, speed);
        });

    }

    /**
     * find all the views
     */
    private void findViews() {
        startMenu_BTN_startGame = findViewById(R.id.startMenu_BTN_startGame);
        startMenu_BTN_scores = findViewById(R.id.startMenu_BTN_scores);
        startMenu_SWITCH_gameOptions = findViewById(R.id.startMenu_SWITCH_gameOptions);
        startMenu_SWITCH_gameSpeed = findViewById(R.id.startMenu_SWITCH_gameSpeed);
    }

}