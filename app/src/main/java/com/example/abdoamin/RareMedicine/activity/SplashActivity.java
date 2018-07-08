package com.example.abdoamin.RareMedicine.activity;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.BuildConfig;
import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.dialog.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


import java.util.HashMap;

import static com.example.abdoamin.RareMedicine.Utiltis.APP_CURRENT_VERSION;
import static com.example.abdoamin.RareMedicine.Utiltis.getCurrentUserLocation;
import static com.example.abdoamin.RareMedicine.Utiltis.getMyPreferenceMode;
import static com.example.abdoamin.RareMedicine.Utiltis.mAuth;
import static com.example.abdoamin.RareMedicine.Utiltis.currentUser;


public class SplashActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    @Override
    protected void onResume() {

        super.onResume();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getCurrentUserLocation(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        countDownTimer=new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                getMyPreferenceMode(SplashActivity.this);
                finish();
            }

        };
        //check and force app to last version if failer happen in version we can stop it from change version form FBConfig "version"

        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(new HashMap<String, Object>(){{put("version",APP_CURRENT_VERSION);}});
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(SplashActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        String welcomeMessage = mFirebaseRemoteConfig.getString("version");
                        if (welcomeMessage.equals(APP_CURRENT_VERSION)) {
                            Toast.makeText(SplashActivity.this, "enjoy  "+welcomeMessage  ,
                                    Toast.LENGTH_SHORT).show();
                            countDownTimer.start();
                        } else {
                            Toast.makeText(SplashActivity.this, "you must update  "+welcomeMessage,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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