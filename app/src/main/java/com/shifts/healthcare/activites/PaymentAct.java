package com.shifts.healthcare.activites;

import static android.content.ContentValues.TAG;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.models.SuccessResGetToken;
import com.shifts.healthcare.models.SuccessResGetUnseenMessageCount;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentAct extends AppCompatActivity {

    String cardNo ="",expirationDate="",cvv = "",cardType = "",holderName="",expirationMonth = "",expirationYear = "";

    TextView tvMessageCount;
    LocalBroadcastManager lbm;
    private HealthInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

        AppCompatButton btnAdd =  findViewById(R.id.btnAdd);

        MaterialCheckBox checkBox = findViewById(R.id.defaultCheckBox);

        tvMessageCount = findViewById(R.id.tvMessageCount);

        RelativeLayout rlChat = findViewById(R.id.rlChat);

        ImageView ivBack;
        TextView tvMessage = findViewById(R.id.tvMessage);
        ivBack = findViewById(R.id.ivBack);
        CardForm cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .expirationRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .cvvRequired(true)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity) this);

        rlChat.setOnClickListener(v ->
                {
                    startActivity(new Intent(this, ConversationAct.class));
                }
        );

        getUnseenNotificationCount();

        tvMessage.setVisibility(View.VISIBLE);

        checkBox.setVisibility(View.GONE);

        btnAdd.setText(getString(R.string.pay));

        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                cardNo = cardForm.getCardNumber();
                expirationDate = cardForm.getExpirationMonth()+"/"+cardForm.getExpirationYear();
                expirationMonth = cardForm.getExpirationMonth();
                expirationYear = cardForm.getExpirationYear();
                cvv =  cardForm.getCvv();
                cardType = "";
                holderName = cardForm.getCardholderName();
                if(cardForm.isValid())
                {
//                  clickOnPayNow();

                    getToken();
//                    DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            paymentSuccess();
//                            dialog.dismiss();
//                            DataManager.getInstance().hideProgressMessage();
//                        }
//                    },5000);

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
                    cardType = "";
                    expirationMonth = cardForm.getExpirationMonth();
                    holderName = cardForm.getCardholderName();
                    expirationYear = cardForm.getExpirationYear();
                    cvv = cardForm.getCvv();
                    if(cardForm.isValid())
                    {

                        getToken();

//                        clickOnPayNow();
//                        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                paymentSuccess();
//                                dialog.dismiss();
//                                DataManager.getInstance().hideProgressMessage();
//                            }
//                        },5000);


                    }else
                    {
                        cardForm.validate();
                    }
                }
        );
        ivBack.setOnClickListener(v ->
                {
                    finish();
                }
        );
        
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
            getUnseenNotificationCount();
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String str = intent.getStringExtra("key");
                getUnseenNotificationCount();
                // get all your data from intent and do what you want
            }
        }
    };

    public  void getUnseenNotificationCount()
    {

        String userId = SharedPreferenceUtility.getInstance(PaymentAct.this).getString(USER_ID);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private String token = "";

    public void getToken()
    {

        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("card_number",cardNo);
        map.put("expiry_year",expirationYear);
        map.put("expiry_month",expirationMonth);
        map.put("cvc_code",cvv);

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

                        if(token==null)
                        {
                            showToast(PaymentAct.this,"Invalid card details.");
                        }
                        else
                        {
                            callPaymentApi(token);
                        }

                    } else {
                        showToast(PaymentAct.this, data.message);
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

    private void clickOnPayNow() {

//        Card.Builder card = new Card.Builder(cardNo,
//                Integer.parseInt(expirationMonth),
//                Integer.parseInt(expirationYear),
//                cvv);
//
//        if (!card.build().validateCard()) {
//            cardNo = "";
//            expirationDate = "";
//            cvv = "";
//            showToast(PaymentAct.this,"Please Enter valid card details.");
//            return;
//        }
//
//        Stripe stripe = new Stripe(PaymentAct.this, "pk_test_51Jl1kpIzhVsEreKHYKdvN0fLZUv3xQaOjf4W73C3qvTAMexMXcbJP5SwioNPbeeh6o2cP2ygdUrlV8oBfH2VAH9f000YseP4ES");
//
//        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
//        stripe.createCardToken(
//                card.build(), new ApiResultCallback<Token>() {
//                    @Override
//                    public void onSuccess(@NotNull Token token) {
//                        DataManager.getInstance().hideProgressMessage();
//                        callPaymentApi(token.getId());
//                    }
//
//                    @Override
//                    public void onError(@NotNull Exception e) {
//                        showToast(PaymentAct.this,e.getMessage());
//                        DataManager.getInstance().hideProgressMessage();
//                    }
//                });
    }

    private void callPaymentApi(String token)
    {

        Random rand = new Random();
        int maxNumber = 10000;
        int randomNumber = rand.nextInt(maxNumber) + 1;
        DataManager.getInstance().showProgressMessage(PaymentAct.this, getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(PaymentAct.this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id", userId);
        map.put("request_id", randomNumber+"");
        map.put("amount", "56.5");
        map.put("currency", "CAD");
        map.put("token", token);

        Call<ResponseBody> call = apiInterface.workerPayment(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        paymentSuccess();
//                        PaymentAct.this.onBackPressed();
                    } else if (data.equals("0")) {
                        showToast(PaymentAct.this, message);
                    }else if (data.equals("2")) {
                        showToast(PaymentAct.this, jsonObject.getString("result"));
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

    public void paymentSuccess() {

        final Dialog dialog = new Dialog(PaymentAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_success);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog.setCanceledOnTouchOutside(false);

        AppCompatButton appCompatButton = dialog.findViewById(R.id.btnLogin);

        ImageView ivCancel = dialog.findViewById(R.id.cancel);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        appCompatButton.setOnClickListener(v ->
                {

                    String url = "https://lime.certn.co/browse/packages/87dfca5a-25b8-4ede-ae53-1a193209c9e9";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


}