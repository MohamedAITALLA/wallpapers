package com.aitalla.wallpapersapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aitalla.wallpapersapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    ImageView imageView;

    CardView cardView;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.cat_text);
        imageView = itemView.findViewById(R.id.cat_image);
        cardView = itemView.findViewById(R.id.cat_container);
    }
}
