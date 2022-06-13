package com.shifts.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.CancelledRehireShiftAdapter;
import com.shifts.healthcare.databinding.FragmentCancelledRehireShiftsBinding;
import com.shifts.healthcare.models.SuccessResDeleteShifts;
import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetPost;
import com.shifts.healthcare.models.SuccessResUpdateRehireShift;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.DeleteShifts;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelledRehireShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelledRehireShiftsFragment extends Fragment implements DeleteShifts {

    FragmentCancelledRehireShiftsBinding binding;

    HealthInterface apiInterface;

    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CancelledRehireShiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CancelledRehireShiftsFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static CancelledRehireShiftsFragment newInstance(String param1, String param2) {
        CancelledRehireShiftsFragment fragment = new CancelledRehireShiftsFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cancelled_rehire_shifts, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                    getCancelledRehireShifts();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }

                binding.srlRefreshContainer.setRefreshing(false);
            }
        });

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getCancelledRehireShifts();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    public void getCancelledRehireShifts()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetPost> call = apiInterface.getCancelledRehiredShifts(map);
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
                        binding.rvShifts.setAdapter(new CancelledRehireShiftAdapter(getActivity(),postedList,true,CancelledRehireShiftsFragment.this));

                    } else {
                        postedList.clear();
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new CancelledRehireShiftAdapter(getActivity(),postedList,true,CancelledRehireShiftsFragment.this));

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
    public void onClick(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftsId);
        map.put("status","Deleted");

        Call<SuccessResDeleteShifts> call = apiInterface.deleteShifts(map);
        call.enqueue(new Callback<SuccessResDeleteShifts>() {
            @Override
            public void onResponse(Call<SuccessResDeleteShifts> call, Response<SuccessResDeleteShifts> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResDeleteShifts data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        getCancelledRehireShifts();

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResDeleteShifts> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    @Override
    public void rejectSHift(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {

    }

    @Override
    public void deleteCurrentShiftsClick(String shiftsId, String userId,SuccessResGetCurrentSchedule.PostshiftTime dateTime) {

    }

    @Override
    public void shiftConfimation(String shiftIds, String userId, String workerId, String status) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftIds);

        Call<SuccessResUpdateRehireShift> call = apiInterface.updateRehiredShifts(map);
        call.enqueue(new Callback<SuccessResUpdateRehireShift>() {
            @Override
            public void onResponse(Call<SuccessResUpdateRehireShift> call, Response<SuccessResUpdateRehireShift> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResUpdateRehireShift data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        getCancelledRehireShifts();

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResUpdateRehireShift> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    @Override
    public void userCancelRecruit(String shiftIds, String userId, String workerId, String status) {

    }
}