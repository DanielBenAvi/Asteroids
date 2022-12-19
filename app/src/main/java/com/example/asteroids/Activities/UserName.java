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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.asteroids.Other.Constants;
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

import im.delight.android.location.SimpleLocation;

public class UserName extends AppCompatActivity {
    public static SimpleLocation location;

    MaterialButton usr_BTN_add;
    TextInputEditText usr_EDT_name;

    private LocationRequest locationRequest;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);


        findViews();
        buttons();

        // init the location request
        locationRequest = LocationRequest.create(); // create a new location request
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // set the priority
        locationRequest.setInterval(100); // set the interval
        locationRequest.setFastestInterval(100); // set the fastest interval
    }

    private void buttons() {
        usr_BTN_add.setOnClickListener(v -> changeTOGame());
    }

    private void changeTOGame() {
//        getCurrentLocation();
        requestLocationPermission(new SimpleLocation(this));
        if (longitude == 0 || latitude == 0) { // if the location is not available
            Constants.toast(UserName.this, "Didn't get location yet - try again");
            return;
        }

        Intent gameIntent = new Intent(this, AsteroidsMainActivity.class);
        String name = usr_EDT_name.getText() + "";
        gameIntent.putExtra(AsteroidsMainActivity.KEY_NAME, name);
        gameIntent.putExtra(AsteroidsMainActivity.KEY_LATITUDE, latitude);
        gameIntent.putExtra(AsteroidsMainActivity.KEY_LONGITUDE, longitude);
        startActivity(gameIntent);
        finish();
    }

    private void findViews() {
        usr_BTN_add = findViewById(R.id.usr_BTN_add);
        usr_EDT_name = findViewById(R.id.usr_EDT_name);
    }


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
            if (ActivityCompat.checkSelfPermission(UserName.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // if the permission is granted
                // permission granted
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(UserName.this).requestLocationUpdates(locationRequest, new LocationCallback() { // get the location
                        // location is available
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(UserName.this).removeLocationUpdates(this); // remove the location updates
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
                                resolvableApiException.startResolutionForResult(UserName.this, 2); // start the resolution
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

    private void requestLocationPermission(SimpleLocation location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            putLatLon(location);
        } else {
            putLatLon(location);
        }
    }

    private void putLatLon(SimpleLocation location) {
        location.beginUpdates();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }


}