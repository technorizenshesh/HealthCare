package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentWorkerAvailableJobsFragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerAvailableJobsFragmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerAvailableJobsFragmentFragment extends Fragment {

    FragmentWorkerAvailableJobsFragmentBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerAvailableJobsFragmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerAvailableJobsFragmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerAvailableJobsFragmentFragment newInstance(String param1, String param2) {
        WorkerAvailableJobsFragmentFragment fragment = new WorkerAvailableJobsFragmentFragment();
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
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_worker_available_jobs_fragment, container, false);

        binding.tvSortByDistance.setOnClickListener(v ->
                {
                    binding.tvSortByDistance.setBackgroundResource(R.drawable.button_bg);
                    binding.tvSortByTime.setBackgroundResource(0);
                    binding.tvSortByTime.setTextColor(getResources().getColor(R.color.black));
                    binding.tvSortByDistance.setTextColor(getResources().getColor(R.color.white));
                }
        );

        binding.tvSortByTime.setOnClickListener(v ->
                {
                    binding.tvSortByTime.setBackgroundResource(R.drawable.button_bg);
                    binding.tvSortByDistance.setBackgroundResource(0);
                    binding.tvSortByDistance.setTextColor(getResources().getColor(R.color.black));
                    binding.tvSortByTime.setTextColor(getResources().getColor(R.color.white));
                }
        );


        return binding.getRoot();
    }
}