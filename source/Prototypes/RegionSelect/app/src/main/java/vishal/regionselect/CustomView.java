package vishal.regionselect;

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
        private float cx;
        private float cy;
        private float cr;
        private Paint paint;
    }

    public class RegionClass{
        private Rect rect;
        private Region region;
        private Paint paint;
        LinkedList<Lights> lights;
    }

    RegionClass[] regionlist=new RegionClass[3];
    Lights[] lightlist=new Lights[3];

    public CustomView(Context context) {
        super(context);

        maContext = (MainActivity) context;

        int i;
        for(i=0;i<3;i++)
            regionlist[i]=new RegionClass();

    //    for(i=0;i<3;i++)
    //        lightlist[i]=new Lights();

        int x0=300;
        int y0=500;
        int sideLengthx0 = x0 + 200;
        int sideLengthy0 = y0 + 200;
        // create a rectangle that we'll draw later
        regionlist[0].rect = new Rect(x0, y0, sideLengthx0, sideLengthy0);
        regionlist[0].region=new Region(regionlist[0].rect);

        int x1=100;
        int y1=100;
        int sideLengthx1 = x1 + 400;
        int sideLengthy1 = y1 + 400;
        // create a rectangle that we'll draw later
        regionlist[1].rect = new Rect(x1, y1, sideLengthx1, sideLengthy1);
        regionlist[1].region=new Region(regionlist[1].rect);

        int x2=100;
        int y2=300;
        int sideLengthx2 = x2 + 200;
        int sideLengthy2 = y2 + 200;
        // create a rectangle that we'll draw later
        regionlist[2].rect = new Rect(x2, y2, sideLengthx2, sideLengthy2);
        regionlist[2].region=new Region(regionlist[2].rect);

        // create the Paint and set its color
        for (i=0;i<3;i++)
            regionlist[i].paint = new Paint();
        regionlist[0].paint.setColor(Color.RED);
        regionlist[1].paint.setColor(Color.YELLOW);
        regionlist[2].paint.setColor(Color.GREEN);

        int lightrad=25;

        lightlist[0]=new Lights();
        lightlist[0].cx=regionlist[2].rect.left;
        lightlist[0].cy=regionlist[2].rect.top;
        lightlist[0].cr=lightrad;
        lightlist[0].paint=new Paint();
        lightlist[0].paint.setColor(Color.WHITE);

        lightlist[1]=new Lights();
        lightlist[1].cx=regionlist[0].rect.left;
        lightlist[1].cy=regionlist[0].rect.top;
        lightlist[1].cr=lightrad;
        lightlist[1].paint=new Paint();
        lightlist[1].paint.setColor(Color.BLACK);

        lightlist[2]=new Lights();
        lightlist[2].cx=regionlist[1].rect.right;
        lightlist[2].cy=regionlist[1].rect.bottom;
        lightlist[2].cr=lightrad;
        lightlist[2].paint=new Paint();
        lightlist[2].paint.setColor(Color.WHITE);

        regionlist[0].lights= new LinkedList<Lights>();
        regionlist[0].lights.add(lightlist[1]);
        regionlist[0].lights.add(lightlist[2]);

        regionlist[1].lights= new LinkedList<Lights>();
        regionlist[1].lights.add(lightlist[0]);
        regionlist[1].lights.add(lightlist[1]);
        regionlist[1].lights.add(lightlist[2]);

        regionlist[2].lights= new LinkedList<Lights>();
        regionlist[2].lights.add(lightlist[0]);
        regionlist[2].lights.add(lightlist[1]);
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
        for (i=0;i<3;i++)
            canvas.drawRect(regionlist[i].rect, regionlist[i].paint);
        for (i=0;i<3;i++){
            canvas.drawCircle(lightlist[i].cx,lightlist[i].cy,lightlist[i].cr,lightlist[i].paint);
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
            for (i = 2; i >= 0; i--) {
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
