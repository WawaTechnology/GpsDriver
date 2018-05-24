package com.example.unsan.gpsdriver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

/**
 * Created by Unsan on 25/4/18.
 */

public class DeliveredActivity extends AppCompatActivity {
    ImageButton takeButton;
    Button submit;
    int RC_PHOTO_PICKER = 123;
    ImageView imgview;
    Customer customer;

    FirebaseStorage firebaseStorage;
    FirebaseDatabase fbd;
    ProgressDialog progressDialog;
    TextView name, phone, addressTextView, restName, zipText;
    DatabaseReference customerDeliveredReference;

    private CustomerSqlite customerSqlite;


    DatabaseReference destinationReference;


    String carNumber;


    String startAddress;
    NetworkInfo networkInfo;
    DatabaseReference driverDayDelivery;


    private StorageReference storageReference;
 Uri capturedImageUri = null;
    String mCurrentPhotoPath;

    String dateString;

    double longitude, latitude;
    String address;

    String startTime;
    Uri downloadUrl;
    String node;
    String driverName;
    String customerchinese;
    String vehicleNum;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("HH a");
    Date todayDate = new Date();
    String thisDate ;
    String hour;
    List<String> hourList;
    String hh1;

    SharedPreferences sharedPreferences;

    private String gpsDestAddress;
    File photoFile;
   // FusedLocationProviderClient mFusedLocationProviderClient;
    private BroadcastReceiver br = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    Toast.makeText(DeliveredActivity.this, "No Connectivity", Toast.LENGTH_LONG).show();
                }

            }
        }
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeliveredActivity.this, MainPage.class);
        startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_layout);
        customerSqlite = new CustomerSqlite(this);




        takeButton = (ImageButton) findViewById(R.id.imgbut);


        submit = (Button) findViewById(R.id.submit);
        imgview = (ImageView) findViewById(R.id.imgv);
        restName = (TextView) findViewById(R.id.rest_name);
        zipText = (TextView) findViewById(R.id.zip);


        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        addressTextView = (TextView) findViewById(R.id.address);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("destination_photo");
        sharedPreferences = getSharedPreferences("location_driver", Context.MODE_PRIVATE);
        driverName = sharedPreferences.getString("dname", null);
        carNumber = sharedPreferences.getString("carNumber", null);
        vehicleNum=sharedPreferences.getString("vehicleNumber",null);



        fbd = FirebaseDatabase.getInstance();


        destinationReference = fbd.getReference("DriverDelivery");
        driverDayDelivery=fbd.getReference("driverDayRecord");
        customerDeliveredReference=fbd.getReference("CustomerTodayRecord");
      //  customerReference=fbd.getReference("Customer");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(br, intentFilter);


        // globalProvider=GlobalProvider.getGlobalInstance(DeliveredActivity.this);


        Intent intent = getIntent();
         customer= (Customer) intent.getSerializableExtra("customernd");
        customerchinese =intent.getStringExtra("restName");
        if(customer!=null) {
            name.setText(customer.getContactPerson() + "");
            phone.setText(customer.getContactNumber() + "");
            addressTextView.setText(customer.getAddress());

            restName.setText(customerchinese);
            zipText.setText(customer.getZip() + "");
        }
        else
        {
            Toast.makeText(DeliveredActivity.this,"Please press back button and try again!",Toast.LENGTH_LONG).show();
        }
        hour=simpleDateFormat1.format(todayDate);
        hh1=hour.substring(3);
        thisDate=simpleDateFormat.format(todayDate);


        hourList=new ArrayList<>();
        hourList.add("00 am");
        hourList.add("01 am");
        hourList.add("02 am");
        hourList.add("03 am");
        hourList.add("04 am");
        hourList.add("05 am");




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Commenting code for gpslocation
        /*

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            gpsDestAddress="GPS Problem";

            return;
        }
        else {


            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            //TODO implement this functionality on Button's click

            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d("loclistener", "called");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    convertAddress();


                }
            });
        }
        */


        // customer=globalProvider.getCustomer();
        // startAddress=globalProvider.getStartingAddress();
        //  node=globalProvider.getNode();




        // customer= sharedPreferences.getString("Customer",null);


        // String car=customer.carNumber.trim();






        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkInfo == null) {
                    Toast.makeText(DeliveredActivity.this, "Action cannot be perfomed,Please turn on Internet ", Toast.LENGTH_LONG).show();
                } else {


                    //  capturedImageUri= FileProvider.getUriForFile(DeliveredActivity.this, getApplicationContext().getPackageName() + ".com.example.unsan.gpstracker.GenericFileProvider", file);
                    //capturedImageUri = Uri.fromFile(file);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // intent.setType("image/jpeg");
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri uri = FileProvider.getUriForFile(DeliveredActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                            // Uri uri = Uri.fromFile(new File(path));


                        /*Uri photoURI = FileProvider.getUriForFile(DeliveredActivity.this,
                                "com.example.unsan.gpstracker.GenericFileProvider",
                                photoFile);
                                */
                            capturedImageUri = Uri.fromFile(photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivityForResult(takePictureIntent, RC_PHOTO_PICKER);
                        }
                    }
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(capturedImageUri==null)
                {
                    Toast.makeText(DeliveredActivity.this,"Please take an image first!",Toast.LENGTH_LONG).show();
                }
                else {


                    long date = System.currentTimeMillis();
                    SimpleDateFormat sdt = new SimpleDateFormat("HH:mm");

                    dateString = simpleDateFormat.format(date);

                    String desttime = sdt.format(date);
                    long tm = -1 * new Date().getTime();

                    // Delivery delivery = new Delivery(tm,startTime, desttime, dateString, downloadUrl.toString(), customerN.getRestaurantName(), customer.Address, startAddress, carNumber, "name1", gpsDestAddress);
                    DriverDelivery deliveryDriver = new DriverDelivery(desttime, dateString, tm, customerchinese, customer.address, carNumber,vehicleNum, driverName);
                    node = destinationReference.push().getKey();
                    destinationReference.child(node).setValue(deliveryDriver);
                    customerSqlite.insertContact(node, capturedImageUri.toString());
                    Cursor cursor = customerSqlite.getData(node);
                    if (cursor != null)
                        cursor.moveToFirst();
                    String urladd = cursor.getString(1);
                    Log.d("urlsaved", urladd);
                    customerSqlite.close();

                        if(hourList.contains(hour))
                        {
                            thisDate=getYesterdayDateString();
                            driverDayDelivery.child(thisDate).child(node).setValue("delivered");
                            customerDeliveredReference.child(thisDate).child(carNumber).child(customerchinese).setValue(desttime);
                            Intent intet = new Intent(DeliveredActivity.this, MainPage.class);
                            startActivity(intet);

                        }
                        else {


                            driverDayDelivery.child(thisDate).child(node).setValue("delivered");
                            customerDeliveredReference.child(thisDate).child(carNumber).child(customerchinese).setValue(desttime );
                            Intent intet = new Intent(DeliveredActivity.this, MainPage.class);
                            startActivity(intet);
                        }








                }
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
    /*

    private void getCustomerData(final String customerchinese) {
        Log.d("checkchinesesize",customerchinese.length()+"");
        customerReference.child(customerchinese).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Log.d("existd","true");
                }
                customer=dataSnapshot.getValue(Customer.class);
                if(customer==null)
                {
                    Log.d("nullcustomer","null");

                }
                else {
                    name.setText(customer.getContactPerson() + "");
                    phone.setText(customer.getContactNumber() + "");
                    addressTextView.setText(customer.getAddress());

                    restName.setText(customerchinese);
                    zipText.setText(customer.getZip() + "");
                    prgBar.setVisibility(GONE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    */
    //comment convertDDRESS
    /*

    private void convertAddress() {
        Geocoder geocoder = new Geocoder(DeliveredActivity.this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            gpsDestAddress = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addressList.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("catch", "Could not get address....!");
        }
    }
    */



    public void onActivityResult(int requestcode, int resultcode, Intent data) {

        if (requestcode == RC_PHOTO_PICKER && resultcode == RESULT_OK) {

            //  final Uri SelectedImageUri = data.getData();

            Uri uri = capturedImageUri;
            Log.d("uripic",capturedImageUri.getLastPathSegment());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap myImg = BitmapFactory.decodeFile(uri.getPath(),options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            myImg.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();

            imgview.setImageBitmap(myImg);
            Toast.makeText(DeliveredActivity.this,"Photo Saved!",Toast.LENGTH_SHORT).show();






/*            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgview.setImageBitmap(imageBitmap);
            */

//            Log.d("checkim",SelectedImageUri.toString());


            /*
            photoref.putFile(capturedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     downloadUrl = taskSnapshot.getDownloadUrl();
                     Log.d("checkurld",downloadUrl.toString());
                     pgbar.setVisibility(View.GONE);
                }
            });
            */


            // encodeBitmapAndSaveToFirebase(imageBitmap);

        }
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
    public void onDestroy()
    {
        super.onDestroy();
        if(br!=null)
            unregisterReceiver(br);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
