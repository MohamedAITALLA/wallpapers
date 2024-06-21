package com.aitalla.wallpapersapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.models.Wallpaper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WallpaperSliderAdapter extends PagerAdapter {

    Context context;
    ArrayList<Wallpaper> range;
    String startingUrl;

    public WallpaperSliderAdapter(Context context, ArrayList<Wallpaper> range, String startingUrl) {
        this.context = context;
        this.range = range;
        this.startingUrl = startingUrl;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.view_wallpaper_slider_adapter, container, false);
        ImageView imageView =  view.findViewById(R.id.iv_image_slider);
        Glide.with(context).load(range.get(position).getUrl()).into(imageView);
        if (view.getParent() !=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return range.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
