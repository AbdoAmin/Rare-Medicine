package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewMedicineActivity extends AppCompatActivity {
    @BindView(R.id.add_medicine_activity_medicine_id_editText)
    public EditText medicineIdEditText;
    @BindView(R.id.add_medicine_activity_medicine_name_editText)
    public EditText medicineNameEditText;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_add_new_medicine);
        ButterKnife.bind(this);
        //menu
        Utiltis.setUpMenuNavView(this, toolbar, drawer, navigationView, Utiltis.currentMode);

        String barCodeResult = getIntent().getStringExtra("code");
        if (barCodeResult != null) {
            medicineIdEditText.setText(barCodeResult);
            medicineIdEditText.setEnabled(false);
        }
    }

    @OnClick(R.id.add_medicine_activity_add_medicine_btn)
    void addMedicineOnClick() {
        String medicineId = medicineIdEditText.getText().toString();
        String medicineName = medicineNameEditText.getText().toString();
        if (!medicineId.equals("") && !medicineName.equals("")) {
            Utiltis.sendRequestAddNewMedicine(this, medicineId, medicineName);
        }
        clearText();

    }

    @OnClick(R.id.add_medicine_activity_barcode_btn)
    void openBarcodeActivity() {
        Utiltis.barCode(this);
    }
    void clearText(){
        medicineIdEditText.setText("");
        medicineNameEditText.setText("");
        medicineIdEditText.setEnabled(true);

    }

}
