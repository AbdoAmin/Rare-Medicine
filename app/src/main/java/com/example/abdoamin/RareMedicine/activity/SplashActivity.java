package com.example.abdoamin.RareMedicine.activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.object.Pharmacy;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);




        Utiltis.getCurrentUserLocation(this);
//        Toast.makeText(this, String.valueOf(Utiltis.userLat) + " , " + String.valueOf(Utiltis.userLng), Toast.LENGTH_LONG).show();
//        Utiltis.searchMedicine(100,this);
//        Toast.makeText(this, Utiltis.nearbyPharmacyList.get(0).getName(), Toast.LENGTH_LONG).show();
//        Utiltis.barCode(SplashActivity.this);
//
//        Utiltis.searchMedicineByName("revo",this, new Utiltis.ReturnValueResult() {
//            @Override
//            public void onResult(Object object) {
//                if(object==null){
//                    Toast.makeText(SplashActivity.this, "This medicine is not supported /n in our system", Toast.LENGTH_LONG).show();
//                }
//                else if(object instanceof String)
//                    Utiltis.searchMedicine(Long.valueOf((String)object),SplashActivity.this);
//            }
//        });


        Utiltis.nearbyPharmacyList=new ArrayList<>();
        Utiltis.nearbyPharmacyList.add(new Pharmacy("El_Plascy Pharmacy",30.619,32.293,50));
        Utiltis.nearbyPharmacyList.add(new Pharmacy("Agzahana Pharmacy",30.618,32.295,60));
        Utiltis.nearbyPharmacyList.add(new Pharmacy("Mostafa El- Amir Pharmacy",30.617,32.299,70));
        Utiltis.nearbyPharmacyList.add(new Pharmacy("Agzahana Pharmacy",30.616,32.290,60));
        Utiltis.nearbyPharmacyList.add(new Pharmacy("Agzahana Pharmacy",30.620,32.290,60));
        Utiltis.nearbyPharmacyList.add(new Pharmacy("Mostafa El- Amir Pharmacy",30.614,32.287,70));
//        startActivity(new Intent(this,PharmacyMapActivity.class));
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this,SignUpActivity.class));
            }

        }.start();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
