package com.android.rdc.criminalintent;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class Time implements Serializable {
    private int hour;
    private int mins;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }
}
