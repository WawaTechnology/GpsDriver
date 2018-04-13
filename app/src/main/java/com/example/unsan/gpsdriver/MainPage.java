package com.example.unsan.gpsdriver;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Unsan on 12/4/18.
 */

public class MainPage extends AppCompatActivity implements View.OnClickListener {

    ListView listView,alphaViewList;
    DatabaseReference customerReference;
    public static boolean sorted;
    Map<String, Integer> mapIndex;



    List<CustomerNode> customerList;
    CustomAdapter customAdapter;
    ProgressBar pgbar;



    ArrayAdapter<String> arrayAdapter;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        pgbar=(ProgressBar) findViewById(R.id.pgbar);

        FirebaseDatabase fbd=FirebaseDatabase.getInstance();
        //fbd.setPersistenceEnabled(true);


        customerList=new ArrayList<>();
        listView=(ListView) findViewById(R.id.list_view);
        customerReference=fbd.getReference("Customer");



        customAdapter=new CustomAdapter(MainPage.this,R.layout.simple_display,customerList);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        pgbar.setVisibility(View.VISIBLE);
        getCustomerData();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
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


        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed()
    {
        super.onBackPressed();

    }

    private void getCustomerAddress() {
        Log.d("chksee","we are here");
        sorted =true;
        customerList.clear();
        customerReference.orderByChild("Address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String key=ds.getKey();
                    Log.d("checkkey",key);
                    Customer c=ds.getValue(Customer.class);
                    CustomerNode customerNode=new CustomerNode(key,c);
                    customerList.add(customerNode);
                    pgbar.setVisibility(View.GONE);
                    customAdapter.notifyDataSetChanged();
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

    }

    private void getCustomerData() {
        Log.d("chksee","we are here");
        sorted=false;
        customerList.clear();
        customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("checkk","here");

                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String key=ds.getKey();
                    Log.d("checkkey",key);
                    Customer c=ds.getValue(Customer.class);
                    CustomerNode customerNode=new CustomerNode(key,c);
                    customerList.add(customerNode);
                    customAdapter.notifyDataSetChanged();
                }
                getIndexList(customerList);

                displayIndex();
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

    @Override
    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        listView.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}
