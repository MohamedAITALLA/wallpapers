package com.aitalla.wallpapersapp.models;

public class Category {
    private String imageUrl;
    private String name;
    private String keywords;

    public Category(String imageUrl, String name, String keywords) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.keywords = keywords;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getName() {
        return name;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "Category{" +
                "imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
