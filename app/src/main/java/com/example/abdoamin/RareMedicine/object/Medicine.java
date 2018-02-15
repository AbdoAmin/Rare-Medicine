package com.example.abdoamin.RareMedicine.object;

/**
 * Created by Abdo Amin on 12/10/2017.
 */

public class Medicine {
    private String name;
    private String medID;

    public Medicine(String name, String medID) {
        this.name = name;
        this.medID = medID;
    }

    public String getName() {
        return name;
    }

    public String getMedID() {
        return medID;
    }

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof Medicine)) {
            return false;
        }
        Medicine otherMember = (Medicine)anObject;
        return otherMember.getMedID().equals(getMedID());
    }
}
