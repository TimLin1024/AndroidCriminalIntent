package com.android.rdc.criminalintent;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2017/2/27 0027.
 */
//，该对象是一个数据集中存储池，用来存储 Crime 对象。
public class CrimeLab implements Serializable {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//        }
    }
    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            synchronized (CrimeLab.class){
                if (sCrimeLab == null){
                    sCrimeLab = new CrimeLab(context);
                }
            }
        }
        return sCrimeLab;
    }

    public void deleteCrime(UUID crimeID){
        if (crimeID == null){
            return;
        }
        mCrimes.remove(getCrime(crimeID));
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }
}
