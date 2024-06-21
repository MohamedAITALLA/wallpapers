package com.aitalla.wallpapersapp.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.viewpager.widget.ViewPager;


import com.aitalla.wallpapersapp.BuildConfig;
import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.adapters.WallpaperSliderAdapter;
import com.aitalla.wallpapersapp.models.Wallpaper;
import com.aitalla.wallpapersapp.room.FavWallpaper;
import com.aitalla.wallpapersapp.room.WallpapersDatabase;
import com.aitalla.wallpapersapp.utilities.BottomSheetDialog;
import com.aitalla.wallpapersapp.utilities.ProgressDialog;
import com.aitalla.wallpapersapp.utilities.Utilities;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WallpaperSliderActivity extends AppCompatActivity {

    private static final int WRITE_INTERNAL_STORAGE_CODE = 1;
    String urlType="";
    String startingPosition, startingUrl;
    ArrayList<Wallpaper> range = new ArrayList<>();
    ViewPager viewPager;
    WallpaperSliderAdapter wallpaperSliderAdapter;
    ImageView iv_fav, iv_download, iv_share;
    WallpapersDatabase wallpapersDB;

    public static void setWallpaper(Context context, Bitmap bitmap) {
        WallpaperManager manager = WallpaperManager.getInstance(context);
        try{
            manager.setBitmap(bitmap);
            Utilities.toast((Activity) context,"Wallpaper Set !");
        }
        catch (IOException e){
            Utilities.toast((Activity) context,"Home Screen wallpaper not supported !");
        }
    }

    public static void setWallpaperLockScreen(Context context, Bitmap bitmap) {
        WallpaperManager manager = WallpaperManager.getInstance(context);
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                manager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);
                Utilities.toast((Activity) context,"Lock Screen Wallpaper Set");
            }
            else{
                Utilities.toast((Activity) context,"Lock Screen Wallpaper Not Set !");
            }
        }
        catch (Exception e){
            Utilities.toast((Activity) context,e.getMessage());
        }
    }

    public static void setCustom(Context context, Bitmap bitmap) {
        try{
            File file = new File(context.getExternalCacheDir(),File.separator+"THEOLOGOS.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true,false);
            Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_ATTACH_DATA);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            intent.setDataAndType((photoURI),"image/jpg");
            intent.putExtra("mimeType","image/jpg");
            context.startActivity(Intent.createChooser(intent,"Set Wallpaper As"));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void download(Context context, String url) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                String[] permission = {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions((Activity) context,permission,WRITE_INTERNAL_STORAGE_CODE);
            }else{
                saveWallpaper(context,url);
            }
        }
        else{
            saveWallpaper(context,url);
        }
    }

    private static void saveWallpaper(Context context, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("4K WALLPAPER");
        Utilities.toast((Activity) context,"Download Successfully");
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"/4K Wallpaper/My Download/4K_"+ UUID.randomUUID()+".JPEG");
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_slider);
        declare();
        init();
        Utilities.showRewardVideo(this);
    }

    private void declare() {
        viewPager = findViewById(R.id.slidePager);
        iv_fav = findViewById(R.id.iv_fav);
        iv_download = findViewById(R.id.iv_download);
        iv_share = findViewById(R.id.iv_share);

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

        wallpapersDB = Room.databaseBuilder(getApplicationContext(),WallpapersDatabase.class,"wallpapersDB").addCallback(callback).build();
    }

    private void init() {
        getData();
        setUpData();
        handleClick();
        setFavIcon();
    }

    private void setFavIcon(){
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        String url = range.get(viewPager.getCurrentItem()).getUrl();
        executor.execute(()->{
            FavWallpaper fw =  wallpapersDB.getWallpaperDAO().getWallpaperByUrl(url);
            handler.post(()->{
                if(fw!=null){
                    iv_fav.setImageResource(R.drawable.ic_fav_white);
                }
                else{

                    iv_fav.setImageResource(R.drawable.ic_fav_empty_white);

                }
            });

        });
    }

    private void handleClick() {
        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlType="DOWNLOAD";
                ProgressDialog.showProgressDialog(WallpaperSliderActivity.this);
                String url = range.get(viewPager.getCurrentItem()).getUrl();
                getImageFromUrl(url);
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlType="SHARE";
                ProgressDialog.showProgressDialog(WallpaperSliderActivity.this);
                String url = range.get(viewPager.getCurrentItem()).getUrl();
                getImageFromUrl(url);
            }
        });

        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ExecutorService executor = Executors.newSingleThreadExecutor();
                final Handler handler = new Handler(Looper.getMainLooper());
                ProgressDialog.showProgressDialog(WallpaperSliderActivity.this);
                String url = range.get(viewPager.getCurrentItem()).getUrl();
                boolean isPremium =  range.get(viewPager.getCurrentItem()).isPremium();
                executor.execute(()->{

                    FavWallpaper fw =  wallpapersDB.getWallpaperDAO().getWallpaperByUrl(url);
                    if(fw!=null){
                        wallpapersDB.getWallpaperDAO().deleteWallpaper(fw);
                    }
                    else{
                        wallpapersDB.getWallpaperDAO().addWallpaper(new FavWallpaper(url,isPremium));
                    }

                    handler.post(()->{
                        if(fw!=null){
                            iv_fav.setImageResource(R.drawable.ic_fav_empty_white);
                        }
                        else{
                            iv_fav.setImageResource(R.drawable.ic_fav_white);
                        }

                        ProgressDialog.hideProgressDialog();
                    });

                });
            }
        });
    }

    private void getImageFromUrl(String url){
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            Bitmap bitmap = null;
            InputStream inputStream;

            try {
                inputStream = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Bitmap finalBitmap = bitmap;
            handler.post(()->{
                ProgressDialog.hideProgressDialog();
                if(urlType.equals("DOWNLOAD")){
                    openBottomSheetDialog(finalBitmap);
                }
                else if(urlType.equals("SHARE")){
                    shareImage(finalBitmap);
                }

            });

        });
    }

    private void shareImage(Bitmap bitmap) {
        try{
            File file = new File(getExternalCacheDir(),File.separator+"KILOGRAM.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true,false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoUri = FileProvider.getUriForFile(WallpaperSliderActivity.this,BuildConfig.APPLICATION_ID+".provider",file);
            intent.putExtra(Intent.EXTRA_STREAM,photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpg");
            WallpaperSliderActivity.this.startActivity(Intent.createChooser(intent,"Share Image Via"));
        } catch (FileNotFoundException e){
            e.printStackTrace();
            Utilities.toast(WallpaperSliderActivity.this,e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Utilities.toast(WallpaperSliderActivity.this,e.getMessage());
        }
    }

    private void openBottomSheetDialog(Bitmap bitmap) {
        String url = range.get(viewPager.getCurrentItem()).getUrl();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(WallpaperSliderActivity.this,bitmap,url);
        bottomSheetDialog.show((WallpaperSliderActivity.this).getSupportFragmentManager(),"bottomSheet");
    }

    private void setUpData() {
        wallpaperSliderAdapter = new WallpaperSliderAdapter(this, range, startingUrl);
        viewPager.setAdapter(wallpaperSliderAdapter);
    }

    private void getData() {
        Intent intent = getIntent();
        startingPosition = intent.getStringExtra("startingPosition");
        startingUrl = intent.getStringExtra("startingUrl");
        range = (ArrayList<Wallpaper>) intent.getSerializableExtra("range");
        Log.d("NEW RANGE LIST", new Gson().toJson(range));

    }
}