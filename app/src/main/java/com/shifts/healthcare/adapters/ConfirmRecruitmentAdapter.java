package com.shifts.healthcare.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.activites.ConversationAct;
import com.shifts.healthcare.models.SuccessResGetChat;
import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResInsertChat;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.RecruitmentShiftConfirmationInterface;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import static android.content.ContentValues.TAG;
import static com.shifts.healthcare.adapters.ShiftsAdapter.getTime24Difference;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class ConfirmRecruitmentAdapter extends RecyclerView.Adapter<ConfirmRecruitmentAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private ArrayList<SuccessResGetCurrentSchedule.Result> postedList ;
    List<String> monthsList = new LinkedList<>();

    private RecruitmentShiftConfirmationInterface shifts;

    private boolean showNotes = false;

    private boolean from;

    public ConfirmRecruitmentAdapter(Context context, ArrayList<SuccessResGetCurrentSchedule.Result> postedList, RecruitmentShiftConfirmationInterface shifts,boolean from)
    {
        this.context = context;
        this.postedList = postedList;
        this.shifts =shifts;
        this.from = from;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.current_schedule_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        RelativeLayout rlChat;

        rlChat = holder.itemView.findViewById(R.id.rlChat);

        showNotes = false;
        List<String> dates = new LinkedList<>();
        List<String> listStartTime = new LinkedList<>();
        List<String> listEndTime = new LinkedList<>();

        RelativeLayout rlAddress = holder.itemView.findViewById(R.id.rlAddress);


        List<SuccessResGetCurrentSchedule.PostshiftTime> postshiftTimeList =  new LinkedList<>();
        postshiftTimeList = postedList.get(position).getPostshiftTime();
        TextView tvWorkerMsg = holder.itemView.findViewById(R.id.tvWorkerMsg);

        TextView tvCompanyName = holder.itemView.findViewById(R.id.tvCompanyName);
        TextView tvJobPosition = holder.itemView.findViewById(R.id.jobPosition);
        TextView tvDuty = holder.itemView.findViewById(R.id.tvDuty);
        TextView tvBreak = holder.itemView.findViewById(R.id.tvBreak);
        TextView hrRate = holder.itemView.findViewById(R.id.hrRate);
        TextView tvCovid = holder.itemView.findViewById(R.id.tvCovid);
        TextView tvLocation = holder.itemView.findViewById(R.id.tvLocation);
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        TextView tvTime = holder.itemView.findViewById(R.id.tvSignleTime);
        TextView tvMultipleTime = holder.itemView.findViewById(R.id.tvMutlipleTime);
        TextView tvNumOfShifts = holder.itemView.findViewById(R.id.tvNumOfShifts);
        TextView tvDistance = holder.itemView.findViewById(R.id.tvDistance);
        TextView tvWorkerName = holder.itemView.findViewById(R.id.tvWorkerName);
        TextView tvWorkerDesignation = holder.itemView.findViewById(R.id.tvWorkerDesignation);
        TextView tvID = holder.itemView.findViewById(R.id.tvID);
        TextView tvShiftNotes = holder.itemView.findViewById(R.id.tvShiftsNotes);
        TextView tvShiftNumber = holder.itemView.findViewById(R.id.tvShiftNumber);
        TextView tvTransit = holder.itemView.findViewById(R.id.tvTransit);

        LinearLayout llRecruit =  holder.itemView.findViewById(R.id.llRecruitment);
        AppCompatButton btnDelete = holder.itemView.findViewById(R.id.btnDelete);

        ImageView ivAdd = holder.itemView.findViewById(R.id.plus);
        ImageView ivMinus = holder.itemView.findViewById(R.id.minus);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        ImageView ivChat = holder.itemView.findViewById(R.id.ivChat);
        tvShiftNotes.setText(postedList.get(position).getShiftsdetail().get(0).getShiftNotes());

        tvShiftNumber.setText(postedList.get(position).getShiftNo());

        AppCompatButton btnCancel = holder.itemView.findViewById(R.id.btnCancel);
        AppCompatButton btnRecruit = holder.itemView.findViewById(R.id.btnRecruit);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms( new RoundedCorners(13));

        rlChat.setVisibility(View.GONE);

        if(!from)
        {
            rlAddress.setVisibility(View.GONE);
        }

        if(!postedList.get(position).getTotalUnseenMessage().equalsIgnoreCase("0"))
        {
            tvWorkerMsg.setVisibility(View.VISIBLE);
            tvWorkerMsg.setText(postedList.get(position).getTotalUnseenMessage());
        }else
        {
            tvWorkerMsg.setVisibility(View.GONE);
        }

        if(from)
        {
            llRecruit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        }
        else
        {
            llRecruit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        }

        ImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);
        ImageView ivWorkerProfile = holder.itemView.findViewById(R.id.ivWorker);

        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);

        if(postedList.get(position).getShiftsdetail().get(0).getNoVacancies().equalsIgnoreCase("1"))
        {

            tvID.setVisibility(View.GONE);

        }
        else
        {
            tvID.setVisibility(View.VISIBLE);
        }

        tvID.setText(postedList.get(position).getVacanciesNo());


        btnRecruit.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Recruit Worker")
                            .setMessage("Are you sure you want to recruit worker?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    shifts.recruitConfirmation(v,postedList.get(position).getShiftId(),postedList.get(position).getUserId(),postedList.get(position).getWorkerId(),"Accepted",postedList.get(position));

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();

                }
        );


        btnCancel.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Cancel Worker")
                            .setMessage("Are you sure you want to cancel worker?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    shifts.cancelRecruitShift(postedList.get(position).getShiftId(),postedList.get(position).getUserId(),postedList.get(position).getWorkerId(),"Accepted");

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();

                }
        );

        btnCancel.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Cancel Worker")
                            .setMessage("Are you sure you want to cancel worker?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    shifts.cancelRecruitShift(postedList.get(position).getShiftId(),postedList.get(position).getUserId(),postedList.get(position).getWorkerId(),"Accepted");

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();

                }
        );

        btnDelete.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Cancel Worker")
                            .setMessage("Are you sure you want to cancel worker?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    shifts.cancelRecruitShift(postedList.get(position).getShiftId(),postedList.get(position).getUserId(),postedList.get(position).getWorkerId(),"Accepted");

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();

                }
        );

        tvDistance.setText(context.getString(R.string.distance)+postedList.get(position).getShiftsdetail().get(0).getDistance()+" "+context.getString(R.string.miles));

        String date ="";
        String time ="";

        for (SuccessResGetCurrentSchedule.PostshiftTime dateTime:postshiftTimeList)
        {
            date = date+ dateTime.getNewDate()+",";
            dates.add(dateTime.getNewDate());
            listStartTime.add(dateTime.getStartTime());
            listEndTime.add(dateTime.getEndTime());
        }

        if (date.endsWith(","))
        {
            date = date.substring(0, date.length() - 1);
        }

        if(postedList.get(position).getShiftsdetail().get(0).getTimeType().equalsIgnoreCase("Single"))
        {

            tvTime.setVisibility(View.VISIBLE);
            tvMultipleTime.setVisibility(View.GONE);
            String startTime = postshiftTimeList.get(0).getStartTime();
            String endTime = postshiftTimeList.get(0).getEndTime();
            String text;

            String totalHours =  getTime24Difference(postshiftTimeList.get(0).getStartTimeNew(),postshiftTimeList.get(0).getEndTimeNew(),postedList.get(position).getShiftsdetail().get(0).getTransitAllowance());

            Double d = Double.parseDouble(totalHours);
            if(d < 2.0)
            {
                text  = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+totalHours+" hr)";
            }
            else
            {
                text = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+totalHours+" hrs)";
            }

//            double d=Double.parseDouble(postshiftTimeList.get(0).getTotalHours());
//
//            if(d < 2.0)
//            {
//                text  = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+postshiftTimeList.get(0).getTotalHours()+" hr)";
//            }
//            else
//            {
//                text = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+postshiftTimeList.get(0).getTotalHours()+" hrs)";
//            }

            //Set Total Time Here
            tvTime.setText(text);
        }
        else

        {
            tvTime.setVisibility(View.GONE);
            tvMultipleTime.setVisibility(View.VISIBLE);
        }

        tvMultipleTime.setOnClickListener(v ->
                {
                    showImageSelection(dates,listStartTime,listEndTime);
                }
        );

        if(postshiftTimeList.size()==1)
        {

            String dtStart = null;
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            try {

                dtStart = postshiftTimeList.get(0).getShiftDate()+" 8:20";

                Date  myDate = format.parse(dtStart);

                String pattern = "EEE, MMM d, yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(myDate);
                tvDate.setText(context.getString(R.string.date_col)+todayAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else
        {
            for (SuccessResGetCurrentSchedule.PostshiftTime dateTime:postshiftTimeList)
            {

                String month = dateTime.getNewMonth();
                Log.d(TAG, "onBindViewHolder: "+month);
                if(!isExistMonth(month))
                {
                    monthsList.add(month);
                }
            }
            String myMultipleDate = "";

            for (String monthItem:monthsList)
            {
                myMultipleDate = myMultipleDate + monthItem+" ";

                for(SuccessResGetCurrentSchedule.PostshiftTime dateTime:postshiftTimeList)
                {
                    if(monthItem.equalsIgnoreCase(dateTime.getNewMonth()))
                    {
                        myMultipleDate = myMultipleDate + dateTime.getNewDateSingle()+",";

                    }
                }

                if (myMultipleDate.endsWith(","))
                {
                    myMultipleDate = myMultipleDate.substring(0, myMultipleDate.length() - 1);
                }

                myMultipleDate = myMultipleDate +" | ";

            }

            if (myMultipleDate.endsWith(" | "))
            {
                myMultipleDate = myMultipleDate.substring(0, myMultipleDate.length() - 3);
            }

            tvDate.setText(context.getString(R.string.date_col)+myMultipleDate);

        }


        tvDuty.setText("("+postedList.get(position).getShiftsdetail().get(0).getDutyOfWorker()+")");

        if(postedList.get(position).getShiftsdetail().get(0).getUnpaidBreak().equalsIgnoreCase("None"))
        {
            tvBreak.setText(context.getString(R.string.unpaid_break)+postedList.get(position).getShiftsdetail().get(0).getUnpaidBreak());

        }
        else
        {
            tvBreak.setText(context.getString(R.string.unpaid_break)+postedList.get(position).getShiftsdetail().get(0).getUnpaidBreak()+" Minutes");

        }

        if(postedList.get(position).getShiftsdetail().get(0).getTransitAllowance().equalsIgnoreCase("None"))
        {
            tvTransit.setText(context.getString(R.string.transit_allowance)+postedList.get(position).getShiftsdetail().get(0).getTransitAllowance());

        }
        else
        {
            tvTransit.setText(context.getString(R.string.transit_allowance)+postedList.get(position).getShiftsdetail().get(0).getTransitAllowance()+" Hour");
        }

        String pay = " $" + postshiftTimeList.get(0).getPayamount() +" @ $"+postedList.get(position).getShiftsdetail().get(0).getHourlyRate()+"/hr";

        hrRate.setText(context.getString(R.string.pay_col)+pay);
        tvCovid.setText(context.getString(R.string.covid_19_negative)+postedList.get(position).getShiftsdetail().get(0).getCovidStatus());
        tvLocation.setText(postedList.get(position).getShiftsdetail().get(0).getShiftLocation());

        tvJobPosition.setText(postedList.get(position).getShiftsdetail().get(0).getJobPosition());

        if(postedList.get(position).getShiftsdetail().get(0).getAccountType().equalsIgnoreCase("Company"))
        {
            tvCompanyName.setText(postedList.get(position).getShiftsdetail().get(0).getCompany());
        }else
        {
            tvCompanyName.setText(postedList.get(position).getShiftsdetail().get(0).getUserName());
        }

        Glide.with(context)
                .load(postedList.get(position).getShiftsdetail().get(0).getUserImage())
                .apply(requestOptions)
                .into(ivProfile);

        rlShiftsNote.setOnClickListener(v ->
                {
                    // showShiftsNotes(postedList.get(position).getShiftNotes());
                    showNotes = !showNotes;
                    if(showNotes)
                    {
                        tvShiftNotes.setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        ivMinus.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvShiftNotes.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.VISIBLE);
                        ivMinus.setVisibility(View.GONE);
                    }

                }
        );


        tvLocation.setOnClickListener(v ->
                {
                  /*  Uri gmmIntentUri = Uri.parse("geo:13.0698281,77.58960549999999");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
*/

                    String lat = postedList.get(position).getShiftsdetail().get(0).getLocationLat();
                    String lon = postedList.get(position).getShiftsdetail().get(0).getLocationLon();

                    //                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat,lon);

                    String uri = "http://maps.google.com/maps?q=loc:"+lat+","+lon;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);

                }
        );

        tvWorkerName.setText(postedList.get(position).getShiftsdetail().get(0).getWorkerName());
        tvWorkerDesignation.setText("( "+postedList.get(position).getShiftsdetail().get(0).getWorkerDesignation()+" )");

        Glide.with(context)
                .load(postedList.get(position).getShiftsdetail().get(0).getWorkerImage())
                .apply(requestOptions)
                .into(ivWorkerProfile);

        int lastPosition = postedList.size()-1;

        if(postedList.size() == 1)
        {
            if(position == 0)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 30, 25, 95);
                cv.setLayoutParams(params);
            }
        }
        else
        {
            if(position == lastPosition)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 0, 25, 100);
                cv.setLayoutParams(params);
            }

            if(position == 0)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 30, 25, 75);
                cv.setLayoutParams(params);
            }
        }



    }

    RecyclerView rvMessageItem ;
    HealthInterface apiInterface;

    ChatAdapter chatAdapter;

    EditText etChat;

    private String name = "",id = "", image ="",strChatMessage = "";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    public void fullScreenDialog(String myId)
    {

        id = myId;

        Dialog  dialog = new Dialog(context, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.fragment_one2_one_chat);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        ImageView ivBack = dialog.findViewById(R.id.ivBack);
        
        ImageView ivSend = dialog.findViewById(R.id.ivSendMessage);

        etChat = dialog.findViewById(R.id.etChat);

        rvMessageItem = dialog.findViewById(R.id.rvMessageItem);

        ivBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        ImageView ivAdminChat = dialog.findViewById(R.id.ivAdminChat);

        ivAdminChat.setOnClickListener(v ->
                {

                    context.startActivity(new Intent(context, ConversationAct.class));

                }
        );
        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);

        chatAdapter = new ChatAdapter(context,chatList,userId);
        rvMessageItem.setHasFixedSize(true);
        rvMessageItem.setLayoutManager(new LinearLayoutManager(context));
        rvMessageItem.setAdapter(chatAdapter);
        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));

        getChat();

       Timer timer = new Timer();
       timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isLastVisible()){
                    getChat();
                }
            }
        },0,5000);

       ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strChatMessage = etChat.getText().toString();
                if(!strChatMessage.equals(""))
                {
                    insertChat();
                }

            }
        });

        dialog.show();

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

        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);
        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));

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
                        rvMessageItem.setLayoutManager(new LinearLayoutManager(context));
                        rvMessageItem.setAdapter(chatAdapter);
                        //chatAdapter.notifyDataSetChanged();
                        rvMessageItem.scrollToPosition(chatList.size()-1);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {

                        showToast((Activity) context, data.message);

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
        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);
       /*
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody receiverID = RequestBody.create(MediaType.parse("text/plain"),receiverId);
        RequestBody chatMessage = RequestBody.create(MediaType.parse("text/plain"), strChatMessage);
        RequestBody itemId = RequestBody.create(MediaType.parse("text/plain"),itemID);
*/

        DataManager.getInstance().showProgressMessage((Activity) context, context.getString(R.string.please_wait));
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

                        showToast((Activity) context, data.message);

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





    public void showImageSelection(List<String> dates,List<String> startTimeList,List<String> endTimeList) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.multiple_date_recycler_view_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        RecyclerView rvDateTime = dialog.findViewById(R.id.rvDateTime);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);


        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        rvDateTime.setHasFixedSize(true);
        rvDateTime.setLayoutManager(new LinearLayoutManager(context));
        rvDateTime.setAdapter(new ShowDateTimeAdapter(context,dates,startTimeList,endTimeList));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public boolean isExistMonth(String month)
    {

        for (String myMonth:monthsList)
        {

            if(month.equalsIgnoreCase(myMonth))
            {
                return true;
            }

        }

        return false;
    }


    public void showShiftsNotes(String notes) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_shifts_notes);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);


        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        TextView tvShiftsNotes = dialog.findViewById(R.id.tvShiftsNotes);

        tvShiftsNotes.setText(notes);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return postedList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public String modifyDateLayout(String inputDate) throws ParseException{
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(inputDate);
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }

}
