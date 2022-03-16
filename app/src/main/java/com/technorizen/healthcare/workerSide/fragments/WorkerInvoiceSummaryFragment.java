package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.InvoiceSummaryAdapter;
import com.technorizen.healthcare.adapters.InvoiceSummaryWorkerAdapter;
import com.technorizen.healthcare.databinding.FragmentWorkerInvoiceSummaryBinding;
import com.technorizen.healthcare.models.SuccessResGetProfile;
import com.technorizen.healthcare.models.SuccessResGetTransactionOverview;
import com.technorizen.healthcare.models.SuccessResInvoiceSummaryUser;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

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
        getInvoiceSummary();
        return binding.getRoot();
    }
    public void getInvoiceSummary()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
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
                        showToast(getActivity(), data.message);
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