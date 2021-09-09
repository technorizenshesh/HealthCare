package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ShiftsAdapter;
import com.technorizen.healthcare.databinding.FragmentWorkerHomeBinding;
import com.technorizen.healthcare.fragments.HomeFragment;
import com.technorizen.healthcare.models.SuccessResAcceptShift;
import com.technorizen.healthcare.models.SuccessResDeleteShifts;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.DeleteShifts;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerHomeFragment extends Fragment implements DeleteShifts {

    FragmentWorkerHomeBinding binding;
    HealthInterface apiInterface;
    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerHomeFragment newInstance(String param1, String param2) {
        WorkerHomeFragment fragment = new WorkerHomeFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_home, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

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

        getWorkerShifts();

        return binding.getRoot();
    }

    public void getWorkerShifts()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResGetPost> call = apiInterface.getWorkerShift(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {

                        postedList.clear();
                        postedList.addAll(data.getResult());
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false, WorkerHomeFragment.this::onClick));

                    } else {
                        showToast(getActivity(), data.message);
                        postedList.clear();
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false,WorkerHomeFragment.this::onClick));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetPost> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    @Override
    public void onClick(String shiftsId) {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftsId);
        map.put("worker_id",userId);
        map.put("status","Accepted");

        Call<SuccessResAcceptShift> call = apiInterface.acceptShift(map);
        call.enqueue(new Callback<SuccessResAcceptShift>() {
            @Override
            public void onResponse(Call<SuccessResAcceptShift> call, Response<SuccessResAcceptShift> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResAcceptShift data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        getWorkerShifts();

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResAcceptShift> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }
}