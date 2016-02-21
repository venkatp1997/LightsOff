package com.example.venkat.regionselectui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] Imageid;

    public CustomGrid(Context c,String[] web,int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.length;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            Typeface typeFace1 = Typeface.createFromAsset(mContext.getAssets(), "angry.ttf");
            textView.setTypeface(typeFace1);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(web[position]);
            imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
