package com.example.unsan.gpsdriver;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.List;

/**
 * Created by Unsan on 25/4/18.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.MyViewHolder> {
    Context context;
    List<String> imgList;
    ImageRecyclerAdapter(Context context,List<String> imgList)
    {
        this.context=context;
        this.imgList=imgList;
    }
    @Override
    public ImageRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.image_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageRecyclerAdapter.MyViewHolder holder, int position) {
       String pos= imgList.get(position);
       Log.d("imagelist",pos);
      //  Uri photoUri = Uri.fromFile( new File(pos));
     /*   Glide.with(context)
                .load(pos).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                Log.d("glideexception",e.getMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }). // Uri of the picture
    into(holder.imgView);
    */
     holder.locText.setText("image "+position+1);









    }

    @Override
    public int getItemCount() {
        Log.d("ssize",imgList.size()+"");
        return imgList.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        //ImageView imgView;
        TextView locText;


        public MyViewHolder(View itemView) {
            super(itemView);
            //imgView=(ImageView) itemView.findViewById(R.id.imgv);
            locText=(TextView)itemView.findViewById(R.id.loc);

        }
    }
}
