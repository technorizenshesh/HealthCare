package com.shifts.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.InvoiceSummaryWorkerAdapter;
import com.shifts.healthcare.databinding.FragmentWorkerInvoiceSummaryBinding;
import com.shifts.healthcare.models.SuccessResInvoiceSummaryUser;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
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
 * Use the {@link WorkerInvoiceSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class WorkerInvoiceSummaryFragment extends Fragment {

    FragmentWorkerInvoiceSummaryBinding binding;
    private HealthInterface apiInterface;
    private ArrayList<SuccessResInvoiceSummaryUser.Result> invoiceSummaryList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String type = "All";

    public WorkerInvoiceSummaryFragment() {
    }
    // TODO: Rename and change types and number of parameters
    public static WorkerInvoiceSummaryFragment newInstance(String param1, String param2) {
        WorkerInvoiceSummaryFragment fragment = new WorkerInvoiceSummaryFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_invoice_summary, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        binding.tvAll.setOnClickListener(v ->
                {
                    type  = "All";
                    binding.tvAll.setBackground(getResources().getDrawable(R.drawable.button_bg));
                    binding.tvAll.setTextColor(getResources().getColor(R.color.white));
                    binding.tvUnpaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
                    binding.tvUnpaid.setTextColor(getResources().getColor(R.color.black));
                    binding.tvPaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
                    binding.tvPaid.setTextColor(getResources().getColor(R.color.black));
                    getInvoiceSummary();
                }
        );

        binding.tvUnpaid.setOnClickListener(v ->
                {
                    type  = "Unpaid";
                    binding.tvUnpaid.setBackground(getResources().getDrawable(R.drawable.button_bg));
                    binding.tvUnpaid.setTextColor(getResources().getColor(R.color.white));
                    binding.tvAll.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
                    binding.tvAll.setTextColor(getResources().getColor(R.color.black));
                    binding.tvPaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
                    binding.tvPaid.setTextColor(getResources().getColor(R.color.black));
                    getInvoiceSummary();
                }
        );
        binding.tvPaid.setOnClickListener(v ->
                {
                    type  = "Paid";
                    binding.tvPaid.setBackground(getResources().getDrawable(R.drawable.button_bg));
                    binding.tvPaid.setTextColor(getResources().getColor(R.color.white));
                    binding.tvAll.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
                    binding.tvAll.setTextColor(getResources().getColor(R.color.black));
                    binding.tvUnpaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
                    binding.tvUnpaid.setTextColor(getResources().getColor(R.color.black));
                    getInvoiceSummary();
                }
        );
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getInvoiceSummary();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    public void getInvoiceSummary()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("type",type);
        Call<SuccessResInvoiceSummaryUser> call = apiInterface.getWorkerInvoiceSummary(map);
        call.enqueue(new Callback<SuccessResInvoiceSummaryUser>() {
            @Override
            public void onResponse(Call<SuccessResInvoiceSummaryUser> call, Response<SuccessResInvoiceSummaryUser> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInvoiceSummaryUser data = response.body();
                    if (data.status.equals("1")) {
                        invoiceSummaryList.clear();
                        invoiceSummaryList.addAll(data.getResult());
                        binding.rvPayment.setHasFixedSize(true);
                        binding.rvPayment.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPayment.setAdapter(new InvoiceSummaryWorkerAdapter(getActivity(),invoiceSummaryList));
                    } else {
//                        showToast(getActivity(), data.message);

                        invoiceSummaryList.clear();
                        binding.rvPayment.setHasFixedSize(true);
                        binding.rvPayment.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPayment.setAdapter(new InvoiceSummaryWorkerAdapter(getActivity(),invoiceSummaryList));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResInvoiceSummaryUser> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
}