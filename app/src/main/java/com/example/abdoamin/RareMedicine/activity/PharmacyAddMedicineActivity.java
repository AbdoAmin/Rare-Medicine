package com.example.abdoamin.RareMedicine.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abdoamin.RareMedicine.AddProfileMedicineFragment;
import com.example.abdoamin.RareMedicine.DeleteProfileMedicineFragment;
import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.adapter.ModifyProfileMedicineFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PharmacyAddMedicineActivity extends AppCompatActivity {
    @BindView(R.id.pharmacy_add_medicine_tabLayout)
    public TabLayout mTabLayout;
    @BindView(R.id.pharmacy_add_medicine_viewPager)
    ViewPager mViewPager;

    static public String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_add_medicine);
        ButterKnife.bind(this);
        final ModifyProfileMedicineFragmentAdapter mModifyProfileMedicineFragmentAdapter =
                new ModifyProfileMedicineFragmentAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mModifyProfileMedicineFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        code = getIntent().getStringExtra("code");
        if (code != null) {
            mViewPager.setCurrentItem(ModifyProfileMedicineFragmentAdapter.currentTabIndex);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int newPosition) {
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pharmacy_add_medicine_viewPager + ":" + mViewPager.getCurrentItem());
                switch (mViewPager.getCurrentItem()){
                    case 1:
                        ((AddProfileMedicineFragment)page).prepareToAdd();
                        break;
                    case 0:
                        ((DeleteProfileMedicineFragment)page).prepareToDelete();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
