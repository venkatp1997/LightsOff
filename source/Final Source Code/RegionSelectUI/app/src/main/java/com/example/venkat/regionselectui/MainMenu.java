package com.example.venkat.regionselectui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {
    private Handler mHandler;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mHandler = new Handler();

        TextView myTextView = (TextView) findViewById(R.id.Title);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "Existence-Light.otf");
        myTextView.setTypeface(typeFace);

        TextView myTextView1 = (TextView) findViewById(R.id.subTitle);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "Existence-Light.otf");
        myTextView1.setTypeface(typeFace1);

        startRepeatingTask();
    }

    public void onClick(View view){
        Intent i = new Intent(this,PlayActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ImageView imgView = (ImageView) findViewById(R.id.on);
            if((i%2)==0)imgView.setVisibility(View.INVISIBLE);
            else imgView.setVisibility(View.VISIBLE);
            i++;
            mHandler.postDelayed(runnable, 2000);
        }

    };
    void startRepeatingTask() {
        runnable.run();
    }
}

