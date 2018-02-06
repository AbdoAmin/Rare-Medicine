package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.sign_up_continue_next_btn)
    Button nextBtn;
    @BindView(R.id.sign_up_continue_skip_btn)
    Button skipBtn;

    String userID;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_continue);
        ButterKnife.bind(this);
        userID=getIntent().getStringExtra(getString(R.string.Pharmacy_id));
    }

    @OnClick(R.id.sign_up_continue_imag_btn)
    void onImagePickerClick() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

    }
    @OnClick(R.id.sign_up_continue_skip_btn)
    void onSkipBtnClick(){
        Utiltis.pharmacySignUpContinue(this,userID,null,null,null);
    }
    @OnClick(R.id.sign_up_continue_next_btn)
    void onNextBtnClick(){
        Utiltis.pharmacySignUpContinue(this,userID,imageUri,address.getText().toString(),phoneNumber.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageShow.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }
}
