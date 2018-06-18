package com.example.unsan.gpsdriver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 12/4/18.
 */

class CustomAdapter extends ArrayAdapter<CustomerOrder> {
    Context context;
    List<CustomerOrder> objects;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date todayDate = new Date();
    String thisDate;
    DatabaseReference customerTodayReference, customerReference;


    SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("HH a");
    FirebaseDatabase fbd;
    Customer customerDetail;
    List<String> hourList;
    String hour;
    String hh1;
    String carNumber;



    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<CustomerOrder> objects) {
        super(context,resource,objects);

        this.context = context;
        this.objects = objects;
        thisDate = simpleDateFormat.format(todayDate);
        fbd = FirebaseDatabase.getInstance();
        customerTodayReference = fbd.getReference("CustomerTodayRecord");
        customerReference = fbd.getReference("Customer");
        hour=simpleDateFormat1.format(todayDate);
        hh1=hour.substring(3);
        SharedPreferences sharedPreferences=context.getSharedPreferences("location_driver", Context.MODE_PRIVATE);
        carNumber=sharedPreferences.getString("carNumber","Car 1");


        hourList=new ArrayList<>();
        hourList.add("00 am");
        hourList.add("01 am");
        hourList.add("02 am");
        hourList.add("03 am");
        hourList.add("04 am");
        hourList.add("05 am");

        Log.d("thisdate", thisDate);
    }


    @Override
    public int getCount() {
        if (objects != null)
            return objects.size();
        else
            return -1;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listV = convertView;
        if (listV == null)
            listV = LayoutInflater.from(context).inflate(R.layout.simple_display, parent, false);
        TextView tv = (TextView) listV.findViewById(R.id.custnamedisp);
        TextView engtv = (TextView) listV.findViewById(R.id.customereng);
        final TextView tv1 = (TextView) listV.findViewById(R.id.delivery_status);



        final CustomerOrder customer = objects.get(position);
        tv.setText(customer.getCustomerChinese());
        engtv.setText(customer.getOrderStatus().engName);
        tv1.setText(customer.getOrderStatus().getStatus());



        listV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(context,CustomerDetail.class);
                customerReference.child(customer.getCustomerChinese()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d("existd", "true");
                        }
                        customerDetail = dataSnapshot.getValue(Customer.class);
                        if(customerDetail!=null) {


                            Intent intent = new Intent(context, DeliveredActivity.class);
                            intent.putExtra("customernd", customerDetail);
                            intent.putExtra("restName", customer.getCustomerChinese());
                            context.startActivity(intent);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        return listV;
    }

    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(yesterday());
    }
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}


