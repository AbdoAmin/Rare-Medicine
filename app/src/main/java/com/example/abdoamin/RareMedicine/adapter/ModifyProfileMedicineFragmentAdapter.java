package com.example.abdoamin.RareMedicine.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.abdoamin.RareMedicine.ProfileModifyMedicineFragment;

/**
 * Created by Abdo Amin on 2/9/2018.
 */

public class ModifyProfileMedicineFragmentAdapter extends FragmentPagerAdapter {
    public ModifyProfileMedicineFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0://top Rated
//                return new TopRatedFragment();
                return ProfileModifyMedicineFragment.newInstance("Delete");
            case 1:
//                return  new PopularFragment();
                return ProfileModifyMedicineFragment.newInstance("Add");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Delete Medicine";
            case 1:
                return "Add Medicine";
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;

    }

}
