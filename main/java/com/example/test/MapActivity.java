package com.example.test;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final long TIME_INTERVAL = 3000;
    private static final float MIN_DISTANCE = 10;
    private GoogleMap map;
    private Location currentLocation;
    private LocationManager locationManager;
    private final LocationListener locationListener = new LocationListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_INTERVAL,
                    MIN_DISTANCE, this.locationListener);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            return;
        }
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(new LatLng(40.984847, 29.052589))
                .title("Marmara"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(14)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
        map.setMyLocationEnabled(true);
    }
}
