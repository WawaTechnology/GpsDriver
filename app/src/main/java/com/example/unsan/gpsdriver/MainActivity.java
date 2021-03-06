package com.example.unsan.gpsdriver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2;
    Button submit;
    ImageView deleteEmail,deletePsd;
    private static final int REQUEST = 112;
    private static final int PERMISSION_REQUEST_CODE=124;
    final static String[] PERMISSIONS={Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    SharedPreferences sharedPreferences;
    DatabaseReference loginReference;
    boolean matched;
    List<String> HistoryList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.email);
        ed2=(EditText)findViewById(R.id.psd);
        submit=(Button)findViewById(R.id.submit);
        deleteEmail=(ImageView)findViewById(R.id.deleteEmail);
        deletePsd=(ImageView)findViewById(R.id.deletePsd);
        deleteEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed1.setText("");
            }
        });
        deletePsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed2.setText("");
            }
        });
        sharedPreferences=getSharedPreferences("location_driver", Context.MODE_PRIVATE);
        boolean val=sharedPreferences.getBoolean("isLogin",false);
        Log.d("getb",""+val);
        HistoryList=new ArrayList<>();

        if(sharedPreferences.getBoolean("isLogin",false))
        {
            Intent intent =new Intent(MainActivity.this,MainPage.class);
            startActivity(intent);
        }
        loginReference=FirebaseDatabase.getInstance().getReference("DriverRecord");


        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionGpsCheck=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        // Log.d("checkpermission"," "+permissionCheck);
        if(permissionCheck==-1||permissionGpsCheck==-1)
        {
            requestPermission();
        }
        try {
            getHistoryList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed1.getText().length()<=0||ed2.getText().length()<=0)
                {
                    Toast.makeText(MainActivity.this,"Email and Password can not be empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    checkLoginDb();

                }
            }
        });

    }

    private void checkLoginDb() {
        loginReference.child(ed2.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                   String email= dataSnapshot.getValue(String.class);
                   if(email.equals(ed1.getText().toString()))
                   {
                       matched=true;
                       SharedPreferences.Editor editor=sharedPreferences.edit();
                       editor.putBoolean("isLogin",true);
                       editor.putString("email",email);

                       editor.commit();
                       try {
                           setHistoryList();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       Intent intent =new Intent(MainActivity.this,MainPage.class);
                       //Intent intent =new Intent(MainActivity.this,TestActivity.class);
                       startActivity(intent);

                   }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Password or email incorrect",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void getHistoryList() throws IOException
    {
        FileInputStream fileInputStream=new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/gpsDriver.txt");
        InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
        BufferedReader br=new BufferedReader(inputStreamReader);
        String s;

        while((s=br.readLine())!=null)
        {
            HistoryList.add(s);
        }
        if(HistoryList.size()>0) {
            ed1.setText(HistoryList.get(0));
            ed2.setText(HistoryList.get(1));
        }
    }
    private void setHistoryList() throws IOException {

        FileOutputStream fileOutputStream=new FileOutputStream(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/gpsDriver.txt");
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream);
        BufferedWriter bw=new BufferedWriter(outputStreamWriter);
        bw.write("");
        bw.write(ed1.getText().toString());
        bw.newLine();
        bw.write(ed2.getText().toString());
        bw.newLine();
        bw.close();
        outputStreamWriter.close();
        fileOutputStream.close();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean locationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writePermission && locationPermission) {


                    } else {




                    }
                }
                break;
        }
    }
    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                ActivityCompat.shouldShowRequestPermissionRationale
                        (MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){



        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }
}
