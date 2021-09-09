package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentWorkerPrivacyAndLegalBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerPrivacyAndLegalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerPrivacyAndLegalFragment extends Fragment {

    FragmentWorkerPrivacyAndLegalBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerPrivacyAndLegalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerPrivacyAndLegalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerPrivacyAndLegalFragment newInstance(String param1, String param2) {
        WorkerPrivacyAndLegalFragment fragment = new WorkerPrivacyAndLegalFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_privacy_and_legal, container, false);

        binding.tabLay.addTab(binding.tabLay.newTab().setText("Privacy Policy"));
        binding.tabLay.addTab(binding.tabLay.newTab().setText("Terms of Service"));
        binding.tabLay.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTabSelected= tab.getPosition();
                if(currentTabSelected==0)
                {

                    binding.ll1.setVisibility(View.VISIBLE);
                    binding.ll2.setVisibility(View.GONE);

                }else if(currentTabSelected==1)
                {
                    //Go for Upcoming
                    binding.ll1.setVisibility(View.GONE);
                    binding.ll2.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        return binding.getRoot();
    }
}