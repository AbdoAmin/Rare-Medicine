package com.example.abdoamin.pharmacien.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abdoamin.pharmacien.R;
import com.example.abdoamin.pharmacien.object.Medicine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdo Amin on 1/26/2018.
 */

public class MedicineRecycleAdapter extends RecyclerView.Adapter<MedicineRecycleAdapter.ViewHolder> {

    private List<Medicine> medicineList;
    private MedicineClickListener mMedicineClickListener;
    Context mContext;

    public MedicineRecycleAdapter(List<Medicine> medicines, Context context, MedicineClickListener medicineClickListener) {
        this.medicineList = new ArrayList<Medicine>();
        medicineList.addAll(medicines);
        mMedicineClickListener = medicineClickListener;
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

        public ViewHolder(View itemView) {
            super(itemView);

        }

        void bind(int position) {

        }


        @Override
        public void onClick(View v) {
            mMedicineClickListener.onMedicineClick(getAdapterPosition());
        }
    }

    public interface MedicineClickListener {
        void onMedicineClick(int position);
    }
}
