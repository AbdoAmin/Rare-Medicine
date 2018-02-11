package com.example.abdoamin.RareMedicine.object;

import java.util.List;

/**
 * Created by Abdo Amin on 11/26/2017.
 */

public class Pharmacy {
    private String name;

    private Double latitude;

    private Double longitude;

    private Double distance;

    private String address;

    private String img;

    private String phone;

    private List<Medicine> medicine;

    public Pharmacy(String name, Double latitude, Double longitude, Double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.name = name;
    }

    public Pharmacy(String name, Double latitude, Double longitude, Double distance, String address, String img, String phone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.address = address;
        this.img = img;
        this.phone = phone;
    }

    public Pharmacy(String name, Double latitude, Double longitude, Double distance, String address, String imgURL, String phoneNumber, List<Medicine> medicine) {

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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getDistance() {
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

    public List<Medicine> getMedicine() {
        return medicine;
    }

    public void setMedicine(List<Medicine> medicineList){
        medicine=medicineList;
    }

}
