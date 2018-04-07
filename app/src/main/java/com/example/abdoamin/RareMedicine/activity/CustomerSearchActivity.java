package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.abdoamin.RareMedicine.NetworkChangeReceiver;
import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.adapter.MedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.object.Medicine;
import com.example.abdoamin.RareMedicine.object.Pharmacy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class CustomerSearchActivity extends AppCompatActivity {
    @BindView(R.id.customer_search_medicine_list_recycleView)
    RecyclerView mRecyclerView;
    @BindView(R.id.customer_search_search_editText)
    EditText searchEditText;

    //menu
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    MedicineRecycleAdapter mMedicineRecycleAdapter;
    ArrayList<Medicine> medicineArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_customer_search);
        ButterKnife.bind(this);
        String barCodeResult = getIntent().getStringExtra("code");

        //menu
        Utiltis.setUpMenuNavView(this, toolbar, drawer, navigationView, Utiltis.currentMode);

        medicineArrayList = new ArrayList<>();
        if (Utiltis.allSystemMedicineList == null)
            Utiltis.allSystemMedicineList = new ArrayList<>();
        mMedicineRecycleAdapter = new MedicineRecycleAdapter(this, Utiltis.allSystemMedicineList, new MedicineRecycleAdapter.MedicineClickListener() {
            @Override
            public void onMedicineClick(Medicine medicine) {
                searchMedicineByID(medicine.getMedID());
            }
        });
        Utiltis.getAllMedicineInList(this,searchEditText,barCodeResult, mRecyclerView, mMedicineRecycleAdapter, new Utiltis.ReturnValueResult<List<Medicine>>() {
            @Override
            public void onResult(List<Medicine> object) {

            }
        });
    }

    @OnTextChanged(R.id.customer_search_search_editText)
    void textChange() {
        medicineArrayList.clear();
        for (Medicine medicine : Utiltis.allSystemMedicineList) {
//            if (medicine.getName().contains(searchEditText.getText().toString()) ||
//                    medicine.getMedID().contains(searchEditText.getText().toString()))
            if (Utiltis.searchAboutAllCharacter(searchEditText.getText().toString(), medicine.getName()) ||
                    medicine.getMedID().contains(searchEditText.getText().toString()))
                medicineArrayList.add(medicine);
        }
        if (searchEditText.getText().toString().trim().equals(""))
            mMedicineRecycleAdapter.addList(Utiltis.allSystemMedicineList);
        else
            mMedicineRecycleAdapter.addList(medicineArrayList);
    }

    @OnClick(R.id.customer_search_barcode_btn)
    void barcode() {
        Utiltis.barCode(this);
    }

//    @OnClick(R.id.customer_search_search_btn)
//    void OnSearchBtnClick() {
//        searchEditText.getText().toString();
//    }

    void searchMedicineByID(String medicineId) {
        Utiltis.searchMedicine(this, medicineId, new Utiltis.ReturnValueResult<List<Pharmacy>>() {
            @Override
            public void onResult(List<Pharmacy> pharmacyList) {
                startActivity(new Intent(CustomerSearchActivity.this, SearchMedicineResultActivity.class));
            }
        });
        //TODO complete searchMedicine function
    }

    NetworkChangeReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        receiver = new NetworkChangeReceiver();
        registerReceiver(
                receiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Utiltis.removeEventListener();
    }
}
