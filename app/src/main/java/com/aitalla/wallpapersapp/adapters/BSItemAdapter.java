package com.aitalla.wallpapersapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.models.BSItem;

import java.util.List;

public class BSItemAdapter extends BaseAdapter {

    protected List<?> bsItems;
    protected LayoutInflater inflater;
    static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
    public BSItemAdapter(List<?> bsItems, Context context) {
        this.bsItems = bsItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return bsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((BSItem) bsItems.get(position)).getImage();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BSItem item = (BSItem) bsItems.get(position);
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.bottom_sheet_item,parent,false);
            convertView.setTag(holder);
            holder.imageView = convertView.findViewById(R.id.bs_image);
            holder.textView = convertView.findViewById(R.id.bs_text);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(item.getImage());
        holder.textView.setText(item.getText());

        return convertView;
    }
}
