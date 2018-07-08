package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.adapter.MedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.dialog.VerifyDialog;
import com.example.abdoamin.RareMedicine.object.Medicine;
import com.example.abdoamin.RareMedicine.object.Pharmacy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.abdoamin.RareMedicine.Utiltis.currentMode;
import static com.example.abdoamin.RareMedicine.Utiltis.currentUser;
import static com.example.abdoamin.RareMedicine.Utiltis.getPharmacyProfileInfo;
import static com.example.abdoamin.RareMedicine.Utiltis.logOut;
import static com.example.abdoamin.RareMedicine.Utiltis.setUpMenuNavView;
import static com.example.abdoamin.RareMedicine.Utiltis.ReturnValueResult;

public class PharmacyProfileActivity extends AppCompatActivity {
    @BindView(R.id.pharmacy_profile_image_imagView)
    CircleImageView photo;
    @BindView(R.id.pharmacy_profile_name_textView)
    TextView name;
    @BindView(R.id.pharmacy_profile_phone_textView)
    TextView phone;
    @BindView(R.id.pharmacy_profile_address_textView)
    TextView address;
    @BindView(R.id.pharmacy_profile_medicine_recycleView)
    RecyclerView mMedicineRecycleView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Unbinder unbinder;


    Pharmacy mPharmacy;
    MedicineRecycleAdapter mMedicineRecycleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_pharmacy_profile);
        unbinder = ButterKnife.bind(this);
        //menu
        setUpMenuNavView(this, toolbar, drawer, navigationView, currentMode);

        if (currentUser == null) {
            finish();
            logOut(this);
        }
        else {
            if (currentUser.isEmailVerified()) {
                getPharmacyProfileInfo(this, currentUser.getUid(), new ReturnValueResult<Pharmacy>() {
                    @Override
                    public void onResult(Pharmacy pharmacy) {
                        mPharmacy = pharmacy;
                        setViews();
                    }
                }, new ReturnValueResult<Pharmacy>() {
                    @Override
                    public void onResult(Pharmacy pharmacy) {
                        mPharmacy = pharmacy;
                        mMedicineRecycleAdapter = new MedicineRecycleAdapter(PharmacyProfileActivity.this, mPharmacy.getMedicine()
                                , new MedicineRecycleAdapter.MedicineClickListener() {
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
            else {
                //Todo:dissmis when verify
                VerifyDialog verifyDialog=new VerifyDialog(this);
                verifyDialog.show();
                verifyDialog.setCanceledOnTouchOutside(false);
            }
        }
    }

    @OnClick(R.id.pharmacy_profile_medicine_modify_btn)
    void onModifyBtnClick() {
        startActivity(new Intent(this, PharmacyAddMedicineActivity.class));
    }

    void setViews() {
        name.setText(mPharmacy.getName());
        phone.setText(mPharmacy.getPhone());
        address.setText(mPharmacy.getAddress());
        Picasso.with(this).load(Uri.parse(mPharmacy.getImg())).into(photo);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}