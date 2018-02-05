package com.example.abdoamin.RareMedicine.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.sign_up_email_editText)EditText email;
    @BindView(R.id.sign_up_password_editText)EditText password;
    @BindView(R.id.sign_up_confirm_password_editText)EditText confirmedPassword;
    @BindView(R.id.sign_up_pharmacy_name_editText)EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }
    @OnClick(R.id.sign_up_map_btn)
    void onMapBtnClick(){

    }
    @OnClick(R.id.sign_up_sign_up_btn)
    void onSignUpBtnClick(){
        if(confirmedPassword.getText().toString().equals(password.getText().toString())) {
            Utiltis.pharmacySignUp(this, email.getText().toString(), password.getText().toString(), name.getText().toString(),30,30);
        }
    }
}
