package com.example.abdoamin.RareMedicine.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.abdoamin.RareMedicine.R;

import com.example.abdoamin.RareMedicine.adapter.RequestMedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.object.Medicine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.abdoamin.RareMedicine.Utiltis.acceptRequestAddNewMedicine;
import static com.example.abdoamin.RareMedicine.Utiltis.declineRequestAddNewMedicine;
import static com.example.abdoamin.RareMedicine.Utiltis.getRequestMedicineInRecyclerView;
import static com.example.abdoamin.RareMedicine.Utiltis.ReturnValueResult;

public class AdminRequestMedicineMangerActivity extends AppCompatActivity {
    @BindView(R.id.activity_admin_request_medicine_manger_recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    private Paint p = new Paint();


    RequestMedicineRecycleAdapter mRequestMedicineRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_medicine_manger);
        unbinder= ButterKnife.bind(this);
        mRequestMedicineRecycleAdapter=new RequestMedicineRecycleAdapter(this,new  ArrayList<Medicine>());
        getRequestMedicineInRecyclerView(this, mRecyclerView, mRequestMedicineRecycleAdapter
                , new ReturnValueResult<List<Medicine>>() {
            @Override
            public void onResult(List<Medicine> object) {
                initSwipe(object);
            }
        });
    }

    private void initSwipe(final List<Medicine> requestMedicineList){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    acceptRequestAddNewMedicine(AdminRequestMedicineMangerActivity.this
                    ,requestMedicineList.get(position).getMedID()
                    ,requestMedicineList.get(position).getName());
                    requestMedicineList.remove(position);
                    mRequestMedicineRecycleAdapter.addList(requestMedicineList);

                } else {
                    declineRequestAddNewMedicine(AdminRequestMedicineMangerActivity.this
                            ,requestMedicineList.get(position).getMedID());
                    requestMedicineList.remove(position);
                    mRequestMedicineRecycleAdapter.addList(requestMedicineList);

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#f00000"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getLeft()+dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                    } else {
                        p.setColor(Color.parseColor("#00f000"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }



}
