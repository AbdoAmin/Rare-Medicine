package com.example.abdoamin.RareMedicine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abdoamin.RareMedicine.R;
import com.example.abdoamin.RareMedicine.object.Medicine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Abdo Amin on 1/26/2018.
 */

public class MedicineRecycleAdapter extends RecyclerView.Adapter<MedicineRecycleAdapter.ViewHolder> {

    private List<Medicine> medicineList;
    private MedicineClickListener mMedicineClickListener;
    Context mContext;

    public MedicineRecycleAdapter(Context context, List<Medicine> medicines, MedicineClickListener medicineClickListener) {
        this.medicineList = new ArrayList<Medicine>();
        medicineList.addAll(medicines);
        mMedicineClickListener = medicineClickListener;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_medicine, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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

    public Medicine getMedicine(int position) {
        return medicineList.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.adapter_medicine_item_medicine_name)
        TextView medicineName;
        @BindView(R.id.adapter_medicine_item_medicine_id)
        TextView medicineId;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            medicineName.setText(getMedicine(position).getName());
            medicineId.setText(getMedicine(position).getMedID());
        }


        @OnClick
        public void onClick(View v) {
            mMedicineClickListener.onMedicineClick(getMedicine(getAdapterPosition()));
        }
    }

    public interface MedicineClickListener {
        void onMedicineClick(Medicine medicine);
    }
}
