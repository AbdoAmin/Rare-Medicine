package com.example.abdoamin.RareMedicine.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.FileUtil;
import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;

public class PharmacyEditProfile extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    @BindView(R.id.pharmacy_edit_profile_image_shower)
    ImageView imageShow;
    @BindView(R.id.pharmacy_edit_profile_pharmacy_name_editText)
    EditText pharmacyName;
    @BindView(R.id.pharmacy_edit_profile_address_editText)
    EditText pharmacyAddress;
    @BindView(R.id.pharmacy_edit_profile_phone_editText)
    EditText pharmacyPhone;
    Unbinder unbinder;

    Uri imageUri;
    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    Marker marker;
    LatLng pharmacyLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_edit_profile);
        unbinder= ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pharmacy_edit_profile_map_fragment);
        mapFragment.getMapAsync(this);
    }


    @OnClick({R.id.pharmacy_edit_profile_image_shower, R.id.pharmacy_edit_profile_imag_btn})
    void onImagePickerClick() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up_next, menu);
        MenuItem nextMenu = menu.findItem(R.id.sign_up_next_menu);
        Button nextMenuActionView =
                (Button) nextMenu.getActionView();
        Drawable icon = this.getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_white_24dp);
        nextMenuActionView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        nextMenuActionView.setBackgroundColor(Color.TRANSPARENT);
        nextMenuActionView.setText(R.string.next);
        nextMenuActionView.setPadding(80, 0, 40, 0);
        nextMenuActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPharmacyProfile();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_up_next_menu:
                editPharmacyProfile();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!");
                return;
            }
            try {
                File actualImage;
                File compressedImage;
                actualImage = FileUtil.from(this, data.getData());
                Log.e("^_^", actualImage.getPath());
                compressedImage = new Compressor(this).compressToFile(actualImage);
                imageShow.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
                imageUri = Uri.fromFile(compressedImage);
                Log.e("^_^", imageUri.toString());
                Log.e("^_^", compressedImage.getPath());

            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
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


    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void editPharmacyProfile(){
        Utiltis.editPharmacyInfo(this,Utiltis.currentUser.getUid(),imageUri,pharmacyAddress.getText().toString(),
                pharmacyPhone.getText().toString(),pharmacyName.getText().toString(),pharmacyLocation.latitude,pharmacyLocation.longitude);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
