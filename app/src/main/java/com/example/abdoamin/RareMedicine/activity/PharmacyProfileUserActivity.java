package com.example.abdoamin.RareMedicine.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.object.Pharmacy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PharmacyProfileUserActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    @BindView(R.id.pharmacy_profile_image_imagView)
    ImageView photo;
    @BindView(R.id.pharmacy_profile_name_textView)
    TextView name;
    @BindView(R.id.pharmacy_profile_phone_textView)
    TextView phone;
    @BindView(R.id.pharmacy_profile_address_textView)
    TextView address;
    @BindView(R.id.drawer_layout)DrawerLayout drawer;
    @BindView(R.id.nav_view)NavigationView navigationView;
    @BindView(R.id.toolbar)Toolbar toolbar;

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    GoogleMap mMap;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_pharmacy_profile_user);
        ButterKnife.bind(this);

        //menu
        Utiltis.setUpMenuNavView(this,toolbar,drawer,navigationView,Utiltis.MODE_USER);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pharmacy_profile_user_map);
        mapFragment.getMapAsync(this);
        position = getIntent().getIntExtra(getString(R.string.pharmacy_position), 0);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            }
        }
        Pharmacy pharmacy = Utiltis.nearbyPharmacyList.get(position);
        setViews(pharmacy);
        LatLng pharmcyLatLng = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
        mMap.addMarker(new MarkerOptions().position(pharmcyLatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pharmcyLatLng, 12));


    }

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


    void setViews(Pharmacy mPharmacy) {
        name.setText(mPharmacy.getName());
        phone.setText(mPharmacy.getPhone());
        address.setText(mPharmacy.getAddress());
        Picasso.with(this).load(Uri.parse(mPharmacy.getImg())).into(photo);
    }

}
