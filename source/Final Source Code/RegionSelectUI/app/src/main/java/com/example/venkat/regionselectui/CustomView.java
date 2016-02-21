package com.example.venkat.regionselectui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;

/**
 * Created by vishal kaja on 01/11/2015.
 */


public class CustomView extends View {
    private int ab;
    GameActivity maContext;
    private class Edge{
        int v1;
        int v2;
    }
    private class path extends LinkedList<Edge> {}
    private int offsetx;
    private int offsety;
    private int plenght;

    private path[] p;
    private int pn=0;

    private Path[] drawable_p;
    private int d_pn=0;
        private class Light {
        private Point c;
        private int state;
        private int idx;
    }

    private Light[] alllights;
    int[] booll;
    int ln=0;

    private class region {
        private Point[] points ;
        private Path draw_path;
        private Paint paint;
        private int draw_last;
        public boolean contains(Point test) {
            int i;
            int l=points.length;
            int j=points.length-1;
            if(points[j]==null) {
                j--;
                l--;
            }
            boolean result = false;
            for (i=0; i<l; j=i++) {
                if ((points[i].y>test.y)!=(points[j].y>test.y)&&(test.x<(points[j].x-points[i].x)*(test.y-points[i].y)/(points[j].y-points[i].y)+points[i].x)) {
                    result = !result;
                }
            }
            return result;
        }
        public boolean isnode(Point test) {
            int i;
            boolean result = false;
            int j = points.length;
            while(points[j-1]==null)
                j--;
            for (i=0;i<j;i++){
                if(points[i].x==test.x&&points[i].y==test.y) {
                    result = true;
                    return result;
                }
            }
            return result;
        }
        private LinkedList<Light> lights;

        public void fliplights() {
            ListIterator<Light> li = lights.listIterator();
            while(li.hasNext()){
                Light l = li.next();
                l.state=(l.state+1)%2;
                //lightstate[l.idx][rn+1]=l.state;
            }
        }
    }

    private region[] regions;
    private region max_region;
    int rn=0;
    private region undo_region= new region();

    private Path regionGen(path boundary, int maxnode) {
        ListIterator<Edge> p_itr = boundary.listIterator();

        Path p = new Path();
        int i=0;
        while (p_itr.hasNext()) {
            Edge e = p_itr.next();


            if (e.v1 > e.v2) {
                int t = e.v1;
                e.v1 = e.v2;
                e.v2 = t;
            }

            int cx1, cy1, cx2, cy2;
            if (e.v2 - e.v1 == 1) {
                cx1 = offsetx + ((e.v1 % maxnode) - 1) * plenght;
                cy1 = offsety + (e.v1 / maxnode) * plenght;
                cx2 = cx1 + plenght;
                cy2 = cy1;
            } else if (e.v1 % maxnode == 0) {
                cx1 = offsetx + (maxnode - 1) * plenght;
                cy1 = offsety + (e.v1 / maxnode - 1) * plenght;
                cx2 = cx1;
                cy2 = cy1 + plenght;
            } else {
                cx1 = offsetx + ((e.v1 % maxnode) - 1) * plenght;
                cy1 = offsety + (e.v1 / maxnode) * plenght;
                cx2 = cx1;
                cy2 = cy1 + plenght;
            }
            p.moveTo(cx1, cy1);
            p.lineTo(cx2, cy2);
        }
        p.close();
        return p;
    }

    private static class Node{
        int data;
        int v ;
    }

    private static class AdNodes extends LinkedList<Node>{}

    private static LinkedList<Edge> dfs_stack = new LinkedList<Edge>();
    private static int[] boolv;
    private static int[] degree;
    private static AdNodes[] adlist;

    private Light genlight(int v,int maxnode,int s) {
        Light l =new Light();
        Point p = new Point();
        p=getcords(v,maxnode);
        l.c=p;
        l.state=s;
        return l;
    }

    private Point getcords(int t,int maxnode){
        Point p = new Point();
        int cx,cy;
        if(t%maxnode==0){
            cx=offsetx+(maxnode-1)*plenght;
            cy=offsety+(t/maxnode-1)*plenght;
        }
        else{
            cx=offsetx+(t%maxnode-1)*plenght;
            cy=offsety+(t/maxnode)*plenght;
        }
        p.x=cx;
        p.y=cy;
        return p;
    }

