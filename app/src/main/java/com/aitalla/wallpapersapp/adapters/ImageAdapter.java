package com.aitalla.wallpapersapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.ui.activities.DownloadsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    Activity activity;
    List<File> stringList;
    String arrayType;

    public ImageAdapter(Activity activity, List<File> stringList, String arrayType) {
        this.activity = activity;
        this.stringList = stringList;
        this.arrayType = arrayType;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.view_wallpaper,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Glide.with(activity).load(stringList.get(position).toString()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wallpaper);
        }
    }
}
