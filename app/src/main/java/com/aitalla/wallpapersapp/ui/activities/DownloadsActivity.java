package com.aitalla.wallpapersapp.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.adapters.ImageAdapter;
import com.aitalla.wallpapersapp.utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DownloadsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView textView;

    String root;
    File file;
    List<File> download;
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        declare();
        init();

    }

    private void declare(){
        toolbar = findViewById(R.id.download_toolbar);
        swipeRefreshLayout = findViewById(R.id.download_swipe);
        recyclerView = findViewById(R.id.download_recycler);
        textView = findViewById(R.id.download_empty_text);

        swipeRefreshLayout.setOnRefreshListener(this);
        ///   swipeRefreshLayout.setRefreshing (true);
        swipeRefreshLayout.setColorSchemeColors(Color.YELLOW);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK);
    }

    private void init(){
        setUpToolBar();
        Utilities.setStatusBrandNavigationBarColorPrimary(DownloadsActivity.this);
        getImageFromFolder();
    }

    private void getImageFromFolder() {
        root = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/4K Wallpaper/My Download/"));
        file = new File(root);
        if(!file.exists()){
            file.mkdirs();
        }

        download = new ArrayList<>();
        download.clear();
        download = getListFiles(file);

        if(download.isEmpty()){
            textView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
        else{
            textView.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager manager = new GridLayoutManager(DownloadsActivity.this,3);
            imageAdapter = new ImageAdapter(DownloadsActivity.this,download,"download");
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(imageAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private List<File> getListFiles(File parentDir) {
        List<File> inFiles = new ArrayList<>();
        try {
            Queue<File> files = new LinkedList<>(Arrays.asList(parentDir.listFiles()));
            while (!files.isEmpty()){
                File file = files.remove();
                if(file.isDirectory()){
                    files.addAll(Arrays.asList(file.listFiles()));
                }
                else if(file.getName().endsWith(".JPEG")){
                    inFiles.add(file);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return inFiles;
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Downloads");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
    }
}