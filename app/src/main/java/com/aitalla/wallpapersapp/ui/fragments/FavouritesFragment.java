package com.aitalla.wallpapersapp.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.adapters.WallpaperAdapter;
import com.aitalla.wallpapersapp.listeners.OnWallpaperClickListener;
import com.aitalla.wallpapersapp.models.Wallpaper;
import com.aitalla.wallpapersapp.room.FavWallpaper;
import com.aitalla.wallpapersapp.room.WallpapersDatabase;
import com.aitalla.wallpapersapp.ui.activities.WallpaperSliderActivity;
import com.aitalla.wallpapersapp.utilities.ProgressDialog;
import com.aitalla.wallpapersapp.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavouritesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnWallpaperClickListener {
    View view;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int page = 1;
    WallpapersDatabase wallpapersDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_favourites, container, false);
        declare();
        setData();
        Utilities.showMintegralInterstitial(getActivity());
        return view;
    }

    private void declare(){
        recyclerView = view.findViewById(R.id.recycler_fav);
        swipeRefreshLayout = view.findViewById(R.id.swipe_fav);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing (true);
        swipeRefreshLayout.setColorSchemeColors(Color.YELLOW);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK);

        RoomDatabase.Callback callback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                super.onDestructiveMigration(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        wallpapersDB = Room.databaseBuilder(getActivity().getApplicationContext(),WallpapersDatabase.class,"wallpapersDB").addCallback(callback).build();
    }
    private void setData() {
        ProgressDialog.showProgressDialog(getActivity());
        swipeRefreshLayout.setRefreshing(true);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(()->{
            List<FavWallpaper> favWallpapers =  wallpapersDB.getWallpaperDAO().getAllWallpapers();
            handler.post(()->{
               ArrayList<Wallpaper> wallpapers = new ArrayList<>();
               for(FavWallpaper favWallpaper:favWallpapers){
                   wallpapers.add(new Wallpaper(favWallpaper.getUrl(),favWallpaper.isPremium()));
               }

                Collections.shuffle(wallpapers);
                WallpaperAdapter adapter = new WallpaperAdapter(getActivity(),wallpapers,this);
                RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(),3);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                ProgressDialog.hideProgressDialog();
            });

        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        setData();
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