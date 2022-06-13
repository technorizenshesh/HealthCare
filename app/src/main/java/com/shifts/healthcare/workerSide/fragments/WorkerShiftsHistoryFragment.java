package com.shifts.healthcare.workerSide.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.ShiftsHistoryWorkerAdapter;
import com.shifts.healthcare.databinding.FragmentWorkerShiftsHistoryBinding;
import com.shifts.healthcare.models.SuccessResGetShiftInProgress;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerShiftsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class WorkerShiftsHistoryFragment extends Fragment {


    LocalBroadcastManager lbm;

    FragmentWorkerShiftsHistoryBinding binding;
    HealthInterface apiInterface;
    private ArrayList<SuccessResGetShiftInProgress.Result> shiftInProgressList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerShiftsHistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WorkerShiftsHistoryFragment newInstance(String param1, String param2) {
        WorkerShiftsHistoryFragment fragment = new WorkerShiftsHistoryFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_shifts_history, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getShiftHistory();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShiftHistory();
                binding.srlRefreshContainer.setRefreshing(false);
            }
        });

        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("filter_string_1"));

        return binding.getRoot();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
//                String str = intent.getStringExtra("key");
//                getUnseenNotificationCount();
                // get all your data from intent and do what you want
                getShiftHistory();
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);

    }


    public void getShiftHistory()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        Call<SuccessResGetShiftInProgress> call = apiInterface.getWorkerShiftsHistory(map);
        call.enqueue(new Callback<SuccessResGetShiftInProgress>() {
            @Override
            public void onResponse(Call<SuccessResGetShiftInProgress> call, Response<SuccessResGetShiftInProgress> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetShiftInProgress data = response.body();
                    if (data.status.equals("1")) {
                        //      getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        shiftInProgressList.clear();
                        shiftInProgressList.addAll(data.getResult());
                        setShiftList();
                    } else {
//                        showToast(getActivity(), data.message);
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
    public void setShiftList()
    {
        if(shiftInProgressList.size()>10)
        {
            ArrayList<SuccessResGetShiftInProgress.Result> subList = new ArrayList<>(shiftInProgressList.subList(0,9));
            binding.rvShiftInProgress.setHasFixedSize(true);
            binding.rvShiftInProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShiftInProgress.setAdapter(new ShiftsHistoryWorkerAdapter(getActivity(),subList,false,"workerHistory"));
            binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.rvShiftInProgress.setHasFixedSize(true);
            binding.rvShiftInProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShiftInProgress.setAdapter(new ShiftsHistoryWorkerAdapter(getActivity(),shiftInProgressList,false,"workerHistory"));
            binding.btnLoadMoreShifts.setVisibility(View.GONE);
        }

        binding.btnLoadMoreShifts.setOnClickListener(v ->
                {
                    binding.btnLoadMoreShifts.setVisibility(View.GONE);
                    binding.btnLoadLessShifts.setVisibility(View.VISIBLE);
                    binding.rvShiftInProgress.setHasFixedSize(true);
                    binding.rvShiftInProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShiftInProgress.setAdapter(new ShiftsHistoryWorkerAdapter(getActivity(),shiftInProgressList,false,"workerHistory"));
                }
        );

        binding.btnLoadLessShifts.setOnClickListener(v ->
                {
                    binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);
                    binding.btnLoadLessShifts.setVisibility(View.GONE);
                    ArrayList<SuccessResGetShiftInProgress.Result> subList = new ArrayList<>(shiftInProgressList.subList(0,9));
                    binding.rvShiftInProgress.setHasFixedSize(true);
                    binding.rvShiftInProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShiftInProgress.setAdapter(new ShiftsHistoryWorkerAdapter(getActivity(),subList,false,"workerHistory"));
                }
        );
    }
}