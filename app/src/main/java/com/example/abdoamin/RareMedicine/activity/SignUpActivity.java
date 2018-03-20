package com.example.abdoamin.RareMedicine.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

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
    ShowHidePasswordEditText password;
    @BindView(R.id.sign_up_confirm_password_editText)
    ShowHidePasswordEditText confirmedPassword;
    @BindView(R.id.sign_up_pharmacy_name_editText)
    EditText name;

//    //menu
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawer;
//    @BindView(R.id.nav_view)
//    NavigationView navigationView;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setColorEditTextShowPasswordIcon();
        //menu
//        Utiltis.setUpMenuNavView(this, toolbar, drawer, navigationView, Utiltis.MODE_PHARMACIST_NONE);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.sign_up_map_fragment);
        mapFragment.getMapAsync(this);

    }

    @OnClick(R.id.sign_up_map_fragment)
    void onMapBtnClick() {

    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up_next, menu);
        MenuItem nextMenu = menu.findItem(R.id.sign_up_next_menu);
        Button nextMenuActionView =
                (Button) nextMenu.getActionView();
        Drawable icon= this.getResources().getDrawable( R.drawable.ic_keyboard_arrow_right_white_24dp);
        nextMenuActionView.setCompoundDrawablesWithIntrinsicBounds(null , null, icon, null );
        nextMenuActionView.setBackgroundColor(Color.TRANSPARENT);
        nextMenuActionView.setText(R.string.next);
        nextMenuActionView.setPadding(80,0,40,0);
        nextMenuActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpBtnClick();
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_up_next_menu:
                onSignUpBtnClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void setColorEditTextShowPasswordIcon(){
        password.setTintColor(getResources().getColor(R.color.colorPrimary));
        confirmedPassword.setTintColor(getResources().getColor(R.color.colorPrimary));
    }

}
