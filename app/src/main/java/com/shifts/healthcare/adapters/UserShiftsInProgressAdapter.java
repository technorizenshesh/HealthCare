package com.shifts.healthcare.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
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
import com.shifts.healthcare.activites.One2OneChatAct;
import com.shifts.healthcare.models.SuccessResGetChat;
import com.shifts.healthcare.models.SuccessResGetShiftInProgress;
import com.shifts.healthcare.models.SuccessResInsertChat;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.SharedPreferenceUtility;
import com.shifts.healthcare.util.ShiftCompletedClick;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.shifts.healthcare.adapters.ShiftsAdapter.getTime24Difference;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;
import static com.shifts.healthcare.workerSide.fragments.WorkerShiftsInProgressFragment.getTimeTodayOrNot;

public class UserShiftsInProgressAdapter extends RecyclerView.Adapter<UserShiftsInProgressAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;
    private Context context;
    private  ArrayList<SuccessResGetShiftInProgress.Result> postedList ;
    List<String> monthsList = new LinkedList<>();
    private boolean from ;
    private boolean showNotes = false;
    private String fromWhere;
    long startTim = 0;
    long endTim = 0;
    Date date2= null;
    Date date3= null;
    Date date1 = null;
    long totalSeconds;
    long newEndTime = 0;
    long displayTim = 0;
    private ShiftCompletedClick shiftCompletedClick;
    public UserShiftsInProgressAdapter(Context context, ArrayList<SuccessResGetShiftInProgress.Result> postedList, boolean from, String fromWhere,ShiftCompletedClick shiftCompletedClick)

    {
        this.context = context;
        this.postedList = postedList;
        this.from = from;
        this.fromWhere = fromWhere;
        this.shiftCompletedClick = shiftCompletedClick;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.shift_in_progress_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        RelativeLayout rlChat;

        rlChat = holder.itemView.findViewById(R.id.rlChat);
        TextView tvWorkerMsg = holder.itemView.findViewById(R.id.tvWorkerMsg);
         startTim = 0;
         endTim = 0;
         date2= null;
         date1 = null;
         holder.handler = null;
         totalSeconds = 0;
         holder.runnable = null;
         displayTim = 0;
        List<String> dates = new LinkedList<>();
        List<String> listStartTime = new LinkedList<>();
        List<String> listEndTime = new LinkedList<>();
        showNotes = false;
        List<SuccessResGetShiftInProgress.PostshiftTime> postshiftTimeList =  new LinkedList<>();
        postshiftTimeList = postedList.get(position).getPostshiftTime();
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
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        tvShiftNotes.setText(postedList.get(position).getShiftsdetail().get(0).getShiftNotes());
        ImageView ivAdd = holder.itemView.findViewById(R.id.plus);
        ImageView ivMinus = holder.itemView.findViewById(R.id.minus);
        ImageView ivChat = holder.itemView.findViewById(R.id.ivChat);
        AppCompatButton btnClock = holder.itemView.findViewById(R.id.btnClock);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms( new RoundedCorners(15));
        ImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);
        ImageView ivWorkerProfile = holder.itemView.findViewById(R.id.ivWorker);
        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);
        if(postedList.get(position).getShiftsdetail().get(0).getDayType().equalsIgnoreCase("Single"))
        {
            tvShiftNumber.setText(postedList.get(position).getShiftNo());
        }
        else
        {
            tvShiftNumber.setText(postedList.get(position).getShiftSubNo());
        }

        if(!postedList.get(position).getTotalUnseenMessage().equalsIgnoreCase("0"))
        {
            tvWorkerMsg.setVisibility(View.VISIBLE);
            tvWorkerMsg.setText(postedList.get(position).getTotalUnseenMessage());
        }else
        {
            tvWorkerMsg.setVisibility(View.GONE);
        }

        if(postedList.get(position).getShiftsdetail().get(0).getNoVacancies().equalsIgnoreCase("1"))
        {
            tvID.setVisibility(View.GONE);
        }
        else
        {
            tvID.setVisibility(View.VISIBLE);
        }
        tvID.setText(postedList.get(position).getVacanciesNo());
        tvDistance.setText(context.getString(R.string.distance)+postedList.get(position).getShiftsdetail().get(0).getDistance()+" "+context.getString(R.string.miles));
        String date ="";
        String time ="";
        for (SuccessResGetShiftInProgress.PostshiftTime dateTime:postshiftTimeList)
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
        tvTime.setVisibility(View.VISIBLE);
        tvMultipleTime.setVisibility(View.GONE);
        String startTime = postshiftTimeList.get(0).getStartTime();
        String endTime = postshiftTimeList.get(0).getEndTime();
