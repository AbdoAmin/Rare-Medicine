package com.example.abdoamin.RareMedicine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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

/**
 * Created by Abdo Amin on 2/9/2018.
 */

public class DeleteProfileMedicineFragment extends Fragment {
    static public String TAG = "DeleteProfileMedicineFragment";
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
        prepareToDelete();
        if (PharmacyAddMedicineActivity.code != null && ModifyProfileMedicineFragmentAdapter.currentTabIndex == 0) {
            searchEditText.setText(PharmacyAddMedicineActivity.code);
            PharmacyAddMedicineActivity.code = null;
        }
    }

    @OnClick(R.id.fragment_modify_medicine_barcode_btn)
    void onBarCodeBtnClick() {
        ModifyProfileMedicineFragmentAdapter.currentTabIndex = 0;
        Utiltis.barCode(getActivity());
    }

    @OnTextChanged(R.id.fragment_modify_medicine_search_editText)
    void onSearchMedicineTextChange() {
        currentList.clear();
        for (Medicine medicine : oldList) {
            if (medicine.getName().contains(searchEditText.getText().toString()) ||
                    medicine.getMedID().contains(searchEditText.getText().toString()))
                currentList.add(medicine);
        }
        if (searchEditText.getText().toString().trim().equals("")) {
            Utiltis.removeDuplicatedItemsInList(oldList);
            mMedicineRecycleAdapter.addList(oldList);
        } else {
            Utiltis.removeDuplicatedItemsInList(currentList);
            mMedicineRecycleAdapter.addList(currentList);
        }
    }


    public static DeleteProfileMedicineFragment newInstance() {
        DeleteProfileMedicineFragment fragment = new DeleteProfileMedicineFragment();
        return fragment;
    }

    public void prepareToDelete() {
        Utiltis.removeDuplicatedItemsInList(Utiltis.mMedicineList);
        oldList = Utiltis.mMedicineList;
        mMedicineRecycleAdapter = new MedicineRecycleAdapter(getActivity(), Utiltis.mMedicineList, new MedicineRecycleAdapter.MedicineClickListener() {
            @Override
            public void onMedicineClick(final Medicine medicine) {
                Utiltis.deleteMedicineFromPharmacy(Utiltis.currentUser.getUid(), medicine.getMedID(), new Utiltis.ReturnValueResult<Boolean>() {
                    @Override
                    public void onResult(Boolean object) {
                        if (object) {
                            searchEditText.setText("");
                            oldList.remove(medicine);//3
                            Utiltis.mMedicineList.remove(medicine);//1
                            Utiltis.allSystemMedicineList.add(medicine);
                            Utiltis.removeDuplicatedItemsInList(Utiltis.mMedicineList);
                            mMedicineRecycleAdapter.addList(Utiltis.mMedicineList);
                        }
                    }
                });
            }
        });
        mMedicineRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMedicineRecycleView.setAdapter(mMedicineRecycleAdapter);
        mMedicineRecycleView.setHasFixedSize(true);
        mMedicineRecycleView.setItemAnimator(new DefaultItemAnimator());
        mMedicineRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        Log.e("^_^:ABDO:^_&:" + TAG, "onDestroyView");
    }
}
