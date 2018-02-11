package com.example.abdoamin.RareMedicine.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class PharmacyMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
//    private double latitude;
//    private double longitude;
//    private String img;
//    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_map);

//        setVariable();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng pharmcyLatLng = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions()
//                        .position(pharmcyLatLng)
//                        .title(name)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mariosmall))
////                      .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//        );
//        mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(30.619, 32.293))
//                        .title(name)
////                      .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background))
////                      .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//        );
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pharmcyLatLng, 15));
        Utiltis.showAllNearbyPharmacyOnMap(this,Utiltis.nearbyPharmacyList,mMap);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            }
        }

    }

//    private void setVariable() {
//        Intent intent = getIntent();
//        latitude = intent.getDoubleExtra(getString(R.string.latitude_map), 0);
//        longitude = intent.getDoubleExtra(getString(R.string.longitude_map), 0);
//        img = intent.getStringExtra(getString(R.string.img_map));
//        name = intent.getStringExtra(getString(R.string.name_map));
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }

            } else {
                finish();
            }
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mMap.getCameraPosition()),
                2000, null);
        return true;
    }

}