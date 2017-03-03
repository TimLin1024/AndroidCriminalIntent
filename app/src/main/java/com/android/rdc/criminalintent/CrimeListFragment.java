package com.android.rdc.criminalintent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.android.rdc.criminalintent.CrimePagerActivity.newIntent;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private static final int REQUEST_CRIME = 1;
    private static final String TAG = "CrimeListFragment";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private CrimeLab mCrimeLab;
    private boolean mSubTitleVisible;
/*
    调用 onOptionsItemSelected(MenuItem) 方法时，传入的参数是用户点击的 MenuItem 。虽
    然可以在这个方法里更新Show Subtitle菜单项的文字，但设备旋转重建工具栏时，子标题的变化
    会丢失。

    比较好的解决方法是在 onCreateOptionsMenu(...) 方法内更新Show Subtitle菜单项，并在
    用户点击子标题菜单项时重建工具栏。对于用户选择菜单项或重建工具栏的场景，都可以使用这
    段菜单项更新代码。
*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubTitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.fragment_crime_list, menu);
            MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);
            //菜单创建时，如果子标题是可见的，设置为隐藏
            if (mSubTitleVisible) {
                menuItem.setTitle(R.string.hide_subtitle);
            } else {
            menuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubTitleVisible = !mSubTitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubTitle();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubTitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTv;
        private TextView mDateTv;
        private CheckBox mSolveCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDateTv = (TextView) itemView.findViewById(R.id.tv_date);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
            mSolveCheckBox = (CheckBox) itemView.findViewById(R.id.cb_list_item_crime_solved);
        }

        private void bindCrime(Crime c) {
            mCrime = c;
            mTitleTv.setText(mCrime.getTitle());
            mDateTv.setText(mCrime.getDate().toString());
            mSolveCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            Intent intent = newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(v);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    private class EmptyAdapter extends RecyclerView.Adapter<EmptyHolder> {

        @Override
        public EmptyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_crime_empty, parent, false);
            return new EmptyHolder(v);
        }

        @Override
        public void onBindViewHolder(EmptyHolder holder, int position) {
            holder.btnAddCrime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crime crime = new Crime();
                    mCrimeLab.addCrime(crime);
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private class EmptyHolder extends RecyclerView.ViewHolder {
        Button btnAddCrime;

        public EmptyHolder(View itemView) {
            super(itemView);
            btnAddCrime = (Button) itemView.findViewById(R.id.btn_add_crime);
        }
    }

    private void updateUI() {
        mCrimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = mCrimeLab.getCrimes();
        if (crimes.size() == 0){        //如果没有内容可显示
            mCrimeRecyclerView.setAdapter(new EmptyAdapter());

            return;
        }

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        updateSubTitle();
    }

    private void updateSubTitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeSize = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);
        //根据 mSubtitleVisible 变量值，联动菜单项标题与子标题
        if (!mSubTitleVisible) {
            subtitle = null;
        }

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CRIME) {
            //do sth
        }
    }
}
