package com.example.groceriesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class ListAdapter extends BaseAdapter {

    Context context;
    private String stores[];
    private int picIds[];

    public ListAdapter(Context context, String [] stores, int [] picIds){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.stores=stores;
        this.picIds=picIds;
    }

    @Override
    public int getCount() {
        return stores.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.stores, parent, false);

            viewHolder.image = (ImageView) convertView.findViewById(R.id.storeImage);
            viewHolder.image.setImageResource(picIds[position]);

            viewHolder.store = (TextView) convertView.findViewById(R.id.storeName);
            viewHolder.store.setText(stores[position]);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView store;
    }

}
