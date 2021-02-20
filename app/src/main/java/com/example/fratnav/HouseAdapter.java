package com.example.fratnav;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class HouseAdapter extends BaseAdapter {
    private Context context;
    private final String[] houseValues;

    public HouseAdapter(Context context, String[] houseValues){
        this.context = context;
        this.houseValues = houseValues;

    }

    @Override
    public int getCount() {
        return houseValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
