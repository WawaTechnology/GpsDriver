package com.example.unsan.gpsdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Unsan on 25/4/18.
 */

public class ShowImagesActivity extends AppCompatActivity  {
    RecyclerView rcv;
    CustomerSqlite customerSqlite;
    List<String> imgList;
    List<String> imgKey;
    ImageRecyclerAdapter imageRecyclerAdapter;
    Button submitimgs;
    DatabaseReference imgDatabase;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    private Uri downloadUrl;
    Uri uri[];
    ValueEventListener valueEventListener;
    Button submitStatus,deleteImgs;
    boolean connected;
   // Button deleteImgs;



    public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.delivery_list);
    rcv=(RecyclerView)findViewById(R.id.list_images);
    submitimgs=(Button)findViewById(R.id.submit_imgs);
    submitStatus=(Button) findViewById(R.id.status);
  //  deleteImgs=(Button)findViewById(R.id.delete_imgs);
    customerSqlite=new CustomerSqlite(ShowImagesActivity.this);
    deleteImgs=(Button)findViewById(R.id.delete_imgs);
    imgList=new ArrayList<>();
    imgKey=new ArrayList<>();

    imgDatabase=FirebaseDatabase.getInstance().getReference("imgReferences");
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    storageReference = firebaseStorage.getReference().child("destination_photo");

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);





    imageRecyclerAdapter=new ImageRecyclerAdapter(ShowImagesActivity.this,imgList);
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ShowImagesActivity.this,LinearLayoutManager.VERTICAL,false);
    rcv.setLayoutManager(linearLayoutManager);
    rcv.setAdapter(imageRecyclerAdapter);
    Cursor cursor=customerSqlite.getData();
    int size=customerSqlite.numberOfRows();
    Log.d("ss",size+"");


    if (cursor.moveToFirst()) {

        do {


            String key=cursor.getString(0);
            String val=cursor.getString(1);
            imgList.add(val);
            imgKey.add(key);
            imageRecyclerAdapter.notifyDataSetChanged();


            Log.d("getn",key);
            Log.d("getval",val);
        } while (cursor.moveToNext());
        cursor.close();
    }
   /* deleteImgs.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Cursor cursor1=customerSqlite.getData();
            if(cursor1.moveToFirst())
            {
                do {
                    String k=cursor1.getString(0);
                    customerSqlite.deleteDelivery(k);




                    }while(cursor1.moveToNext());
                }
                cursor1.close();
            }
        });
        */


    uri=new Uri[imgList.size()];
    submitStatus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(ShowImagesActivity.this,DayRecordActivity.class);
            startActivity(intent);
        }
    });



    submitimgs.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if(imgList.size()<=0)
            {
                Toast.makeText(ShowImagesActivity.this,"No Photos Found!",Toast.LENGTH_LONG).show();
            }




            else {
                checkFirebaseConnection();
















            /*
            for(int i=0;i<imgList.size();i++) {


                progressDialog = new ProgressDialog(ShowImagesActivity.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Uploading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setCancelable(false);

                String uriString = imgList.get(i);
                Uri capturedImageUri = Uri.parse(uriString);
                Uri uri = capturedImageUri;
                Log.d("uripic", capturedImageUri.getLastPathSegment());

                Bitmap myImg = BitmapFactory.decodeFile(uri.getPath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                myImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();


                StorageReference photoref = storageReference.child(capturedImageUri.getLastPathSegment());

                UploadTask uploadTask = photoref.putBytes(b);
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        long l = taskSnapshot.getTotalByteCount();

                        Log.d("checkimg", l + "");
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //sets and increments value of progressbar
                        progressDialog.incrementProgressBy((int) progress);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("imageexception", exception.getMessage());
                        Toast.makeText(ShowImagesActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        progressDialog.dismiss();
                        Toast.makeText(ShowImagesActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                        downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("checkurld", downloadUrl.toString());


                    }
                });
            }

            Toast.makeText(ShowImagesActivity.this,"Upload Finished",Toast.LENGTH_LONG).show();
            */
            }


        }


    });
    deleteImgs.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i < imgList.size(); i++) {
              String img = imgList.get(i);
                Uri uris = Uri.parse(img);
             File file = new File(uris.getPath());
                Log.d("checkfs",file.getAbsolutePath());
                try
                {
                    if(file.exists())
                    {
                        boolean val= file.delete();
                        Log.d("filedeletedstatus",val+"");
                        imgList.remove(i);
                        imageRecyclerAdapter.notifyDataSetChanged();
                        customerSqlite.deleteDelivery(imgKey.get(i));
                    }
                    else
                    {
                        imgList.remove(i);
                        imageRecyclerAdapter.notifyDataSetChanged();
                        customerSqlite.deleteDelivery(imgKey.get(i));


                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }

        }
    });






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







public void checkFirebaseConnection()
{
    DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
    connectedRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            connected = snapshot.getValue(Boolean.class);
            if (connected) {
                for (int i = 0; i < imgList.size(); i++) {
                    final String img = imgList.get(i);
                    uri[i] = Uri.parse(img);
                    final  File file=new File(uri[i].getPath());
                    final String key = imgKey.get(i);
                    final int imageNum = i + 1;


                    StorageReference ref = storageReference.child(uri[i].getLastPathSegment());
                    ref.putFile(uri[i])
                            .addOnSuccessListener(ShowImagesActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    String content = downloadUrl.toString();
                                    if (content.length() > 0) {

                                        imgDatabase.child(key).setValue(content);
                                        customerSqlite.deleteDelivery(key);
                                        imgList.remove(img);
                                        imageRecyclerAdapter.notifyDataSetChanged();
                                        try
                                        {
                                            if(file.exists())
                                            {
                                                boolean val= file.delete();
                                                Log.d("filedeletedstatus",val+"");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(ShowImagesActivity.this, "image " + imageNum + " added", Toast.LENGTH_LONG).show();

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(ShowImagesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                }

            } else {
                Toast.makeText(ShowImagesActivity.this,"Connection Issue! Please try again later!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            System.err.println("Listener was cancelled");
        }
    });


}

public void onDestroy()
{
    super.onDestroy();
    customerSqlite.close();
}

public void onStart()
{
    super.onStart();

}
}
