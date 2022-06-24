package com.shifts.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.databinding.FragmentSetRateBinding;
import com.shifts.healthcare.models.SuccessResGetWorkerProfile;
import com.shifts.healthcare.models.SuccessResUpdateRate;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
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
 * Use the {@link SetRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SetRateFragment extends Fragment {

    FragmentSetRateBinding binding;
    ArrayList<String> hrrate = new ArrayList<>();
    ArrayAdapter ad;

    HealthInterface apiInterface;
    private ArrayList<SuccessResGetWorkerProfile.Result> userList = new ArrayList<>();

    private String strRate = "";

    private String selectedRate = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetRateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetRateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetRateFragment newInstance(String param1, String param2) {
        SetRateFragment fragment = new SetRateFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_set_rate, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        int z=17;
        int m=0;
        while (z<=150)
        {
            hrrate.add("$"+z+"");
            z++;
            m++;
        }
        getProfile();
        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                hrrate);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        binding.spinnerRate.setAdapter(ad);
        binding.spinnerRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnSave.setOnClickListener(v ->
                {
                    strRate = binding.spinnerRate.getSelectedItem().toString();
                    strRate = strRate.substring(1);
                    setRate();
                }
                );
        return binding.getRoot();
    }

    public void setRate()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id", userId);
        map.put("rate", strRate);

        Call<SuccessResUpdateRate> call = apiInterface.setRate(map);

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

                        userList.clear();

                        userList.addAll(data.getResult());

                        setSpinner();

                        //     setUserDetails(userList);

                    } else {
//                        showToast(getActivity(), data.message);
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

    public void setSpinner()
    {

        selectedRate = userList.get(0).getRate();

        if(!selectedRate.equalsIgnoreCase("0"))
        {

            selectedRate = "$"+selectedRate;

            binding.spinnerRate.setSelection(getSpinnerCountryPosition(selectedRate));

        }

    }


    public int getSpinnerCountryPosition(String code)
    {

        int i=0;
        for (String rate : hrrate) {
            if (rate.equalsIgnoreCase(code)) {
                return i;
            }

            i++;
        }
        return 0;
    }




}