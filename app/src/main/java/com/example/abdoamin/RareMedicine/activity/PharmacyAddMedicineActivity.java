package com.example.abdoamin.RareMedicine.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abdoamin.RareMedicine.ProfileModifyMedicineFragment;
import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.adapter.ModifyProfileMedicineFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PharmacyAddMedicineActivity extends AppCompatActivity {
    @BindView(R.id.pharmacy_add_medicine_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.pharmacy_add_medicine_viewPager)
    ViewPager mViewPager;

    static public String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_add_medicine);
        ButterKnife.bind(this);
        ModifyProfileMedicineFragmentAdapter mModifyProfileMedicineFragmentAdapter =
                new ModifyProfileMedicineFragmentAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mModifyProfileMedicineFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mModifyProfileMedicineFragmentAdapter.notifyDataSetChanged();
        code=getIntent().getStringExtra("code");
        if (code!=null) {
            switch (ProfileModifyMedicineFragment.lastRequestType) {
                case "Delete":
                    mTabLayout.getTabAt(0);
                    break;
                case "Add":
                    mTabLayout.getTabAt(1);
                    break;
            }
        }

    }
}
