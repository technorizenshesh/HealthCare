package com.shifts.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.HiredWorkerAdapter;
import com.shifts.healthcare.databinding.FragmentHiresWorkerBinding;
import com.shifts.healthcare.models.SuccessResAddRating;
import com.shifts.healthcare.models.SuccessResBlockUnblock;
import com.shifts.healthcare.models.SuccessResGetHiredWorker;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.BlockAddRatingInterface;
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
 * Use the {@link HiresWorkerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HiresWorkerFragment extends Fragment implements BlockAddRatingInterface {

    FragmentHiresWorkerBinding binding;
    HealthInterface apiInterface;
    private ArrayList<SuccessResGetHiredWorker.Result> hiredWorkerList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HiresWorkerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HiresWorkerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HiresWorkerFragment newInstance(String param1, String param2) {
        HiresWorkerFragment fragment = new HiresWorkerFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_hires_worker, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getHiredWoker();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    public void getHiredWoker()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetHiredWorker> call = apiInterface.getHiredWorker(map);
        call.enqueue(new Callback<SuccessResGetHiredWorker>() {
            @Override
            public void onResponse(Call<SuccessResGetHiredWorker> call, Response<SuccessResGetHiredWorker> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetHiredWorker data = response.body();
                    if (data.status.equals("1")) {

                        hiredWorkerList.clear();
                        hiredWorkerList.addAll(data.getResult());
                        binding.rvHiredWorker.setHasFixedSize(true);
                        binding.rvHiredWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvHiredWorker.setAdapter(new HiredWorkerAdapter(getActivity(),hiredWorkerList,HiresWorkerFragment.this));

                    } else {
//                        showToast(getActivity(), data.message);
                        hiredWorkerList.clear();
                        binding.rvHiredWorker.setHasFixedSize(true);
                        binding.rvHiredWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvHiredWorker.setAdapter(new HiredWorkerAdapter(getActivity(),hiredWorkerList,HiresWorkerFragment.this));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetHiredWorker> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    @Override
    public void block(String worker_id, String status) {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("worker_id",worker_id);
        map.put("status",status);

        Call<SuccessResBlockUnblock> call = apiInterface.addBlockUnblock(map);
        call.enqueue(new Callback<SuccessResBlockUnblock>() {
            @Override
            public void onResponse(Call<SuccessResBlockUnblock> call, Response<SuccessResBlockUnblock> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResBlockUnblock data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResBlockUnblock> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    @Override
    public void addRating(String worker_id, String review, String rating) {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("worker_id",worker_id);
        map.put("review",review);
        map.put("rating",rating);

        Call<SuccessResAddRating> call = apiInterface.addRating(map);
        call.enqueue(new Callback<SuccessResAddRating>() {
            @Override
            public void onResponse(Call<SuccessResAddRating> call, Response<SuccessResAddRating> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResAddRating data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddRating> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }
}