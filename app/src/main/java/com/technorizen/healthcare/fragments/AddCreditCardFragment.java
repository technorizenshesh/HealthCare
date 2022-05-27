package com.technorizen.healthcare.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.maxpilotto.creditcardview.models.Brand;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.activites.ConversationAct;
import com.technorizen.healthcare.adapters.CardAdapter;
import com.technorizen.healthcare.databinding.FragmentAddCreditCardBinding;
import com.technorizen.healthcare.models.SuccessResAddCardDetails;
import com.technorizen.healthcare.models.SuccessResDeleteCard;
import com.technorizen.healthcare.models.SuccessResGetCardDetails;
import com.technorizen.healthcare.models.SuccessResGetProfile;
import com.technorizen.healthcare.models.SuccessResGetToken;
import com.technorizen.healthcare.models.SuccessResGetUnseenMessageCount;
import com.technorizen.healthcare.models.SuccessResStripePayment;
import com.technorizen.healthcare.models.SuccessResUpdateCards;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.CardInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class AddCreditCardFragment extends Fragment implements CardInterface {

    private Dialog dialog ;

    private HealthInterface apiInterface;

    private SuccessResGetCardDetails.Result cardDetails;

    private boolean userFrom = false;

    private int cardSelectedPosition = -1;

    private List<SuccessResGetCardDetails.Result> cardList = new ArrayList<>();

    private String myInvoceId = "", myAmount = "", myUserId ="", myWorkerId ="", myShiftId="";

    private String token = "",useCardAsDefault="";

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

        binding.nestedScrollView.setNestedScrollingEnabled(false);

        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            from = bundle.getString("from");
            if(from.equalsIgnoreCase("wallet"))
            {
                walletAmount = bundle.getString("amount");

                userFrom = true;

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

                    if(cardList.size()!=0)
                    {
                        if(cardSelectedPosition==-1)
                        {
                            Toast.makeText(getActivity(),"Please select a card",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            cardNum = cardList.get(cardSelectedPosition).getCardNo();
                            year = cardList.get(cardSelectedPosition).getExpYear();
                            month = cardList.get(cardSelectedPosition).getExpMonth();
                            getCvv();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Please add a card",Toast.LENGTH_SHORT).show();
                    }

                }
                );
        getCards();
        return binding.getRoot();
    }

    TextView tvMessageCount;

    public  void getUnseenNotificationCount()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetUnseenMessageCount> call = apiInterface.getUnseenMessage(map);

        call.enqueue(new Callback<SuccessResGetUnseenMessageCount>() {
            @Override
            public void onResponse(Call<SuccessResGetUnseenMessageCount> call, Response<SuccessResGetUnseenMessageCount> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUnseenMessageCount data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        int unseenNoti = Integer.parseInt(data.getResult().getTotalUnseenMessage());

                        if(unseenNoti!=0)
                        {

                            tvMessageCount.setVisibility(View.VISIBLE);
                            tvMessageCount.setText(unseenNoti+"");

                        }
                        else
                        {

                            tvMessageCount.setVisibility(View.GONE);

                        }

                    } else if (data.status.equals("0")) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUnseenMessageCount> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void fullScreenDialog() {
        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_card);
        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);
        MaterialCheckBox checkBox = dialog.findViewById(R.id.defaultCheckBox);
        ImageView ivBack;

        tvMessageCount = dialog.findViewById(R.id.tvMessageCount);

        RelativeLayout rlChat = dialog.findViewById(R.id.rlChat);


        ivBack = dialog.findViewById(R.id.ivBack);
        CardForm cardForm = dialog.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .expirationRequired(true)
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
                cardType = "";
                holderName = cardForm.getCardholderName();
                if(cardForm.isValid())
                {

                    useCardAsDefault = "";

                    if(checkBox.isChecked())
                    {
                        useCardAsDefault = "1";
                    }
                    else
                    {
                        useCardAsDefault = "0";
                    }
                    addCardDetails();
                }else
                {
                    cardForm.validate();
                }
            }
        });

        rlChat.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ConversationAct.class));
                }
        );

        getUnseenNotificationCount();

        btnAdd.setOnClickListener(v ->
                {
                    cardNo = cardForm.getCardNumber();
                    expirationDate = cardForm.getExpirationDateEditText().getText().toString();
                    cardType = "";
                    expirationMonth = cardForm.getExpirationMonth();
                    holderName = cardForm.getCardholderName();
                    expirationYear = cardForm.getExpirationYear();

                    useCardAsDefault = "";

                    if(checkBox.isChecked())
                    {
                        useCardAsDefault = "1";
                    }
                    else
                    {
                        useCardAsDefault = "0";
                    }

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
        map.put("set_default",useCardAsDefault);
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

                        if(userFrom)
                        {
                            binding.btnPay.setVisibility(View.VISIBLE);
                        }

                        cardDetails = data.getResult().get(data.getResult().size()-1);
//
//                        binding.card1.setVisibility(View.VISIBLE);
//                        binding.card1.setHolder(cardDetails.getCardHolderName());
//                        String card = cardDetails.getCardNo().substring(cardDetails.getCardNo().length()-3);
//                        StringBuffer string = new StringBuffer(card);
//                        String date = cardDetails.getExpDate()+""+cardDetails.getExpMonth();
//                        binding.card1.setCardData(cardDetails.getCardHolderName(),"XXXXXXXXXXXXX"+card,"",date);

                        cardList.clear();
                        cardList.addAll(data.getResult());

                        binding.rvCards.setHasFixedSize(true);
                        binding.rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCards.setAdapter(new CardAdapter(getActivity(),cardList,userFrom,AddCreditCardFragment.this));

//                        binding.card1.applyStyle(Brand.DINERS);
//
//                        binding.card5.setCardExpiry(cardDetails.getExpDate()+""+cardDetails.getExpMonth());

                    } else {
                        showToast(getActivity(), data.message);
                        binding.card5.setVisibility(View.GONE);
                        binding.btnPay.setVisibility(View.GONE);
                        cardList.clear();
                        binding.rvCards.setHasFixedSize(true);
                        binding.rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCards.setAdapter(new CardAdapter(getActivity(),cardList,userFrom,AddCreditCardFragment.this));

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


    private void deleteCards(String id)
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("id",id);

        Log.e(TAG,"Test Request "+map);

        Call<ResponseBody> loginCall = apiInterface.deleteCard(map);

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {

                    getCards();

                    } else if (data.equals("0")) {
                        showToast(getActivity(), message);
                    }else if (data.equals("2")) {
                        showToast(getActivity(), jsonObject.getString("result"));
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

    private void getCvv()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_cvv);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);

        EditText editTextCvv = dialog.findViewById(R.id.etCvv);

        AppCompatButton appCompatButton = dialog.findViewById(R.id.btnLogin);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        appCompatButton.setOnClickListener(v ->
                {
                    if(editTextCvv.getText().toString().equalsIgnoreCase("") || editTextCvv.getText().toString().length()!=3)
                    {
                        Toast.makeText(getActivity(),"Please Enter a valid cvv",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        cvv = editTextCvv.getText().toString();
                        getToken();

                    }
                }
                );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateDefaultCard(String id)
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("set_default","1");
        map.put("id",id);

        Log.e(TAG,"Test Request "+map);

        Call<SuccessResUpdateCards> loginCall = apiInterface.updateCard(map);
        loginCall.enqueue(new Callback<SuccessResUpdateCards>() {
            @Override
            public void onResponse(Call<SuccessResUpdateCards> call, Response<SuccessResUpdateCards> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUpdateCards data = response.body();

                    String responseString = new Gson().toJson(response.body());

                    showToast(getActivity(),data.message);

                    getCards();

                    dialog.dismiss();

                    Log.e(TAG,"Test Response :"+responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateCards> call, Throwable t) {
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

    @Override
    public void deleteCard(String cardId) {

        deleteCards(cardId);

    }

    @Override
    public void updateDefault(String cardID) {

        updateDefaultCard(cardID);

    }

    @Override
    public void cardSelectdPosition(int position) {

        cardSelectedPosition = position;

    }
}