package com.example.abdoamin.RareMedicine.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.activity.PharmacyMapActivity;
import com.example.abdoamin.RareMedicine.object.Pharmacy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Abdo Amin on 1/27/2018.
 */

public class PharmacyRecycleAdapter extends RecyclerView.Adapter<PharmacyRecycleAdapter.ViewHolder> {

    private List<Pharmacy> pharmacyList;
    private PharmacyClickListener mPharmacyClickListener;
    private Context mContext;

    public PharmacyRecycleAdapter(List<Pharmacy> medicines, Context context, PharmacyClickListener medicineClickListener) {
        this.pharmacyList = new ArrayList<>();
        pharmacyList.addAll(medicines);
        mPharmacyClickListener = medicineClickListener;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pharmacy, parent, false);

        return new ViewHolder(view);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.item_pharmacy_photo)
        CircleImageView photo;
        @BindView(R.id.item_pharmacy_name_textView)
        TextView name;
        @BindView(R.id.item_pharmacy_address_textView)
        TextView address;
        @BindView(R.id.item_pharmacy_distance_textView)
        TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            name.setText(getPharmacy(position).getName());
            address.setText(getPharmacy(position).getAddress());
            distance.setText(Utiltis.mileToKm_MeterToStringFormat(getPharmacy(position).getDistance()));
            Picasso.with(mContext).load(Uri.parse(getPharmacy(position).getImg())).into(photo);
            Picasso.with(mContext).load(Uri.parse(getPharmacy(position).getImg())).into(photo);

        }


        @OnClick
        public void onClick(View v) {
            mPharmacyClickListener.onPharmacyClick(getAdapterPosition());
        }
        @OnClick(R.id.item_pharmacy_menu_imageView)
        void onLocationClick(){
            Intent intent=new Intent(mContext, PharmacyMapActivity.class);
            intent.putExtra(mContext.getString(R.string.specificItemClickPosition),getAdapterPosition());
            mContext.startActivity(intent);
        }
    }

    public interface PharmacyClickListener {
        void onPharmacyClick(int position);
    }
}
