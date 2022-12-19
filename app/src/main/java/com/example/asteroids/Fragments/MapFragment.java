package com.example.asteroids.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asteroids.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Get the SupportMapFragment and request notification
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_FCV_map);

        // Get the map
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return view;
    }

    /**
     * Zoom to the user location
     *
     * @param latitude  the latitude of the user (double)
     * @param longitude the longitude of the user (double)
     */
    public void zoom(double latitude, double longitude) {
        LatLng randomPlace = new LatLng(latitude, longitude);
        addMarker(randomPlace);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder().target(randomPlace)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    /**
     * Add a marker to the map
     *
     * @param randomPlace the location of the marker (latitude, longitude)
     */


    private void addMarker(LatLng randomPlace) {
        map.addMarker(new MarkerOptions().position(randomPlace));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     *
     * @param googleMap is the GoogleMap object
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;


//        for (User user : myDB.getUsers()) {
//            addMarker(new LatLng(user.getLatitude(), user.getLongitude()));
//        }


    }
}