package com.example.asteroids.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.asteroids.Other.Constants;
import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;

import es.dmoral.toasty.Toasty;

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
        startMenu_SWITCH_gameOptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if the switch is on - the game will be with accelerometer
                if (isChecked) {
                    Constants.gameOption = Constants.GameOptions.ACCELEROMETER.value;
                    Toasty.info(StartMenuActivity.this, "Accelerometer", Toasty.LENGTH_SHORT).show();
                } else {
                    Constants.gameOption = Constants.GameOptions.BUTTONS.value;
                    Toasty.info(StartMenuActivity.this, "Buttons", Toasty.LENGTH_SHORT).show();
                }
            }

        });

        // game speed switch
        startMenu_SWITCH_gameSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if the switch is on - the game will be with be faster
                if (!isChecked) {
                    Constants.gameSpeed = 1000;
                    Toasty.info(StartMenuActivity.this, "Slow", Toasty.LENGTH_SHORT).show();
                } else {
                    Constants.gameSpeed = 300;
                    Toasty.info(StartMenuActivity.this, "Fast", Toasty.LENGTH_SHORT).show();
                }
            }
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