package com.android.rdc.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class CrimeDateDialogActivity extends SingleFragmentActivity {
    public static final String EXTRA_DATE_OF_CRIME = "extra_date_of_crime";
    private DatePickerFragment mDatePickerFragment;
    @Override
    protected Fragment createFragment() {
        mDatePickerFragment = new DatePickerFragment();
        return mDatePickerFragment;
    }

    public static Intent newIntent(Context context, Date date){
        Intent intent = new Intent(context,CrimeDateDialogActivity.class);
        intent.putExtra(EXTRA_DATE_OF_CRIME,date);
        return intent;
    }

    @Override
    public void onBackPressed() {
        mDatePickerFragment.sendResult(RESULT_OK,mDatePickerFragment.getTimePickerDate());
    }
}
