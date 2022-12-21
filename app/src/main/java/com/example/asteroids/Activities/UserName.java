package com.example.asteroids.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.asteroids.Other.Constants;
import com.example.asteroids.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import im.delight.android.location.SimpleLocation;

public class UserName extends AppCompatActivity {

    MaterialButton usr_BTN_add;
    TextInputEditText usr_EDT_name;

    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);


        findViews();
        buttons();

    }

    private void buttons() {
        usr_BTN_add.setOnClickListener(v -> changeTOGame());
    }

    private void changeTOGame() {
//        getCurrentLocation();
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

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationPermission(new SimpleLocation(this));
    }

    private void findViews() {
        usr_BTN_add = findViewById(R.id.usr_BTN_add);
        usr_EDT_name = findViewById(R.id.usr_EDT_name);
    }


    /**
     * This method is responsible for getting the current location
     */
//
    private void requestLocationPermission(SimpleLocation location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
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