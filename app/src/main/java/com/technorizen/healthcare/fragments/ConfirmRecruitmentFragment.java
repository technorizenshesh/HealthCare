package com.technorizen.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ConfirmRecruitmentAdapter;
import com.technorizen.healthcare.databinding.FragmentConfirmRecruitmentBinding;
import com.technorizen.healthcare.models.SuccessResAcceptRejectRecruitment;
import com.technorizen.healthcare.models.SuccessResAcceptShift;
import com.technorizen.healthcare.models.SuccessResDeleteShifts;
import com.technorizen.healthcare.models.SuccessResGetCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResGetPendingRecruitmentShift;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.DeleteShifts;
import com.technorizen.healthcare.util.NetworkAvailablity;
import com.technorizen.healthcare.util.RecruitmentShiftConfirmationInterface;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmRecruitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmRecruitmentFragment extends Fragment implements RecruitmentShiftConfirmationInterface {

    FragmentConfirmRecruitmentBinding binding;

    private ArrayList<SuccessResGetCurrentSchedule.Result> confirmRecruitmentList = new ArrayList<>();

    private HealthInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmRecruitmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmRecruitmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmRecruitmentFragment newInstance(String param1, String param2) {
        ConfirmRecruitmentFragment fragment = new ConfirmRecruitmentFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_confirm_recruitment, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                    getShifts();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }

                binding.srlRefreshContainer.setRefreshing(false);
            }
        });

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getShifts();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    public void getShifts()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCurrentSchedule> call = apiInterface.getPendingRecruitmentShifts(map);
        call.enqueue(new Callback<SuccessResGetCurrentSchedule>() {
            @Override
            public void onResponse(Call<SuccessResGetCurrentSchedule> call, Response<SuccessResGetCurrentSchedule> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCurrentSchedule data = response.body();
                    if (data.status.equals("1")) {

                        confirmRecruitmentList.clear();
                        confirmRecruitmentList.addAll(data.getResult());
                        binding.rvRecruitmentConfirmation.setHasFixedSize(true);
                        binding.rvRecruitmentConfirmation.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvRecruitmentConfirmation.setAdapter(new ConfirmRecruitmentAdapter(getActivity(),confirmRecruitmentList,ConfirmRecruitmentFragment.this,true));

                    } else {
                        confirmRecruitmentList.clear();
                        binding.rvRecruitmentConfirmation.setHasFixedSize(true);
                        binding.rvRecruitmentConfirmation.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvRecruitmentConfirmation.setAdapter(new ConfirmRecruitmentAdapter(getActivity(),confirmRecruitmentList,ConfirmRecruitmentFragment.this,true));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCurrentSchedule> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }


    @Override
    public void recruitConfirmation(View v,String shiftIds, String userId, String workerId, String status,SuccessResGetCurrentSchedule.Result result) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftIds);
        map.put("worker_id",workerId);
        map.put("status",status);
        map.put("user_id",userId);

        Call<SuccessResAcceptRejectRecruitment> call = apiInterface.acceptRejectRecruitment(map);
        call.enqueue(new Callback<SuccessResAcceptRejectRecruitment>() {
            @Override
            public void onResponse(Call<SuccessResAcceptRejectRecruitment> call, Response<SuccessResAcceptRejectRecruitment> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResAcceptRejectRecruitment data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        getShifts();


                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResAcceptRejectRecruitment> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    @Override
    public void cancelRecruitShift(String shiftIds, String userId, String workerId, String status) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftIds);
        map.put("worker_id",workerId);
        map.put("status","Rejected");
        map.put("user_id",userId);

        Call<SuccessResAcceptRejectRecruitment> call = apiInterface.acceptRejectRecruitment(map);
        call.enqueue(new Callback<SuccessResAcceptRejectRecruitment>() {
            @Override
            public void onResponse(Call<SuccessResAcceptRejectRecruitment> call, Response<SuccessResAcceptRejectRecruitment> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResAcceptRejectRecruitment data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        getShifts();

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResAcceptRejectRecruitment> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });


    }
}