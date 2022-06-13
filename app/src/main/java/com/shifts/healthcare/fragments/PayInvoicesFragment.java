package com.shifts.healthcare.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.PayInvoiceAdapter;
import com.shifts.healthcare.databinding.FragmentPayInvoicesBinding;
import com.shifts.healthcare.models.SuccessResAddCardDetails;
import com.shifts.healthcare.models.SuccessResGetCardDetails;
import com.shifts.healthcare.models.SuccessResGetInvoices;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.PayInvoiceInterface;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayInvoicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayInvoicesFragment extends Fragment implements PayInvoiceInterface {

    FragmentPayInvoicesBinding binding;

    AppCompatButton btnAdd,btnPay;

    CreditCardView card5;
    RecyclerView rvCards;

    private SuccessResGetCardDetails.Result cardDetails;

    private Dialog dialog;

    String cardNo ="",expirationDate="",cvv = "",cardType = "",holderName="",expirationMonth = "",expirationYear = "";


    HealthInterface apiInterface;

    private String myInvoceId = "", myAmount = "", myUserId ="", myWorkerId ="", myShiftId="";

    private ArrayList<SuccessResGetInvoices.Result> invoicesList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PayInvoicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayInvoicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PayInvoicesFragment newInstance(String param1, String param2) {
        PayInvoicesFragment fragment = new PayInvoicesFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pay_invoices, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        getUnpaidInvoices();

        return binding.getRoot();
    }

    public void getUnpaidInvoices()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type","Unpaid");

        Call<SuccessResGetInvoices> call = apiInterface.getUserInvoice(map);
        call.enqueue(new Callback<SuccessResGetInvoices>() {
            @Override
            public void onResponse(Call<SuccessResGetInvoices> call, Response<SuccessResGetInvoices> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetInvoices data = response.body();
                    if (data.status.equals("1")) {

                        invoicesList.clear();
                        invoicesList.addAll(data.getResult());
                        binding.rvInvoices.setHasFixedSize(true);
                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvInvoices.setAdapter(new PayInvoiceAdapter(getActivity(),invoicesList,PayInvoicesFragment.this));

                    } else {
                        showToast(getActivity(), data.message);
                        invoicesList.clear();
                        binding.rvInvoices.setHasFixedSize(true);
                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvInvoices.setAdapter(new PayInvoiceAdapter(getActivity(),invoicesList,PayInvoicesFragment.this));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetInvoices> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    @Override
    public void payInvoice(View v,String invoceId, String amount, String userId, String workerId, String shiftId) {

//        Bundle bundle = new Bundle();
//        bundle.putString("from","pay");
//        bundle.putString("invoiceId",invoceId);
//        bundle.putString("amount",amount);
//        bundle.putString("userId",userId);
//        bundle.putString("workerId",workerId);
//        bundle.putString("shiftId",shiftId);
//        Navigation.findNavController(v).navigate(R.id.action_payInvoicesFragment_to_addCreditCardFragment,bundle);

    }

    private void fullScreenDialog() {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.dialog_add_card);

        btnAdd =  dialog.findViewById(R.id.btnAdd);
        btnPay =  dialog.findViewById(R.id.btnAdd);

        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.ivBack);

        CardForm cardForm = dialog.findViewById(R.id.card_form);

        getCards();

        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity) getActivity());

        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {

                cardNo = cardForm.getCardNumber();
                expirationDate = cardForm.getExpirationMonth()+"/"+cardForm.getExpirationYear();
                cvv = cardForm.getCvv();
                cardType = "";
                holderName = cardForm.getCardholderName();

                if(cardForm.isValid())
                {

                    addCardDetails();

                }else
                {
                    cardForm.validate();
                }
            }
        });

        btnAdd.setOnClickListener(v ->
                {

                    cardNo = cardForm.getCardNumber();
                    expirationDate = cardForm.getExpirationDateEditText().getText().toString();
                    cvv = cardForm.getCvv();
                    cardType = "";
                    expirationMonth = cardForm.getExpirationMonth();
                    holderName = cardForm.getCardholderName();
                    expirationYear = cardForm.getExpirationYear();

                    if(cardForm.isValid())
                    {
                        addCardDetails();

                    }else
                    {
                        cardForm.validate();
                    }
                }
        );

        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        dialog.show();

    }

    private void addCardDetails()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("card_no",cardNo);
        map.put("exp_date",expirationDate);
        map.put("exp_month",expirationMonth);
        map.put("exp_year",expirationYear);
        map.put("card_holder_name",holderName);

        Log.e(TAG,"Test Request "+map);
        Call<SuccessResAddCardDetails> loginCall = apiInterface.addCard(map);
        loginCall.enqueue(new Callback<SuccessResAddCardDetails>() {
            @Override
            public void onResponse(Call<SuccessResAddCardDetails> call, Response<SuccessResAddCardDetails> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAddCardDetails data = response.body();
                    String responseString = new Gson().toJson(response.body());

                    showToast(getActivity(),data.message);
                    //   getActivity().onBackPressed();
                    dialog.dismiss();

                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddCardDetails> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void getCards()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCardDetails> call = apiInterface.getCards(map);
        call.enqueue(new Callback<SuccessResGetCardDetails>() {
            @Override
            public void onResponse(Call<SuccessResGetCardDetails> call, Response<SuccessResGetCardDetails> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCardDetails data = response.body();
                    if (data.status.equals("1")) {

                        btnPay.setVisibility(View.VISIBLE);
                        btnAdd.setVisibility(View.GONE);

                        card5.setVisibility(View.VISIBLE);
                        cardDetails = data.getResult().get(data.getResult().size()-1);

                        card5.setCardHolderName(cardDetails.getCardHolderName());
                        card5.setCardNumber(cardDetails.getCardNo());
                        card5.setCardExpiry(cardDetails.getExpDate()+""+cardDetails.getExpMonth());

                    } else {
                        showToast(getActivity(), data.message);
                        card5.setVisibility(View.GONE);
                        btnPay.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.VISIBLE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetCardDetails> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

}