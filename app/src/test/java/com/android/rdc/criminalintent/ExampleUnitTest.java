package com.android.rdc.criminalintent;

import android.text.format.DateFormat;
import android.util.Log;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testDate() throws Exception {
        getDate();
    }
    public Date getDate(){
        String date = (String) DateFormat.format("EEEE",new Date());
        Log.e(TAG, "getDate: "+date);
        return new Date(date);
    }
}