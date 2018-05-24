package com.example.unsan.gpsdriver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * Created by Unsan on 12/4/18.
 */

public class MainPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listView,alphaViewList;
    DatabaseReference customerReference,driverDataRef;

    Map<String, Integer> mapIndex;
    String email;
    DatabaseReference driverCarDetails;
ValueEventListener valueEventListener,dvEventListner;

    int index=0;
    boolean sortPressed;







    List<CustomerEngChinese> customerList;

    CustomAdapter customAdapter;
    ProgressBar pgbar;
    SharedPreferences sharedPreferences;
    ActionBar actionBar;
    String driverName,carNumber;
    Spinner spinner,spinner2;
   // List<String> carNumbers;
   // List<String> vehicleNumbers;
    String vehicleNumber;





    ArrayAdapter<String> arrayAdapter,vehicleadapter;
    public static final String STATE_POSITION="LIST_POSITION";

    private int currentPosition;
    private DatabaseReference customerTodayReference;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Log.d("tag","oncreate");
        pgbar=(ProgressBar) findViewById(R.id.pgbar);
        spinner=(Spinner) findViewById(R.id.sp1);
        spinner2=(Spinner) findViewById(R.id.sp2);
       // vehicleNumbers=new ArrayList<>();
        FirebaseDatabase fbd=FirebaseDatabase.getInstance();
        driverCarDetails=fbd.getReference("driverCarDb");


        driverDataRef=fbd.getReference("Driver");
        //customerTodayReference = fbd.getReference("CustomerTodayRecord");







      actionBar=getSupportActionBar();
      actionBar.setDisplayShowTitleEnabled(false);







        //fbd.setPersistenceEnabled(true);


        customerList=new ArrayList<>();
        listView=(ListView) findViewById(R.id.list_view);

       // customerReference=fbd.getReference("CarsDb");
        customerReference=fbd.getReference("carsRecord");

        //carNumbers=new ArrayList<>();
        //ADD vehicle number
       // addVehicleNumbers();
       /* for(int i=1;i<=20;i++)
        {
            carNumbers.add("car "+i);

        }
        */
        sharedPreferences=getSharedPreferences("location_driver", Context.MODE_PRIVATE);
       carNumber= sharedPreferences.getString("carNumber",null);
        String vNumber=sharedPreferences.getString("vehicleNumber",null);
        arrayAdapter=new ArrayAdapter<String>(MainPage.this,R.layout.spinner_item,getResources().getStringArray(R.array.carArray));
        vehicleadapter=new ArrayAdapter<String>(MainPage.this,R.layout.spinner_item,getResources().getStringArray(R.array.vehicleNames));


        spinner.setAdapter(arrayAdapter);
        if(carNumber!=null)
        {
            int spinnerPosition = arrayAdapter.getPosition(carNumber);
            spinner.setSelection(spinnerPosition);
        }
        else
        {
            carNumber=arrayAdapter.getItem(0);
            spinner.setSelection(0);
        }

        spinner.setOnItemSelectedListener(MainPage.this);

        spinner2.setAdapter(vehicleadapter);
        if(vNumber!=null)
        {
            int spinnerPos = arrayAdapter.getPosition(vNumber);
            spinner2.setSelection(spinnerPos);
        }
        else {
            spinner2.setSelection(0);
        }
        spinner2.setOnItemSelectedListener(MainPage.this);



        customAdapter=new CustomAdapter(MainPage.this,R.layout.simple_display,customerList);
        listView.setAdapter(customAdapter);
        if (savedInstanceState != null) {
            Log.d("tag","saved");
            // Restore value of members from saved state and populare your RecyclerView again
           int pos= savedInstanceState.getInt(STATE_POSITION);
            listView.setSelection(pos);

        }






        pgbar.setVisibility(View.VISIBLE);
        getCustomerData();







    }

   /* private void addVehicleNumbers() {
        vehicleNumbers.add("YP2095H");
        vehicleNumbers.add("GBF6032M");
        vehicleNumbers.add("GBD498C");
        vehicleNumbers.add("GBD5898Z");
        vehicleNumbers.add("GBG9001C");
        vehicleNumbers.add("GBG3898X");
        vehicleNumbers.add("GBC7432B");
        vehicleNumbers.add("GBG7570P");


    }
    */


    public void onStart()
    {
        super.onStart();
        Log.d("tag","onstart");



       // getDriverDetail();
    }


    private void getDriverDetail() {
       dvEventListner= driverDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                 Driver driver= ds.getValue(Driver.class);
                String emaild= driver.getEmail();
                if(emaild.equals(email))
                {
                   long phone= driver.getPhone();
                  String name= ds.getKey();
                  Log.d("getvalues",phone +" "+name);
                  SharedPreferences.Editor editor=sharedPreferences.edit();
                  editor.putString("dname",name);
                  editor.putLong("phone",phone);
                  editor.commit();

                }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
      /*  TextView tv = new TextView(this);
        String text="car 5";
        tv.setText(text);
        tv.setTextColor(getResources().getColor(R.color.WHITE));
        tv.setOnClickListener(this);
        tv.setPadding(5, 0, 55, 0);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(14);
        menu.add(0, 0, 1, text).setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        */
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

       //get from source


        email=sharedPreferences.getString("email",null);
        driverName=sharedPreferences.getString("dname",null);

        menu.findItem(R.id.name).setTitle(driverName);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sort:
            {
               sortPressed=true;
               //sortDeliveryOrder();
            }
            case R.id.refresh:
            {
                getCustomerData();
                break;
            }
            case R.id.searchid: {
                Intent intent = new Intent(MainPage.this, SearchResultsActivity.class);
                startActivity(intent);
                break;


            }


            case R.id.show_deliveries:
            {
                Intent intent=new Intent(MainPage.this,ShowImagesActivity.class);
                startActivity(intent);
                break;
            }
           case  R.id.logout:
            {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("isLogin",false);
                editor.putString("dname",null);

                editor.putString("email",null);



                editor.commit();
            Intent intent=new Intent(MainPage.this,MainActivity.class);
            startActivity(intent);

            }


        }
        return super.onOptionsItemSelected(item);
    }
