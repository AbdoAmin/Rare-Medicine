package com.example.abdoamin.RareMedicine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.abdoamin.RareMedicine.activity.PharmacyAddMedicineActivity;
import com.example.abdoamin.RareMedicine.adapter.MedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.object.Medicine;
import com.example.abdoamin.RareMedicine.object.Pharmacy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Abdo Amin on 2/9/2018.
 */

public class ProfileModifyMedicineFragment extends Fragment {
    @BindView(R.id.fragment_modify_medicine_medicine_recycleView)
    RecyclerView mMedicineRecycleView;
    @BindView(R.id.fragment_modify_medicine_search_editText)EditText searchEditText;
    String requestType;
    static public String lastRequestType;
    List<Medicine> oldList;
    List<Medicine> currentList;
    MedicineRecycleAdapter mMedicineRecycleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_medicine, container, false);
        requestType = getArguments().getString("requestType");
        ButterKnife.bind(this, view);
        oldList = new ArrayList<>();
        currentList = new ArrayList<>();
        switch (requestType) {
            case "Delete":
                prepareToDelete();
                break;
            case "Add":
                prepareToAdd();
                break;
        }
        if(PharmacyAddMedicineActivity.code!=null){
            searchEditText.setText(PharmacyAddMedicineActivity.code);
        }
        return view;

    }

    @OnClick(R.id.fragment_modify_medicine_barcode_btn)
    void onBarCodeBtnClick() {
        lastRequestType=requestType;
        Utiltis.barCode(getActivity());
    }

    @OnTextChanged(R.id.fragment_modify_medicine_search_editText)
    void onSearchMedicineTextChange() {
        currentList.clear();
        for (Medicine medicine : oldList) {
            if (medicine.getName().contains(searchEditText.getText().toString())||
                    medicine.getMedID().contains(searchEditText.getText().toString()))
                currentList.add(medicine);
        }
        if (searchEditText.getText().toString().trim() == null)
            mMedicineRecycleAdapter.addList(oldList);
        else
            mMedicineRecycleAdapter.addList(currentList);

    }


    public static ProfileModifyMedicineFragment newInstance(String requestType) {
        Bundle args = new Bundle();
        args.putString("requestType", requestType);
        ProfileModifyMedicineFragment fragment = new ProfileModifyMedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void prepareToAdd() {
        if (Utiltis.allSystemMedicineList == null)
            Utiltis.allSystemMedicineList = new ArrayList<>();
            mMedicineRecycleAdapter= new MedicineRecycleAdapter(getActivity(), Utiltis.allSystemMedicineList, new MedicineRecycleAdapter.MedicineClickListener() {
            @Override
            public void onMedicineClick(Medicine medicine) {
                Boolean taskComplete = Utiltis.addMedicineToPharmacy(Utiltis.currentUser.getUid(), medicine.getMedID()).isComplete();
                if (taskComplete) {
                    Utiltis.mMedicineList.add(medicine);
                    Utiltis.allSystemMedicineList.remove(medicine);
                    oldList.remove(medicine);
                    mMedicineRecycleAdapter.notifyDataSetChanged();
                }
            }
        });
        Utiltis.getAllMedicineInList(getActivity(), mMedicineRecycleView, mMedicineRecycleAdapter, new Utiltis.ReturnValueResult<List<Medicine>>() {
            @Override
            public void onResult(List<Medicine> object) {
                Utiltis.allSystemMedicineList.removeAll(Utiltis.mMedicineList);
                oldList=Utiltis.allSystemMedicineList;
            }
        });

    }

    private void prepareToDelete() {
        Utiltis.getPharmacyProfileMedicine(Utiltis.currentUser.getUid(), new Utiltis.ReturnValueResult<Pharmacy>() {
            @Override
            public void onResult(final Pharmacy pharmacy) {
                oldList=pharmacy.getMedicine();
                 mMedicineRecycleAdapter = new MedicineRecycleAdapter(getActivity(), pharmacy.getMedicine(), new MedicineRecycleAdapter.MedicineClickListener() {
                    @Override
                    public void onMedicineClick(Medicine medicine) {
                        Boolean taskComplete = Utiltis.deleteMedicineFromPharmacy(Utiltis.currentUser.getUid(), medicine.getMedID()).isComplete();
                        if (taskComplete) {
                            oldList.remove(medicine);//3
                            Utiltis.mMedicineList.remove(medicine);//1
                            pharmacy.getMedicine().remove(medicine);//2 equivalent
                            Utiltis.allSystemMedicineList.add(medicine);
                            mMedicineRecycleAdapter.notifyDataSetChanged();
                        }
                    }
                });
                mMedicineRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mMedicineRecycleView.setAdapter(mMedicineRecycleAdapter);
            }
        });
    }
}
