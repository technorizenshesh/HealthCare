package com.shifts.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.ChatAdapter;
import com.shifts.healthcare.databinding.ActivityConversationBinding;
import com.shifts.healthcare.models.SuccessResGetChat;
import com.shifts.healthcare.models.SuccessResInsertChat;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

public class ConversationAct extends AppCompatActivity {

    static boolean active = false;

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

        getChat(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isLastVisible()){
                    getChat(false);
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

    private void getChat(boolean show) {

        String userId = SharedPreferenceUtility.getInstance(ConversationAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(ConversationAct.this, getString(R.string.please_wait));
        Log.d("datatest",userId);
        Map<String,String> map = new HashMap<>();
        map.put("sender_id","1");
        map.put("receiver_id",userId);

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

                        binding.rvMessageItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                if(!isLastVisible())
                                {
                                    LinearLayoutManager layoutManager = ((LinearLayoutManager)  binding.rvMessageItem.getLayoutManager());
                                    int pos = layoutManager.findFirstVisibleItemPosition();

                                    binding.tvCenterText.setVisibility(View.VISIBLE);
                                    binding.tvCenterText.setText(parseDateToddMMyyyy(chatList.get(pos).getDate()));

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            binding.tvCenterText.setVisibility(View.GONE);

                                        }
                                    },5000);

                                }

                                if (dx > 0) {
                                    System.out.println("Scrolled Right");
                                } else if (dx < 0) {
                                    System.out.println("Scrolled Left");
                                } else {
                                    System.out.println("No Horizontal Scrolled");
                                }
                                if (dy > 0) {
                                    System.out.println("Scrolled Downwards");
                                } else if (dy < 0) {
                                    System.out.println("Scrolled Upwards");
                                } else {
                                    System.out.println("No Vertical Scrolled");
                                }




                            }
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                switch (newState) {
                                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                                        System.out.println("SCROLL_STATE_FLING");
                                        break;
                                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                                        System.out.println("SCROLL_STATE_TOUCH_SCROLL");


//                                        LinearLayoutManager layoutManager = ((LinearLayoutManager) rvMessageItem.getLayoutManager());
//                                int pos = layoutManager.findFirstVisibleItemPosition();
//
//                                tvCenterMessage.setVisibility(View.VISIBLE);
//                                tvCenterMessage.setText(parseDateToddMMyyyy(chatList.get(pos).getDate()));
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        tvCenterMessage.setVisibility(View.GONE);
//
//                                    }
//                                },5000);

                                        break;
                                    default:
                                        break;
                                }
                            }
                        });


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


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "E, MMM d, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void insertChat() {
        binding.etChat.setText("");
        String userId = SharedPreferenceUtility.getInstance(ConversationAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("sender_id",userId);
        map.put("receiver_id","1");
        map.put("chat_message",strChatMessage);
        map.put("sender_type","User");

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
                        getChat(false);
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

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

}