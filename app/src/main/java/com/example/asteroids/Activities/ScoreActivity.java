package com.example.asteroids.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class ScoreActivity extends AppCompatActivity {


    public static final String KEY_SCORE = "KEY_SCORE";
    MaterialButton scoreMenu_BTN_StartGame;
    MaterialButton scoreMenu_BTN_addScore;
    MaterialTextView scoreMenu_TXT_score;
    ListView scoreMenu_LST_scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        findViews();
        buttonsListeners();
        setScore();


    }

    private void setScore() {
        Intent intent = getIntent();
        int score = intent.getIntExtra(KEY_SCORE, 0);
        if (score != 0) {
            scoreMenu_TXT_score.setText(score + "");
            scoreMenu_BTN_addScore.setVisibility(View.VISIBLE);
        } else {
            scoreMenu_BTN_addScore.setVisibility(View.INVISIBLE);
        }
    }

    private void buttonsListeners() {
        scoreMenu_BTN_StartGame.setOnClickListener(v -> openMenuActivity());
    }

    private void openMenuActivity() {
        Intent menuActivity = new Intent(this, StartMenuActivity.class);
        startActivity(menuActivity);
        finish();
    }

    private void findViews() {
        scoreMenu_BTN_StartGame = findViewById(R.id.scoreMenu_BTN_StartGame);
        scoreMenu_BTN_addScore = findViewById(R.id.scoreMenu_BTN_addScore);
        scoreMenu_TXT_score = findViewById(R.id.scoreMenu_TXT_score);
    }
}