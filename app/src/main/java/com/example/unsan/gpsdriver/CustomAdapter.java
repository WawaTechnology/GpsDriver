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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 12/4/18.
 */

class CustomAdapter extends ArrayAdapter<CustomerNode> {
    Context context;
    List<CustomerNode> objects;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
    Date todayDate = new Date();
    String thisDate ;
    DatabaseReference customerTodayReference;


    @Override
    public int getCount() {
        if(objects!=null)
            return objects.size();
        else
            return -1;
    }

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<CustomerNode> objects) {
        super(context, resource,0, objects);
        this.context=context;
        this.objects=objects;
        thisDate= simpleDateFormat.format(todayDate);
        customerTodayReference = FirebaseDatabase.getInstance().getReference("CustomerTodayRecord");
        Log.d("thisdate",thisDate);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listV=convertView;
        if(listV==null)
            listV= LayoutInflater.from(context).inflate(R.layout.simple_display,parent,false);

        final CustomerNode customerNode=objects.get(position);
        final Customer c=customerNode.getCustomer();
        TextView tv=(TextView)listV.findViewById(R.id.custnamedisp);
       final TextView tv1=(TextView)listV.findViewById(R.id.delivery_status);

        if(c!=null) {
            customerTodayReference.child(thisDate).child(customerNode.getRestaurantName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        Log.d("customeRec",customerNode.getRestaurantName());
                        tv1.setText("delivered");
                    }
                    else
                    {
                        tv1.setText("Pending");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {


        }


        if(c!=null) {
            if (MainPage.sorted) {
                tv.setText(customerNode.getCustomer().getAddress());
            } else
                tv.setText(customerNode.getRestaurantName());
        }
        else
            tv.setText("No customer Found");
        listV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(context,CustomerDetail.class);
                Intent intent=new Intent(context,DeliveredActivity.class);
                intent.putExtra("customernd",customerNode);
                context.startActivity(intent);



            }
        });
        return listV;

    }
}

