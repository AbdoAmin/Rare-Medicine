package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.adapter.PharmacyRecycleAdapter;
import com.example.abdoamin.RareMedicine.object.Pharmacy;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.abdoamin.RareMedicine.Utiltis.currentMode;
import static com.example.abdoamin.RareMedicine.Utiltis.nearbyPharmacyList;
import static com.example.abdoamin.RareMedicine.Utiltis.removeEventListener;
import static com.example.abdoamin.RareMedicine.Utiltis.searchMedicine;
import static com.example.abdoamin.RareMedicine.Utiltis.setUpMenuNavView;
import static com.example.abdoamin.RareMedicine.Utiltis.ReturnValueResult;

public class SearchMedicineResultActivity extends AppCompatActivity {
    @BindView(R.id.searsh_medicine_result_nearb_pharmacy_recycleView)
    RecyclerView mPharmacyRecycleView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_search_medicine_result);
        ButterKnife.bind(this);
        //menu
        setUpMenuNavView(this, toolbar, drawer, navigationView, currentMode);

        String medicineId=getIntent().getStringExtra(getString(R.string.medicine_id));
        if(medicineId==null){

        }
        searchMedicine(this, medicineId, new ReturnValueResult<List<Pharmacy>>() {
            @Override
            public void onResult(List<Pharmacy> pharmacyList) {
                PharmacyRecycleAdapter mPharmacyRecycleAdapter = new PharmacyRecycleAdapter(nearbyPharmacyList, SearchMedicineResultActivity.this, new PharmacyRecycleAdapter.PharmacyClickListener() {
                    @Override
                    public void onPharmacyClick(int position) {
                        Intent intent = new Intent(SearchMedicineResultActivity.this, PharmacyProfileUserActivity.class);
                        intent.putExtra(getString(R.string.pharmacy_position), position);
                        startActivity(intent);
                    }
                });
                mPharmacyRecycleView.setLayoutManager(new LinearLayoutManager(SearchMedicineResultActivity.this));
                mPharmacyRecycleView.setAdapter(mPharmacyRecycleAdapter);
            }
        });



    }
    @Override
    protected void onDestroy() {
        removeEventListener();
        super.onDestroy();
    }
}
