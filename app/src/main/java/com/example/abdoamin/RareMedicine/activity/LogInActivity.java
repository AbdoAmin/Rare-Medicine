package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.log_in_email_editText)EditText email;
    @BindView(R.id.log_in_password_editText)EditText password;
    @BindView(R.id.log_in_log_in_btn)Button logInBtn;
    @BindView(R.id.log_in_sign_up_btn)Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.log_in_sign_up_btn)
    void onSignUpBtnClick(){
        startActivity(new Intent(this,SignUpActivity.class));
    }
    @OnClick(R.id.log_in_log_in_btn)
    void onLogInBtnClick(){
        if(email.getText().toString().length()<3) {
            Toast.makeText(this, "Email not Valid", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.getText().toString().length()<8) {
            Toast.makeText(this, "Password less than 8", Toast.LENGTH_SHORT).show();
            return;
        }
        Utiltis.logIn(this,email.getText().toString(),password.getText().toString());
    }
}
