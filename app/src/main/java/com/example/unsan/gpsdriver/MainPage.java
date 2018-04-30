package com.example.unsan.gpsdriver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * Created by Unsan on 12/4/18.
 */

public class MainPage extends AppCompatActivity implements View.OnClickListener {

    ListView listView,alphaViewList;
    DatabaseReference customerReference,driverDataRef;
    public static boolean sorted;
    Map<String, Integer> mapIndex;
    String email;
ValueEventListener valueEventListener,dvEventListner;


    List<CustomerNode> customerList;
    CustomAdapter customAdapter;
    ProgressBar pgbar;
    SharedPreferences sharedPreferences;
    ActionBar actionBar;
    String driverName,carNumber;




    ArrayAdapter<String> arrayAdapter;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Log.d("tag","oncreate");
        pgbar=(ProgressBar) findViewById(R.id.pgbar);

        FirebaseDatabase fbd=FirebaseDatabase.getInstance();
        driverDataRef=fbd.getReference("Driver");



      actionBar=getSupportActionBar();
      actionBar.setDisplayShowTitleEnabled(false);




        //fbd.setPersistenceEnabled(true);


        customerList=new ArrayList<>();
        listView=(ListView) findViewById(R.id.list_view);
        customerReference=fbd.getReference("Customer");
        customerReference.keepSynced(true);



        customAdapter=new CustomAdapter(MainPage.this,R.layout.simple_display,customerList);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        pgbar.setVisibility(View.VISIBLE);





    }
    public void onStart()
    {
        super.onStart();


        getCustomerData();
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

        sharedPreferences=getSharedPreferences("location_driver", Context.MODE_PRIVATE);
        email=sharedPreferences.getString("email",null);
        driverName=sharedPreferences.getString("dname",null);
        carNumber=sharedPreferences.getString("carNumber",null);
        menu.findItem(R.id.name).setTitle(driverName);
        menu.findItem(R.id.carNumber).setTitle("car Number: "+carNumber);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.searchid: {
                Intent intent = new Intent(MainPage.this, SearchResultsActivity.class);
                startActivity(intent);
                break;


            }
            case R.id.sortadd:
            {
                getCustomerAddress();
                break;

            }
            case R.id.sortrest:
            {
                getCustomerData();
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


                editor.putString("carNumber",null);
                editor.putString("carInfo",null);

                editor.commit();
            Intent intent=new Intent(MainPage.this,MainActivity.class);
            startActivity(intent);

            }


        }
        return super.onOptionsItemSelected(item);
    }


    private void getCustomerAddress() {
        Log.d("chksee","we are here");
        sorted =true;

        customerReference.orderByChild("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customerList.clear();
                if(dataSnapshot.hasChildren()) {

                    pgbar.setVisibility(View.INVISIBLE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        Log.d("checkkey", key);
                        Customer c = ds.getValue(Customer.class);
                        CustomerNode customerNode = new CustomerNode(key, c);
                        customerList.add(customerNode);



                    }
                    customAdapter.notifyDataSetChanged();

                }
                else {
                    pgbar.setVisibility(GONE);
                    Toast.makeText(MainPage.this, "No record found", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onResume()
    {
        super.onResume();
        Log.d("tag","onresume");

    }

    private void getCustomerData() {
        Log.d("chksee","we are here");
        sorted=false;

       valueEventListener= customerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("checkk","here");
                customerList.clear();
                if(dataSnapshot.hasChildren()) {


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        Log.d("checkkey", key);
                        Customer c = ds.getValue(Customer.class);
                        CustomerNode customerNode = new CustomerNode(key, c);
                        customerList.add(customerNode);
                        customAdapter.notifyDataSetChanged();
                    }
                    pgbar.setVisibility(GONE);
                    getIndexList(customerList);

                    displayIndex();
                }
                else
                {
                    Toast.makeText(MainPage.this,"No records found!",Toast.LENGTH_LONG).show();
                    pgbar.setVisibility(GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("dberror",databaseError.getMessage());

            }
        });


    }

    private void getIndexList(List<CustomerNode> customerList) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < customerList.size(); i++) {
            CustomerNode cn=customerList.get(i);
            String restName=cn.restaurantName;

            String index = restName.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }

    }
    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }
    public void onPause()
    {
        super.onPause();
        Log.d("tag","pasue");
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

    @Override
    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        listView.setSelection(mapIndex.get(selectedIndex.getText()));
    }
    public void onDestroy()
    {
        super.onDestroy();

    }
}
