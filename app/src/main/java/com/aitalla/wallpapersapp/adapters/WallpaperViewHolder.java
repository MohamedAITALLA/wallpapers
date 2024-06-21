package com.aitalla.wallpapersapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aitalla.wallpapersapp.R;

public class WallpaperViewHolder extends RecyclerView.ViewHolder{
    ImageView wallpaper;
    LinearLayout l1_premium, mainLinear;
    public WallpaperViewHolder(@NonNull View itemView) {
        super(itemView);
        wallpaper = itemView.findViewById(R.id.wallpaper);
        l1_premium = itemView.findViewById(R.id.l1_premium);
        mainLinear = itemView.findViewById(R.id.mainLinear);
    }
}
