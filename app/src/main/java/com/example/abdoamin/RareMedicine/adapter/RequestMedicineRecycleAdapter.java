package com.example.abdoamin.RareMedicine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.Utiltis;
import com.example.abdoamin.RareMedicine.object.Medicine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestMedicineRecycleAdapter extends RecyclerView.Adapter<RequestMedicineRecycleAdapter.ViewHolder> {

    private List<Medicine> medicineList;
    private Context mContext;

    public RequestMedicineRecycleAdapter(Context context, List<Medicine> medicines) {
        this.medicineList = new ArrayList<>();
        medicineList.addAll(medicines);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.request_medicine_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }


    public void addList(List<Medicine> medicineListIncoming) {
        medicineList.clear();
        medicineList.addAll(medicineListIncoming);
        notifyDataSetChanged();

    }


    private Medicine getMedicine(int position) {
        return medicineList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adapter_medicine_item_medicine_name)
        TextView medicineName;
        @BindView(R.id.adapter_medicine_item_medicine_barcode)
        TextView medicineBarcode;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            medicineName.setText(getMedicine(position).getName());
            medicineBarcode.setText(getMedicine(position).getMedID());

        }

        @OnClick(R.id.accept)
        public void onAccept() {
            Utiltis.acceptRequestAddNewMedicine(mContext
                    , getMedicine(getAdapterPosition()).getMedID()
                    , getMedicine(getAdapterPosition()).getName());
        }

        @OnClick(R.id.decline)
        public void onDecline() {
            Utiltis.declineRequestAddNewMedicine(mContext, getMedicine(getAdapterPosition()).getMedID());
        }



    }
}
