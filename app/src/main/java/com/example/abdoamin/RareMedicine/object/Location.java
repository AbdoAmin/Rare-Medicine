package com.example.abdoamin.RareMedicine.object;

/**
 * Created by Abdo Amin on 3/2/2018.
 */

public class Location {

    private Double latitude;

    private Double longitude;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
