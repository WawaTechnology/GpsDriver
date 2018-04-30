package com.example.unsan.gpsdriver;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Unsan on 12/4/18.
 */

public class CustomerDetail extends AppCompatActivity  {

    TextView name, phone, address, restName, zipText;
    final int REQUEST_CHECK_SETTINGS = 125;
    Button reached;
    Customer c;
    String timestart, date;


    String carNumber;


    FirebaseDatabase fbdr;
    String mAddressOutput;

    double dlat, dlng;
    DatabaseReference startReference;
    double longitude, latitude;
    String startAddress;
    LocationManager lm;
    protected Location mLastLocation;


    List<String> carList;
    CustomerNode cn;


    public static String REQUESTING_LOCATION_UPDATES_KEY = "location_upd";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail);




        carList=new ArrayList<>();


        restName=(TextView) findViewById(R.id.rest_name);
        zipText=(TextView) findViewById(R.id.zip);

        fbdr = FirebaseDatabase.getInstance();
        startReference = fbdr.getReference("Started");

        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);

        reached = (Button) findViewById(R.id.reached);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        for(int i=1;i<=30;i++)
        {
            carList.add("car "+i);

        }
        Intent intent = getIntent();
        cn = (CustomerNode) intent.getSerializableExtra("customernd");
        c=cn.getCustomer();
        name.setText(c.getContactPerson());
        phone.setText(c.getContactNumber()+"");
        address.setText(c.getAddress());

        restName.setText(cn.getRestaurantName());
        zipText.setText(c.getZip()+"");












        reached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
               // Intent intent = new Intent(CustomerDetail.this, DestinationActivity.class);
                Intent intent = new Intent(CustomerDetail.this, DeliveredActivity.class);
                intent.putExtra("customerds", cn);
                intent.putExtra("carNum",carNumber);


                startActivity(intent);
            }
        });



    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode > 0) {

                }
            }
        }
    }
    @Override
    public void onBackPressed()
    {


        super.onBackPressed();
    }






}
