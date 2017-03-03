package com.android.rdc.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;


/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class TimePickerFragment extends DialogFragment {
    private TimePicker mTimePicker;
    private static final String ARG_TIME = "arg_time";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker);
        Time time = (Time) getArguments().getSerializable(ARG_TIME);
        if (time != null){

        }
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Time of crime")
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
    public static TimePickerFragment newInstance(Time time){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME,time);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return  timePickerFragment;
    }

}
