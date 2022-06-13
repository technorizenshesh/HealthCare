package com.shifts.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.PaymentHistoryAdapter;
import com.shifts.healthcare.databinding.FragmentReportBinding;
import com.shifts.healthcare.models.SuccessResGetTransactionHistory;
import com.shifts.healthcare.models.SuccessResGetWorkerProfile;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    FragmentReportBinding binding;
    String[] year = {"Year 2021"};
    ArrayAdapter ad;

    HealthInterface apiInterface;
    private ArrayList<SuccessResGetTransactionHistory.Result> paymentList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_report, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

           getProfile();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    public void setYear(String text) throws ParseException {

        String lastFourDigits = "";     //substring containing last 4 characters

        if (text.length() > 4)
        {
            lastFourDigits = text.substring(text.length() - 4);
        }
        else
        {
            lastFourDigits = text;
        }

        int myYear = Integer.parseInt(lastFourDigits);


        binding.btnGenerate.setOnClickListener(v ->
                {
                    getPayment(binding.spinnerYear.getSelectedItem().toString());
                }
                );

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = myYear; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                years);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerYear.setAdapter(ad);

        getPayment(binding.spinnerYear.getSelectedItem().toString());

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

                       setYear(data.getResult().get(0).getApprovalDate());

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


    public void getPayment(String year)
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("year",year);

        Call<SuccessResGetTransactionHistory> call = apiInterface.getPaymentsHistoryByYear(map);
        call.enqueue(new Callback<SuccessResGetTransactionHistory>() {
            @Override
            public void onResponse(Call<SuccessResGetTransactionHistory> call, Response<SuccessResGetTransactionHistory> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetTransactionHistory data = response.body();
                    if (data.status.equals("1")) {

                        paymentList.clear();

                        paymentList.addAll(data.getResult());

                        binding.rvPayment.setHasFixedSize(true);
                        binding.rvPayment.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPayment.setAdapter(new PaymentHistoryAdapter(getActivity(),paymentList));

                    } else {
//                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetTransactionHistory> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }



}