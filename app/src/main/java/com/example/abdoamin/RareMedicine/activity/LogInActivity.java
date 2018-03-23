package com.example.abdoamin.RareMedicine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.log_in_email_editText)EditText email;
    @BindView(R.id.log_in_password_editText)ShowHidePasswordEditText password;
    @BindView(R.id.log_in_log_in_btn)Button logInBtn;
    @BindView(R.id.log_in_sign_up_btn)Button signUpBtn;

    //menu
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    AnimationDrawable animationDrawable;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_sign_in);
        ButterKnife.bind(this);
        setColorEditTextShowPasswordIcon();
        //menu
        Utiltis.setUpMenuNavView(this, toolbar, drawer, navigationView, Utiltis.MODE_PHARMACIST_NONE);

        //image background animation color
        imageView = (ImageView)findViewById(R.id.imageView3);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        Utiltis.currentUser=Utiltis.mAuth.getCurrentUser();
        if (Utiltis.currentUser != null) {
            Intent intent = new Intent(this, PharmacyProfileActivity.class);

            startActivity(intent);
            finish();
        }


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
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }
    void setColorEditTextShowPasswordIcon(){
        password.setTintColor(getResources().getColor(R.color.colorPrimary));

    }
}
