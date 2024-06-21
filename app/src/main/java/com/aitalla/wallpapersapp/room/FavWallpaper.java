package com.aitalla.wallpapersapp.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Wallpaper", indices = {@Index(value = {"url"}, unique = true)})
public class FavWallpaper {

    @ColumnInfo(name = "wid")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "url")
    String url;

    @ColumnInfo(name = "premium")
    boolean premium;

    public FavWallpaper(String url, boolean premium) {
        this.url = url;
        this.premium = premium;
        this.id = 0;
    }

    @Ignore
    public FavWallpaper() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    @Ignore
    @Override
    public String toString() {
        return "FavWallpaper{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", premium=" + premium +
                '}';
    }
}
