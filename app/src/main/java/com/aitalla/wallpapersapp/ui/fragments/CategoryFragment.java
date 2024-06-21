package com.aitalla.wallpapersapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.listeners.OnCategoryClickListener;
import com.aitalla.wallpapersapp.models.Category;
import com.aitalla.wallpapersapp.ui.activities.ChoiceActivity;
import com.aitalla.wallpapersapp.ui.activities.WallpaperSliderActivity;
import com.aitalla.wallpapersapp.utilities.Utilities;


public class CategoryFragment extends Fragment implements OnCategoryClickListener{

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recycler_category);
        Utilities.readCategories(getActivity(),recyclerView,this);
        Utilities.showMintegralInterstitial(getActivity());
        return view;
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(getContext(), ChoiceActivity.class);
        intent.putExtra("keywords",category.getKeywords());
        intent.putExtra("name",category.getName());
        startActivity(intent);
    }
}