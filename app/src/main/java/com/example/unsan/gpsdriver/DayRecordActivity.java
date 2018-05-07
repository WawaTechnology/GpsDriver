package com.example.unsan.gpsdriver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 2/5/18.
 */

public class DayRecordActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    DatabaseReference driverDayDatabase,driverDeliveryReference;
    ValueEventListener valueEventListener;
    String dname;

    DatabaseReference imgReference;
    List<String> imgList;
    List<DriverDelivery> driverDeliveryList;
    RecyclerView todayRecycler;
    DayRecordAdapter dayRecordAdapter;
    String dateString;
    SwipeRefreshLayout swipeRefreshLayout;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_record_activity);
        driverDayDatabase= FirebaseDatabase.getInstance().getReference("driverDayRecord");
        driverDeliveryReference=FirebaseDatabase.getInstance().getReference("DriverDelivery");
        imgReference=FirebaseDatabase.getInstance().getReference("imgReferences");
        imgList=new ArrayList<>();
        driverDeliveryList=new ArrayList<>();
        todayRecycler=(RecyclerView) findViewById(R.id.recycler_today);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        dayRecordAdapter=new DayRecordAdapter(DayRecordActivity.this,imgList,driverDeliveryList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(DayRecordActivity.this,LinearLayoutManager.VERTICAL,false);
        todayRecycler.setLayoutManager(linearLayoutManager);
        todayRecycler.setAdapter(dayRecordAdapter);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);
       SharedPreferences sharedPreferences=getSharedPreferences("location_driver", Context.MODE_PRIVATE);
      dname= sharedPreferences.getString("dname",null);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Date today=new Date();
        dateString=simpleDateFormat.format(today);
        Log.d("checkdate",dateString);
        getDeliveries();




    }

    private void getDeliveries() {
        imgList.clear();
        driverDeliveryList.clear();
        valueEventListener=driverDayDatabase.child(dateString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String key=ds.getKey();
                        Log.d("checkdkey",key);

                        getDeliveryRecord(key);


                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDeliveryRecord(final String key) {
        driverDeliveryReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DriverDelivery driverDelivery=dataSnapshot.getValue(DriverDelivery.class);

                if(driverDelivery.driverName.equals(dname))
                {
                    driverDeliveryList.add(driverDelivery);


                    imgReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if(dataSnapshot.exists()) {

                                String img = dataSnapshot.getValue(String.class);
                                if (img != null) {
                                    Log.d("checkim",img);
                                    imgList.add(img);
                                    dayRecordAdapter.notifyDataSetChanged();
                                }



                            }
                            else {
                                imgList.add("no image");
                                dayRecordAdapter.notifyDataSetChanged();
                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRefresh() {
        getDeliveries();
        swipeRefreshLayout.setRefreshing(false);

    }
    public void onDestroy()
    {
        super.onDestroy();
        if(valueEventListener!=null)
        {
            driverDayDatabase.child(dateString).removeEventListener(valueEventListener);
            valueEventListener=null;
        }

    }
}
