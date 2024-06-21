package com.aitalla.wallpapersapp.models;

import java.io.Serializable;

public class Wallpaper implements Serializable {

    private String url;

    private boolean premium;

    public Wallpaper(){

    }

    public Wallpaper(String url, boolean premium) {
        this.url = url;
        this.premium = premium;
    }

    public boolean isPremium() {
        return premium;
    }
    public String getUrl() {
        return url;
    }
    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Wallpaper{" +
                "url='" + url + '\'' +
                ", premium=" + premium +
                '}';
    }
}
