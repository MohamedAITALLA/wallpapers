package com.aitalla.wallpapersapp.listeners;

import com.aitalla.wallpapersapp.models.Wallpaper;

import java.util.ArrayList;

public interface OnWallpaperClickListener extends Listener{
    void onWallpaperClick(String startingPosition, String startingUrl,ArrayList<Wallpaper> range);
}
