package com.example.reaganharper.hiittrainer02;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.reaganharper.hiittrainer02.R.id.intervalTimer;
import static com.example.reaganharper.hiittrainer02.R.id.play;

public class MainActivity extends AppCompatActivity {

    static final int ADD_INTERVAL = 1;
    static final int ZERO_CLOCK = 0;
    private long endTime;
    private PausableTimer fullTimer;
    private PausableTimer intervalTimer;
    private RecyclerView intervalList;
    private RecyclerView.Adapter intervalAdapter;
    private RecyclerView.LayoutManager LayoutManager;
    private ArrayList<Interval> mIntervals;
    private TextView mfullClock;
    private ImageButton mPlay;
    private int counter;
    private TextView mIntervalCLock;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mIntervals != null) {
            outState.putParcelableArrayList("Interval", mIntervals);
        }
        if (fullTimer != null && fullTimer.getCurrentTime() > 0) {
            outState.putLong("Time left", fullTimer.getCurrentTime());
        }
    }

    //Test Change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mIntervals = new ArrayList<>();

        intervalList = (RecyclerView) findViewById(R.id.recycler);
        intervalList.setHasFixedSize(true);

        LayoutManager = new LinearLayoutManager(this);
        intervalList.setLayoutManager(LayoutManager);

        intervalAdapter = new IntervalAdapter(this, mIntervals);
        intervalList.setAdapter(intervalAdapter);

        mPlay = (ImageButton) findViewById(R.id.play);
        ImageButton stop = (ImageButton) findViewById(R.id.stop);

        final TextView intervalClock = (TextView) findViewById(R.id.intervalTimer);
        mIntervalCLock = intervalClock;

        final TextView fullClock = (TextView) findViewById(R.id.fullTimer);
        mfullClock = fullClock;

        FloatingActionButton addInterval = (FloatingActionButton) findViewById(R.id.addInterval);
        Button reset = (Button) findViewById(R.id.reset);

        if (savedInstanceState != null) {
            ArrayList<Interval> recoveredArray = savedInstanceState.getParcelableArrayList("Interval");
            for (int i = 0; i < recoveredArray.size(); i++) {
                mIntervals.add(recoveredArray.get(i));
            }
            intervalAdapter.notifyItemInserted(intervalAdapter.getItemCount());
            Long recoveredTimeLeft = savedInstanceState.getLong("Time left");
        }

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullTimer == null) {
                    setupFullTimer();
                    setupIntervalTimer();
                }

                if(fullTimer.getTimerState() == PausableTimer.TimerState.STOPPED){
                    fullTimer.start();
     //               intervalTimer.start();
                    mPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                }else if(fullTimer.getTimerState() == PausableTimer.TimerState.PAUSED){
                    Toast.makeText(MainActivity.this, "Resume", Toast.LENGTH_SHORT).show();
                    fullTimer.resume(fullTimer.getCurrentTime());
                    intervalTimer.resume(intervalTimer.getCurrentTime());
                    mPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_24dp);
                }else if(fullTimer.getTimerState() == PausableTimer.TimerState.RUNNING){
                    Toast.makeText(MainActivity.this, "Pause", Toast.LENGTH_SHORT).show();
                    fullTimer.pause();
                    intervalTimer.pause();
                    mPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullTimer.getTimerState() != PausableTimer.TimerState.STOPPED) {
                    Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
                    fullTimer.stop();
                    endTime = ZERO_CLOCK;
                    fullClock.setText("00.00");
                }
                mPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
            }
        });

        addInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddInterval();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetIntervalList();
            }
        });
    }

    public long getFullTime() {
        for (int i = 0; i < mIntervals.size(); i++) {
            endTime = endTime + mIntervals.get(i).getIntervalTime();
        }
        return endTime;
    }

    public String convertTime(long mills) {
        long seconds = (mills / 1000) % 60;
        long minutes = ((mills / (1000 * 60)) % 60);
        String convertedSeconds = String.format("%02d", seconds);
        String convertedMinutes = String.format("%02d", minutes);
        return convertedMinutes + ":" + convertedSeconds;
    }

    public void openAddInterval() {
        Intent addInterval = new Intent(MainActivity.this, IntervalSetupActivity.class);
        startActivityForResult(addInterval, ADD_INTERVAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_INTERVAL) {
            if (resultCode == RESULT_OK) {
                Interval returnedInterval = data.getParcelableExtra("Interval");
                mIntervals.add(returnedInterval);
                intervalAdapter.notifyItemInserted(intervalAdapter.getItemCount());
            }
        }
    }

    public void resetIntervalList() {
        mIntervals.clear();
        intervalAdapter.notifyDataSetChanged();
    }

    public long getSingleInterval(int counter){
        return mIntervals.get(counter).getIntervalTime();
    }

    public void setupFullTimer(){
        fullTimer = new PausableTimer(getFullTime(), 500, new OnTickListener() {
            @Override
            public void OnTick(long timeLeft) {
                mfullClock.setText(convertTime(timeLeft));
            }

            @Override
            public void OnFinish() {
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                mPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
                fullTimer = null;
                endTime = ZERO_CLOCK;
                counter = ZERO_CLOCK;
            }
        });
    }

    public void setupIntervalTimer(){
        if(counter < mIntervals.size()) {
             intervalTimer = new PausableTimer(getSingleInterval(counter), 500, new OnTickListener() {
                @Override
                public void OnTick(long timeLeft) {
                    mIntervalCLock.setText(convertTime(timeLeft));
                }

                @Override
                public void OnFinish() {
                    counter ++;
                    setupIntervalTimer();
                }
            });
            intervalTimer.start();
        }
        if (counter == mIntervals.size()){
            intervalTimer = null;
        }
    }
}
