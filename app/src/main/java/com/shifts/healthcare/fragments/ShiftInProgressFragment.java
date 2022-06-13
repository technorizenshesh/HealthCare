package com.shifts.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.UserShiftsInProgressAdapter;
import com.shifts.healthcare.databinding.FragmentShiftInProgressBinding;
import com.shifts.healthcare.models.SuccessResGetShiftInProgress;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.SharedPreferenceUtility;
import com.shifts.healthcare.util.ShiftCompletedClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShiftInProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShiftInProgressFragment extends Fragment implements ShiftCompletedClick {

    FragmentShiftInProgressBinding binding;
    private HealthInterface apiInterface;
    private ArrayList<SuccessResGetShiftInProgress.Result> shiftInProgressList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ShiftInProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShiftInProgressFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ShiftInProgressFragment newInstance(String param1, String param2) {
        ShiftInProgressFragment fragment = new ShiftInProgressFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shift_in_progress, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        getShiftsInProgress();
        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShiftsInProgress();
                binding.srlRefreshContainer.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }

    public void getShiftsInProgress()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetShiftInProgress> call = apiInterface.getUserShiftsInProgress(map);
        call.enqueue(new Callback<SuccessResGetShiftInProgress>() {
            @Override
            public void onResponse(Call<SuccessResGetShiftInProgress> call, Response<SuccessResGetShiftInProgress> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetShiftInProgress data = response.body();
                    if (data.status.equals("1")) {
                        binding.llProgress.setVisibility(View.VISIBLE);
                        shiftInProgressList.clear();
                        shiftInProgressList.addAll(data.getResult());
                        binding.rvShiftInProgress.setHasFixedSize(true);
                        binding.rvShiftInProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShiftInProgress.setAdapter(new UserShiftsInProgressAdapter(getActivity(),shiftInProgressList,true,"usershiftInProgress",ShiftInProgressFragment.this::getClick));
                    } else {
//                        showToast(getActivity(), data.message);
                        binding.llProgress.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetShiftInProgress> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void getClick() {

        getShiftsInProgress();

    }
}