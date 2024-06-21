package com.aitalla.wallpapersapp.ui.fragments;

import static com.aitalla.wallpapersapp.R.*;
import static com.aitalla.wallpapersapp.config.AppConfig.targetedUrl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aitalla.wallpapersapp.R;

import com.aitalla.wallpapersapp.listeners.OnWallpaperClickListener;
import com.aitalla.wallpapersapp.models.Wallpaper;
import com.aitalla.wallpapersapp.ui.activities.WallpaperSliderActivity;
import com.aitalla.wallpapersapp.utilities.Utilities;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnWallpaperClickListener {
    View view;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        declare();
        setData();
        Utilities.showMintegralInterstitial(getActivity());
        return view;
    }

    private void declare(){
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_home);

        swipeRefreshLayout.setOnRefreshListener(this);
     ///   swipeRefreshLayout.setRefreshing (true);
        swipeRefreshLayout.setColorSchemeColors(Color.YELLOW);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK);
    }
    private void setData() {
        Utilities.getPeakPxData(getActivity(), recyclerView, swipeRefreshLayout,this,targetedUrl);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Utilities.getPeakPxData(getActivity(), recyclerView ,swipeRefreshLayout,this,targetedUrl);
    }

    @Override
    public void onWallpaperClick(String startingPosition, String startingUrl, ArrayList<Wallpaper> range) {
        Intent intent = new Intent(getContext(), WallpaperSliderActivity.class);
        intent.putExtra("startingPosition",startingPosition);
        intent.putExtra("startingUrl",startingUrl);
        intent.putExtra("range",range);
        startActivity(intent);
    }
}