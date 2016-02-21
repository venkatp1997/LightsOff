package com.example.venkat.regionselectui;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.security.auth.callback.Callback;

public class GameActivity extends AppCompatActivity {

    com.example.venkat.regionselectui.CustomView MV;
    public int minMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game);
        FrameLayout canvas=(FrameLayout) findViewById(R.id.layout);
        Intent intent=getIntent();
        int level=intent.getIntExtra("id",1);
        MV=new com.example.venkat.regionselectui.CustomView(this,level);
        minMoves=MV.getMinMoves();

        //Toast.makeText(this, "You Clicked at " + level, Toast.LENGTH_SHORT).show();
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER;
        canvas.addView(MV, param);

        String display="Minimum Moves: "+minMoves;
        TextView min=new TextView(this);
        min.setText(display);
        min.setTextSize(25);
        min.setGravity(Gravity.TOP | Gravity.RIGHT);
        Typeface face=Typeface.createFromAsset(getAssets(), "Calibri Bold.ttf");
        min.setTypeface(face);
        canvas.addView(min);

        Button button=new Button(this);
        FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams(150,150);
        param1.gravity=Gravity.BOTTOM | Gravity.RIGHT;
        button.setBackgroundResource(R.drawable.gint);
        button.setOnClickListener(onClickListener);
        canvas.addView(button, param1);

        Button backbutton=new Button(this);
        FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(150,150);
        param2.gravity=Gravity.BOTTOM | Gravity.LEFT;
        backbutton.setBackgroundResource(R.drawable.back);
        backbutton.setOnClickListener(onClickListener1);
        canvas.addView(backbutton, param2);

        Button undobutton=new Button(this);
        FrameLayout.LayoutParams param3 = new FrameLayout.LayoutParams(150,150);
        param3.gravity=Gravity.BOTTOM | Gravity.CENTER;
        undobutton.setBackgroundResource(R.drawable.undo);
        undobutton.setOnClickListener(onClickListener2);
        canvas.addView(undobutton, param3);

        ImageView imgView = (ImageView) findViewById(R.id.diagram);
        ImageView imgView1 = (ImageView) findViewById(R.id.diagram1);
        if(level!=0)imgView.setVisibility(View.INVISIBLE);
        if(level!=1)imgView1.setVisibility(View.INVISIBLE);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
                public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "Pressed", Toast.LENGTH_SHORT).show()
                MV.updateHints();
            }
    };
    View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            //Toast.makeText(getApplicationContext(), "Pressed", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),PlayActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    };
    View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            //Toast.makeText(getApplicationContext(), "Pressed", Toast.LENGTH_SHORT).show();
            MV.undo();
        }
    };
}

