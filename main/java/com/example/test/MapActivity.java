package com.example.test;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    final int REQUEST_LOCATION = 200;
    private static final long TIME_INTERVAL = 3000;
    private static final float MIN_DISTANCE = 10;
    private GoogleMap map;
    private Location currentLocation;
    private LocationManager locationManager;
    private final LocationListener locationListener = new LocationListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
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
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        int count = 0;
        int locationSet = setLocationManager();
        while(locationSet == -1) {
            if(count == 1) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                count++;
            }
            else {
                end();
                return;
            }
            locationSet = setLocationManager();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        initializeMap();
    }



    @SuppressLint("MissingPermission")
    public void initializeMap() {
        map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions().position(new LatLng(40.984847, 29.052589))
                .title("Marmara"));
        Log.d("msg","lat: " + currentLocation.getLatitude() + "long: " + currentLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(14)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    public int setLocationManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return -1;
            }
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        else {
            if(PermissionChecker.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return -1;
            }
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,TIME_INTERVAL,
                MIN_DISTANCE,this.locationListener);
        currentLocation = new Location(LocationManager.GPS_PROVIDER);
        Location tempLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(tempLocation != null) {
            currentLocation.setLatitude(tempLocation.getLatitude());
            currentLocation.setLongitude(tempLocation.getLongitude());
        }
        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            }
            else {
                end();
            }
        }
    }

    public void end() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            finishAffinity();
            super.onBackPressed();
        }
        else {
            finish();
        }
    }
}
