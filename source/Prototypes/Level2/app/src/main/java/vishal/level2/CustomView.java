package vishal.level2;

import android.content.Context;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by deadcode on 04/10/2015.
 */
public class CustomView extends View {

    MainActivity maContext;

    public class Lights{
        private int cx;
        private int cy;
        private int cr;
        private Paint paint;
        private Rect rect;
    }

    public class RegionClass{
        private Rect rect;
        private Region region;
        private Paint paint;
        LinkedList<Lights> lights;
    }

    RegionClass[] regionlist=new RegionClass[8];
    Lights[] lightlist=new Lights[18];

    public CustomView(Context context) {
        super(context);

        maContext = (MainActivity) context;

        int i;
        for(i=0;i<8;i++)
            regionlist[i]=new RegionClass();

        int x,y;

        x=400;
        y=400;
        for(i=0;i<4;i++)
        {
            regionlist[i].rect=new Rect(x,y,x+200,y+200);
            regionlist[i].region=new Region(regionlist[i].rect);
            regionlist[i].lights=new LinkedList<Lights>();
            regionlist[i].paint=new Paint();
            if(i%2==1) {
                regionlist[i].paint.setColor(Color.RED);
                x += 200;
            }
            else
            {
                regionlist[i].paint.setColor(Color.GREEN);
                y += 200;
            }
        }
        x=200;
        y=200;
        for (i=4;i<8;i++)
        {
            regionlist[i].rect=new Rect(x,y,x+400,y+400);
            regionlist[i].region=new Region(regionlist[i].rect);
            regionlist[i].lights=new LinkedList<Lights>();
            regionlist[i].paint=new Paint();
            if(i%2==1) {
                regionlist[i].paint.setColor(Color.RED);
                x += 400;
            }
            else
            {
                regionlist[i].paint.setColor(Color.GREEN);
                y += 400;
            }
        }

        x=200;
        y=200;
        int r=25;
        for (i=0;i<3;i++,y+=400)
        {
            int j;
            for (j=0;j<3;j++,x+=400)
            {
                lightlist[i+j]=new Lights();
                lightlist[i+j].cx=x;
                lightlist[i+j].cy=y;
                lightlist[i+j].cr=r;
                lightlist[i+j].rect=new Rect(x-r/2,y-r/2,r,r);
                lightlist[i+j].paint=new Paint();
                if ((i+j)%2==0)
                    lightlist[i + j].paint.setColor(Color.WHITE);
                else
                    lightlist[i+j].paint.setColor(Color.BLACK);
            }
        }

        x=400;
        y=400;
        int o=9;
        int j;
        for (i=0;i<3;i++,y+=200)
        {
            for (j=0;j<3;j++,x+=200)
            {
                //if(x==600&&y==600)
                 //   continue;
                lightlist[i+j+o]=new Lights();
                lightlist[i+j+o].cx=x;
                lightlist[i+j+o].cy=y;
                lightlist[i+j+o].cr=r;
                lightlist[i+j+o].paint=new Paint();
                lightlist[i+j+o].rect=new Rect(x-r/2,y-r/2,r,r);
                if ((i+j+o)%2==0)
                    lightlist[i+j+o].paint.setColor(Color.WHITE);
                else
                    lightlist[i+j+o].paint.setColor(Color.BLACK);
            }
        }

        for (i=0;i<8;i++) {
            //if(j==13)
            // continue;
            for (j = 0; j < 18; j++) {
                if (lightlist[j].rect.isEmpty()) {
                    Log.i("check", "yes");
                    continue;
                }
                if (regionlist[i].region.contains(lightlist[j].rect.centerX(), lightlist[j].rect.centerY())) {
                    regionlist[i].lights.add(lightlist[j]);
                }
            }
        }
    }

    public void flipLights(Lights l){
        if(l.paint.getColor()==Color.BLACK)
            l.paint.setColor(Color.WHITE);
        else
            l.paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
        int i;
        for (i=0;i<8;i++)
            canvas.drawRect(regionlist[i].rect, regionlist[i].paint);
        for (i=0;i<18;i++){
            //if(i==13)
             //   continue;
            canvas.drawCircle(lightlist[i].cx, lightlist[i].cy, lightlist[i].cr, lightlist[i].paint);
        }

        // Delay
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) { }

        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            int i, fl = 0;
            for (i = 8; i >= 0; i--) {
                if (regionlist[i].region.contains((int) event.getX(), (int) event.getY())) {
                    Log.i("Region", ": " + i);
                    fl = 1;

                    ListIterator<Lights> j=regionlist[i].lights.listIterator();
                    while(j.hasNext()){
                        Lights l=j.next();
                        Log.i("Lights",": "+l.cx +","+ l.paint.getColor());
                        flipLights(l);

                    }
                    break;
                }
            }
            if (fl == 0) {
                Log.i("Region", ": " + 3);
            }
            return true;
        }
        return false;
    }
}
