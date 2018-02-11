package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.adapter.MedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.object.Medicine;
import com.example.abdoamin.RareMedicine.object.Pharmacy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PharmacyProfileActivity extends AppCompatActivity {
    @BindView(R.id.pharmacy_profile_image_imagView)
    ImageView photo;
    @BindView(R.id.pharmacy_profile_name_textView)
    TextView name;
    @BindView(R.id.pharmacy_profile_phone_textView)
    TextView phone;
    @BindView(R.id.pharmacy_profile_address_textView)
    TextView address;
    @BindView(R.id.pharmacy_profile_medicine_recycleView)
    RecyclerView mMedicineRecycleView;

    String pharmacyID;
    Pharmacy mPharmacy;
    MedicineRecycleAdapter mMedicineRecycleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_profile);
        ButterKnife.bind(this);
        pharmacyID = getIntent().getStringExtra(getString(R.string.pharmacy_id));
        if (pharmacyID == null)
            finish();
        Utiltis.getPharmacyProfileInfo(pharmacyID, new Utiltis.ReturnValueResult<Pharmacy>() {
            @Override
            public void onResult(Pharmacy pharmacy) {
                mPharmacy =  pharmacy;
                setViews();
            }
        }, new Utiltis.ReturnValueResult<Pharmacy>() {
            @Override
            public void onResult(Pharmacy pharmacy) {
                mPharmacy =  pharmacy;
                mMedicineRecycleAdapter = new MedicineRecycleAdapter(PharmacyProfileActivity.this, mPharmacy.getMedicine(), new MedicineRecycleAdapter.MedicineClickListener() {
                    @Override
                    public void onMedicineClick(Medicine medicine) {
                        //Todo take action onclick medicine
                    }
                });
                mMedicineRecycleView.setLayoutManager(new LinearLayoutManager(PharmacyProfileActivity.this));
                mMedicineRecycleView.setAdapter(mMedicineRecycleAdapter);
            }
        });
    }
    @OnClick(R.id.pharmacy_profile_medicine_modify_btn)
    void onModifyBtnClick(){
        startActivity(new Intent(this,PharmacyAddMedicineActivity.class));
    }

    void setViews() {
        name.setText(mPharmacy.getName());
        phone.setText(mPharmacy.getPhone());
        address.setText(mPharmacy.getAddress());
        Picasso.with(this).load(Uri.parse(mPharmacy.getImg())).into(photo);
    }
}
