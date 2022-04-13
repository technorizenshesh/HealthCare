package com.technorizen.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentWorkerNotificationBinding;
import com.technorizen.healthcare.models.SuccessResGetWorkerProfile;
import com.technorizen.healthcare.models.SuccessResUpdateRate;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
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
 * Use the {@link WorkerNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerNotificationFragment extends Fragment {

    FragmentWorkerNotificationBinding binding;

    HealthInterface apiInterface;

    private String push = "",sms = "",mail = "";
    private String selectedPush = "",selectedms = "",selectedMail = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerNotificationFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static WorkerNotificationFragment newInstance(String param1, String param2) {
        WorkerNotificationFragment fragment = new WorkerNotificationFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_notification, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        getProfile();

        binding.btnSave.setOnClickListener(v ->
                {
                    updateNotifications();
                }
                );

        return binding.getRoot();
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

                        selectedPush = data.getResult().get(0).getPsusNotification();
                        selectedms = data.getResult().get(0).getSmsNotification();
                        selectedMail = data.getResult().get(0).getEmailNotification();
                        setNotifications();

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

    public void updateNotifications()
    {

        if(binding.switchPush.isChecked())
        {
            push = "1";
        }
        else
        {
            push = "0";
        }


        if(binding.switchSms.isChecked())
        {
            sms = "1";
        }
        else
        {
            sms = "0";

        }

        if(binding.switchEmail.isChecked())
        {
            mail = "1";
        }
        else
        {
            mail = "0";
        }


        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("psus_notification", push);
        map.put("sms_notification", sms);
        map.put("email_notification", mail);

        Call<SuccessResUpdateRate> call = apiInterface.updateWorkerNoti(map);

        call.enqueue(new Callback<SuccessResUpdateRate>() {
            @Override
            public void onResponse(Call<SuccessResUpdateRate> call, Response<SuccessResUpdateRate> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateRate data = response.body();
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
            public void onFailure(Call<SuccessResUpdateRate> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    public void setNotifications()
    {
        if(selectedPush.equalsIgnoreCase("0"))
        {
            binding.switchPush.setChecked(false);
        }else

        {
            binding.switchPush.setChecked(true);

        }


        if(selectedms.equalsIgnoreCase("0"))
        {
            binding.switchSms.setChecked(false);
        }else

        {
            binding.switchSms.setChecked(true);

        }

        if(selectedMail.equalsIgnoreCase("0"))
        {
            binding.switchEmail.setChecked(false);
        }else

        {
            binding.switchEmail.setChecked(true);

        }


    }


}