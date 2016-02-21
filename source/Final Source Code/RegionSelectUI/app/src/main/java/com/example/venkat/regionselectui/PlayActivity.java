package com.example.venkat.regionselectui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {
    GridView grid;
    String[] web = {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9"

    } ;
    int[] imageId = {
            R.drawable.level,
            R.drawable.level,
            R.drawable.level,
            R.drawable.level,
            R.drawable.level,
            R.drawable.level,
            R.drawable.level,
            R.drawable.level,
            R.drawable.level

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_play);

        TextView textView = (TextView) findViewById(R.id.level);
        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "angry.ttf");
        textView.setTypeface(typeFace);

        CustomGrid adapter = new CustomGrid(PlayActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(PlayActivity.this, GameActivity.class);
                i.putExtra("id", position);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });
        /*LinearLayout canvas=(LinearLayout) findViewById(R.id.layout1);
        Button backbutton=new Button(this);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(150,150);
        param2.gravity= Gravity.BOTTOM | Gravity.LEFT;
        backbutton.setBackgroundResource(R.drawable.back);
        backbutton.setOnClickListener(onClickListener1);
        canvas.addView(backbutton, param2);*/
    }
    /*View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            //Toast.makeText(getApplicationContext(), "Pressed", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),MainMenu.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    };*/
}