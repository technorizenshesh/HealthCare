package com.technorizen.healthcare.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentAddCreditCardBinding;
import com.technorizen.healthcare.models.SuccessResAddCardDetails;
import com.technorizen.healthcare.models.SuccessResGetCardDetails;
import com.technorizen.healthcare.models.SuccessResGetProfile;
import com.technorizen.healthcare.models.SuccessResGetToken;
import com.technorizen.healthcare.models.SuccessResStripePayment;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCreditCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCreditCardFragment extends Fragment {

    private Dialog dialog ;

    private HealthInterface apiInterface;

    private SuccessResGetCardDetails.Result cardDetails;

    private String myInvoceId = "", myAmount = "", myUserId ="", myWorkerId ="", myShiftId="";

    private String token = "";

    private String key = "";

    private String cardNum ="",year = "",month = "",cvc = "";

    boolean pay = false;

    FragmentAddCreditCardBinding binding;

    String cardNo ="",expirationDate="",cvv = "",cardType = "",holderName="",expirationMonth = "",expirationYear = "";

    private String from = "";

    private String walletAmount = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCreditCardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddCreditCardFragment newInstance(String param1, String param2) {
        AddCreditCardFragment fragment = new AddCreditCardFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_credit_card, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            from = bundle.getString("from");
            if(from.equalsIgnoreCase("wallet"))
            {
                walletAmount = bundle.getString("amount");
            }
            else
            {
                myInvoceId = bundle.getString("invoiceId");
                myAmount = bundle.getString("amount");
                myUserId = bundle.getString("userId");
                myWorkerId = bundle.getString("workerId");
                myShiftId = bundle.getString("shiftId");
                pay = true;
            }
            binding.btnPay.setVisibility(View.VISIBLE);
        }

        binding.btnAddCreditCard.setOnClickListener(v ->
                {
                    fullScreenDialog();
                }
                );

        binding.btnPay.setOnClickListener(v ->
                {
                    getToken();
                }
                );
        getCards();
        return binding.getRoot();
    }

    private void fullScreenDialog() {
        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_card);
        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);
        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.ivBack);
        CardForm cardForm = dialog.findViewById(R.id.card_form);
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
                expirationMonth = cardForm.getExpirationMonth();
                expirationYear = cardForm.getExpirationYear();
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
        map.put("cvv",cvv);

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

                    dialog.dismiss();

                    getCards();

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

                        binding.btnPay.setVisibility(View.VISIBLE);

                        binding.card5.setVisibility(View.VISIBLE);

                        cardDetails = data.getResult().get(data.getResult().size()-1);

                        binding.card5.setCardHolderName(cardDetails.getCardHolderName());
                        binding.card5.setCardNumber(cardDetails.getCardNo());
                        binding.card5.setCardExpiry(cardDetails.getExpDate()+""+cardDetails.getExpMonth());

                        cardNum = cardDetails.getCardNo();

                        year = cardDetails.getExpYear();

                        month = cardDetails.getExpMonth();

                        cvc = cardDetails.getCvv();

                    } else {
                        showToast(getActivity(), data.message);
                        binding.card5.setVisibility(View.GONE);
                        binding.btnPay.setVisibility(View.GONE);

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

    public void getToken()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("card_number",cardNum);
        map.put("expiry_year",year);
        map.put("expiry_month",month);
        map.put("cvc_code",cvc);

        Call<SuccessResGetToken> call = apiInterface.getToken(map);
        call.enqueue(new Callback<SuccessResGetToken>() {
            @Override
            public void onResponse(Call<SuccessResGetToken> call, Response<SuccessResGetToken> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetToken data = response.body();
                    if (data.status == 1) {

                        Log.d(TAG, "onResponse: "+token);

                        token = data.getResult().getId();
//                        makeStripePayment();
                        makePayment();

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetToken> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    public void makeStripePayment()

    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("invoce_id",myInvoceId);
        map.put("trans_id","12091");
        map.put("amount",myAmount);
        map.put("user_id",myUserId);
        map.put("worker_id",myWorkerId);
        map.put("shift_id",myShiftId);
        map.put("token",token);
        map.put("currency","USD");

        Call<ResponseBody> call = apiInterface.stripePayment(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    ResponseBody data = response.body();
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.getString("status").equals("1")) {
                        showToast(getActivity(), jsonObject.getString("message"));
                        getActivity().onBackPressed();
                    } else {
                        showToast(getActivity(), jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void makePayment()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("trans_id","12091");
        map.put("amount",walletAmount);
        map.put("token",token);
        map.put("currency","USD");

        Call<ResponseBody> call = apiInterface.stripePayment(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    ResponseBody data = response.body();
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.getString("status").equals("1")) {
                        showToast(getActivity(), jsonObject.getString("message"));
                        getActivity().onBackPressed();
                    } else {
                        showToast(getActivity(), jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}