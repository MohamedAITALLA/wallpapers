package com.aitalla.wallpapersapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BSItem implements Parcelable {

    private int image;
    private String text;

    public BSItem(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return (this.image);
    }

    public String getText() {
        return text;
    }

    protected BSItem(Parcel in) {
        this.image = in.readInt();
        this.text = in.readString();
    }

    public static final Creator<BSItem> CREATOR = new Creator<BSItem>() {
        @Override
        public BSItem createFromParcel(Parcel in) {
            return new BSItem(in);
        }

        @Override
        public BSItem[] newArray(int size) {
            return new BSItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.image);
        dest.writeString(this.text);
    }
}
