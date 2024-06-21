package com.aitalla.wallpapersapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aitalla.wallpapersapp.R;

import com.aitalla.wallpapersapp.listeners.OnWallpaperClickListener;
import com.aitalla.wallpapersapp.models.Wallpaper;
import com.aitalla.wallpapersapp.utilities.Utilities;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    OnWallpaperClickListener listener;
    ArrayList<Wallpaper> wallpapers;
    ArrayList<Wallpaper> range;

    public WallpaperAdapter(Context context, ArrayList<Wallpaper> wallpapers, OnWallpaperClickListener listener) {
        this.context = context;
        this.wallpapers = wallpapers;
        this.range = new ArrayList<>();
        this.listener = listener;
    }

    private static final int ITEM_TYPE_WALLPAPER = 777;
    private static final int ITEM_TYPE_BANNER = 799;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case ITEM_TYPE_WALLPAPER:
                return new WallpaperViewHolder(LayoutInflater.from(context).inflate(R.layout.view_wallpaper,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ITEM_TYPE_WALLPAPER:
                Wallpaper selected = wallpapers.get(position);
                WallpaperViewHolder wallpaperViewHolder = (WallpaperViewHolder) holder;
                Glide.with(context).load(selected.getUrl()).into(wallpaperViewHolder.wallpaper);
                if(selected.isPremium()){
                    wallpaperViewHolder.l1_premium.setVisibility(View.VISIBLE);
                }
                else{
                    wallpaperViewHolder.l1_premium.setVisibility(View.GONE);
                }

                String startingPosition = String.valueOf(position);
                String startingUrl = selected.getUrl();

                wallpaperViewHolder.mainLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        range.clear();
                        int arraySize = wallpapers.size();
                        int positions = position + 10;
                        int increment = 10;

                        if(positions == arraySize){
                            for (int i = position; i < position + increment; i++) {
                                Wallpaper w = wallpapers.get(i);
                                range.add(new Wallpaper(
                                        w.getUrl(),
                                        w.isPremium()));

                            }
                        }
                        else if(positions < arraySize){
                            for (int i = position; i < position + increment; i++) {
                                Wallpaper w = wallpapers.get(i);
                                range.add(new Wallpaper(
                                        w.getUrl(),
                                        w.isPremium()));
                            }
                        }
                        else{
                            increment = arraySize - position;
                            for (int i = position; i < position + increment; i++) {
                                Wallpaper w = wallpapers.get(i);
                                range.add(new Wallpaper(
                                        w.getUrl(),
                                        w.isPremium()));
                            }
                        }

                        try{
                        //    Log.d("NEW RANGE LIST",new Gson().toJson(range));
                            listener.onWallpaperClick(startingPosition,startingUrl,range);
                        }catch (ClassCastException exception){
                            Utilities.toast((Activity) context,exception.getMessage());

                        }
                    }
                });
            case ITEM_TYPE_BANNER:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return wallpapers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(false)
             return ITEM_TYPE_BANNER;
        else return ITEM_TYPE_WALLPAPER;
    }
}

