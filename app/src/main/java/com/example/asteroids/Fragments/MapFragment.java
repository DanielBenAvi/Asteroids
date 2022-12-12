package com.example.asteroids.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asteroids.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textview.MaterialTextView;

import es.dmoral.toasty.Toasty;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_FCV_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
    }

    public void zoom(double latitude, double longitude) {
        LatLng randomPlace = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(randomPlace));
        //
        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder().target(randomPlace)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        Toasty.info(getContext(), "Map is ready", Toasty.LENGTH_SHORT).show();
    }
}