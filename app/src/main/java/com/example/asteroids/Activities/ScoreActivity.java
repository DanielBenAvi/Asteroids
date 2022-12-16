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

    int score;


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

        myDB = MyDB.getInstance();
        loadDB(ScoreActivity.this);


        listFragment = new ListFragment();
        listFragment.setCallback(callBack_userProtocol);

        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.scoreMenu_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.scoreMenu_FRAME_map, mapFragment).commit();


        buttonsListeners();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);


    }

    /**
     * load the database from the shared preferences
     *
     * @param context the context
     */
    public static void loadDB(Context context) {
        String fromJSON = MySharedPreferences.getInstance(context).getString(MySharedPreferences.KEY_USERS, "");
        myDB = new Gson().fromJson(fromJSON, MyDB.class);


    }

    /**
     * save the database to the shared preferences
     *
     * @param context the context
     */
    public static void updateDB(Context context) {
        String gson = new Gson().toJson(myDB);
        MySharedPreferences.getInstance(context).putString(MySharedPreferences.KEY_USERS, gson);
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


    /**
     * This method is responsible for the buttons listeners
     */
    CallBack_userProtocol callBack_userProtocol = new CallBack_userProtocol() {
        @Override
        public void changeLocation(double latitude, double longitude) {
            mapFragment.zoom(latitude, longitude);
        }
    };

    /**
     * This method is responsible for the buttons listeners
     */
    @SuppressLint("SetTextI18n")
    private void setScore() {
        Intent intent = getIntent();
        score = intent.getIntExtra(KEY_SCORE, 0);
        if (score != 0) {
            scoreMenu_TXT_score.setText("" + score);
            scoreMenu_LAY_addScore.setVisibility(View.VISIBLE);
        } else {
            scoreMenu_LAY_addScore.setVisibility(View.GONE);
        }
    }

    /**
     * This method is responsible for the buttons listeners
     */
    private void buttonsListeners() {
        scoreMenu_BTN_addScore.setOnClickListener(v -> {
            getCurrentLocation();
            if (longitude == 0) {
                return;
            }
            if (latitude == 0) {
                return;
            }

            User user = new User();
            user.setName("" + scoreMenu_EDT_addName.getText());
            user.setScore(score);
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            updateDB(ScoreActivity.this);


            listFragment.addScore(user);
            scoreMenu_LAY_addScore.setVisibility(View.GONE);
            scoreMenu_TXT_score.setVisibility(View.GONE);


        });

        scoreMenu_BTN_StartGame.setOnClickListener(v -> openMenuActivity());
    }


    private void openMenuActivity() {
        Intent menuActivity = new Intent(this, StartMenuActivity.class);
        startActivity(menuActivity);
        updateDB(ScoreActivity.this);
        finish();
    }

    private void findViews() {
        scoreMenu_BTN_StartGame = findViewById(R.id.scoreMenu_BTN_StartGame);
        scoreMenu_BTN_addScore = findViewById(R.id.scoreMenu_BTN_addScore);
        scoreMenu_TXT_score = findViewById(R.id.scoreMenu_TXT_score);
        scoreMenu_EDT_addName = findViewById(R.id.scoreMenu_EDT_addName);
        scoreMenu_LAY_addScore = findViewById(R.id.scoreMenu_LAY_addScore);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    getCurrentLocation();

                } else {

                    turnOnGPS();
                }
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(ScoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(ScoreActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);

                            LocationServices.getFusedLocationProviderClient(ScoreActivity.this).removeLocationUpdates(this);

                            if (locationResult != null && locationResult.getLocations().size() > 0) {

                                int index = locationResult.getLocations().size() - 1;
                                latitude = locationResult.getLocations().get(index).getLatitude();
                                longitude = locationResult.getLocations().get(index).getLongitude();

                            }
                        }
                    }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ScoreActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
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

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }


}