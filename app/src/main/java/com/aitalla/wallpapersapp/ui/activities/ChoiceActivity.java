package com.aitalla.wallpapersapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.listeners.OnWallpaperClickListener;
import com.aitalla.wallpapersapp.models.Wallpaper;
import com.aitalla.wallpapersapp.utilities.Utilities;

import java.util.ArrayList;

public class ChoiceActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnWallpaperClickListener {

    String keywords;
    String name;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;

    int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        declare();
        init();
        Utilities.showMintegralInterstitial(this);
    }

    private void declare() {
        recyclerView = findViewById(R.id.recycler_view_choice);
        swipeRefreshLayout = findViewById(R.id.swipe_choice);
        toolbar = findViewById(R.id.toolbar);


    }

    private void init() {
        getIntentData();
        setUpToolBar();
        swipeRefreshLayout.setOnRefreshListener(this);
        // swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeColors(Color.YELLOW);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK);
        Utilities.getPeakPxData(this, recyclerView, swipeRefreshLayout, this, "https://www.peakpx.com/en/search?q=" + keywords + "&device=1&page=" + page);
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getIntentData() {
        Intent intent = getIntent();
        keywords = intent.getStringExtra("keywords");
        name = intent.getStringExtra("name");
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Utilities.getPeakPxData(this, recyclerView, swipeRefreshLayout, this, "https://www.peakpx.com/en/search?q=" + keywords + "&device=1&page=" + page);
    }

    @Override
    public void onWallpaperClick(String startingPosition, String startingUrl, ArrayList<Wallpaper> range) {
        Intent intent = new Intent(this, WallpaperSliderActivity.class);
        intent.putExtra("startingPosition", startingPosition);
        intent.putExtra("startingUrl", startingUrl);
        intent.putExtra("range", range);
        startActivity(intent);
    }
}