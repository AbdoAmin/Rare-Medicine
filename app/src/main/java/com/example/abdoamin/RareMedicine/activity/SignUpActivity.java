package com.example.abdoamin.RareMedicine.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    Marker marker;
    LatLng pharmacyLocation;


    @BindView(R.id.sign_up_email_editText)
    EditText email;
    @BindView(R.id.sign_up_password_editText)
    EditText password;
    @BindView(R.id.sign_up_confirm_password_editText)
    EditText confirmedPassword;
    @BindView(R.id.sign_up_pharmacy_name_editText)
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.sign_up_map_fragment);
        mapFragment.getMapAsync(this);

    }

    @OnClick(R.id.sign_up_map_fragment)
    void onMapBtnClick() {

    }

    @OnClick(R.id.sign_up_sign_up_btn)
    void onSignUpBtnClick() {
        if(email.getText().toString().length()<3){
            Toast.makeText(this, "Check Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.getText().toString().length()<8){
            Toast.makeText(this, "Password Too Short \nMust be <= 8 Characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.getText().toString().length()<1){
            Toast.makeText(this, "Pharmacy Name Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pharmacyLocation==null){
            Toast.makeText(this, "Select your Location on Map", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmedPassword.getText().toString().equals(password.getText().toString())) {
            Utiltis.pharmacySignUp(this, email.getText().toString(), password.getText().toString(), name.getText().toString(), pharmacyLocation.latitude, pharmacyLocation.longitude);
        }
        else{
            Toast.makeText(this, "This Password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mMap.getCameraPosition()),
                2000, null);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                pharmacyLocation=latLng;
                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng));
                } else

                {
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng));

                }
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

        {
            mMap.setMyLocationEnabled(true);
        } else

        {
            // Show rationale and request permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            }
        }

    }
}
