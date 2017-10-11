package com.example.reaganharper.hiittrainer02;

import android.os.CountDownTimer;

public class PausableTimer{

    private CountDownTimer timer;
    private OnTickListener tickListener;
    public enum TimerState {
        STOPPED, RUNNING, PAUSED
    }
    private TimerState timerState;
    private long mCurrentTime;
    private long millisUntilFinished;
    private long interval;


    public PausableTimer(long millisUntilFinished, long interval, OnTickListener listener){

        tickListener = listener;
        timerState = TimerState.STOPPED;
        this.millisUntilFinished = millisUntilFinished;
        this.interval = interval;
    }

    public void start(){
        resume(this.millisUntilFinished);
    }

    public void stop(){
        this.timer.cancel();
        tickListener.OnTick(0);
    }

    public void pause(){
        this.timer.cancel();
        tickListener.OnTick(mCurrentTime);
        timerState = TimerState.PAUSED;
    }

    public void resume(final long currentTime){
        this.timer = new CountDownTimer(currentTime, this.interval) {
            @Override
            public void onTick(long l) {
                tickListener.OnTick(l);
                mCurrentTime = l;
            }
            @Override
            public void onFinish() {
                tickListener.OnTick(0);
                tickListener.OnFinish();
                timerState = TimerState.STOPPED;
            }
        };
        this.timer.start();
        timerState = TimerState.RUNNING;
    }

    public long getCurrentTime(){
        return mCurrentTime;
    }

    public TimerState getTimerState(){
        return timerState;
    }

}

