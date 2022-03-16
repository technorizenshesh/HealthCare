package com.technorizen.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ChatAdapter;
import com.technorizen.healthcare.databinding.ActivityConversationBinding;
import com.technorizen.healthcare.models.SuccessResGetChat;
import com.technorizen.healthcare.models.SuccessResInsertChat;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

public class ConversationAct extends AppCompatActivity {

    ActivityConversationBinding binding;

    private HealthInterface apiInterface;

    ChatAdapter chatAdapter;

    private String name = "",id = "", image ="",strChatMessage = "";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_conversation);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        binding.ivBack.setOnClickListener(v ->
                {
                    finish();
                }
                );

        String userId = SharedPreferenceUtility.getInstance(this).getString(USER_ID);

        chatAdapter = new ChatAdapter(this,chatList,userId);
        binding.rvMessageItem.setHasFixedSize(true);
        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessageItem.setAdapter(chatAdapter);

        getChat();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isLastVisible()){
                    getChat();
                }
            }
        },0,5000);

        binding.ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strChatMessage = binding.etChat.getText().toString();
                if(!strChatMessage.equals(""))
                {
                    insertChat();
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private boolean isLastVisible() {

        if (chatList != null && chatList.size() != 0) {
            LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.rvMessageItem.getLayoutManager());
            int pos = layoutManager.findLastCompletelyVisibleItemPosition();
            int numItems = binding.rvMessageItem.getAdapter().getItemCount();
            return (pos >= numItems - 1);
        }
        return false;
    }

    private void getChat() {

        String userId = SharedPreferenceUtility.getInstance(ConversationAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ConversationAct.this, getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("sender_id",userId);
        map.put("receiver_id","1");

        Call<SuccessResGetChat> call = apiInterface.getAdminChat(map);
        call.enqueue(new Callback<SuccessResGetChat>() {
            @Override
            public void onResponse(Call<SuccessResGetChat> call, Response<SuccessResGetChat> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetChat data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {

                        String dataResponse = new Gson().toJson(response.body());

                        chatList.clear();
                        chatList.addAll(data.getResult());
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(ConversationAct.this));
                        binding.rvMessageItem.setAdapter(chatAdapter);
                        binding.rvMessageItem.scrollToPosition(chatList.size()-1);

                    } else if (data.status.equals("0")) {

                        showToast(ConversationAct.this, data.message);

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
        binding.etChat.setText("");
        String userId = SharedPreferenceUtility.getInstance(ConversationAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("sender_id",userId);
        map.put("receiver_id","1");
        map.put("chat_message",strChatMessage);

        Call<SuccessResInsertChat> call = apiInterface.insertAdminChat(map);
        call.enqueue(new Callback<SuccessResInsertChat>() {
            @Override
            public void onResponse(Call<SuccessResInsertChat> call, Response<SuccessResInsertChat> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResInsertChat data = response.body();
                    Log.e("data",data.status);
                    String dataResponse = new Gson().toJson(response.body());
                    if (data.status.equals("1")) {

                        getChat();

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {

                        showToast(ConversationAct.this, data.message);

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