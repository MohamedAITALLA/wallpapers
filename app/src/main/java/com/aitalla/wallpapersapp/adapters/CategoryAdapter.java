package com.aitalla.wallpapersapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.ads.mintegral.Banner;
import com.aitalla.wallpapersapp.listeners.OnCategoryClickListener;
import com.aitalla.wallpapersapp.models.Category;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    OnCategoryClickListener listener;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, OnCategoryClickListener listener, ArrayList<Category> categories) {
        this.context = context;
        this.listener = listener;
        this.categories = categories;
    }

    private static final int ITEM_TYPE_CATEGORY = 789;
    private static final int ITEM_TYPE_BANNER = 766;
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_TYPE_CATEGORY:
                return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item,null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ITEM_TYPE_CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
                Glide.with(context).load(categories.get(position).getImageUrl()).into(categoryViewHolder.imageView);
                categoryViewHolder.textView.setText(categories.get(position).getName());
                categoryViewHolder.cardView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 480));
                categoryViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCategoryClick(categories.get(position));
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(false)
            return ITEM_TYPE_BANNER;
       else return ITEM_TYPE_CATEGORY;
    }
}
