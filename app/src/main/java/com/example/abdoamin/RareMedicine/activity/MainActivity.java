package com.example.abdoamin.RareMedicine.activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Utiltis.getCurrentUserLocation(this);
//        Toast.makeText(this, String.valueOf(Utiltis.userLat) + " , " + String.valueOf(Utiltis.userLng), Toast.LENGTH_LONG).show();
//        Utiltis.searchMedicine(100,this);
//        Toast.makeText(this, Utiltis.pharmacyList.get(0).getName(), Toast.LENGTH_LONG).show();
//        Utiltis.barCode(MainActivity.this);
//
//        Utiltis.searchMedicineByName("revo",this, new Utiltis.ReturnValueResult() {
//            @Override
//            public void onResult(Object object) {
//                if(object==null){
//                    Toast.makeText(MainActivity.this, "This medicine is not supported /n in our system", Toast.LENGTH_LONG).show();
//                }
//                else if(object instanceof String)
//                    Utiltis.searchMedicine(Long.valueOf((String)object),MainActivity.this);
//            }
//        });


        startActivity(new Intent(this,ModeSwitch.class));


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
