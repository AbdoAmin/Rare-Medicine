package com.example.abdoamin.RareMedicine.activity;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.dialog.Loading;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SplashActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    @Override
    protected void onStart() {
        super.onStart();
        Utiltis.getCurrentUserLocation(this);
        Utiltis.mAuth = FirebaseAuth.getInstance();
        Utiltis.currentUser=Utiltis.mAuth.getCurrentUser();

        countDownTimer=new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                Utiltis.getMyPreferenceMode(SplashActivity.this);
                finish();
            }

        };
        countDownTimer.start();

    }

    @Override
    protected void onResume() {
        countDownTimer.start();
        super.onResume();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


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
    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void openGpsWifi(){
        //todo:
    }
}