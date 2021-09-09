package com.technorizen.healthcare.workerSide.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentAddNewReferenceBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewReferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewReferenceFragment extends Fragment {

    FragmentAddNewReferenceBinding binding;
    private int mYear, mMonth, mDay, mHour, mMinute;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewReferenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewReferenceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewReferenceFragment newInstance(String param1, String param2) {
        AddNewReferenceFragment fragment = new AddNewReferenceFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_new_reference, container, false);

        binding.tvStart.setOnClickListener(v ->
                {
                    showDialog(1);
                }
                );

        binding.tvEnd.setOnClickListener(v ->
                {
                    showDialog(2);
                }
        );
        return binding.getRoot();
    }

    private void showDialog(int i)
    {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        if(i==1)
                        {
                            binding.tvStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                        else {
                            binding.tvEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

}