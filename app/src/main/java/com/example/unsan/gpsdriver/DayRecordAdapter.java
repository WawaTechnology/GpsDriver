package com.example.unsan.gpsdriver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Unsan on 2/5/18.
 */

public class DayRecordAdapter extends RecyclerView.Adapter<DayRecordAdapter.MyViewHolder> {
    Context context;

    List<String> imgList;
    List<DriverDelivery> driverDeliveryList;
    DayRecordAdapter(Context context,List<String> imgList,List<DriverDelivery> driverDeliveryList)
    {
        this.context=context;
        this.imgList=imgList;
        this.driverDeliveryList=driverDeliveryList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.today_record,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DriverDelivery todayDriverObject=driverDeliveryList.get(position);
        Log.d("checkdeliverysize",driverDeliveryList.size()+"");
        Log.d("imgssize",imgList.size()+"");
        holder.custName.setText(todayDriverObject.customer);
        holder.deliveryTime.setText(todayDriverObject.deliveryTime);

      //  if(imgList.size()>position) {
        try {
            String img = imgList.get(position);

            Glide.with(context).load(img).error(R.drawable.dff).into(holder.imgView);
        }
        catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return driverDeliveryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView custName;
        TextView deliveryTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgView=(ImageView)itemView.findViewById(R.id.day_image);
            custName=(TextView)itemView.findViewById(R.id.customer_name);
            deliveryTime=(TextView)itemView.findViewById(R.id.delivery_time);

        }
    }
}
