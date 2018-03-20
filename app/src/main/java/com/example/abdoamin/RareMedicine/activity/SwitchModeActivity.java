package com.example.abdoamin.RareMedicine.activity;

import android.os.Binder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SwitchModeActivity extends AppCompatActivity {
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_mode);
        unbinder=ButterKnife.bind(this);

    }
    @OnClick(R.id.switch_mode_search_medicine_btn)
    void onSearchMedicineBtnClick(){
        Utiltis.noneModeSelect(this,"user");
    }
    @OnClick(R.id.switch_mode_pharmacist_btn)
    void onPharmacistBtnClick(){
        Utiltis.noneModeSelect(this,"pharmacist");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
