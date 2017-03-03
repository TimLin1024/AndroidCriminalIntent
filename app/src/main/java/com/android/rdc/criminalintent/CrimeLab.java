package com.android.rdc.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.rdc.criminalintent.database.CrimeBaseHelper;
import com.android.rdc.criminalintent.database.CrimeCursorWrapper;
import com.android.rdc.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/2/27 0027.
 */
public class CrimeLab implements Serializable {
    private volatile static CrimeLab sCrimeLab; //声明为volatile 保证new过程中的 原子性
    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime c) {
        ContentValues contentValues = getContentValue(c);
        mSQLiteDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        ContentValues contentValues = getContentValue(crime);
        String uuidString = crime.getId().toString();
        mSQLiteDatabase.update(CrimeTable.NAME, contentValues, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            synchronized (CrimeLab.class) {
                if (sCrimeLab == null) {
                    sCrimeLab = new CrimeLab(context);
                }
            }
        }
        return sCrimeLab;
    }

    public void deleteCrime(UUID crimeID) {
        if (crimeID == null) {
            return;
        }
//        mCrimes.remove(getCrime(crimeID));
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrime(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public List<Crime> getCrimes() {
        mCrimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrime(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mCrimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return mCrimes;
    }

    private static ContentValues getContentValue(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return contentValues;
    }

    public CrimeCursorWrapper queryCrime(String whereClause, String[] keys) {
        Cursor cursor = mSQLiteDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null means selecting all columns
                whereClause,
                keys,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
}