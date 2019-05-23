package com.example.test;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, AsyncResponseMap {

    int color_id;
    NetworkTask networkTask = new NetworkTask();
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
        networkTask.delegate = this;
        int id = getIntent().getIntExtra("pointID",0);
        color_id = getIntent().getIntExtra("colorID",0);
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
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Constants.REQUEST_LOCATION);
                count++;
            }
            else {
                end();
                return;
            }
            locationSet = setLocationManager();
        }
        TaskParams taskParams = new TaskParams(0,id,new LatLng(currentLocation.getLatitude(),
                currentLocation.getLongitude()));
        networkTask.execute(taskParams);
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
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(14)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    @Override
    public void drawPolylineOnMap(Response r) {
        String s = r.polyline;
        List<LatLng> points = PolyUtil.decode(s);
        map.addPolyline(new PolylineOptions().addAll(points).color(Color.BLUE));
        map.addMarker(new MarkerOptions().position(new LatLng(r.lat,r.lng)));
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,Constants.TIME_INTERVAL,
                Constants.MIN_DISTANCE,this.locationListener);
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
        if(requestCode == Constants.REQUEST_LOCATION) {
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

    private class NetworkTask extends AsyncTask<TaskParams, Void, Response> {
        public AsyncResponseMap delegate = null;
        @Override
        protected Response doInBackground(TaskParams... taskParams) {
            Client c = new Client();
            try {
                c.handleRequest(taskParams[0].op, taskParams[0].id, taskParams[0].latLng);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            String polyline = null;
            double lat=0,lng=0;
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(c.received);
                lat = (Double)jsonObject.get("destLat");
                lng = (Double)jsonObject.get("destLng");
                JSONObject jsonObject2 = (JSONObject)jsonObject.get("polyline");
                polyline = (String)jsonObject2.get("points");
            } catch (Exception e) {
                e.printStackTrace();
            }
            c = null;
            Response r = new Response(polyline,lat,lng);
            return r;
        }

        @Override
        protected void onPostExecute(Response r) {
            delegate.drawPolylineOnMap(r);
        }
    }

    private static class TaskParams {
        int op;
        int id;
        LatLng latLng;

        TaskParams(int op, int id, LatLng latLng) {
            this.op = op;
            this.id = id;
            this.latLng = latLng;
        }
    }
}