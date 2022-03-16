package com.technorizen.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentPrivacyAndLegacyBinding;
import com.technorizen.healthcare.models.SuccessResPrivacyPolicy;
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
 * Use the {@link PrivacyAndLegacyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyAndLegacyFragment extends Fragment {

    FragmentPrivacyAndLegacyBinding binding;
    private HealthInterface apiInterface;

    String description = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrivacyAndLegacyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivacyAndLegacyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivacyAndLegacyFragment newInstance(String param1, String param2) {
        PrivacyAndLegacyFragment fragment = new PrivacyAndLegacyFragment();
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
        
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_privacy_and_legacy, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        getPrivacyPolicy();

        binding.tabLay.addTab(binding.tabLay.newTab().setText("Privacy Policy"));
        binding.tabLay.addTab(binding.tabLay.newTab().setText("Terms of Service"));
        binding.tabLay.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentTabSelected= tab.getPosition();
                if(currentTabSelected==0)
                {

                    getPrivacyPolicy();

                }else if(currentTabSelected==1)
                {
                    //Go for Upcoming
                    getTermsOfUse();
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

    private void getPrivacyPolicy() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResPrivacyPolicy> call = apiInterface.getPrivacyPolicy(map);

        call.enqueue(new Callback<SuccessResPrivacyPolicy>() {
            @Override
            public void onResponse(Call<SuccessResPrivacyPolicy> call, Response<SuccessResPrivacyPolicy> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResPrivacyPolicy data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        description = data.getResult().getDescription();
                        setWebView();


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
            public void onFailure(Call<SuccessResPrivacyPolicy> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private void getTermsOfUse() {


        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResPrivacyPolicy> call = apiInterface.getTermsOfUse(map);

        call.enqueue(new Callback<SuccessResPrivacyPolicy>() {
            @Override
            public void onResponse(Call<SuccessResPrivacyPolicy> call, Response<SuccessResPrivacyPolicy> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResPrivacyPolicy data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        description = data.getResult().getDescription();
                        setWebView();


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
            public void onFailure(Call<SuccessResPrivacyPolicy> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private void setWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadData(description, "text/html; charset=utf-8", "UTF-8");

    }

}