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
import com.technorizen.healthcare.databinding.FragmentConfirmPostRequitmentShiftsBinding;
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
 * Use the {@link ConfirmPostRequitmentShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPostRequitmentShiftsFragment extends Fragment {

    FragmentConfirmPostRequitmentShiftsBinding binding;
    ArrayAdapter ad;

    List<String> dates = new LinkedList<>();

    boolean visible = false;
    String[] start = {"6:00 AM","7:00 AM","8:00 AM","9:00 AM","10:00 AM"};
    String[] end = {"6:00 AM","7:00 AM","8:00 AM","9:00 AM","10:00 AM"};

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


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmPostRequitmentShiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostShiftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmPostRequitmentShiftsFragment newInstance(String param1, String param2) {
        ConfirmPostRequitmentShiftsFragment fragment = new ConfirmPostRequitmentShiftsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_confirm_post_requitment_shifts, container, false);

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