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
import com.shifts.healthcare.adapters.InvoiceSummaryAdapter;
import com.shifts.healthcare.databinding.FragmentAccountStatementBinding;
import com.shifts.healthcare.models.SuccessResAccountDetails;
import com.shifts.healthcare.models.SuccessResGetProfile;
import com.shifts.healthcare.models.SuccessResGetTransactionOverview;
import com.shifts.healthcare.models.SuccessResInvoiceSummaryUser;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountStatementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class AccountStatementFragment extends Fragment {

    FragmentAccountStatementBinding binding;

    private HealthInterface apiInterface;

    private SuccessResAccountDetails.Result accountDetail ;

    private String type = "All";

    private List<SuccessResGetProfile.Result> userList = new LinkedList<>();

    private ArrayList<SuccessResGetTransactionOverview.Result>  paymentList = new ArrayList<>();

    private ArrayList<SuccessResGetTransactionOverview.Result> resultArrayList = new ArrayList<>();

    private ArrayList<SuccessResInvoiceSummaryUser.Result> invoiceSummaryList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountStatementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountStatementFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static AccountStatementFragment newInstance(String param1, String param2) {
        AccountStatementFragment fragment = new AccountStatementFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_account_statement, container, false);
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
        map.put("user_id",userId);
        map.put("type",type);
        Call<SuccessResInvoiceSummaryUser> call = apiInterface.getInvoiceSummary(map);
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
                        binding.rvPayment.setAdapter(new InvoiceSummaryAdapter(getActivity(),invoiceSummaryList,accountDetail));
                    } else {
                        showToast(getActivity(), data.message);
                        invoiceSummaryList.clear();
                        binding.rvPayment.setHasFixedSize(true);
                        binding.rvPayment.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvPayment.setAdapter(new InvoiceSummaryAdapter(getActivity(),invoiceSummaryList,accountDetail));
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

//    public void getAccountDetails()
//    {
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//
//        Call<SuccessResAccountDetails> call = apiInterface.getAccountDetail(map);
//        call.enqueue(new Callback<SuccessResAccountDetails>() {
//            @Override
//            public void onResponse(Call<SuccessResAccountDetails> call, Response<SuccessResAccountDetails> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResAccountDetails data = response.body();
//                    if (data.status.equals("1")) {
//
//                        accountDetail = data.getResult().get(0);
//
//                        getInvoiceSummary();
//
//                    } else {
//                        getInvoiceSummary();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResAccountDetails> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//
//    }



//    public void getPayment()
//    {
//        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id",userId);
//
//        Call<SuccessResGetTransactionOverview> call = apiInterface.getTransactionOverview(map);
//        call.enqueue(new Callback<SuccessResGetTransactionOverview>() {
//            @Override
//            public void onResponse(Call<SuccessResGetTransactionOverview> call, Response<SuccessResGetTransactionOverview> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResGetTransactionOverview data = response.body();
//                    if (data.status.equals("1")) {
//
//                        paymentList.clear();
//
//                        paymentList.addAll(data.getResult());
//
//                        binding.rvPayment.setHasFixedSize(true);
//                        binding.rvPayment.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        binding.rvPayment.setAdapter(new TransactionOverviewAdapter(getActivity(),paymentList));
//
//                    } else {
//                        showToast(getActivity(), data.message);
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResGetTransactionOverview> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//
//    }
/*
    public void getProfile()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetProfile> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetProfile data = response.body();
                    if (data.status.equals("1")) {

                        userList.addAll(data.getResult());
                        setData();

                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }*/

   /* public void setData()
    {

        SuccessResGetProfile.Result user = userList.get(0);

        if (user.getAccountType().equalsIgnoreCase("Company"))
        {
            binding.tvCompany.setText(user.getCompany());
        }else
        {
            binding.tvCompany.setText(user.getFirstName()+" "+user.getLastName());
        }

        binding.tvLocation.setText(user.getPostshiftTime().get(0).getAddress());

        Glide
                .with(getActivity())
                .load(user.getImage())
                .centerCrop()
                .into(binding.ivProfile);
    }*/

}