package com.shifts.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.ChatAdapter;
import com.shifts.healthcare.models.SuccessResGetChat;
import com.shifts.healthcare.models.SuccessResGetUnseenMessageCount;
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

public class One2OneChatAct extends AppCompatActivity {

    private HealthInterface apiInterface;

    ImageView ivAdminChat;

    RecyclerView rvMessageItem;

    ChatAdapter chatAdapter;

    EditText etChat;

    private String name = "", id = "", image = "", strChatMessage = "";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    TextView tvMessageCount,tvCenterMessage;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one2_one_chat);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        id = getIntent().getExtras().getString("id");

        tvCenterMessage = findViewById(R.id.tvCenterText);

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
        getChat(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isLastVisible()) {
                    getChat(false);
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


    private void finds(TextView textView,String date,boolean show) {

        if(show)
        {
            textView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },3000);
        }

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

    public void getChat(boolean show) {

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

                        int position = getSeenIdPosition(chatList.get(0).getLastUnseenId());

                        rvMessageItem.scrollToPosition(chatList.size() - 1);

//                        rvMessageItem.setOnScrollChangeListener((View.OnScrollChangeListener) new CustomScrollListener());

                        rvMessageItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                              if(!isLastVisible())
                              {
                                  LinearLayoutManager layoutManager = ((LinearLayoutManager) rvMessageItem.getLayoutManager());
                                  int pos = layoutManager.findFirstVisibleItemPosition();

                                  tvCenterMessage.setVisibility(View.VISIBLE);
                                  tvCenterMessage.setText(parseDateToddMMyyyy(chatList.get(pos).getDate()));

                                  new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {

                                          tvCenterMessage.setVisibility(View.GONE);

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

//                        rvMessageItem.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                            @Override
//                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvMessageItem.getLayoutManager());
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
//
////                                tvCenterMessage.animate()
////                                        .translationY(0)
////                                        .alpha(0.0f)
////                                        .setListener(new AnimatorListenerAdapter() {
////                                            @Override
////                                            public void onAnimationEnd(Animator animation) {
////                                                super.onAnimationEnd(animation);
////                                                tvCenterMessage.setVisibility(View.GONE);
////                                            }
////                                        });
////                                finds(tvCenterMessage,chatList.get(pos).getDate(),true);
//
////                                int numItems = rvMessageItem.getAdapter().getItemCount();
//
//                            }
//                        });

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

                        getChat(true);

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


    private int getSeenIdPosition(String id)
    {
        int i=0;

        for(SuccessResGetChat.Result chat:chatList)
        {
            if(chat.getId().equalsIgnoreCase("id"))
            {
                return i;
            }
            i++;
        }

       return 0;
    }


    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    System.out.println("The RecyclerView is not scrolling");
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    System.out.println("Scrolling now");
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    System.out.println("Scroll Settling");
                    break;

            }

        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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
    }


}