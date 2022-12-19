package com.example.asteroids.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.asteroids.Fragments.ListFragment;
import com.example.asteroids.Fragments.MapFragment;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.Model.MyDB;
import com.example.asteroids.Model.MySharedPreferences;
import com.example.asteroids.Model.User;
import com.example.asteroids.Other.App;
import com.example.asteroids.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import im.delight.android.location.SimpleLocation;


public class ScoreActivity extends AppCompatActivity {
    public static SimpleLocation location;

    public static final String KEY_SCORE = "KEY_SCORE";
    MaterialButton scoreMenu_BTN_StartGame;
    MaterialButton scoreMenu_BTN_addScore;
    MaterialTextView scoreMenu_TXT_score;
    TextInputEditText scoreMenu_EDT_addName;
    LinearLayout scoreMenu_LAY_addScore;

    // score variables
    private int score = 0;


    private ListFragment listFragment;
    private MapFragment mapFragment;

    private double latitude;
    private double longitude;
    private static MyDB myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        findViews();
        setScore();

        location = new SimpleLocation(this);

        // create an instance of the database
        myDB = MyDB.getInstance();
        loadDB(ScoreActivity.this); // load the database from the shared preferences


        listFragment = new ListFragment(); // create a new list fragment
        listFragment.setCallback(callBack_userProtocol); // set the callback

        mapFragment = new MapFragment(); // create a new map fragment

        //  init the fragments
        getSupportFragmentManager().beginTransaction().add(R.id.scoreMenu_FRAME_list, listFragment).commit(); // add the list fragment
        getSupportFragmentManager().beginTransaction().add(R.id.scoreMenu_FRAME_map, mapFragment).commit(); // add the map fragment

        buttonsListeners();

    }

    @Override
    protected void onStop() {
        super.onStop();
        updateDB(ScoreActivity.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateDB(ScoreActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateDB(ScoreActivity.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateDB(ScoreActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDB(ScoreActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDB(ScoreActivity.this);
    }

    /**
     * find all the views
     */
    private void findViews() {
        scoreMenu_BTN_StartGame = findViewById(R.id.scoreMenu_BTN_StartGame);
        scoreMenu_BTN_addScore = findViewById(R.id.scoreMenu_BTN_addScore);
        scoreMenu_TXT_score = findViewById(R.id.scoreMenu_TXT_score);
        scoreMenu_EDT_addName = findViewById(R.id.scoreMenu_EDT_addName);
        scoreMenu_LAY_addScore = findViewById(R.id.scoreMenu_LAY_addScore);
    }

    /**
     * load the database from the shared preferences
     *
     * @param context the context
     */
    public static void loadDB(Context context) {
        String fromJSON = MySharedPreferences.getInstance(context).getString(MySharedPreferences.KEY_USERS, ""); // get the string from the shared preferences
        myDB = new Gson().fromJson(fromJSON, MyDB.class); // convert the string to a database

    }

    /**
     * save the database to the shared preferences
     *
     * @param context the context
     */
    public static void updateDB(Context context) {
        String gson = new Gson().toJson(myDB); // convert the database to a string
        MySharedPreferences.getInstance(context).putString(MySharedPreferences.KEY_USERS, gson); // save the string to the shared preferences
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
    @SuppressLint("SetTextI18n")
    private void setScore() {
        Intent intent = getIntent(); // get the intent
        score = intent.getIntExtra(KEY_SCORE, 0); // get the score from the intent
        if (score != 0) {
            scoreMenu_TXT_score.setVisibility(View.VISIBLE); // show the score text view
            scoreMenu_LAY_addScore.setVisibility(View.VISIBLE); // show the add score layout
            scoreMenu_TXT_score.setText("" + score); // set the score text view
        } else {
            scoreMenu_TXT_score.setVisibility(View.GONE); // hide the score text view
            scoreMenu_LAY_addScore.setVisibility(View.GONE); // hide the add score layout
        }
    }

    /**
     * This method is responsible for the buttons listeners
     */
    private void buttonsListeners() {
        scoreMenu_BTN_addScore.setOnClickListener(v -> {
            requestLocationPermission(location);
//            getCurrentLocation(); // get the current location
            if (longitude == 0 || latitude == 0) { // if the location is not available
                App.toast(ScoreActivity.this, "Didn't get longitude yet - try again");
                return;
            }

            User user = new User(); // create a new user
            user.setName("" + scoreMenu_EDT_addName.getText()); // set the name
            user.setScore(score); // set the score
            user.setLatitude(latitude); // set the latitude
            user.setLongitude(longitude); // set the longitude
            updateDB(ScoreActivity.this); // update the database


            listFragment.addScore(user); // add the score to the list fragment
            scoreMenu_LAY_addScore.setVisibility(View.GONE); // hide the add score layout
            scoreMenu_TXT_score.setVisibility(View.GONE); // hide the score text view


        });
        // start game button
        scoreMenu_BTN_StartGame.setOnClickListener(v -> openMenuActivity());
    }


    /**
     * This method returns to the menu activity
     */
    private void openMenuActivity() {
        Intent menuActivity = new Intent(this, StartMenuActivity.class);
        startActivity(menuActivity);
        updateDB(ScoreActivity.this);
        finish();
    }

    private void requestLocationPermission(SimpleLocation location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            App.toast(ScoreActivity.this, "Please allow location permission");
        } else {
            putLatLon(location);
        }
    }

    private void putLatLon(@NonNull SimpleLocation location) {
        location.beginUpdates();
        latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}