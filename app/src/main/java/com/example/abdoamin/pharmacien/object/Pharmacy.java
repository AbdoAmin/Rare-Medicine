package com.example.abdoamin.pharmacien.object;

import java.util.List;

/**
 * Created by Abdo Amin on 11/26/2017.
 */

public class Pharmacy {

    private String name;

    private double latitude;

    private double longitude;

    private double distance;

    private String address;

    private String imgURL;

    private int phoneNumber;

    private List<Long> medicine;

    public Pharmacy(double latitude, double longitude, double distance,String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.name=name;
    }

    public Pharmacy(String name, double latitude, double longitude, double distance, String address, String imgURL, int phoneNumber, List<Long> medicine) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.address = address;
        this.imgURL = imgURL;
        this.phoneNumber = phoneNumber;
        this.medicine = medicine;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }

    public String getAddress() {
        return address;
    }

    public String getImgURL() {
        return imgURL;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public List<Long> getMedicine() {
        return medicine;
    }
}
