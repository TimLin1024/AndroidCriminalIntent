package com.android.rdc.criminalintent;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;
import static com.android.rdc.criminalintent.CrimeDateDialogActivity.EXTRA_DATE_OF_CRIME;


/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class DatePickerFragment extends DialogFragment {
    private DatePicker mDatePicker;
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "extra_date";
    private Button mBtnDateOk;

//    public static DatePickerFragment newInstance(Date date) {
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_DATE, date);
//        DatePickerFragment datePickerFragment = new DatePickerFragment();
//        datePickerFragment.setArguments(args);
//        return datePickerFragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_date,null);
//        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Date date = (Date)getActivity().getIntent().getSerializableExtra(EXTRA_DATE_OF_CRIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int mon = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, mon, day, null);
        mBtnDateOk = (Button) v.findViewById(R.id.btn_date_ok);
        mBtnDateOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date1 = getTimePickerDate();
                sendResult(RESULT_OK,date1);
            }
        });
        return v;
    }

    public Date getTimePickerDate(){
        int y = mDatePicker.getYear();
        int mon = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();
        return new GregorianCalendar(y,mon,day).getTime();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Date date = (Date) getArguments().getSerializable(ARG_DATE);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int mon = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
//        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
//        mDatePicker.init(year, mon, day, null);
//        return new AlertDialog.Builder(getActivity())
//                .setView(v)
//                .setTitle(R.string.date_picker_title_title)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Date date1 = new GregorianCalendar(mDatePicker.getYear(),mDatePicker.getMonth(),mDatePicker.getDayOfMonth()).getTime();
//                        sendResult(RESULT_OK,date1);
//                    }
//                })
//                .create();
        return super.onCreateDialog(savedInstanceState);
    }

    public void sendResult(int resultCode, Date date) {
//        if (getTargetFragment() == null) {
//            return;
//        }
//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_DATE, date);
//        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getActivity().setResult(resultCode,intent);
        getActivity().finish();
    }
}