/*
    private void sortDeliveryOrder() {
        if(hourList.contains(hour))
        {
            thisDate=getYesterdayDateString();
        }
        customerTodayReference.child(thisDate).child(carNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customerList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getValue(String.class).equals("Ordered"))
                    {
                       final String key=ds.getKey();
                        customerReference.orderByChild("chinese").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                               CustomerEngChinese cs=dataSnapshot.getValue(CustomerEngChinese.class);
                               if(cs.getChinese().equals(key))
                               {
                                   customerList.add(cs);
                                   customAdapter.notifyDataSetChanged();
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
    */





    public void onResume()
    {
        super.onResume();
        Log.d("tag","onresume");








    }

    private void getCustomerData() {
        Log.d("chksee","we are here");
        customerList.clear();
        Log.d("checkcarN",carNumber);
        customAdapter.notifyDataSetChanged();


      // valueEventListener= customerReference.child(carNumber).child("Restaurants").addValueEventListener(new ValueEventListener() {
        valueEventListener=customerReference.child(carNumber).addValueEventListener(new ValueEventListener() {
           @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("checkk","here");
               // customerList.clear();
                if(dataSnapshot.hasChildren()) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("ckk",ds.getKey());


                            CustomerEngChinese customerEngChinese= ds.getValue(CustomerEngChinese.class);
                            Log.d("chinesename",customerEngChinese.chinese+"");




                        customerList.add(customerEngChinese);

                        customAdapter.notifyDataSetChanged();
                    }
                    pgbar.setVisibility(GONE);

                }
                else
                {
                    Toast.makeText(MainPage.this,getString(R.string.no_result),Toast.LENGTH_LONG).show();
                    pgbar.setVisibility(GONE);
                    customAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("dberror",databaseError.getMessage());

            }
        });


    }



    public void onPause()
    {
        super.onPause();
        Log.d("tag","pasue");
       currentPosition = listView.getFirstVisiblePosition();






    }
    public void onStop()
    {
        super.onStop();
        Log.d("tag","stop");
        if(valueEventListener!=null)
            Log.d("destroy called","removing listener");
        customerReference.removeEventListener(valueEventListener);
        if(dvEventListner!=null)
            driverDataRef.removeEventListener(dvEventListner);


    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
// Save the user's current game state
       // savedInstanceState.putInt(STATE_POSITION, );

        Log.d("tag","savedInstanceState");
        savedInstanceState.putInt(STATE_POSITION,currentPosition);
        super.onSaveInstanceState(savedInstanceState);

// Always call the superclass so it can save the view hierarchy state
         }
    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }

    public void onRestart()
    {
        super.onRestart();
        Log.d("tag","restart");
    }


    public void onDestroy()
    {
        super.onDestroy();
        Log.d("tag","destroy");

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) { //Run Code For Major Spinner
            case R.id.sp1: { // code for first spinner. Depending on spinner.getselecteditem assign adapter to second spinner
                carNumber = adapterView.getItemAtPosition(i).toString();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("carNumber", carNumber);
                editor.commit();
                getCustomerData();


                break;
            }
            case R.id.sp2: { // code for second spinner
                //Use get item selected and get selected item position
                vehicleNumber = adapterView.getItemAtPosition(i).toString();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("vehicleNumber", vehicleNumber);
                editor.commit();


                break;
            }
        }





    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
