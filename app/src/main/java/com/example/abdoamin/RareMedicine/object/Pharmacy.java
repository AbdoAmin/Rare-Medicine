package com.example.abdoamin.RareMedicine.object;

import java.util.List;

/**
 * Created by Abdo Amin on 11/26/2017.
 */

public class Pharmacy {
    private String name;

    private Location location;

    private Double distance;

    private String address;

    private String img;

    private String phone;

    private List<Medicine> medicine;

    public Pharmacy(){}

    public Pharmacy(String name, Location location, Double distance) {
        this.location = location;
        this.distance = distance;
        this.name = name;
    }

    public Pharmacy(String name, Location location, Double distance, String address, String img, String phone) {
        this.name = name;
        this.location = location;
        this.distance = distance;
        this.address = address;
        this.img = img;
        this.phone = phone;
    }



    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return location.getLatitude();
    }

    public Double getLongitude() {
        return location.getLongitude();
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

    public void setMedicine(List<Medicine> medicineList) {
        medicine = medicineList;
    }

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof Pharmacy)) {
            return false;
        }
        Pharmacy otherMember = (Pharmacy) anObject;
        return otherMember.getLatitude().equals(getLatitude()) && otherMember.getLongitude().equals(getLongitude());
    }
}
