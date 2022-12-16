package com.example.asteroids.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.asteroids.Other.App;
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

    private void openMainActivity() {
        Intent gameIntent = new Intent(this, AsteroidsMainActivity.class);
        startActivity(gameIntent);
        finish();
    }

    private void openScoreActivity() {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        startActivity(scoreIntent);
        finish();
    }

    private void buttonsListeners() {
        startMenu_BTN_startGame.setOnClickListener(v -> openMainActivity());
        startMenu_BTN_scores.setOnClickListener(v -> openScoreActivity());

        startMenu_SWITCH_gameOptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    App.gameOption = App.GameOptions.ACCELEROMETER.value;
                    App.toastMaker(StartMenuActivity.this, "Accelerometer");
                } else {
                    App.gameOption = App.GameOptions.BUTTONS.value;
                    App.toastMaker(StartMenuActivity.this, "Buttons");
                }
            }

        });


        startMenu_SWITCH_gameSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    App.gameSpeed = 1000;
                    App.toastMaker(StartMenuActivity.this, "Slow");
                } else {
                    App.gameSpeed = 500;
                    App.toastMaker(StartMenuActivity.this, "Fast");
                }
            }
        });

    }

    private void findViews() {
        startMenu_BTN_startGame = findViewById(R.id.startMenu_BTN_startGame);
        startMenu_BTN_scores = findViewById(R.id.startMenu_BTN_scores);
        startMenu_SWITCH_gameOptions = findViewById(R.id.startMenu_SWITCH_gameOptions);
        startMenu_SWITCH_gameSpeed = findViewById(R.id.startMenu_SWITCH_gameSpeed);
    }

}