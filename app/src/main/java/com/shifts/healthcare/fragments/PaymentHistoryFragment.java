package com.shifts.healthcare.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.PaymentHistoryAdapter;
import com.shifts.healthcare.databinding.FragmentPaymentHistoryBinding;
import com.shifts.healthcare.models.SuccessResGetTransactionHistory;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentHistoryFragment extends Fragment {

    FragmentPaymentHistoryBinding binding;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String strTransactionDate= "";
    private String strTransactionDate1= "";
    private HealthInterface apiInterface;

    private ArrayList<SuccessResGetTransactionHistory.Result> paymentList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PaymentHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentHistoryFragment newInstance(String param1, String param2) {
        PaymentHistoryFragment fragment = new PaymentHistoryFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_payment_history, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        getPayment();

        binding.etSelectDate.setOnClickListener(v ->
                {
                    showDialog();
                }
                );

        return binding.getRoot();
    }

    public void searchPaymentHistory()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("transection_date",strTransactionDate1);


        Call<SuccessResGetTransactionHistory> call = apiInterface.searchPaymentHistory(map);
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
                        showToast(getActivity(), data.message);
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

    public void getPayment()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetTransactionHistory> call = apiInterface.getPaymentsHistory(map);
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
                        showToast(getActivity(), data.message);
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

    private void showDialog()
    {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        binding.etSelectDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        strTransactionDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                        Date date = null;
                        try {
                             date = format.parse(strTransactionDate);
                            System.out.println(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        strTransactionDate1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
                        searchPaymentHistory();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

}