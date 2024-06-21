package com.aitalla.wallpapersapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WallpaperDAO {
    @Insert
    void addWallpaper(FavWallpaper wallpaper);

    @Update
    void updateWallpaper(FavWallpaper wallpaper);

    @Delete
    void deleteWallpaper(FavWallpaper wallpaper);

    @Query("SELECT * FROM Wallpaper")
    List<FavWallpaper> getAllWallpapers();

    @Query("SELECT * FROM Wallpaper WHERE wid = :wid LIMIT 1")
    FavWallpaper getWallpaper(int wid);

    @Query("SELECT * FROM Wallpaper WHERE url = :url LIMIT 1")
    FavWallpaper getWallpaperByUrl(String url);
}
