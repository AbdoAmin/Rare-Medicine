package com.example.abdoamin.RareMedicine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.abdoamin.RareMedicine.activity.BarCodeActivity;
import com.example.abdoamin.RareMedicine.activity.CustomerSearchActivity;
import com.example.abdoamin.RareMedicine.activity.LogInActivity;
import com.example.abdoamin.RareMedicine.activity.PharmacyMapActivity;
import com.example.abdoamin.RareMedicine.activity.PharmacyProfileActivity;
import com.example.abdoamin.RareMedicine.activity.SignUpContinueActivity;
import com.example.abdoamin.RareMedicine.activity.SwitchModeActivity;
import com.example.abdoamin.RareMedicine.adapter.MedicineRecycleAdapter;
import com.example.abdoamin.RareMedicine.object.Medicine;
import com.example.abdoamin.RareMedicine.object.Pharmacy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Abdo Amin on 11/25/2017.
 */

public class Utiltis {

    //user Latitude
    public static double userLat;
    //user Longitude
    public static double userLng;
    //nearby PH list Founded
    public static List<Pharmacy> nearbyPharmacyList;
    //all medicine list at system
    public static List<Medicine> allSystemMedicineList;
    //all pharmacy medicine exist
    public static List<Medicine> mMedicineList;
    //current profile data author pharmacy
    public static Pharmacy currentPharmacy;
    //for author pharmacy
    public static FirebaseAuth mAuth;
    public static FirebaseUser currentUser;
    static Double targetDistance;

