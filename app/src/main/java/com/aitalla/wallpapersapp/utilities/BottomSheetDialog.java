package com.aitalla.wallpapersapp.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.adapters.BSItemAdapter;
import com.aitalla.wallpapersapp.models.BSItem;
import com.aitalla.wallpapersapp.ui.activities.WallpaperSliderActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    Context context;
    Bitmap bitmap;
    String url;

    public BottomSheetDialog(Context context, Bitmap bitmap, String url) {
        this.context = context;
        this.bitmap = bitmap;
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        ArrayList<BSItem> items = new ArrayList<>();
        items.add(new BSItem(R.drawable.ic_wallpaper, "SET WALLPAPER"));
        items.add(new BSItem(R.drawable.ic_lock, "SET LOCK SCREEN"));
        items.add(new BSItem(R.drawable.ic_both, "SET BOTH"));
        items.add(new BSItem(R.drawable.ic_custom, "SET WALLPAPER AS CUSTOM"));
        items.add(new BSItem(R.drawable.ic_download, "DOWNLOAD 4K IMAGE"));

        BSItemAdapter adapter = new BSItemAdapter(items, this.context);
        final ListView listView = view.findViewById(R.id.list_items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        WallpaperSliderActivity.setWallpaper(context,bitmap);
                        break;
                    case 1:
                        WallpaperSliderActivity.setWallpaperLockScreen(context,bitmap);
                        break;
                    case 2:
                        WallpaperSliderActivity.setWallpaper(context,bitmap);
                        WallpaperSliderActivity.setWallpaperLockScreen(context,bitmap);
                        break;
                    case 3:
                        WallpaperSliderActivity.setCustom(context,bitmap);
                        break;
                    case 4:
                        WallpaperSliderActivity.download(context,url);
                        break;
                }
            }
        });
        return view;
    }
}
