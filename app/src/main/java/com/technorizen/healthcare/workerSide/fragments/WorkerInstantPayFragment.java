package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentWorkerInstantPayBinding;
import com.technorizen.healthcare.models.SuccessResGetWorkerProfile;
import com.technorizen.healthcare.models.SuccessResUpdateInstantPay;
import com.technorizen.healthcare.models.SuccessResUpdateRate;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.NetworkAvailablity;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerInstantPayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerInstantPayFragment extends Fragment {

    FragmentWorkerInstantPayBinding binding;

    private String intstantPay = "0";

    private HealthInterface apiInterface;

    private String selectedNoti = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerInstantPayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerInstantPayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerInstantPayFragment newInstance(String param1, String param2) {
        WorkerInstantPayFragment fragment = new WorkerInstantPayFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_instant_pay, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getProfile();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.btnSave.setOnClickListener(v ->
                {

                    if(binding.switchInstant.isChecked())
                    {
                        intstantPay = "1";
                    }
                    else
                    {
                        intstantPay = "0";
                    }

                    addIntantPay();

                }
                );

        return binding.getRoot();
    }

    public void addIntantPay()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id", userId);
        map.put("instant_pay", intstantPay);

        Call<SuccessResUpdateInstantPay> call = apiInterface.updateInstant(map);

        call.enqueue(new Callback<SuccessResUpdateInstantPay>() {
            @Override
            public void onResponse(Call<SuccessResUpdateInstantPay> call, Response<SuccessResUpdateInstantPay> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateInstantPay data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        String dataResponse = new Gson().toJson(response.body());

                        getProfile();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);
                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateInstantPay> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void getProfile()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResGetWorkerProfile> call = apiInterface.getWorkerProfile(map);
        call.enqueue(new Callback<SuccessResGetWorkerProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetWorkerProfile> call, Response<SuccessResGetWorkerProfile> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetWorkerProfile data = response.body();
                    if (data.status.equals("1")) {

                        //     setUserDetails(userList);

                        selectedNoti = data.getResult().get(0).getInstantPay();

                        if(selectedNoti.equalsIgnoreCase("0"))
                        {
                            binding.switchInstant.setChecked(false);
                        }
                        else
                        {
                            binding.switchInstant.setChecked(true);
                        }

                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetWorkerProfile> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

}