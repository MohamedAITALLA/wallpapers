package com.aitalla.wallpapersapp.adapters;

import static com.aitalla.wallpapersapp.config.AppConfig.showCategory;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aitalla.wallpapersapp.ui.fragments.CategoryFragment;
import com.aitalla.wallpapersapp.ui.fragments.FavouritesFragment;
import com.aitalla.wallpapersapp.ui.fragments.HomeFragment;
import com.aitalla.wallpapersapp.ui.fragments.PremiumFragment;
import com.aitalla.wallpapersapp.utilities.Utilities;


public class PageAdapter extends FragmentStateAdapter {

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(showCategory){
            switch (position){
                case 0:
                    return new CategoryFragment();
                case 1:
                    return new HomeFragment();
                case 2:
                    return new FavouritesFragment();
            }
        }
        else{
            switch (position){
                case 0:
                    return new HomeFragment();
                case 1:
                    return new FavouritesFragment();
            }
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return Utilities.getTabs().length;
    }
}
