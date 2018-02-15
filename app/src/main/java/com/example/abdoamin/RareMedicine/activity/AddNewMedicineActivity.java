package com.example.abdoamin.RareMedicine.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewMedicineActivity extends AppCompatActivity {
    @BindView(R.id.add_medicine_activity_add_medicine_btn) public Button addMedicineBtn;
    @BindView(R.id.add_medicine_activity_barcode_btn) public Button barCodeBtn;
    @BindView(R.id.add_medicine_activity_medicine_id_editText) public EditText medicineIdEditText;
    @BindView(R.id.add_medicine_activity_medicine_name_editText) public EditText medicineNameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_medicine);
        ButterKnife.bind(this);
        String barCodeResult=getIntent().getStringExtra("code");
        if(barCodeResult!=null){
            medicineIdEditText.setText(barCodeResult);
            medicineIdEditText.setEnabled(false);
        }
    }
    @OnClick(R.id.add_medicine_activity_add_medicine_btn)
    void addMedicineOnClick(){
        String medicineId=medicineIdEditText.getText().toString();
        String medicineName=medicineNameEditText.getText().toString();
        if(medicineId!=null&&medicineName!=null){
            Utiltis.addNewMedicine(this,medicineId,medicineName);
        }

    }
    @OnClick(R.id.add_medicine_activity_barcode_btn)
    void openBarcodActivity(){
        Utiltis.barCode(this);
    }

}
