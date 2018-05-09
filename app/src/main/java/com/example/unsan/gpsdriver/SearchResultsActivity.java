package com.example.unsan.gpsdriver;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unsan on 12/4/18.
 */

public class SearchResultsActivity extends AppCompatActivity {
    ListView lvw;
    TextView tvw;

    List<CustomerEngChinese> list;
    ArrayAdapter<CustomerEngChinese> listAdapter;
    SharedPreferences sharedPreferences;
    DatabaseReference restaurantReference;
    String cNumber;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        lvw=(ListView)findViewById(R.id.listview);
        tvw=(TextView)findViewById(R.id.noResult);

        list=new ArrayList<>();
        sharedPreferences=getSharedPreferences("location_driver", Context.MODE_PRIVATE);
      cNumber= sharedPreferences.getString("carNumber",null);
        String vNumber=sharedPreferences.getString("vehicleNumber",null);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restaurantReference= FirebaseDatabase.getInstance().getReference("CarsDb");
        listAdapter = new CustomAdapter(this, R.layout.searchtext, list);
        lvw.setAdapter(listAdapter);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem=menu.findItem(R.id.searchi);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchItem.expandActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(final String query) {
                list.clear();
                restaurantReference.child(cNumber).child("Restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            String key=ds.getKey();
                            Object valueobj=ds.getValue(Object.class);
                            String value=valueobj.toString();
                            if(key.toLowerCase().contains(query.toLowerCase())||value.toLowerCase().contains(query.toLowerCase()))

                            {


                                CustomerEngChinese customerEngChinese=new CustomerEngChinese(key,value);
                                list.add(customerEngChinese);
                                ;
                                listAdapter.notifyDataSetChanged();
                                tvw.setVisibility(View.GONE);
                            }

                        }
                        if(list.isEmpty())
                        {
                            tvw.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}