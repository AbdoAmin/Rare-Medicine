package com.example.abdoamin.RareMedicine.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;


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
        Utiltis.currentMode=Utiltis.MODE_USER;
        Utiltis.noneModeSelect(this,Utiltis.currentMode);
    }
    @OnClick(R.id.switch_mode_pharmacist_btn)
    void onPharmacistBtnClick(){
        Utiltis.currentMode=Utiltis.MODE_PHARMACIST_NONE;
        Utiltis.noneModeSelect(this,Utiltis.currentMode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
