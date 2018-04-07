package com.example.abdoamin.RareMedicine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.FileUtil;
import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;

import java.io.File;
import java.io.IOException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;


public class SignUpContinueActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    @BindView(R.id.sign_up_continue_imag_btn)
    Button imagePicker;
    @BindView(R.id.sign_up_continue_address_editText)
    EditText address;
    @BindView(R.id.sign_up_continue_phone_editText)
    EditText phoneNumber;
    @BindView(R.id.sign_up_continue_image_shower)
    ImageView imageShow;

    String userID;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_continue);
        ButterKnife.bind(this);
        userID = Utiltis.currentUser.getUid();
    }

    @OnClick({R.id.sign_up_continue_imag_btn, R.id.sign_up_continue_image_shower})
    void onImagePickerClick() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

    }

    @OnClick(R.id.sign_up_continue_next_btn)
    void onNextBtnClick() {
        Utiltis.pharmacySignUpContinue(this, userID, imageUri, address.getText().toString(), phoneNumber.getText().toString());
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

//                new Compressor(this)
//                        .compressToFileAsFlowable(actualImage)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<File>() {
//                            @Override
//                            public void accept(File file) {
//                                compressedImage = file;
//                                ((ImageView)findViewById(R.id.imageViewSplash)).setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
//                                imageUri= Uri.parse(compressedImage.getPath());
//                                Log.e("^_^",compressedImage.getPath());
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) {
//                                throwable.printStackTrace();
//                                showError(throwable.getMessage());
//                            }
//                        });

            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


}
