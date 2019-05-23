package com.example.test;

public class Response {
    String polyline;
    double lat;
    double lng;

    public Response(String polyline, double lat, double lng) {
        this.polyline = polyline;
        this.lat = lat;
        this.lng = lng;
    }
}