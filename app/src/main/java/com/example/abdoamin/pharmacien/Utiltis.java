package com.example.abdoamin.pharmacien;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Abdo Amin on 11/25/2017.
 */

public class Utiltis {

    //user Latitude
     private static long userLat;
     //user Longitude
     private static long userLng;
     //nearby PH list Founded
     public static List<Pharmacy> pharmacyList;


     /*
     * this Algorithm
     * search sequential at all PH
     * then check distance between user and each PH founded if <=650 miles ||1046.07 KM
     * if though record in list
     * */
     //context mean where this function output appear
    static public void searchMedicine(final long medID, final Context mContext){
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("pharmacy");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //inital list
                pharmacyList=new ArrayList<Pharmacy>();
                //search each PH if medicine found
                for (final DataSnapshot mChild : dataSnapshot.getChildren()) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("/pharmacy/"+ mChild.getKey()+"/medicine");
                    myRef.orderByKey().equalTo(String.valueOf(medID)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if found
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(mContext, "here am i", Toast.LENGTH_LONG).show();
                                long lat= (long) mChild.child("location").child("latitude").getValue();
                                long lng= (long) mChild.child("location").child("longitude").getValue();
                                double dist=distance(userLat,userLng,lat,lng);
                                //check distance between PH and User to save in list Max 650 miles
                                if(dist<=650/*miles*/){
                                    //add in list
                                    pharmacyList.add(new Pharmacy(lat,lng,dist));
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(mContext, "Faild To search", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(mContext, "Faild To Log In", Toast.LENGTH_LONG).show();
            }
        });

    }

    static public void getNearbyPharmacy(long mLatitude,long mLongitude, Context mContext){
        if(pharmacyList!=null){
            sortList();
        }

    }

    static private void sortList(){
        Collections.sort(pharmacyList, new Comparator<Pharmacy>() {
            @Override
            public int compare(Pharmacy pharmacy1, Pharmacy pharmacy2) {
                return Double.compare(pharmacy1.getDistance(),pharmacy2.getDistance());
            }
        });
    }

    static public double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    static public void setUserLatitude_UserLongitude(long userLat,long userLng){
        Utiltis.userLat=userLat;
        Utiltis.userLng=userLng;
    }
}
