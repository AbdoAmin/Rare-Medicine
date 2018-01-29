package com.example.abdoamin.RareMedicine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.abdoamin.RareMedicine.activity.BarCodeActivity;
import com.example.abdoamin.RareMedicine.activity.MainActivity;
import com.example.abdoamin.RareMedicine.activity.PharmacyMap;
import com.example.abdoamin.RareMedicine.adapter.MedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.object.Medicine;
import com.example.abdoamin.RareMedicine.object.Pharmacy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Abdo Amin on 11/25/2017.
 */

public class Utiltis {

    //user Latitude
    public static double userLat;
    //user Longitude
    public static double userLng;
    //nearby PH list Founded
    public static List<Pharmacy> pharmacyList;


    /*
     * this Algorithm
     * search sequential at all PH
     * then check distance between user and each PH founded if <=650 miles ||1046.07 KM
     * if though record in list
     * */
    //context mean where this function output appear//TODO show list into ui
    static public void searchMedicine(final long medID, final Context mContext) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("pharmacy");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //inital list
                pharmacyList = new ArrayList<Pharmacy>();
                //search each PH if medicine found
                for (final DataSnapshot mChild : dataSnapshot.getChildren()) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("/pharmacy/" + mChild.getKey() + "/medicine");
                    myRef.orderByKey().equalTo(String.valueOf(medID)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if found
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(mContext, "here am i", Toast.LENGTH_LONG).show();
                                double lat = (double) mChild.child("location").child("latitude").getValue();
                                double lng = (double) mChild.child("location").child("longitude").getValue();
                                double dist = distance(userLat, userLng, lat, lng);
                                //check distance between PH and User to save in list Max 650 miles
                                if (dist <= 650/*miles*/) {
                                    String name = (String) mChild.child("name").getValue();
                                    String img = (String) mChild.child("img").getValue();
                                    String address = (String) mChild.child("address").getValue();
                                    String phone = (String) mChild.child("phone").getValue();

                                    //add in list
                                    pharmacyList.add(new Pharmacy(name, lat, lng, dist, address, img, phone));
                                    Toast.makeText(mContext, pharmacyList.get(0).getName(), Toast.LENGTH_LONG).show();

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(mContext, "Faild To search", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(mContext, "Faild To Log In", Toast.LENGTH_LONG).show();
            }
        });

    }

    //TODO: not implemented yet
    static public void getNearbyPharmacy(double mLatitude, double mLongitude, Context mContext) {
        if (pharmacyList != null) {
            sortList();
        }

    }


    static private void sortList() {
        Collections.sort(pharmacyList, new Comparator<Pharmacy>() {
            @Override
            public int compare(Pharmacy pharmacy1, Pharmacy pharmacy2) {
                return Double.compare(pharmacy1.getDistance(), pharmacy2.getDistance());
            }
        });
    }


    //calculate distance between 2 PH by earth equation
    static public double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }


    static public void getCurrentUserLocation(Context mContext) {
        GPSTracker gps = new GPSTracker(mContext);
        Utiltis.userLat = gps.getLatitude();
        Utiltis.userLng = gps.getLongitude();

    }


    //open Activity code bar detected with camera
    static public void barCode(Context mContext) {
        Intent intent = new Intent(mContext, BarCodeActivity.class);
        mContext.startActivity(intent);
    }


    //TODO: Set which Activity will start from ui
    //get result barcode from camera to Search Activity
    static public void barCodeResult(Context mContext, String code) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("code", code);
        mContext.startActivity(intent);
    }


    //TODO: put into ui , get medicine info ,, 2 view customer,PH
    static public void getPharmacyProfileInfo(long pharmacyID) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("pharmacy/" + String.valueOf(pharmacyID));
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Medicine> mMedicineList = new ArrayList<>();
                String name = (String) dataSnapshot.child("name").getValue();
                String address = (String) dataSnapshot.child("address").getValue();
                String imgURL = (String) dataSnapshot.child("img").getValue();
                String phoneNumber = (String) dataSnapshot.child("phone").getValue();
                double lat = (double) dataSnapshot.child("latitude").getValue();
                double lng = (double) dataSnapshot.child("longitude").getValue();
                final List<Long> medicine = new ArrayList<Long>(((HashMap<Long, String>) dataSnapshot.child("medicine").getValue()).keySet());
                for (final Long medID : medicine) {
                    getMedicineInfo(medID, new ReturnValueResult() {
                        @Override
                        public void onResult(Object med) {

                            mMedicineList.add((Medicine) med);
                            if (medicine.get(medicine.size() - 1) == medID) {
                                //TODO: output into ui
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Toast.makeText(mContext, "Faild To Log In", Toast.LENGTH_LONG).show();
            }
        });
    }


    //get each medicine info from firebase by its id
    static public void getMedicineInfo(final Long medID, final ReturnValueResult mReturnValueResult) {

        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("medicine/" + String.valueOf(medID));
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                Medicine searchedMedicine = new Medicine(name, medID);
                mReturnValueResult.onResult(searchedMedicine);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Toast.makeText(mContext, "Faild To Log In", Toast.LENGTH_LONG).show();
            }
        });
    }


    //check if medicine exist
    static public void isMedicineExist(long medID, final ReturnValueResult mReturnValueResult) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("medicine/");
        mDatabaseReference.orderByKey().equalTo(String.valueOf(medID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if found
                if (dataSnapshot.getValue() != null) {
                    mReturnValueResult.onResult(true);
                } else
                    mReturnValueResult.onResult(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(mContext, "Faild To search", Toast.LENGTH_LONG).show();
            }
        });
    }


    //TODO make aprove by : add in DB FB child named : aprovement , when admin open (his ui) apper to list of requestes (yes mean make this quesry by get all Data from FB)
    //add new medicine by admin or pharmacist
    static public void addNewMedicine(final long medID, final String name, final Context mContext) {
        isMedicineExist(medID, new ReturnValueResult() {
            @Override
            public void onResult(Object object) {
                if (!((Boolean) object)) {
                    FirebaseDatabase mFirebaseDatabase;
                    DatabaseReference mDatabaseReference;
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mDatabaseReference = mFirebaseDatabase.getReference("medicine/");
                    mDatabaseReference.child(String.valueOf(medID)).setValue("name", name);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.med_exist), Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    //search medicine by it's name then return it's ID.. return in OnResult interface with it's caller
    static public void searchMedicineByName(final String medName, final Context mContext, final ReturnValueResult mReturnValueResult) {
        FirebaseDatabase mFirebaseDatabase;
        final DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("medicine/");
        mDatabaseReference.orderByChild("name").equalTo(medName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if found
                if (dataSnapshot.getValue() != null) {
                    mReturnValueResult.onResult(new ArrayList<String>(((HashMap<String, String>) dataSnapshot.getValue()).keySet()).get(0));
                } else
                    mReturnValueResult.onResult(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, mContext.getString(R.string.error_search), Toast.LENGTH_LONG).show();
            }
        });

    }


    //add all medicine in list to customer||pharmacist to search||add med
    static public void getAllMedicineInList(final RecyclerView mRecyclerView, final Context mContext, final MedicineRecycleAdapter.MedicineClickListener medicineClickListener) {
        FirebaseDatabase mFirebaseDatabase;
        final DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("medicine/");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if found
                if (dataSnapshot.getValue() != null) {
                    //creat list of medicine that will pass to recycleView Adpter
                    List<Medicine> medicineList = new ArrayList<Medicine>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Medicine medicine = new Medicine((String) child.child("name").getValue(), Long.valueOf(child.getKey()));
                        medicineList.add(medicine);
                    }
                    //TODO call into PH add, customer search
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerView.setAdapter(new MedicineRecycleAdapter(medicineList, mContext, medicineClickListener));
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_med_in_firebase), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, mContext.getString(R.string.error_search), Toast.LENGTH_LONG).show();
            }
        });

    }



    //pass location and Pharmacy to open into map
    static public  void openPharmacyMap(Pharmacy pharmacy,Context mContext){
        Intent intent=new Intent(mContext, PharmacyMap.class);
        intent.putExtra(mContext.getString(R.string.latitude_map),pharmacy.getLatitude());
        intent.putExtra(mContext.getString(R.string.longitude_map),pharmacy.getLongitude());
        intent.putExtra(mContext.getString(R.string.img_map),pharmacy.getImg());
        intent.putExtra(mContext.getString(R.string.name_map),pharmacy.getName());
        mContext.startActivity(intent);

    }




    //this interface act between function and caller to get a return value form background thread
    public interface ReturnValueResult {
        public void onResult(Object object);
    }


}