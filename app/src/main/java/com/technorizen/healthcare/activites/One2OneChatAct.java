package com.technorizen.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ChatAdapter;
import com.technorizen.healthcare.models.SuccessResGetChat;
import com.technorizen.healthcare.models.SuccessResGetUnseenMessageCount;
import com.technorizen.healthcare.models.SuccessResInsertChat;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.io.File;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

public class One2OneChatAct extends AppCompatActivity {

    private HealthInterface apiInterface;

    ImageView ivAdminChat;

    RecyclerView rvMessageItem;

    ChatAdapter chatAdapter;

    EditText etChat;

    private String name = "", id = "", image = "", strChatMessage = "";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    TextView tvMessageCount;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one2_one_chat);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        id = getIntent().getExtras().getString("id");

        ImageView ivBack = findViewById(R.id.ivBack);

        ImageView ivSend = findViewById(R.id.ivSendMessage);

        etChat = findViewById(R.id.etChat);

        rvMessageItem = findViewById(R.id.rvMessageItem);

        tvMessageCount = findViewById(R.id.tvMessageCount);

        ivBack.setOnClickListener(v -> {

            finish();

        });

        ivAdminChat = findViewById(R.id.ivAdminChat);

        ivAdminChat.setOnClickListener(v ->
                {

                    startActivity(new Intent(One2OneChatAct.this, ConversationAct.class));

                }
        );

        String userId = SharedPreferenceUtility.getInstance(One2OneChatAct.this).getString(USER_ID);

        chatAdapter = new ChatAdapter(One2OneChatAct.this, chatList, userId);
        rvMessageItem.setHasFixedSize(true);
        rvMessageItem.setLayoutManager(new LinearLayoutManager(One2OneChatAct.this));
        rvMessageItem.setAdapter(chatAdapter);
        DataManager.getInstance().showProgressMessage((Activity) One2OneChatAct.this, One2OneChatAct.this.getString(R.string.please_wait));
        getUnseenNotificationCount();
        getChat();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isLastVisible()) {
                    getChat();
                }
            }
        }, 0, 5000);

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strChatMessage = etChat.getText().toString();
                if (!strChatMessage.equals("")) {
                    insertChat();
                }

            }
        });

//        Currency currency = Currency.getInstance(Locale.getDefault());
//        Locale defaultLocale = Locale.getDefault();
//        TelephonyManager tm = (TelephonyManager)this.getSystemService(One2OneChatAct.this.TELEPHONY_SERVICE);
//        String locale = tm.getNetworkCountryIso();
//       String currencyCode =  getCurrencyCode(locale);
//        String currencySymbol = getCurrencySymbol(locale);
//        Log.d("TAG", "onCreate:1+ "+currencyCode);
//        Log.d("TAG", "onCreate:2+ "+currencySymbol);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


    public void getUnseenNotificationCount() {

        String userId = SharedPreferenceUtility.getInstance(One2OneChatAct.this).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);

        Call<SuccessResGetUnseenMessageCount> call = apiInterface.getUnseenMessage(map);

        call.enqueue(new Callback<SuccessResGetUnseenMessageCount>() {
            @Override
            public void onResponse(Call<SuccessResGetUnseenMessageCount> call, Response<SuccessResGetUnseenMessageCount> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUnseenMessageCount data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        int unseenNoti = Integer.parseInt(data.getResult().getTotalUnseenMessage());

                        if (unseenNoti != 0) {

                            tvMessageCount.setVisibility(View.VISIBLE);
                            tvMessageCount.setText(unseenNoti + "");

                        } else {

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

    private boolean isLastVisible() {

        if (chatList != null && chatList.size() != 0) {
            LinearLayoutManager layoutManager = ((LinearLayoutManager) rvMessageItem.getLayoutManager());
            int pos = layoutManager.findLastCompletelyVisibleItemPosition();
            int numItems = rvMessageItem.getAdapter().getItemCount();
            return (pos >= numItems - 1);
        }
        return false;
    }

    public void getChat() {

        String userId = SharedPreferenceUtility.getInstance(One2OneChatAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage((Activity) One2OneChatAct.this, One2OneChatAct.this.getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("sender_id", id);
        map.put("receiver_id", userId);

        Call<SuccessResGetChat> call = apiInterface.getChat(map);
        call.enqueue(new Callback<SuccessResGetChat>() {
            @Override
            public void onResponse(Call<SuccessResGetChat> call, Response<SuccessResGetChat> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetChat data = response.body();
                    Log.e("data", data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        chatList.clear();
                        chatList.addAll(data.getResult());
                        rvMessageItem.setLayoutManager(new LinearLayoutManager(One2OneChatAct.this));
                        rvMessageItem.setAdapter(chatAdapter);
                        //chatAdapter.notifyDataSetChanged();
                        rvMessageItem.scrollToPosition(chatList.size() - 1);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {

                        showToast((Activity) One2OneChatAct.this, data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void insertChat() {
        etChat.setText("");
        String userId = SharedPreferenceUtility.getInstance(One2OneChatAct.this).getString(USER_ID);
       /*
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody receiverID = RequestBody.create(MediaType.parse("text/plain"),receiverId);
        RequestBody chatMessage = RequestBody.create(MediaType.parse("text/plain"), strChatMessage);
        RequestBody itemId = RequestBody.create(MediaType.parse("text/plain"),itemID);
*/

        DataManager.getInstance().showProgressMessage((Activity) One2OneChatAct.this, One2OneChatAct.this.getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("sender_id", userId);
        map.put("receiver_id", id);
        map.put("chat_message", strChatMessage);

        Call<SuccessResInsertChat> call = apiInterface.insertChat(map);
        call.enqueue(new Callback<SuccessResInsertChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertChat> call, Response<SuccessResInsertChat> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInsertChat data = response.body();
                    Log.e("data", data.status);
                    String dataResponse = new Gson().toJson(response.body());
                    if (data.status.equals("1")) {

                        getChat();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {

                        showToast((Activity) One2OneChatAct.this, data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResInsertChat> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


}