    private void findallpaths(int tmp,int maxnode) {
        int start = tmp;
        int par=0;
        char[] chdir = new char[2];chdir[0]='R';chdir[1]='L';
        char prevdir;
        int i,j;
        for(i=0;i<2;i++){
            for(j=0;j<2;j++) {
                prevdir=chdir[(i+1)%2];
                int end=start;
                par=0;
                path tmp_path = new path();
                char d;
                int n=end;
                int fl=4;
                do {
                    fl=3;
                    while(fl>0){
                        d = getdirection(j, prevdir);
                        n = getnode(d, end, maxnode);
                        if(isnode(end,n)&&n!=0&&par!=n){
                            if(checkedge(end,n)){
                                Edge e = new Edge();
                                e.v1 = end;
                                e.v2 = n;
                                tmp_path.add(e);
                                fl=-1;
                                prevdir = d;
                                par = end;
                                end = n;
                            }
                            else {
                                fl=0;
                            }
                        }
                        else
                        {
                            prevdir=d;
                            if(par!=n)
                                fl--;
                        }
                    }
                }while(end!=start&&fl!=0);
                if(end==start&&tmp_path.size()!=0) {
                    ListIterator<Edge> pi = tmp_path.listIterator();
                    while(pi.hasNext()){
                        Edge e = pi.next();
                        setedge(e.v1,e.v2);
                        setedge(e.v2,e.v1);
                    }
                    p[pn] = new path();
                    p[pn] = tmp_path;
                    pn++;
                }
            }
        }
    }

    private void setedge(int v1, int v2) {
        ListIterator<Node> li = adlist[v1].listIterator();
        while(li.hasNext()) {
            Node node = li.next();
            if (node.data == v2) {
                node.v++;
                return;
            }
        }
    }

