package com.example.abdoamin.RareMedicine.activity;


import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class BarCodeActivity extends AppCompatActivity implements BarcodeRetriever {
    String activity;
    @BindView(R.id.focus)
    SwitchCompat autoFocus;
    @BindView(R.id.support_multiple)
    SwitchCompat supportMultiple;
    @BindView(R.id.on_flash)
    SwitchCompat flash;
    @BindView(R.id.front_cam)
    SwitchCompat frontCam;
    Unbinder unbinder;

    BarcodeCapture barcodeCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        unbinder = ButterKnife.bind(this);
        activity = getIntent().getStringExtra(getString(R.string.activity_name));
        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
    }

    @OnCheckedChanged({R.id.focus, R.id.support_multiple, R.id.front_cam, R.id.on_flash})
    void onOptionChange() {
        barcodeCapture.setShowDrawRect(true)
                .setSupportMultipleScan(supportMultiple.isChecked())
                .setTouchAsCallback(false)
                .shouldAutoFocus(autoFocus.isChecked())
                .setShowFlash(flash.isChecked())
                .setBarcodeFormat(Barcode.ALL_FORMATS)
                .setCameraFacing(frontCam.isChecked() ? CameraSource.CAMERA_FACING_FRONT : CameraSource.CAMERA_FACING_BACK)
                .setShouldShowText(true);

        barcodeCapture.refresh();
    }


    @Override
    public void onRetrieved(final Barcode barcode) {
        Log.e("^_^", "Barcode read: " + barcode.displayValue);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Utiltis.barCodeResult(BarCodeActivity.this, barcode.displayValue, activity);

            }
        });
        barcodeCapture.stopScanning();


    }

    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                        "codes in frame include : \n";
                for (int index = 0; index < barcodeGraphics.size(); index++) {
                    Barcode barcode = barcodeGraphics.get(index).getBarcode();
                    message += (index + 1) + ". " + barcode.displayValue + "\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(BarCodeActivity.this)
                        .setTitle("code retrieved")
                        .setMessage(message);
                builder.show();
            }
        });

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        for (int i = 0; i < sparseArray.size(); i++) {
            Barcode barcode = sparseArray.valueAt(i);
            Log.e("value", barcode.displayValue);
        }

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onPermissionRequestDenied() {

    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
