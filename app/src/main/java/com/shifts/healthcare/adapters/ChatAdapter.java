package com.shifts.healthcare.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shifts.healthcare.R;
import com.shifts.healthcare.databinding.AdapterChatBinding;
import com.shifts.healthcare.models.SuccessResGetChat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

        RelativeLayout chatLeftMsgLayout = holder.itemView.findViewById(R.id.chat_left_msg_layout);
        RelativeLayout chatRightMsgLayout = holder.itemView.findViewById(R.id.chat_right_msg_layout);

        TextView centerDate = holder.itemView.findViewById(R.id.tv_date);

        TextView tvTimeRight = holder.itemView.findViewById(R.id.tv_time_right);
        TextView chatRightMsgTextView = holder.itemView.findViewById(R.id.chat_right_msg_text_view);

        TextView tvTimeLeft = holder.itemView.findViewById(R.id.tv_time_left);
        TextView chatLeftMsgTextView = holder.itemView.findViewById(R.id.chat_left_msg_text_view);

        if(myId.equalsIgnoreCase(chatList.get(position).getSenderId()))
     {
        chatLeftMsgLayout.setVisibility(View.GONE);
        chatRightMsgLayout.setVisibility(View.VISIBLE);
        tvTimeRight.setText(chatList.get(position).getTimeAgo());
        chatRightMsgTextView.setText(chatList.get(position).getChatMessage());
     }
     else
     {
        chatLeftMsgLayout.setVisibility(View.VISIBLE);
        chatRightMsgLayout.setVisibility(View.GONE);
        tvTimeLeft.setText(chatList.get(position).getTimeAgo());
        chatLeftMsgTextView.setText(chatList.get(position).getChatMessage());
     }


     long firstDate =0 ;
     long secondDate =0 ;

      //  2022-05-23 13:32:34

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String dateString = chatList.get(position).getDate();
                try {
                    Date date = sdf.parse(dateString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    System.out.println("Given Time in milliseconds : start"+calendar.getTimeInMillis());
                    firstDate = calendar.getTimeInMillis();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

        if(position>0){

//            Message pm = messages.get(position-1);
//            previousTs = pm.getTimeStamp();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateString1 = chatList.get(position-1).getDate();
            try {
                Date date = sdf1.parse(dateString1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                System.out.println("Given Time in milliseconds : start"+calendar.getTimeInMillis());
                secondDate = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.d("TAG", "onBindViewHolder: Position "+position);
                setTimeTextVisibility(firstDate,secondDate,centerDate,position);
    }

    private void setTimeTextVisibility(long ts1, long ts2, TextView timeText,int position){

        if(ts2==0){
            timeText.setVisibility(View.VISIBLE);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(ts1);
            Log.d("TAG", "setTimeTextVisibility: milis"+ts1+" Calander: "+cal.getTime()+" position "+position );
//            timeText.setText(chatList.get(position).getDate());

//            timeText.setText(cal.getTime()+" Position : ***"+position);

            Date date = cal.getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String strDate = dateFormat.format(date);

            //                    date = inputFormat.parse(time);

            timeText.setText("------------------------"+parseDateToddMMyyyy(strDate)+"------------------------");


//            timeText.setText(Utils.formatDayTimeHtml(ts1));
        }else {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTimeInMillis(ts1);
            cal2.setTimeInMillis(ts2);

            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                    ;

            if(sameMonth){
                timeText.setVisibility(View.GONE);
                timeText.setText("");
            }else {
                timeText.setVisibility(View.VISIBLE);
//                timeText.setText(Utils.formatDayTimeHtml(ts2));

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(ts1);
                Log.d("TAG", "setTimeTextVisibility: milis"+ts1+" Calander: "+cal.getTime());



                Date date = cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);


                //                    date = inputFormat.parse(time);

                timeText.setText("------------------------"+parseDateToddMMyyyy(strDate)+"------------------------");

            }

        }
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
