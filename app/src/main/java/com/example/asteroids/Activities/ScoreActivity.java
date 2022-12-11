package com.example.asteroids.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asteroids.Fragments.ListFragment;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.Model.User;
import com.example.asteroids.R;
import com.example.asteroids.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import es.dmoral.toasty.Toasty;

public class ScoreActivity extends AppCompatActivity {


    public static final String KEY_SCORE = "KEY_SCORE";
    MaterialButton scoreMenu_BTN_StartGame;
    MaterialButton scoreMenu_BTN_addScore;
    MaterialTextView scoreMenu_TXT_score;
    TextInputEditText scoreMenu_EDT_addName;
    LinearLayout scoreMenu_LAY_addScore;

    int score;


    private ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        findViews();
        setScore();


        listFragment = new ListFragment();
        listFragment.setCallback(callBack_userProtocol);
        getSupportFragmentManager().beginTransaction().add(R.id.scoreMenu_FRAME_list, listFragment).commit();

        buttonsListeners();

    }

    CallBack_userProtocol callBack_userProtocol = new CallBack_userProtocol() {
        @Override
        public void user(String name) {
            showUserLocation(name);
        }
    };

    private void showUserLocation(String name) {
        Toasty.success(this, "User: " + name, Toast.LENGTH_SHORT).show();
    }

    private void setScore() {
        Intent intent = getIntent();
        score = intent.getIntExtra(KEY_SCORE, 0);
        if (score != 0) {
            scoreMenu_TXT_score.setText(score + "");
            scoreMenu_LAY_addScore.setVisibility(View.VISIBLE);
        } else {
            scoreMenu_LAY_addScore.setVisibility(View.GONE);
        }
    }

    private void buttonsListeners() {
        scoreMenu_BTN_addScore.setOnClickListener(v -> {
            listFragment.addScore(new User().setScore(score).setName("" + scoreMenu_EDT_addName.getText()));
            scoreMenu_LAY_addScore.setVisibility(View.GONE);
            scoreMenu_TXT_score.setVisibility(View.GONE);
        });
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
        scoreMenu_EDT_addName = findViewById(R.id.scoreMenu_EDT_addName);
        scoreMenu_LAY_addScore = findViewById(R.id.scoreMenu_LAY_addScore);
    }
}