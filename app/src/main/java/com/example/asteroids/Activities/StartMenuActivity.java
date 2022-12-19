package com.example.asteroids.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.asteroids.Model.MyDB;
import com.example.asteroids.Model.MySharedPreferences;
import com.example.asteroids.Other.App;
import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

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

        // initialize Database and SharedPreferences on first run
        MyDB.initDB();
        MySharedPreferences.initSharedPreferences(this);
        loadDB(this);

        findViews();
        buttonsListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateDB(StartMenuActivity.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateDB(StartMenuActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateDB(StartMenuActivity.this);

    }

    /**
     * This method is responsible for updating the DB
     *
     * @param context - the context of the activity
     */
    public static void loadDB(Context context) {
        String fromJSON = MySharedPreferences.getInstance(context).getString(MySharedPreferences.KEY_USERS, "");
        App.myDB = new Gson().fromJson(fromJSON, MyDB.class);


    }

    /**
     * save the database to the shared preferences
     *
     * @param context the context
     */
    public static void updateDB(Context context) {
        String gson = new Gson().toJson(App.myDB);
        MySharedPreferences.getInstance(context).putString(MySharedPreferences.KEY_USERS, gson);
    }

    /**
     * open the game activity
     */
    private void openMainActivity() {
        Intent gameIntent = new Intent(this, AsteroidsMainActivity.class);
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
                    App.gameOption = App.GameOptions.ACCELEROMETER.value;
                    Toasty.info(StartMenuActivity.this, "Accelerometer", Toasty.LENGTH_SHORT).show();
                } else {
                    App.gameOption = App.GameOptions.BUTTONS.value;
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
                    App.gameSpeed = 1000;
                    Toasty.info(StartMenuActivity.this, "Slow", Toasty.LENGTH_SHORT).show();
                } else {
                    App.gameSpeed = 500;
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