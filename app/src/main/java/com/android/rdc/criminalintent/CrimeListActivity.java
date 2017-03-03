package com.android.rdc.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
