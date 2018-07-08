package com.example.abdoamin.RareMedicine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.example.abdoamin.RareMedicine.activity.PharmacyAddMedicineActivity;
import com.example.abdoamin.RareMedicine.adapter.MedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.adapter.ModifyProfileMedicineFragmentAdapter;
import com.example.abdoamin.RareMedicine.object.Medicine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

import static com.example.abdoamin.RareMedicine.Utiltis.addMedicineToPharmacy;
import static com.example.abdoamin.RareMedicine.Utiltis.allSystemMedicineList;
import static com.example.abdoamin.RareMedicine.Utiltis.barCode;
import static com.example.abdoamin.RareMedicine.Utiltis.currentUser;
import static com.example.abdoamin.RareMedicine.Utiltis.getAllRareMedicineInRecyclerView;
import static com.example.abdoamin.RareMedicine.Utiltis.mMedicineList;
import static com.example.abdoamin.RareMedicine.Utiltis.removeDuplicatedItemsInList;
import static com.example.abdoamin.RareMedicine.Utiltis.ReturnValueResult;

/**
 * Created by Abdo Amin on 2/13/2018.
 */

public class AddProfileMedicineFragment extends Fragment {
    static public String TAG = "AddProfileMedicineFragment";
    @BindView(R.id.fragment_modify_medicine_medicine_recycleView)
    RecyclerView mMedicineRecycleView;
    @BindView(R.id.fragment_modify_medicine_search_editText)
    EditText searchEditText;
    Unbinder mUnbinder;
    List<Medicine> oldList;
    List<Medicine> currentList;
    MedicineRecycleAdapter mMedicineRecycleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_modify_medicine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("^_^:ABDO:^_&:" + TAG, "onViewCreated");
        mUnbinder = ButterKnife.bind(this, view);
        oldList = new ArrayList<>();
        currentList = new ArrayList<>();
        prepareToAdd();
    }

    @OnClick(R.id.fragment_modify_medicine_barcode_btn)
    void onBarCodeBtnClick() {
        ModifyProfileMedicineFragmentAdapter.currentTabIndex = 1;
        barCode(getActivity());
    }

    //TODO check barcode result not responde to search
    @OnTextChanged(R.id.fragment_modify_medicine_search_editText)
    void onSearchMedicineTextChange() {
        currentList.clear();
        for (Medicine medicine : oldList) {
            if (medicine.getName().contains(searchEditText.getText().toString()) ||
                    medicine.getMedID().contains(searchEditText.getText().toString()))
                currentList.add(medicine);
        }
        if (searchEditText.getText().toString().trim().equals("")) {
            removeDuplicatedItemsInList(oldList);
            mMedicineRecycleAdapter.addList(oldList);
        } else {
            removeDuplicatedItemsInList(currentList);
            mMedicineRecycleAdapter.addList(currentList);
        }
    }

    public static AddProfileMedicineFragment newInstance() {
        return new AddProfileMedicineFragment();
    }

    public void prepareToAdd() {
        if (allSystemMedicineList == null)
            allSystemMedicineList = new ArrayList<>();
        mMedicineRecycleAdapter = new MedicineRecycleAdapter(getActivity(), allSystemMedicineList, new MedicineRecycleAdapter.MedicineClickListener() {
            @Override
            public void onMedicineClick(final Medicine medicine) {
                addMedicineToPharmacy(currentUser.getUid(), medicine.getMedID(), new ReturnValueResult<Boolean>() {
                    @Override
                    public void onResult(Boolean object) {
                        if (object) {
                            searchEditText.setText("");
                            allSystemMedicineList.remove(medicine);
                            oldList.remove(medicine);
                            mMedicineRecycleAdapter.addList(allSystemMedicineList);
                        }
                    }
                });

            }
        });
        getAllRareMedicineInRecyclerView(getActivity(),searchEditText,PharmacyAddMedicineActivity.code, mMedicineRecycleView, mMedicineRecycleAdapter, new ReturnValueResult<List<Medicine>>() {
            @Override
            public void onResult(List<Medicine> object) {
                allSystemMedicineList.removeAll(mMedicineList);
                oldList = allSystemMedicineList;
                mMedicineRecycleAdapter.addList(allSystemMedicineList);
                if (PharmacyAddMedicineActivity.code != null && ModifyProfileMedicineFragmentAdapter.currentTabIndex == 1) {
                    searchEditText.setText(PharmacyAddMedicineActivity.code);
                    PharmacyAddMedicineActivity.code = null;
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        Log.e("^_^:ABDO:^_&:" + TAG, "onDestroyView");
    }


}
