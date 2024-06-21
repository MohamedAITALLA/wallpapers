package com.aitalla.wallpapersapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {FavWallpaper.class},version = 1)
public abstract class WallpapersDatabase extends RoomDatabase {
    public abstract WallpaperDAO getWallpaperDAO();

}
