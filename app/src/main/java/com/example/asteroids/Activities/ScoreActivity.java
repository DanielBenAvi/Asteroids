package com.example.asteroids.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asteroids.Fragments.ListFragment;
import com.example.asteroids.Fragments.MapFragment;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;

import im.delight.android.location.SimpleLocation;


public class ScoreActivity extends AppCompatActivity {
    public static SimpleLocation location;

    MaterialButton scoreMenu_BTN_StartGame;

    // score variables
    private int score = 0;


    private ListFragment listFragment;
    private MapFragment mapFragment;

    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        findViews();
        location = new SimpleLocation(this);


        listFragment = new ListFragment(); // create a new list fragment
        listFragment.setCallback(callBack_userProtocol); // set the callback

        mapFragment = new MapFragment(); // create a new map fragment

        //  init the fragments
        getSupportFragmentManager().beginTransaction().add(R.id.scoreMenu_FRAME_list, listFragment).commit(); // add the list fragment
        getSupportFragmentManager().beginTransaction().add(R.id.scoreMenu_FRAME_map, mapFragment).commit(); // add the map fragment

        buttonsListeners();

    }

    /**
     * find all the views
     */
    private void findViews() {
        scoreMenu_BTN_StartGame = findViewById(R.id.scoreMenu_BTN_StartGame);
    }

    /**
     * This method is responsible for the buttons listeners
     */
    CallBack_userProtocol callBack_userProtocol = new CallBack_userProtocol() {
        @Override
        public void changeLocation(double latitude, double longitude) {
            mapFragment.zoom(latitude, longitude); // zoom the map to the location
        }
    };


    /**
     * This method is responsible for the buttons listeners
     */
    private void buttonsListeners() {
        // start game button
        scoreMenu_BTN_StartGame.setOnClickListener(v -> openMenuActivity());
    }


    /**
     * This method returns to the menu activity
     */
    private void openMenuActivity() {
        Intent menuActivity = new Intent(this, StartMenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}