package com.example.abdoamin.RareMedicine.object;

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

    private String img;

    private String phone;

    private List<Long> medicine;

    public Pharmacy(String name, double latitude, double longitude, double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.name = name;
    }

    public Pharmacy(String name, double latitude, double longitude, double distance, String address, String img, String phone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.address = address;
        this.img = img;
        this.phone = phone;
    }

    public Pharmacy(String name, double latitude, double longitude, double distance, String address, String imgURL, String phoneNumber, List<Long> medicine) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.address = address;
        this.img = imgURL;
        this.phone = phoneNumber;
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

    public String getImg() {
        return img;
    }

    public String getPhone() {
        return phone;
    }

    public List<Long> getMedicine() {
        return medicine;
    }

    public void setUrlPath(String email) {
        img = "gs://pharmacien-e9a90.appspot.com/pharmacyProfile/" + email + ".jpg";
    }
}
