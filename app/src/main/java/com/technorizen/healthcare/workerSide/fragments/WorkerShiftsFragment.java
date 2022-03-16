package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentWorkerShiftsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerShiftsFragment extends Fragment {

    FragmentWorkerShiftsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerShiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerShiftsFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static WorkerShiftsFragment newInstance(String param1, String param2) {
        WorkerShiftsFragment fragment = new WorkerShiftsFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_shifts, container, false);

        binding.tvShiftInProgress.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_workerShiftsFragment_to_workerShiftsInProgressFragment);

                }
        );

        binding.tvCurrent.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_workerShiftsFragment_to_workerCurrentScheduleFragment);
                }
        );

        binding.tvPost.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_workerShiftsFragment_to_workerAvailableJobsFragmentFragment);

                }
        );

        binding.tvShiftHistory.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_workerShiftsFragment_to_workerShiftsHistoryFragment);

                }
        );

        binding.tvPendingRecruitmentShift.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_workerShiftsFragment_to_workerPendingRecruitmentShiftFragment);

                }
        );

        return binding.getRoot();
    }
}