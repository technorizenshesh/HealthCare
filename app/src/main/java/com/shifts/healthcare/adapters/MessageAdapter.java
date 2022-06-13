package com.shifts.healthcare.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shifts.healthcare.R;
import com.shifts.healthcare.databinding.ConversationItemBinding;
import com.shifts.healthcare.models.SuccessResGetConversation;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.StoriesViewHolder> {

    private Context context;

    ConversationItemBinding binding;

    private ArrayList<SuccessResGetConversation.Result> conversationList;

    public MessageAdapter(Context context, ArrayList<SuccessResGetConversation.Result> conversationList)
    {
      this.context = context;
      this.conversationList = conversationList;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= ConversationItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);

        TextView tvUserName,tvvFullName;

        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);

        tvvFullName = holder.itemView.findViewById(R.id.tvMessage);

        tvUserName.setText(conversationList.get(position).getFirstName()+" "+conversationList.get(position).getLastName());

        tvvFullName.setText(conversationList.get(position).getLastMessage());

        Glide
                .with(context)
                .load(conversationList.get(position).getImage())
                .centerCrop()
                .into(circleImageView);

        rlParent.setOnClickListener(v ->
                {

                    if(conversationList.get(position).getMyType().equalsIgnoreCase("User"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("name",conversationList.get(position).getFirstName()+" "+conversationList.get(position).getLastName());
                        bundle.putString("image",conversationList.get(position).getImage());
                        bundle.putString("id",conversationList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_userConversationFragment_to_one2OneChatFragment,bundle);

                    }
                    else
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("name",conversationList.get(position).getFirstName()+" "+conversationList.get(position).getLastName());
                        bundle.putString("image",conversationList.get(position).getImage());
                        bundle.putString("id",conversationList.get(position).getId());
                        Navigation.findNavController(v).navigate(R.id.action_userConversationFragment2_to_one2OneChatFragment2,bundle);

                    }

                }
        );

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(ConversationItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
