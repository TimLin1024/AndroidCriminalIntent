package com.android.rdc.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.android.rdc.criminalintent.CrimeDateDialogActivity.newIntent;
import static com.android.rdc.criminalintent.CrimePagerActivity.EXTRA_CRIME_ID;
import static com.android.rdc.criminalintent.DatePickerFragment.EXTRA_DATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";
    private EditText mEtTitleField;
    private Button mBtnDate;
    private Button mBtnTime;
    private CheckBox mSolvedCheckBox;
    private Crime mCrime;
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String ARG_CRIME = "crime_id";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    public CrimeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime.getId());
                getActivity().finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mEtTitleField = (EditText) v.findViewById(R.id.et_crime_title);
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.cb_crime_solved);
        mBtnDate = (Button) v.findViewById(R.id.btn_crime_date);
        mBtnTime = (Button) v.findViewById(R.id.btn_crime_time);
        if (mCrime != null) {
            mEtTitleField.setText(mCrime.getTitle());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            updateDate();
        }
        mEtTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
//                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
//                datePickerFragment.show(fragmentManager, DIALOG_DATE);
                Intent intent = CrimeDateDialogActivity.newIntent(getActivity(), mCrime.getDate());
                startActivityForResult(intent, REQUEST_DATE);
            }
        });
        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(null);
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(fragmentManager, DIALOG_TIME);
            }
        });

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }

    public static Fragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CRIME_ID, id);

        Fragment fragment = new CrimeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_TIME) {

        }
    }

    private void updateDate() {
        mBtnDate.setText(mCrime.getDate().toString());
    }

    private void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }
}
