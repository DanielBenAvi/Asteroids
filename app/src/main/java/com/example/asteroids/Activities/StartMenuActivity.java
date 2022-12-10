package com.example.asteroids.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.example.asteroids.Other.Constants;
import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;

import es.dmoral.toasty.Toasty;

public class StartMenuActivity extends AppCompatActivity {

    MaterialButton startMenu_BTN_startGame;
    MaterialButton startMenu_BTN_scores;
    SwitchCompat startMenu_SWITCH_gameOptions;

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
                    Constants.gameOption = Constants.GameOptions.ACCELEROMETER.value;
                    toastMaker("Accelerometer");
                } else {
                    Constants.gameOption = Constants.GameOptions.BUTTONS.value;
                    toastMaker("Buttons");
                }
            }
        });

    }

    private void toastMaker(String gameOption) {
        Context context = getApplicationContext();
        Toasty.success(context, gameOption).show();
    }

    private void findViews() {
        startMenu_BTN_startGame = findViewById(R.id.startMenu_BTN_startGame);
        startMenu_BTN_scores = findViewById(R.id.startMenu_BTN_scores);
        startMenu_SWITCH_gameOptions = findViewById(R.id.startMenu_SWITCH_gameOptions);
    }

}