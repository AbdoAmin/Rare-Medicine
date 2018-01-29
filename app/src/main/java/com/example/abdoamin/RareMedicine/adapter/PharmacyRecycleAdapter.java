package com.example.abdoamin.RareMedicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.object.Pharmacy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdo Amin on 1/27/2018.
 */

public class PharmacyRecycleAdapter extends RecyclerView.Adapter<PharmacyRecycleAdapter.ViewHolder> {

    private List<Pharmacy> pharmacyList;
    private PharmacyClickListener mPharmacyClickListener;
    Context mContext;

    public PharmacyRecycleAdapter(List<Pharmacy> medicines, Context context, PharmacyClickListener medicineClickListener) {
        this.pharmacyList = new ArrayList<Pharmacy>();
        pharmacyList.addAll(medicines);
        mPharmacyClickListener = medicineClickListener;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(/*TODO change layout*/R.layout.activity_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }


    public void addList(List<Pharmacy> pharmacyListIncoming) {
        pharmacyList.clear();
        pharmacyList.addAll(pharmacyListIncoming);
        notifyDataSetChanged();

    }

    public Pharmacy getPharmacy(int position) {
        return pharmacyList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);

        }

        void bind(int position) {

        }


        @Override
        public void onClick(View v) {
            mPharmacyClickListener.onPharmacyClick(getAdapterPosition());
        }
    }

    public interface PharmacyClickListener {
        void onPharmacyClick(int position);
    }
}
