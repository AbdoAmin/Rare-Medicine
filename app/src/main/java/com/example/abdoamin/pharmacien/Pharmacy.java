package com.example.abdoamin.pharmacien;

/**
 * Created by Abdo Amin on 11/26/2017.
 */

public class Pharmacy {

    private String name;

    private long latitude;

    private long longitude;

    private double distance;

    public Pharmacy(long latitude, long longitude, double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }
}
