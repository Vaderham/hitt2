package com.example.reaganharper.hiittrainer02;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class Interval implements Serializable, Parcelable {

    private String name;
    private long intervalTime;

    public Interval(String name, long intervalTime) {
        this.name = name;
        this.intervalTime = intervalTime;
    }

    public String getName() {
        return name;
    }

    public long getIntervalTime() {
        return intervalTime;
    }


    protected Interval(Parcel in) {
        name = in.readString();
        intervalTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(intervalTime);
    }

    public static final Parcelable.Creator<Interval> CREATOR = new Parcelable.Creator<Interval>() {
        @Override
        public Interval createFromParcel(Parcel in) {
            return new Interval(in);
        }

        @Override
        public Interval[] newArray(int size) {
            return new Interval[size];
        }
    };
}