//        String text = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+postshiftTimeList.get(0).getTotalHours()+" hrs)";
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
        tvTime.setText(text);
        String dtStart = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            dtStart = postshiftTimeList.get(0).getShiftDate()+" 8:20";
            Date  myDate = format.parse(dtStart);
            String pattern = "EEE, MMM d, yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            String todayAsString = df.format(myDate);
            tvDate.setText(context.getString(R.string.date_col)+todayAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                    String lat = postedList.get(position).getShiftsdetail().get(0).getLocationLat();
                    String lon = postedList.get(position).getShiftsdetail().get(0).getLocationLon();
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

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        String dateString = postedList.get(position).getShiftDate()+" "+postedList.get(position).getClockInTime();
        try {
            date1 = sdf.parse(dateString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);
//            startTim = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

        String newTIme = postedList.get(position).getEndTime();
        String[] splitStr = newTIme.split("\\s+");
        String  shiftEndTime = splitStr[0]+":00 "+splitStr[1];

        String dateString2 = postedList.get(position).getShiftDate()+" "+shiftEndTime;
        try {
            date2 = sdf2.parse(dateString2);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(date2);
//
//            endTim = calendar2.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(getTimeTodayOrNot(postedList.get(position).getPostshiftTime().get(0).getStartTimeNew(),postedList.get(position).getPostshiftTime().get(0).getEndTimeNew()))
        {

          Log.d(TAG, "runTimer: "+"Today");

          holder.handler = new Handler();
          holder.runnable = new Runnable() {
            @Override
            public void run() {
                holder.handler.postDelayed(holder.runnable, 1000);
                long currentTim = 0;
                Log.d(TAG, "run: "+position+" "+totalSeconds);
                try {
                     date3 = new Date();//sdf3.parse(dateString2);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(date3);
                    currentTim = calendar3.getTimeInMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                if (endTim > currentTim) {
//                    if (startTim <= currentTim) {
//                        displayTim = currentTim - startTim;
//                    } else {
//
//                        displayTim = startTim - currentTim;
//                    }

                if (date2.compareTo(date3)==1) {
//                    if (startTim <= currentTim) {
                    if (date1.compareTo(date3) <= 0) {
                        displayTim = date3.getTime() - date1.getTime();
                    } else {
                        displayTim = date1.getTime() - date3.getTime();
                    }
                    totalSeconds = displayTim / 1000;
                    long difference = date1.getTime() - date2.getTime();
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                    Log.d(TAG, "run: "+diffInSec);
                    int currentSecond = (int) totalSeconds % 60;
                    long totalMinutes = totalSeconds / 60;
                    int currentMinute = (int) totalMinutes % 60;
                    long totalHours = totalMinutes / 60;
                    int currentHour = (int) totalHours % 12;
                    String time
                            = String
                            .format(Locale.getDefault(),
                                    "%02d:%02d:%02d", currentHour,
                                    currentMinute, currentSecond);
                    btnClock.setText(time);
                } else {
                    long difference = date2.getTime() - date1.getTime();
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                    totalSeconds = diffInSec;
                    holder.handler.removeCallbacks(null);
                    int currentSecond = (int) totalSeconds % 60;
                    long totalMinutes = totalSeconds / 60;
                    int currentMinute = (int) totalMinutes % 60;
                    long totalHours = totalMinutes / 60;
                    int currentHour = (int) totalHours % 12;
                    String time
                            = String
                            .format(Locale.getDefault(),
                                    "%02d:%02d:%02d", currentHour,
                                    currentMinute, currentSecond);
                    btnClock.setText(time);
                    shiftCompletedClick.getClick();
                }
            }
        };
        holder.handler.postDelayed(holder.runnable, 1000);
        }
        else
        {
            SimpleDateFormat sdf12 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            String dateString12 = postedList.get(position).getShiftDate()+" "+postedList.get(position).getClockInTime();
            try {
                date1 = sdf12.parse(dateString12);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date1);
                startTim = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf21 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            String newShiftEndTime21 = postedList.get(position).getEndTime();
            String newTIme21 = newShiftEndTime21;
            String[] splitStr21 = newTIme21.split("\\s+");
            String timeMy = splitStr21[0]+":00 "+splitStr21[1];
            String dateString21 = postedList.get(position).getShiftDate()+" "+timeMy;
            try {
                date2 = sdf21.parse(dateString21);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date2);
                calendar2.add(Calendar.DATE, 1);
                date2 = calendar2.getTime();
                endTim = calendar2.getTimeInMillis();
                newEndTime = endTim;

            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.handler = new Handler();

            holder.runnable = new Runnable() {
                @Override
                public void run() {
                    holder.handler.postDelayed(holder.runnable, 1000);
                    long currentTim = 0;
                    try {
                        date3 = new Date();//sdf3.parse(dateString2);
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(date3);
                        currentTim = calendar3.getTimeInMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


//                    if(displayTim<endTim)
//                    {
//                        if(currentTim<newEndTime)
//                        {
//                            currentTim = currentTim + 1440 * 60 * 1000;
//                        }
//
//                        if (startTim <= currentTim) {
//
//                            if(endTim<currentTim)
//                            {
//                                Calendar calendar2 = Calendar.getInstance();
//                                calendar2.setTime(date2);
//                                calendar2.add(Calendar.DATE, 1);
//                                date2 = calendar2.getTime();
//                                long difference = date2.getTime() - date1.getTime();
//                                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                                totalSeconds = diffInSec;
//                                holder.handler.removeCallbacks(holder.runnable);
//
//                                int currentSecond = (int) totalSeconds % 60;
//                                long totalMinutes = totalSeconds / 60;
//                                int currentMinute = (int) totalMinutes % 60;
//                                long totalHours = totalMinutes / 60;
//                                int currentHour = (int) totalHours % 12;
//
//                                String time
//                                        = String
//                                        .format(Locale.getDefault(),
//                                                "%02d:%02d:%02d", currentHour,
//                                                currentMinute, currentSecond);
//                                btnClock.setText(time);
//
//                            }
//                            displayTim = currentTim - startTim;
//                        } else {
//                            displayTim = startTim - currentTim;
//                        }
//
//                        totalSeconds = displayTim / 1000;
//                        long difference = date1.getTime() - date2.getTime();
//                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                        Log.d(TAG, "run: "+diffInSec);
//                        int currentSecond = (int) totalSeconds % 60;
//                        long totalMinutes = totalSeconds / 60;
//                        int currentMinute = (int) totalMinutes % 60;
//                        long totalHours = totalMinutes / 60;
//                        int currentHour = (int) totalHours % 12;
//                        String time
//                                = String
//                                .format(Locale.getDefault(),
//                                        "%02d:%02d:%02d", currentHour,
//                                        currentMinute, currentSecond);
//
//                        btnClock.setText(time);
//
//                    }

                    if (date2.compareTo(date3)==1) {
//                    if (startTim <= currentTim) {
                        if (date1.compareTo(date3) <= 0) {
                            displayTim = date3.getTime() - date1.getTime();
                        } else {
                            displayTim = date1.getTime() - date3.getTime();
                        }
                        totalSeconds = displayTim / 1000;
                        long difference = date1.getTime() - date2.getTime();
                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                        Log.d(TAG, "run: "+diffInSec);
                        int currentSecond = (int) totalSeconds % 60;
                        long totalMinutes = totalSeconds / 60;
                        int currentMinute = (int) totalMinutes % 60;
                        long totalHours = totalMinutes / 60;
                        int currentHour = (int) totalHours % 12;
                        String time
                                = String
                                .format(Locale.getDefault(),
                                        "%02d:%02d:%02d", currentHour,
                                        currentMinute, currentSecond);
                        btnClock.setText(time);
                    }

                    else
                    {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(date2);
                        date2 = calendar2.getTime();
                        long difference = date2.getTime() - date1.getTime();
                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                        totalSeconds = diffInSec;

                        int currentSecond = (int) totalSeconds % 60;
                        long totalMinutes = totalSeconds / 60;
                        int currentMinute = (int) totalMinutes % 60;
                        long totalHours = totalMinutes / 60;
                        int currentHour = (int) totalHours % 12;

                        String time
                                = String
                                .format(Locale.getDefault(),
                                        "%02d:%02d:%02d", currentHour,
                                        currentMinute, currentSecond);
                        btnClock.setText(time);
                        shiftCompletedClick.getClick();

                     }

//                    if (endTim > currentTim) {
//
//                    } else {
//
//                        updateTime = true;
//                        updatTimeInShiftsInProgress();
//                        long difference = date2.getTime() - date1.getTime();
//                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                        totalSeconds = diffInSec;
//                        handler.removeCallbacks(runnable);
//                        timerHandler.removeCallbacks(null);
//
////                        shiftCompleted();
//
//                    }

                }
            };

            holder.handler.postDelayed(holder.runnable, 1000);

//        String time = shiftDate+" "+shiftStartTime;
//        String endTime = shiftDate+" "+shiftEndTime;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
//        try {
//
//            date = dateFormat.parse(time);
//            endDate = dateFormat.parse(endTime);
////            Calendar c = Calendar.getInstance();
////            c.setTime(endDate);
////            c.add(Calendar.DATE, 1);
////            endDate = c.getTime();
//
//        } catch (ParseException e) {
//        }
//        Date currentTime = Calendar.getInstance().getTime();
//        long difference = date.getTime() - currentTime.getTime();
//        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//         i = (int) diffInSec;
//        if(i < 0 || i==0)
//        {
//
//            if(handler==null)
//            {
//
//                handler      = new Handler();
//                timerHandler = new Handler();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        int hours = seconds / 3600;
//                        int minutes = (seconds % 3600) / 60;
//                        int secs = seconds % 60;
//                        Date currentTime1 = Calendar.getInstance().getTime();
//                        long difference1 = endDate.getTime() - currentTime1.getTime();
//                        long diffInSec1 = TimeUnit.MILLISECONDS.toSeconds(difference1);
//                        int  j = (int) diffInSec1;
//                        if(j<0)
//                        {
//                            timerHandler.removeCallbacks(null);
//                            handler.removeCallbacks(null);
//                            shiftCompleted();
//                        }
//
////                        Date currentTime = Calendar.getInstance().getTime();
////
////                        long difference = date.getTime() - currentTime.getTime();
////
////                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
////
////                        i = (int) diffInSec;
////
////                        seconds = Math.abs(i);
//
//                        String time
//                                = String
//                                .format(Locale.getDefault(),
//                                        "%02d:%02d:%02d", hours,
//                                        minutes, secs);
//
//                        binding.btnClock.setText(time);
//
//                        if (running) {
//                            seconds++;
//                            SharedPreferenceUtility.getInstance(getActivity()).putInt(SEC,seconds);
//                        }
//
//                        handler.postDelayed(this, 1 * 1000);
//                    }
//                });
//
//                timerHandler.post(new Runnable() {
//                    @Override
//
//                    public void run()
//                    {
//                        updatTimeInShiftsInProgress();
//                        handler.postDelayed(this, 1 * 1000);
//                    }
//                });
//            }
//        }
//        else
//        {
//
//            if(negativeHandler==null)
//            {
//
//                negativeHandler = new Handler();
//                negativeHandler.post(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        if(i==0 || i < 0 )
//                        {
//                            i=-1;
//                            negativeHandler.removeCallbacks(null);
//                            runTimer();
//                        }
//
//                        int hours = i / 3600;
//                        int minutes = (i % 3600) / 60;
//                        int secs = i % 60;
//                        String time
//                                = String
//                                .format(Locale.getDefault(),
//                                        "%02d:%02d:%02d", hours,
//                                        minutes, secs);
//                        if(i>0)
//                        {
//                            binding.btnClock.setText(time);
//                        }
//
//                        if (running) {
//                            i = i-1;
//                        }
//                        negativeHandler.postDelayed(this, 1 * 1000);
//                    }
//                });
//            }
//        }

        }


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

        rlChat.setOnClickListener(v ->
                {

                    if(fromWhere.equalsIgnoreCase("userhome"))
                    {

//                        fullScreenDialog(postedList.get(position).getWorkerId());

                        context.startActivity(new Intent(context, One2OneChatAct.class).putExtra("id",postedList.get(position).getWorkerId()));


                     /*   Bundle bundle = new Bundle();
                        bundle.putString("name",postedList.get(position).getShiftsdetail().get(0).getWorkerName());
                        bundle.putString("image",postedList.get(position).getShiftsdetail().get(0).getWorkerImage());
                        bundle.putString("id",postedList.get(position).getWorkerId());
                        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_one2OneChatFragment,bundle);
*/
                    }else  if(fromWhere.equalsIgnoreCase("usershifthistory"))
                    {

                        context.startActivity(new Intent(context, One2OneChatAct.class).putExtra("id",postedList.get(position).getWorkerId()));

//                        Bundle bundle = new Bundle();
//                        bundle.putString("name",postedList.get(position).getShiftsdetail().get(0).getWorkerName());
//                        bundle.putString("image",postedList.get(position).getShiftsdetail().get(0).getWorkerImage());
//                        bundle.putString("id",postedList.get(position).getWorkerId());
//                        Navigation.findNavController(v).navigate(R.id.action_shiftHistoryFragment_to_one2OneChatFragment,bundle);

                    }else  if(fromWhere.equalsIgnoreCase("workerHistory"))
                    {

                        context.startActivity(new Intent(context, One2OneChatAct.class).putExtra("id",postedList.get(position).getUserId()));


//                        Bundle bundle = new Bundle();
//                        bundle.putString("name",postedList.get(position).getShiftsdetail().get(0).getWorkerName());
//                        bundle.putString("image",postedList.get(position).getShiftsdetail().get(0).getWorkerImage());
//                        bundle.putString("id",postedList.get(position).getUserId());
//                        Navigation.findNavController(v).navigate(R.id.action_workerShiftsHistoryFragment_to_one2OneChatFragment2,bundle);

                    }else  if(fromWhere.equalsIgnoreCase("usershiftInProgress"))
                    {

                        context.startActivity(new Intent(context, One2OneChatAct.class).putExtra("id",postedList.get(position).getWorkerId()));

//                        Bundle bundle = new Bundle();
//                        bundle.putString("name",postedList.get(position).getShiftsdetail().get(0).getWorkerName());
//                        bundle.putString("image",postedList.get(position).getShiftsdetail().get(0).getWorkerImage());
//                        bundle.putString("id",postedList.get(position).getWorkerId());
//                        Navigation.findNavController(v).navigate(R.id.action_shiftInProgressFragment_to_one2OneChatFragment,bundle);

                    }
                }
                );

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
                    Log.e("data",data.getResult()+"");
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

        private Handler handler;
        private Runnable runnable;

        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public String modifyDateLayout(String inputDate) throws ParseException{
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(inputDate);
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }

}
