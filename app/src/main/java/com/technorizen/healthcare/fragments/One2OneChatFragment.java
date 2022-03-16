package com.technorizen.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ChatAdapter;
import com.technorizen.healthcare.databinding.FragmentOne2OneChatBinding;
import com.technorizen.healthcare.databinding.FragmentOneChatBinding;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link One2OneChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class One2OneChatFragment extends Fragment {

    FragmentOneChatBinding binding;

    HealthInterface apiInterface;

    ChatAdapter chatAdapter;

    private String name = "",id = "", image ="",strChatMessage = "";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    Timer timer = new Timer();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public One2OneChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment One2OneChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static One2OneChatFragment newInstance(String param1, String param2) {
        One2OneChatFragment fragment = new One2OneChatFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_one_chat, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        Bundle bundle = this.getArguments();

        if (bundle!=null)
        {
            id = bundle.getString("senderId");
        }

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);

        chatAdapter = new ChatAdapter(getContext(),chatList,userId);
        binding.rvMessageItem.setHasFixedSize(true);
        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(getContext()));
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

        return binding.getRoot();    }

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

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> map = new HashMap<>();
        map.put("sender_id",userId);
        map.put("receiver_id",id);

        Call<SuccessResGetChat> call = apiInterface.getChat(map);
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
                        binding.rvMessageItem.setLayoutManager(new LinearLayoutManager(getContext()));
                        binding.rvMessageItem.setAdapter(chatAdapter);
                        //chatAdapter.notifyDataSetChanged();
                        binding.rvMessageItem.scrollToPosition(chatList.size()-1);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {

                        showToast(getActivity(), data.message);

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
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("sender_id",userId);
        map.put("receiver_id",id);
        map.put("chat_message",strChatMessage);

        Call<SuccessResInsertChat> call = apiInterface.insertChat(map);
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

                        showToast(getActivity(), data.message);

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