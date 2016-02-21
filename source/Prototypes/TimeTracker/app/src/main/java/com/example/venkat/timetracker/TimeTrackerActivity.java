package com.example.venkat.timetracker;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Created by venkat on 11/4/2015.
 */
public class TimeTrackerActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "Created";
    private TimeListAdapter mTimeListAdapter = null;
    private TimeHandler mHandler;
    private long mStart = 0;
    private long mTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mHandler = new TimeHandler(this);

        //Initialize the timer
        TextView counter = (TextView) findViewById(R.id.counter);
        counter.setText(DateUtils.formatElapsedTime(0));

        Button startButton = (Button) findViewById(R.id.start_stop);
        startButton.setOnClickListener(this);

        Button stopButton = (Button) findViewById(R.id.start_stop);
        stopButton.setOnClickListener(this);

        Button finishButton = (Button) findViewById(R.id.reset);
        finishButton.setOnClickListener(this);

        List<Long> values = new ArrayList<Long>();
        if (savedInstanceState != null) {
            long[] arr = savedInstanceState.getLongArray("times");
            for (long l : arr) {
                values.add(l);
            }
            CharSequence seq = savedInstanceState.getCharSequence("currentTime");
            if (seq != null)
                counter.setText(seq);
        }
        if (mTimeListAdapter == null)
            mTimeListAdapter = new TimeListAdapter(this, 0);

        ListView list = (ListView) findViewById(R.id.time_list);
        list.setAdapter(mTimeListAdapter);
    }

    private void startTimer() {
        mStart = System.currentTimeMillis();
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessage(0);
        Log.d("Hi", "Here3");
    }

    private void stopTimer() {
        mHandler.removeMessages(0);
    }

    private void resetTimer() {
        stopTimer();
        if (mTimeListAdapter != null)
            mTimeListAdapter.add(mTime / 1000);
        mTime = 0;
    }

    private boolean isTimerRunning() {
        if (mHandler.hasMessages(0))
            return true;
        else
            return false;
    }

    @Override
    public void onClick(View v) {
        TextView ssButton = (TextView) findViewById(R.id.start_stop);
        if (v.getId() == R.id.start_stop) {
            if (!isTimerRunning()) {
                Log.d("Hi","Here1");
                startTimer();
                ssButton.setText(R.string.stop);
            }
            else {
                Log.d("Hi","Here2");
                stopTimer();
                ssButton.setText(R.string.start);
            }
        } else if (v.getId() == R.id.reset) {
            resetTimer();
            TextView counter = (TextView) findViewById(R.id.counter);
            counter.setText(DateUtils.formatElapsedTime(0));
            ssButton.setText(R.string.start);
        }
    }

    private static class TimeHandler extends Handler {
        WeakReference<TimeTrackerActivity> mActivityRef;

        public TimeHandler(TimeTrackerActivity activity) {
            mActivityRef = new WeakReference<TimeTrackerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TimeTrackerActivity activity = mActivityRef.get();
            if (activity != null) {
                long current = System.currentTimeMillis();
                activity.mTime += current - activity.mStart;
                activity.mStart = current;

                TextView counter = (TextView) activity.findViewById(R.id.counter);
                counter.setText(DateUtils.formatElapsedTime(activity.mTime/1000));

                sendEmptyMessageDelayed(0, 250);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_all:
                FragmentManager fm=getSupportFragmentManager();
                if(fm.findFragmentByTag("dialog")==null){
                    ConfirmClearDialogFragment frag = ConfirmClearDialogFragment.newInstance(mTimeListAdapter);
                    frag.show(getFragmentManager(),"dialog");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