    private boolean checkedge(int end, int n) {
        ListIterator<Node> li = adlist[end].listIterator();
        while(li.hasNext()){
            Node node = li.next();
            if (node.data==n) {
                if (node.v < 2) {
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

    private boolean isnode(int end, int n) {
        ListIterator<Node> li = adlist[end].listIterator();
        while(li.hasNext()){
            Node node = li.next();
            if (node.data==n)
                return true;
        }
        return false;
    }

    private int getnode(char d, int end,int maxnode) {
        if(d=='R') return (end+1)>0?(end+1):0;
        else if(d=='L') return (end-1)>0?(end-1):0;
        else if(d=='U') return (end-maxnode)>0?(end-maxnode):0;
        else if(d=='D') return (end+maxnode)<=(maxnode*maxnode)?(end+maxnode):0;
        return end;
    }

    private char getdirection(int j, char dir) {
        if(j==0){
            if (dir=='R') return 'D';
            else if (dir=='D') return 'L';
            else if (dir=='L') return 'U';
            else if (dir=='U') return 'R';
        }
        else{
            if (dir=='R') return 'U';
            else if (dir=='D') return 'R';
            else if (dir=='L') return 'D';
            else if (dir=='U') return 'L';
        }
        return dir;
    }

    private region touchGen(path p,int maxnode){
        region r = new region();
        r.points = new Point[p.size()];
        //System.out.println(p.size());
        int[] boolp = new int[adlist.length+1];
        Arrays.fill(boolp, 0);
        int s=0;
        ListIterator<Edge> pi = p.listIterator();
        while(pi.hasNext()){
            Edge e = pi.next();
            boolp[e.v1]=1;
            boolp[e.v2]=1;
            s=e.v1;
        }
        int i=0,fl=1;
        boolp[s]=0;
        //Log.i("new test"," : "+s);
        r.points[i] = new Point();
        r.points[i] = getcords(s,maxnode);
        i++;
        while(fl==1) {
            ListIterator<Node> li = adlist[s].listIterator();
            while(li.hasNext()){
                Node n =li.next();
                //Log.i("adlist"," : "+s+" "+n.v);
                if(boolp[n.data]==1){
                    s=n.data;
                    boolp[n.data]=0;
                    r.points[i] = new Point();
                    //Log.i("test"," : "+s);
                    r.points[i] = getcords(s,maxnode);
                    i++;
                    fl=1;
                    break;
                }
                else if(n.data!=s)
                    fl=0;
            }
        }
        return r;
    }

    private static int[][] lightstate ;
    private static int[] hints;
    private static int no_hints;

    /*private void updateHints(){
    }*/

    public void updateHints() {
        getLightState();
        no_hints = rn+1;
        Arrays.fill(hints, 0);
        int[] tmp_hints = new int[hints.length];
        Arrays.fill(tmp_hints,0);
        if(findHint(ln, rn + 2, 0,tmp_hints)==1)
            showHint();
    }

    private void rowETransform(int m, int n){
        int i,j,k;
        for(k=0;k<(m<n?m:n);k++){
            int max = lightstate[k][k];
            j=k;
            for(i=k+1;i<m;i++)
                if(lightstate[i][k]>max)
                {
                    max=lightstate[i][k];
                    j=i;
                }
            int[] tmp;tmp=lightstate[j];lightstate[j]=lightstate[k];lightstate[k]=tmp;
            for(i=k+1;i<m;i++)
            {
                int t = (lightstate[i][k]/lightstate[k][k]);
                for(j=k+1;j<n;j++)
                    lightstate[i][j]^=lightstate[k][j]&t;
                lightstate[i][k]=0;
            }
        }
    }
    private int findHint(int m,int n,int c,int[] tmp_hints){
        int i,fl=1;
        for(i=0;i<m;i++)
        {
            if(lightstate[i][n-1]==1)
            {
                fl=0;
                if(c+1>=no_hints)
                    return fl;
                int j;
                for(j=0;j<n-1;j++) {
                    if (lightstate[i][j] == 1 && tmp_hints[j] == 0) {
                        tmp_hints[j] = 1;
                        //System.out.println(j);
                        flipHint(j, m, n);
                        if (findHint(m, n, c + 1,tmp_hints)==1) {
                            flipHint(j, m, n);
                            fl = 1;
                            tmp_hints[j]=0;
                        } else {
                            tmp_hints[j] = 0;
                            flipHint(j, m, n);
                        }
                    }
                }
                return fl;
            }
        }
        if(c<no_hints) {
            fl=1;
            no_hints = c;
            for (i=0;i<hints.length;i++)
                hints[i]=tmp_hints[i];
            return fl;
        }
        else {
            fl=0;
            return fl;
        }
    }


    private void flipHint(int j, int m, int n) {
        int i;
        for(i=0;i<m;i++)
        {
            if(lightstate[i][j]==1)
                lightstate[i][n-1]=(lightstate[i][n-1]==1)?0:1;
        }
    }

    private void showHint(){
        int i;
        for(i=0;i<hints.length;i++)
            if(hints[i]==1){
                hints[i]=0;
                System.out.println(i);
                regions[i].paint.setColor(Color.GREEN);
                regions[i].draw_last=1;
                break;
            }
            else if(hints[hints.length-1]==1){
                hints[hints.length-1]=0;
                max_region.paint.setColor(Color.GREEN);
                max_region.draw_last=1;
                break;
            }
    }

    public CustomView(Context context,int level) {
        super(context);
        ab=level;
        maContext = (GameActivity) context;

        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Scanner scan = null;
        if(level==0) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test1));
        else if(level==1) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test2));
        else if (level==2) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test3));
        else if (level==3) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test4));
        else if (level==4) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test5));
        else if (level==5) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test6));
        else if (level==6) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test7));
        else if (level==7) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test8));
        else if (level==8) scan = new Scanner(this.getResources().openRawResource(R.raw.level_test9));

        int maxnode=scan.nextInt();
        //int minnode=scan.nextInt();

        plenght=(point.x)/(maxnode+1);
        offsetx=plenght;
        offsety=250;

        int n=scan.nextInt();
        int m=scan.nextInt();

        //System.out.println(m);
        int i;
        Stack<Edge> edgeStack = new Stack<Edge>();

        for(i=1;i<=m;i++){
            Edge tmp_edge = new Edge();
            tmp_edge.v1 = scan.nextInt();
            tmp_edge.v2 = scan.nextInt();
            edgeStack.push(tmp_edge);
        }

        adlist = new AdNodes[n+1];
        for(i=1;i<=n;i++)
            adlist[i] = new AdNodes();
        degree = new int[n+1];
        Arrays.fill(degree, -1);

        for (i=0;i<m;i++){
            Edge e;
            e=edgeStack.pop();
            /*if(adlist[e.v1]==null)
                adlist[e.v1] = new AdNodes();
            if(adlist[e.v2]==null)
                adlist[e.v2] = new AdNodes();*/
            Node n1 = new Node();
            n1.data = e.v2;
            n1.v = 0;
            degree[e.v1]+=1;
            adlist[e.v1].addFirst(n1);
            Node n2 = new Node();
            n2.data = e.v1;
            n2.v = 0;
            degree[e.v2]+=1;
            adlist[e.v2].addFirst(n2);
        }

        p = new path[n];
        ln = scan.nextInt();
        alllights= new Light[ln+1];
        int tmp;
        int s;
        for(i=0;i<ln;i++) {
            tmp = scan.nextInt();
            s = scan.nextInt();
            findallpaths(tmp,maxnode);
            alllights[i] = new Light();
            alllights[i] = genlight(tmp, maxnode,s);
        }

        boolv = new int[n+1];
        Arrays.fill(boolv, 0);

        regions = new region[n];
        booll = new int[n+1];
        Arrays.fill(booll, 0);

        /*for (i=0;i<pn;i++){
            path tmp_path = p[i];
            System.out.println("new path of size "+p[i].size());
            ListIterator<Edge> li = tmp_path.listIterator();
            while(li.hasNext()){
                Edge e = li.next();
                System.out.println(e.v1+" "+e.v2);
            }
        }*/

        int max_boundary=0;
        for (i=0;i<pn;i++){
            if(p[i].size()>max_boundary)
                max_boundary=p[i].size();
        }


        d_pn=0;
        rn=0;
        drawable_p = new Path[pn+1];
        for(i=0;i<pn;i++){
            if (p[i]!=null&&p[i].size()!=0&&p[i].size()<max_boundary) {
                drawable_p[d_pn] = regionGen(p[i], maxnode);
                regions[rn] = new region();
                regions[rn].points = new Point[p[i].size()];
                regions[rn] = touchGen(p[i], maxnode);
                regions[rn].draw_path = drawable_p[d_pn];
                regions[rn].draw_last = 0;
                Paint def_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                def_paint.setStyle(Paint.Style.FILL_AND_STROKE);
                def_paint.setStrokeWidth(10);
                def_paint.setColor(Color.WHITE);
                def_paint.setAntiAlias(true);
                regions[rn].paint = def_paint;
                rn++;
                d_pn++;
            }
            else if(p[i]!=null&&p[i].size()!=0&&p[i].size()==max_boundary){
                drawable_p[d_pn] = regionGen(p[i], maxnode);
                max_region = new region();
                max_region.points = new Point[p[i].size()];
                max_region = touchGen(p[i], maxnode);
                max_region.draw_path = drawable_p[d_pn];
                max_region.draw_last = 0;
                Paint def_paint = new Paint();
                def_paint.setAntiAlias(true);
                def_paint.setStyle(Paint.Style.FILL_AND_STROKE);
                def_paint.setStrokeWidth(10);
                def_paint.setColor(Color.WHITE);
                max_region.paint = def_paint;
                d_pn++;
            }
        }
        lightstate = new int[ln][rn+2];
        hints = new int[rn+1];
        no_hints=rn+1;

        for(i=0;i<rn;i++){
            regions[i].lights = new LinkedList<Light>();
            //System.out.println(i + " region");
            int j;
            for(j=0;j<ln;j++)
            {
                if(regions[i].isnode(alllights[j].c)) {
                    Light l;
                    l = alllights[j];
                    l.idx=j;
                    lightstate[l.idx][i]=1;
                    lightstate[l.idx][rn+1]=(l.state+1)%2;
                    regions[i].lights.add(l);
                    //System.out.println(j + " light");
                }
            }
        }

        max_region.lights = new LinkedList<Light>();
        for (i=0;i<ln;i++) {
            if (max_region.isnode(alllights[i].c)) {
                Light l;
                l = alllights[i];
                l.idx = i;
                lightstate[l.idx][rn] = 1;
                lightstate[l.idx][rn + 1] = (l.state + 1) % 2;
                max_region.lights.add(l);
            }
        }

        lightstate = new int[ln][rn+2];
        hints = new int[rn+1];
        no_hints=rn+1;

        for(i=0;i<rn;i++){
            regions[i].lights = new LinkedList<Light>();
            //System.out.println(i + " region");
            int j;
            for(j=0;j<ln;j++)
            {
                if(regions[i].isnode(alllights[j].c)) {
                    Light l;
                    l = alllights[j];
                    l.idx=j;
                    lightstate[l.idx][i]=1;
                    lightstate[l.idx][rn+1]=(l.state+1)%2;
                    regions[i].lights.add(l);
                    //System.out.println(j + " light");
                }
            }
        }

        max_region.lights = new LinkedList<Light>();
        for (i=0;i<ln;i++) {
            if (max_region.isnode(alllights[i].c)) {
                Light l;
                l = alllights[i];
                l.idx = i;
                lightstate[l.idx][rn] = 1;
                lightstate[l.idx][rn + 1] = (l.state + 1) % 2;
                max_region.lights.add(l);
            }
        }
    }

    private void getLightState(){
        int i;
        for (i=0;i<ln;i++){
            int j;
            for(j=0;j<rn+2;j++)
                lightstate[i][j]=0;
        }
        for(i=0;i<rn;i++) {
            ListIterator<Light> li = regions[i].lights.listIterator();
            while(li.hasNext()){
                Light l = li.next();
                //System.out.println(l.idx+" "+i);
                lightstate[l.idx][i]=1;
                lightstate[l.idx][rn+1]=(l.state+1)%2;
            }
        }
        ListIterator<Light> li = max_region.lights.listIterator();
        while(li.hasNext()){
            Light l = li.next();
            lightstate[l.idx][rn]=1;
            lightstate[l.idx][rn+1]=(l.state+1)%2;
        }
        for (i=0;i<ln;i++){
            int j;
            for(j=0;j<rn+2;j++){
                System.out.print(lightstate[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("-------");
        rowETransform(ln,rn+2);
        for (i=0;i<ln;i++){
            int j;
            for(j=0;j<rn+2;j++){
                System.out.print(lightstate[i][j]+" ");
            }
            System.out.println();
        }
    }
    @Override
    protected void onDraw(Canvas canvas){
        int i;
        for (i=0;i<rn;i++) {
            //drawable_p[i].setFillType(Path.FillType.EVEN_ODD);
            if(regions[i].draw_last==0)
                canvas.drawPath(regions[i].draw_path, regions[i].paint);
        }
        for (i=0;i<rn;i++) {
            if (regions[i].draw_last == 1) {
                canvas.drawPath(regions[i].draw_path, regions[i].paint);
                break;
            }
            else
                canvas.drawPath(max_region.draw_path, max_region.paint);
        }
        for (i=0;i<ln;i++){
            Paint[] pl = new Paint[2];
            pl[0]=new Paint();
            pl[0].setColor(Color.BLACK);
            pl[1]=new Paint();
            pl[1].setColor(Color.YELLOW);
            canvas.drawCircle(alllights[i].c.x, alllights[i].c.y, 25, pl[alllights[i].state]);
        }

        // Delay
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) { }

        invalidate();

    }
    private void checkCompletion(){
        getLightState();
        int fl=1;
        for(int i=0;i<ln;i++){
            if(lightstate[i][rn+1]==1)
                fl=0;
        }
        if(fl==1){
            Toast.makeText(maContext, "Completed", Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(maContext).create(); //Read Update
            AlertDialog.Builder builder = new AlertDialog.Builder(maContext);
            builder.setTitle("Quit");
            builder.setMessage("You completed the level!!");

            builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(maContext, PlayActivity.class);
                    maContext.startActivity(i);
                }
            });
            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(maContext, GameActivity.class);
                    i.putExtra("id",ab);
                    maContext.startActivity(i);
                }
            });
            builder.show();  //<-- See This!
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point p = new Point();
        p.x = (int) event.getX();
        p.y = (int) event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            //Log.i("use"," this");
            int i,fl=0;
            for (i = 0; i < rn; i++) {
                if (regions[i].contains(p)) {
                    regions[i].paint.setColor(Color.RED);
                    regions[i].draw_last=1;
                    //Log.i("region", " : " + i);
                    fl=1;
                }
                else {
                    regions[i].draw_last=0;
                    regions[i].paint.setColor(Color.WHITE);
                }
            }
            if(fl!=1){
                max_region.draw_last=1;
                max_region.paint.setColor(Color.RED);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int i,fl=0;
            for (i = 0; i < rn; i++) {
                if (regions[i].contains(p)) {
                    regions[i].paint.setColor(Color.WHITE);
                    //Log.i("yes", ": finally"+" "+i);
                    if (regions[i].draw_last == 1) {
                        regions[i].fliplights();
                        undo_region=regions[i];
                        regions[i].draw_last=0;
                        fl=1;
                        Log.i("region"," : "+i);
                    }
                    max_region.draw_last=0;
                    regions[i].paint.setColor(Color.WHITE);
                }
                else {
                    regions[i].draw_last = 0;
                    regions[i].paint.setColor(Color.WHITE);
                }
            }
            if(fl!=1){
                max_region.paint.setColor(Color.WHITE);
                if(max_region.draw_last==1) {
                    max_region.fliplights();
                    Log.i("region", " : " + rn);
                    max_region.draw_last=0;
                    undo_region=max_region;
                }
                //Log.i("no", "die");
            }
        }
        checkCompletion();
        //updateHints();
        return true;
    }
    public void undo(){
        undo_region.fliplights();
    }
    public int minMoves=0;
    public int getMinMoves(){
        getLightState();
        no_hints=rn+1;
        Arrays.fill(hints,0);
        int[] tmp_hints = new int[hints.length];
        Arrays.fill(tmp_hints,0);
        if(findHint(ln, rn + 2, 0,tmp_hints)==1){
            for(int i=0;i<hints.length;i++){
                if(hints[i]==1)minMoves++;
            }
        }
        return minMoves;
    }
}

