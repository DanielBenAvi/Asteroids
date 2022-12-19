package com.example.asteroids.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.asteroids.Fragments.ListFragment;
import com.example.asteroids.Fragments.MapFragment;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.Model.MyDB;
import com.example.asteroids.Model.MySharedPreferences;
import com.example.asteroids.Model.User;
import com.example.asteroids.Other.App;
import com.example.asteroids.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;


public class ScoreActivity extends AppCompatActivity {


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

    private LocationRequest locationRequest;
    private double latitude;
    private double longitude;
    private static MyDB myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        findViews();
        setScore();

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

        // init the location request
        locationRequest = LocationRequest.create(); // create a new location request
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // set the priority
        locationRequest.setInterval(5000); // set the interval
        locationRequest.setFastestInterval(2000); // set the fastest interval
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
            getCurrentLocation(); // get the current location
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

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Location methods
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method is responsible for getting the current location
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // call the super method
        if (requestCode == 1) { // if the request code is 1
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // if the permission is granted
                if (isGPSEnabled()) { // if the GPS is enabled
                    getCurrentLocation(); // get the current location
                } else {
                    turnOnGPS(); // turn on the GPS
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // call the super method
        // if the request code is 2 and the result code is RESULT_OK
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentLocation(); // get the current location
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // if the version is higher than M
            if (ActivityCompat.checkSelfPermission(ScoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // if the permission is granted
                // permission granted
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(ScoreActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() { // get the location
                        // location is available
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(ScoreActivity.this).removeLocationUpdates(this); // remove the location updates
                            if (locationResult.getLocations().size() > 0) { // if the location is available
                                int index = locationResult.getLocations().size() - 1; // get the last location
                                latitude = locationResult.getLocations().get(index).getLatitude(); // get the latitude
                                longitude = locationResult.getLocations().get(index).getLongitude(); // get the longitude
                            }
                        }
                    }, Looper.getMainLooper()); // get the location
                } else {
                    turnOnGPS(); // turn on the GPS
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); // request permission
            }
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest); // create a builder
        builder.setAlwaysShow(true); // set the builder to always show
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());     // create a task
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() { // add a listener to the task
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) { // when the task is complete
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class); // get the response
                } catch (ApiException e) {  // if there is an error
                    switch (e.getStatusCode()) { // switch the error code
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: // if the resolution is required
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;     // create a resolvable api exception
                                resolvableApiException.startResolutionForResult(ScoreActivity.this, 2); // start the resolution
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();   // print the stack trace
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    // check if the GPS is enabled
    private boolean isGPSEnabled() {
        LocationManager locationManager = null; // create a location manager
        boolean isEnabled = false; // create a boolean to check if the GPS is enabled
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // get the location manager
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // check if the GPS is enabled
        return isEnabled;
    }
}