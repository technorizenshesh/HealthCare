package com.technorizen.healthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.AdapterChatBinding;
import com.technorizen.healthcare.models.SuccessResGetChat;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Ravindra Birla on 18,March,2021
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    AdapterChatBinding binding;

   private Context context;
   private List<SuccessResGetChat.Result> chatList = new LinkedList<>();
   String myId;

 public ChatAdapter(Context context, List<SuccessResGetChat.Result> chatList, String myId)
 {
     this.context = context;
     this.chatList = chatList;
     this.myId = myId;
 }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.adapter_chat, parent, false);
        return new ChatViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

     if(myId.equalsIgnoreCase(chatList.get(position).getSenderId()))
     {
         binding.chatLeftMsgLayout.setVisibility(View.GONE);
         binding.chatRightMsgLayout.setVisibility(View.VISIBLE);
         binding.tvTimeRight.setText(chatList.get(position).getTimeAgo());
         binding.chatRightMsgTextView.setText(chatList.get(position).getChatMessage());
     }
     else
     {
         binding.chatLeftMsgLayout.setVisibility(View.VISIBLE);
         binding.chatRightMsgLayout.setVisibility(View.GONE);
         binding.tvTimeLeft.setText(chatList.get(position).getTimeAgo());
         binding.chatLeftMsgTextView.setText(chatList.get(position).getChatMessage());
     }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {

        public ChatViewHolder(AdapterChatBinding adapterChatBinding) {
            super(adapterChatBinding.getRoot());
        }
    }

}