    /*
     * this Algorithm
     * search sequential at all PH
     * then check distance between user and each PH founded if <=650 miles ||1046.07 KM
     * if though record in list
     * */
    //context mean where this function output appear//TODO show list into ui
    static public void searchMedicine(final Context mContext, final String medID, final ReturnValueResult<List<Pharmacy>> listReturnValueResult) {
        isMedicineExist(medID, new ReturnValueResult<Boolean>() {
            @Override
            public void onResult(Boolean exist) {
                if (exist) {
                    targetDistance = 650.0;/*miles*/
                    nearbyPharmacyList = new ArrayList<Pharmacy>();
                    FirebaseDatabase mFirebaseDatabase;
                    DatabaseReference mDatabaseReference;
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mDatabaseReference = mFirebaseDatabase.getReference().child("/medicine-pharmacy/" + medID);
                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                final List<String> pharmacyIdList = new ArrayList<String>(((HashMap<String, String>) dataSnapshot.getValue()).keySet());
                                if (pharmacyIdList.size() <= 10)
                                    targetDistance = 1000000.0;
                                loop:
                                for (final String pharmacyId : pharmacyIdList) {
                                    FirebaseDatabase mFirebaseDatabase;
                                    DatabaseReference mDatabaseReference;
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    mDatabaseReference = mFirebaseDatabase.getReference().child("/pharmacy/" + pharmacyId);
                                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Double lat = dataSnapshot.child("location").child("latitude").getValue(Double.class);
                                            Double lng = dataSnapshot.child("location").child("longitude").getValue(Double.class);
                                            Double dist = distance(userLat, userLng, lat, lng);
                                            if (dist <= targetDistance/*miles*/) {
                                                String name = dataSnapshot.child("name").getValue(String.class);
                                                String img = dataSnapshot.child("img").getValue(String.class);
                                                String address = dataSnapshot.child("address").getValue(String.class);
                                                String phone = dataSnapshot.child("phone").getValue(String.class);
                                                //add in list
                                                nearbyPharmacyList.add(new Pharmacy(name, lat, lng, dist, address, img, phone));
                                                Toast.makeText(mContext, nearbyPharmacyList.get(0).getName(), Toast.LENGTH_LONG).show();
                                                if (pharmacyId == pharmacyIdList.get(pharmacyIdList.size() - 1)) {
                                                    if (nearbyPharmacyList.size() < 10 && pharmacyIdList.size() > 11) {
                                                        targetDistance *= 2;
//                                                        return loop;
                                                    } else {
                                                        //sort nearby list of pharmacy
                                                        getNearbyPharmacy();
                                                        listReturnValueResult.onResult(nearbyPharmacyList);
                                                    }
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else
                    Toast.makeText(mContext, mContext.getString(R.string.med_not_exist), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //TODO: not implemented yet
    static public void getNearbyPharmacy() {
        if (nearbyPharmacyList != null) {
            sortList();
        }

    }


    static private void sortList() {
        Collections.sort(nearbyPharmacyList, new Comparator<Pharmacy>() {
            @Override
            public int compare(Pharmacy pharmacy1, Pharmacy pharmacy2) {
                return Double.compare(pharmacy1.getDistance(), pharmacy2.getDistance());
            }
        });
    }


    //calculate distance between 2 PH by earth equation
    static public Double distance(Double lat1, Double lng1, Double lat2, Double lng2) {

        Double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        Double dLat = Math.toRadians(lat2 - lat1);
        Double dLng = Math.toRadians(lng2 - lng1);

        Double sindLat = Math.sin(dLat / 2);
        Double sindLng = Math.sin(dLng / 2);

        Double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        Double dist = earthRadius * c;

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
        intent.putExtra(mContext.getString(R.string.activity_name), mContext.getClass().getSimpleName());
        mContext.startActivity(intent);
    }


    //TODO: Set which Activity will start from ui
    //get result barcode from camera to Search Activity
    static public void barCodeResult(Context mContext, String code, String activity) {
        Intent intent = null;
        try {
            intent = new Intent(mContext, Class.forName("com.example.abdoamin.RareMedicine.activity." + activity));
            intent.putExtra("code", code);
            mContext.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    //get each medicine info from firebase by its id
    static public void getMedicineInfo(final String medID, final ReturnValueResult<Medicine> mReturnValueResult) {

        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("medicine/" + medID);
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
    static public void isMedicineExist(String medID, final ReturnValueResult<Boolean> mReturnValueResult) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("medicine/");
        mDatabaseReference.orderByKey().equalTo(medID).addListenerForSingleValueEvent(new ValueEventListener() {
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
    static public void addNewMedicine(final Context mContext, final String medID, final String name) {
        isMedicineExist(medID, new ReturnValueResult<Boolean>() {
            @Override
            public void onResult(Boolean object) {
                if (!((Boolean) object)) {
                    final HashMap<String, String> nameKey = new HashMap<String, String>() {{
                        put("name", name);
                    }};
                    FirebaseDatabase mFirebaseDatabase;
                    DatabaseReference mDatabaseReference;
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mDatabaseReference = mFirebaseDatabase.getReference("medicine/");
                    mDatabaseReference.child(medID).setValue(nameKey);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.med_exist), Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    //search medicine by it's name then return it's ID.. return in OnResult interface with it's caller
    static public void searchMedicineByName(final Context mContext, final String medName, final ReturnValueResult<String> mReturnValueResult) {
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
    static public void getAllMedicineInList(final Context mContext, final RecyclerView mRecyclerView, final MedicineRecycleAdapter mMedicineRecycleAdapter, final ReturnValueResult<List<Medicine>> allMedicineListWaitingOrderReturnVaule) {
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
                    allSystemMedicineList = new ArrayList<Medicine>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Medicine medicine = new Medicine((String) child.child("name").getValue(), child.getKey());
                        allSystemMedicineList.add(medicine);
                    }
                    allMedicineListWaitingOrderReturnVaule.onResult(allSystemMedicineList);
                    //TODO call into PH add, customer search
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    mMedicineRecycleAdapter.addList(allSystemMedicineList);
                    mRecyclerView.setAdapter(mMedicineRecycleAdapter);
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

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


    //TODO: put into ui , get medicine info ,, 2 view customer,PH
    static public void getPharmacyProfileInfo(final String pharmacyID, final ReturnValueResult<Pharmacy> returnValueResult, final ReturnValueResult<Pharmacy> medicineReturnValueResult) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("pharmacy/" + pharmacyID);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMedicineList = new ArrayList<>();
                final String name = (String) dataSnapshot.child("name").getValue();
                final String address = (String) dataSnapshot.child("address").getValue();
                final String imgURL = (String) dataSnapshot.child("img").getValue();
                final String phoneNumber = (String) dataSnapshot.child("phone").getValue();
                final Double lat = dataSnapshot.child("latitude").getValue(Double.class);
                final Double lng = dataSnapshot.child("longitude").getValue(Double.class);
                currentPharmacy = new Pharmacy(name, lat, lng, 0.0, address, imgURL, phoneNumber);
                returnValueResult.onResult(currentPharmacy);
                getPharmacyProfileMedicine(pharmacyID, medicineReturnValueResult);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }


    //pass location and Pharmacy to open into map
    static public void openPharmacyMap(Context mContext, Pharmacy pharmacy) {
        Intent intent = new Intent(mContext, PharmacyMapActivity.class);
        intent.putExtra(mContext.getString(R.string.latitude_map), pharmacy.getLatitude());
        intent.putExtra(mContext.getString(R.string.longitude_map), pharmacy.getLongitude());
        intent.putExtra(mContext.getString(R.string.img_map), pharmacy.getImg());
        intent.putExtra(mContext.getString(R.string.name_map), pharmacy.getName());
        mContext.startActivity(intent);

    }


    //show all Pharmacy on map from pressing button in result activity
    static public void showAllNearbyPharmacyOnMap(Context mContext, List<Pharmacy> pharmacyList, GoogleMap map) {
        Pharmacy theNearbyPharmacy = pharmacyList.get(0);
        LatLng theNearbyPharmcyLatLng = new LatLng(theNearbyPharmacy.getLatitude(), theNearbyPharmacy.getLongitude());
        map.addMarker(new MarkerOptions()
                        .position(theNearbyPharmcyLatLng)
                        .title(theNearbyPharmacy.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mariosmall))
//                      .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left//visibilty trancparent of markker
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(theNearbyPharmcyLatLng, 15));
        for (Pharmacy pharmacy :
                pharmacyList) {
            if (pharmacy == theNearbyPharmacy)
                continue;
            String name = pharmacy.getName();
            LatLng pharmacyLatLng = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
            map.addMarker(new MarkerOptions()
                            .position(pharmacyLatLng)
                            .title(name)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mariosmall))
//                      .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left//visibilty trancparent of markker
            );
        }

    }

    static public void pharmacySignUp(final Context mContext, final String email, String password, final String name, final double latitude, final double longitude) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, email, Toast.LENGTH_SHORT).show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("/pharmacy/" + task.getResult().getUser().getUid().toString());
                    myRef.setValue(new HashMap<String, Object>() {
                        {
                            put("name", name);
                            put("location", new HashMap<String, Double>() {
                                {
                                    put("latitude", latitude);
                                    put("longitude", longitude);
                                }
                            });
                        }
                    });
                    Intent intent = new Intent(mContext, SignUpContinueActivity.class);
                    intent.putExtra(mContext.getString(R.string.pharmacy_id), task.getResult().getUser().getUid().toString());
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                } else {
                    Toast.makeText(mContext, "This Email Already Exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    static public void pharmacySignUpContinue(final Context mContext, final String userID, final Uri imageURI, final String address, final String phone) {
        final ReturnValueResult<Uri> returnValueResult = new ReturnValueResult<Uri>() {
            @Override
            public void onResult(final Uri downloadUrl) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("/pharmacy/" + userID);
                myRef.updateChildren(new HashMap<String, Object>() {
                    {
                        if (address != null)
                            put("address", address);
                        if (downloadUrl == null || imageURI == null)
                            put("img", "https://firebasestorage.googleapis.com/v0/b/pharmacien-e9a90.appspot.com/o/pharmacyProfile%2Fdefult.png?alt=media&token=a5165e48-296e-44e5-966c-397361adba5c");
                        else
                            put("img", (downloadUrl).toString());
                        if (phone != null)
                            put("phone", phone);

                    }

                });
                Intent intent = new Intent(mContext, PharmacyProfileActivity.class);
                intent.putExtra(mContext.getString(R.string.pharmacy_id), userID);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }
        };
        final Uri[] downloadUrl = new Uri[1];
        if (imageURI != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
//            Uri file = Uri.fromFile(new File(imageURI));
            StorageReference storageRef = storage.getReference("/pharmacyProfile/" + userID);
            storageRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    downloadUrl[0] = taskSnapshot.getDownloadUrl();
                    returnValueResult.onResult(downloadUrl[0]);
                }
            });
        } else {
            returnValueResult.onResult(null);
        }
    }


    static public void logIn(final Context mContext, String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                currentUser = task.getResult().getUser();
                Intent intent = new Intent(mContext, PharmacyProfileActivity.class);
                intent.putExtra(mContext.getString(R.string.pharmacy_id), currentUser.getUid());
                mContext.startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "This Email Not Registed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //open profile pharmacy
//    static public void getPharmacyProfileInfoMedicine(final String pharmacyID, final ReturnValueResult<Pharmacy> infoReturnValueResult, final ReturnValueResult<Pharmacy> medicineReturnValueResult) {
//        getPharmacyProfileInfo(pharmacyID, infoReturnValueResult);
//        getPharmacyProfileMedicine(pharmacyID,medicineReturnValueResult);
//    }

    //customer on pharmacy click
//    static public void getPharmacyProfileInfoUser(final String pharmacyID, final ReturnValueResult<Pharmacy> infoReturnValueResult) {
//        getPharmacyProfileInfo(pharmacyID, infoReturnValueResult);
//    }

    static public void getPharmacyProfileMedicine(final String pharmacyID, final ReturnValueResult<Pharmacy> medicineReturnValueResult) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("pharmacy/" + pharmacyID + "/medicine");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMedicineList = new ArrayList<>();
                if ((dataSnapshot.getValue()) == null) {
                    currentPharmacy.setMedicine(mMedicineList);
                    medicineReturnValueResult.onResult(currentPharmacy);
                    return;
                }

                final List<String> medicine = new ArrayList<String>(((HashMap<String, String>) dataSnapshot.getValue()).keySet());
                for (final String medID : medicine) {
                    getMedicineInfo(medID, new ReturnValueResult<Medicine>() {
                        @Override
                        public void onResult(Medicine med) {
                            mMedicineList.add(med);
                            if (medicine.get(medicine.size() - 1) == medID) {
                                currentPharmacy.setMedicine(mMedicineList);
                                medicineReturnValueResult.onResult(currentPharmacy);
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


    static public void addMedicineToPharmacy(final String pharmacyID, final String medicineID, final ReturnValueResult<Boolean> taskReturnValueResult) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        HashMap<String, Object> medicineUpdate = new HashMap<String, Object>() {{
            put("medicine-pharmacy/" + medicineID + "/" + pharmacyID, true);
            put("pharmacy/" + pharmacyID + "/medicine/" + medicineID, true);
        }};
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.updateChildren(medicineUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    taskReturnValueResult.onResult(true);
                else
                    taskReturnValueResult.onResult(false);
            }
        });
    }

    static public void deleteMedicineFromPharmacy(final String pharmacyID, final String medicineID, final ReturnValueResult<Boolean> taskReturnValueResult) {
        FirebaseDatabase mFirebaseDatabase;
        DatabaseReference mDatabaseReference;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        HashMap<String, Object> medicineUpdate = new HashMap<String, Object>() {{
            put("medicine-pharmacy/" + medicineID + "/" + pharmacyID, null);
            put("pharmacy/" + pharmacyID + "/medicine/" + medicineID, null);
        }};
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.updateChildren(medicineUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    taskReturnValueResult.onResult(true);
                else
                    taskReturnValueResult.onResult(false);
            }
        });

    }

    //all menu
    static public void userMenuOnSelect( Context mContextint ,int itemMenuID){
        switch(itemMenuID){
            case R.id.user_menu_search_medicine:
                mContextint.startActivity(new Intent(mContextint, CustomerSearchActivity.class));
                break;
            case R.id.user_menu_switch_mode:
                mContextint.startActivity(new Intent(mContextint, SwitchModeActivity.class));
                break;
        }

    }

    static public void pharmacistMenuOnSelect( Context mContextint ,int itemMenuID){
        switch(itemMenuID){
            case R.id.pharmacist_menu_profile:
                mContextint.startActivity(new Intent(mContextint, PharmacyProfileActivity.class));
                break;
            case R.id.pharmacist_menu_edit_profile:
                //todo create edit activity
                break;
            case R.id.pharmacist_menu_log_out:
                //todo logout
                break;
            case R.id.pharmacist_menu_switch_mode:
                mContextint.startActivity(new Intent(mContextint, SwitchModeActivity.class));
                break;
        }

    }

    static public void NoneMenuOnSelect( Context mContextint ,int itemMenuID){
        switch(itemMenuID){
            case R.id.switch_mode_user:
                //todo add prefrance
                mContextint.startActivity(new Intent(mContextint, CustomerSearchActivity.class));
                break;
            case R.id.switch_mode_pharmacist:
                //todo add prefrance
                mContextint.startActivity(new Intent(mContextint, LogInActivity.class));
                break;
            case R.id.switch_mode_rate_us:
                //todo rate later
                break;
            case R.id.switch_mode_language:
                // TODO: language later
                break;
        }

    }




    static public <T> void removeDuplicatedItemsInList(List<T> mList) {
    // add elements to al, including duplicates
        Set<T> hs = new HashSet<T>();
        hs.addAll(mList);
        mList.clear();
        mList.addAll(hs);
    }

    //this interface act between function and caller to get a return value form background thread
    public interface ReturnValueResult<T> {
        public void onResult(T object);
    }
}