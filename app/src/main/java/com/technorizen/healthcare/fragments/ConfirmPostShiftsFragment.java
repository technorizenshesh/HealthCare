package com.technorizen.healthcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.SelectTimeAdapter;
import com.technorizen.healthcare.adapters.SelectTimeStaticAdapter;
import com.technorizen.healthcare.databinding.FragmentConfirmPostShiftsBinding;
import com.technorizen.healthcare.databinding.FragmentPostShiftsBinding;
import com.wisnu.datetimerangepickerandroid.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.MULTIPLE;
import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmPostShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPostShiftsFragment extends Fragment {

    FragmentConfirmPostShiftsBinding binding;
    ArrayAdapter ad;

    List<String> dates = new LinkedList<>();

    boolean visible = false;
    String[] start = {"12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM","11:15 PM","11:30 PM","11:45 PM"};
    String[] end = {"6:00 PM","7:00 PM","8:00 PM","9:00 PM","10:00 PM"};

    String[] jobPosition = {"Select Job Position","Certified Nurse Aid", "Development Support Worker", "Health Care Aide","Home Health Aide",

            "Licensed Practical Nurse","Licensed Vocational Nurse","Massage Therapist","Massage Therapy Assistant",
            "Nurse Practitioner","Personal Care Aide","Personal Support Worker","Pharmacy Technician","Physician Assistant","Physiotherapist",
            "Physiotherapist Assistant","Registered Nurse","Registered Practical Nurse"
    };

    String[] vaccine = new String[100];
    ArrayList<String> hrrate = new ArrayList<>();
    String[] duty = {"Floor Duty","1:1"};
    String[] status = {"Negative","Positive"};
    String[] entry = {"Single Day", "Multi Day"};
    String[] day = {"17 july 2021"};
    String[] unpaid = {"None","30 Minutes","45 Minutes","60 Minutes","120 Minutes"};
    String[] transit = {"None","1 Hour","2 Hour","3 Hour","4 Hour"};
    String[] selectAddress = {"Sh 785 street 22 Varsova","Sh 785 street 22 near Bank"};


    // TODO: RenPMe parPMeter arguments, choose nPMes that match
    // the fragment initialization parPMeters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARPM1 = "parPM1";
    private static final String ARG_PARPM2 = "parPM2";

    // TODO: RenPMe and change types of parPMeters
    private String mParPM1;
    private String mParPM2;

    public ConfirmPostShiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parPMeters.
     *
     * @parPM parPM1 ParPMeter 1.
     * @parPM parPM2 ParPMeter 2.
     * @return A new instance of fragment PostShiftsFragment.
     */
    // TODO: RenPMe and change types and number of parPMeters
    public static ConfirmPostShiftsFragment newInstance(String parPM1, String parPM2) {
        ConfirmPostShiftsFragment fragment = new ConfirmPostShiftsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARPM1, parPM1);
        args.putString(ARG_PARPM2, parPM2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParPM1 = getArguments().getString(ARG_PARPM1);
            mParPM2 = getArguments().getString(ARG_PARPM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_confirm_post_shifts, container, false);


        binding.btnEdit.setOnClickListener(v ->
                {
                    getActivity().onBackPressed();
                }
                );


        dates.clear();
        dates.add("12/4/2021");
        dates.add("13/4/2021");
        dates.add("14/4/2021");
        dates.add("15/4/2021");
        binding.rvDate.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvDate.setHasFixedSize(true);
        binding.rvDate.setAdapter(new SelectTimeStaticAdapter(dates,getActivity()));
        binding.rvDate.setVisibility(View.VISIBLE);
        binding.llSingleDate.setVisibility(View.GONE);


        return binding.getRoot();
    }


}