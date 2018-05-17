package com.example.unsan.gpsdriver;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 12/4/18.
 */

class CustomAdapter extends ArrayAdapter<CustomerEngChinese> {
    Context context;
    List<CustomerEngChinese> objects;
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

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<CustomerEngChinese> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        thisDate = simpleDateFormat.format(todayDate);
        fbd = FirebaseDatabase.getInstance();
        customerTodayReference = fbd.getReference("CustomerTodayRecord");
        customerReference = fbd.getReference("Customer");
        hour=simpleDateFormat1.format(todayDate);
        hh1=hour.substring(3);


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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listV = convertView;
        if (listV == null)
            listV = LayoutInflater.from(context).inflate(R.layout.simple_display, parent, false);
        TextView tv = (TextView) listV.findViewById(R.id.custnamedisp);
        TextView engtv = (TextView) listV.findViewById(R.id.customereng);
        final TextView tv1 = (TextView) listV.findViewById(R.id.delivery_status);

        final CustomerEngChinese customer = objects.get(position);


        if (customer != null) {

                if(hourList.contains(hour))
                {
                    thisDate=getYesterdayDateString();
                }


            customerTodayReference.child(thisDate).child(((MainPage) context).carNumber).child(customer.getChinese()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("customeRec", customer.getChinese());
                        if (dataSnapshot.getValue(String.class).equals("Ordered"))
                            tv1.setText(context.getResources().getString(R.string.waiting));
                        else
                            tv1.setText(context.getResources().getString(R.string.delivered));
                    } else {
                        tv1.setText(context.getResources().getString(R.string.no_order));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {


        }


        if (customer != null) {

            tv.setText(customer.getChinese());
            engtv.setText(customer.getEnglish() + "");
        } else
            tv.setText("No customer Found");
        listV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(context,CustomerDetail.class);
                customerReference.child(customer.chinese).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d("existd", "true");
                        }
                        customerDetail = dataSnapshot.getValue(Customer.class);
                        if(customerDetail!=null) {


                            Intent intent = new Intent(context, DeliveredActivity.class);
                            intent.putExtra("customernd", customerDetail);
                            intent.putExtra("restName", customer.chinese);
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


