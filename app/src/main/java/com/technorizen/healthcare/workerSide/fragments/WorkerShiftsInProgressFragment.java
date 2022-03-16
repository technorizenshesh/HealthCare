package com.technorizen.healthcare.workerSide.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.activites.ConversationAct;
import com.technorizen.healthcare.activites.LoginAct;
import com.technorizen.healthcare.adapters.ChatAdapter;
import com.technorizen.healthcare.adapters.ShowDateTimeAdapter;
import com.technorizen.healthcare.databinding.FragmentWorkerShiftsInProgressBinding;
import com.technorizen.healthcare.models.SuccessResGetChat;
import com.technorizen.healthcare.models.SuccessResGetShiftInProgress;
import com.technorizen.healthcare.models.SuccessResInsertChat;
import com.technorizen.healthcare.models.SuccessResShiftCompleted;
import com.technorizen.healthcare.models.SuccessResUpdateShiftInProgressTime;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

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
import static android.content.Context.POWER_SERVICE;
import static com.technorizen.healthcare.adapters.ShiftsAdapter.getTime24Difference;
import static com.technorizen.healthcare.retrofit.Constant.RUNNING;
import static com.technorizen.healthcare.retrofit.Constant.SEC;
import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.WASRUNNING;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerShiftsInProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerShiftsInProgressFragment extends Fragment {
    private String shift24hrStartTime = "";
    private String shiftID = "";
    private String strUnpaidBreak = "";
    FragmentWorkerShiftsInProgressBinding binding;
    HealthInterface apiInterface;
    private String shiftEndTime = "";
    private long totalSeconds;
    private String shiftDate = "";
    Handler timerHandler = null;
    Runnable runnable;
    long displayTim = 0;
    private String newCurrentStartTime = "";
    private String strHourlyRate = "";
    private String shiftStartTime = "";
    private String strUnpaidBreakHours = "0";
    private String updateTimeshiftID = "";
    List<String> monthsList = new LinkedList<>();
    private String shift24HrEndTime = "";
    private ArrayList<SuccessResGetShiftInProgress.Result> shiftInProgressList = new ArrayList<>();

    private boolean hasShiftInProgress = false;
    private SuccessResGetShiftInProgress.Result shiftInProgress ;
    Handler handler = null;
    //    private int seconds = 0;
    // Is the stopwatch running?
    private boolean running;
    private boolean wasRunning;
    long startTim = 0;
    long endTim = 0;
    long newEndTime = 0;
    Date date2= null;
    Date date = null;
    Date date1 = null;
    Date date3 = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String strTotalHours = "";
    public WorkerShiftsInProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerShiftsInProgressFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static WorkerShiftsInProgressFragment newInstance(String param1, String param2) {
        WorkerShiftsInProgressFragment fragment = new WorkerShiftsInProgressFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_shifts_in_progress, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        getShiftsInProgress();
        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShiftsInProgress();
                binding.srlRefreshContainer.setRefreshing(false);
            }
        });

        binding.btnClockOut.setOnClickListener(v ->
                {
                    updatTimeInShiftsInProgress();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Clock Out")
                            .setMessage("Are you sure you want to clock out shift?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    shiftCompleted();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();
                }
        );
        return binding.getRoot();
    }

    public void getShiftsInProgress()
    {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        Call<SuccessResGetShiftInProgress> call = apiInterface.getShiftsInProgress(map);
        call.enqueue(new Callback<SuccessResGetShiftInProgress>() {
            @Override
            public void onResponse(Call<SuccessResGetShiftInProgress> call, Response<SuccessResGetShiftInProgress> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetShiftInProgress data = response.body();
                    if (data.status.equals("1")) {
                        hasShiftInProgress = true;
                        binding.cvSHiftInProgress.setVisibility(View.VISIBLE);
                        shiftInProgressList.clear();
                        shiftInProgressList.addAll(data.getResult());
                        shiftInProgress = shiftInProgressList.get(0);
                        updateTimeshiftID = shiftInProgress.getSid();
                        shiftID = shiftInProgressList.get(0).getSid();
                        setShiftInProgress();
                        shiftStartTime = shiftInProgressList.get(0).getClockInTime();
                        shiftEndTime = shiftInProgressList.get(0).getPostshiftTime().get(0).getEndTime();
                        shiftDate = shiftInProgressList.get(0).getPostshiftTime().get(0).getShiftDate();
                        strHourlyRate = shiftInProgressList.get(0).getShiftsdetail().get(0).getHourlyRate();
                        strUnpaidBreak = shiftInProgressList.get(0).getShiftsdetail().get(0).getUnpaidBreak();
                        shift24hrStartTime = shiftInProgressList.get(0).getPostshiftTime().get(0).getStartTimeNew();
                        strTotalHours = shiftInProgressList.get(0).getPostshiftTime().get(0).getTotalHours()+"";
                        shift24HrEndTime = shiftInProgressList.get(0).getPostshiftTime().get(0).getEndTimeNew();

                        SharedPreferenceUtility.getInstance(getActivity()).putBoolean(RUNNING,true);
                        SharedPreferenceUtility.getInstance(getActivity()).putBoolean(WASRUNNING,true);
                        running
                                = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(RUNNING);
                        wasRunning
                                = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(WASRUNNING);
                        runTimer();
                    } else {
                        binding.cvSHiftInProgress.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetShiftInProgress> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private boolean showNotes = false;

    public void setShiftInProgress()
    {
        showNotes = false;
        List<String> dates = new LinkedList<>();
        List<String> listStartTime = new LinkedList<>();
        List<String> listEndTime = new LinkedList<>();
        List<SuccessResGetShiftInProgress.PostshiftTime> postshiftTimeList =  new LinkedList<>();
        postshiftTimeList = shiftInProgress.getPostshiftTime();

        binding.tvID.setText(shiftInProgress.getId());
        binding.tvDistance.setText(getString(R.string.distance)+shiftInProgress.getShiftsdetail().get(0).getDistance());

        String date ="";
        String time ="";

        binding.ivChat.setOnClickListener(v ->
                {
                    fullScreenDialog(shiftInProgress.getUserId());
                }
                );
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
        if(shiftInProgress.getShiftsdetail().get(0).getTimeType().equalsIgnoreCase("Single"))
        {
            binding.tvSignleTime.setVisibility(View.VISIBLE);
            binding.tvMutlipleTime.setVisibility(View.GONE);
            String startTime = postshiftTimeList.get(0).getStartTime();
            String endTime = postshiftTimeList.get(0).getEndTime();
            String text;
            String totalHours =  getTime24Difference(postshiftTimeList.get(0).getStartTimeNew(),postshiftTimeList.get(0).getEndTimeNew(),shiftInProgress.getShiftsdetail().get(0).getTransitAllowance());
            Double d = Double.parseDouble(totalHours);
            if(d < 2.0)
            {
                text  = getActivity().getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+totalHours+" hr)";
            }
            else
            {
                text = getActivity().getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+totalHours+" hrs)";
            }
            //Set Total Time Here
            binding.tvSignleTime.setText(text);
        }
        else
        {
            binding.tvSignleTime.setVisibility(View.GONE);
            binding.tvMutlipleTime.setVisibility(View.VISIBLE);
        }

        binding.tvMutlipleTime.setOnClickListener(v ->
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
                Date myDate = format.parse(dtStart);
                String pattern = "EEE, MMM d, yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String todayAsString = df.format(myDate);
                binding.tvDate.setText(getString(R.string.date_col)+todayAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else

        {

            for (SuccessResGetShiftInProgress.PostshiftTime dateTime:postshiftTimeList)

            {
                String month = dateTime.getNewMonth();
                if(!isExistMonth(month))
                {
                    monthsList.add(month);
                }
            }

            String myMultipleDate = "";
            for (String monthItem:monthsList)
            {
                myMultipleDate = myMultipleDate + monthItem+" ";
                for(SuccessResGetShiftInProgress.PostshiftTime dateTime:postshiftTimeList)
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
            binding.tvDate.setText(getString(R.string.date_col)+myMultipleDate);
            binding.tvShiftsNotes.setText(shiftInProgress.getShiftsdetail().get(0).getShiftNotes());
        }

        binding.tvDuty.setText("("+shiftInProgress.getShiftsdetail().get(0).getDutyOfWorker()+")");
        binding.tvBreak.setText(getString(R.string.unpaid_break)+shiftInProgress.getShiftsdetail().get(0).getUnpaidBreak());

        String pay = " $" + postshiftTimeList.get(0).getPayamount() +" @ $"+shiftInProgress.getShiftsdetail().get(0).getHourlyRate()+"/hr";

        binding.hrRate.setText(getString(R.string.pay_col)+pay);
        binding.tvCovid.setText(getString(R.string.covid_19_negative)+shiftInProgress.getShiftsdetail().get(0).getCovidStatus());
        binding.tvLocation.setText(shiftInProgress.getShiftsdetail().get(0).getShiftLocation());

        binding.jobPosition.setText(shiftInProgress.getShiftsdetail().get(0).getJobPosition());

        if(shiftInProgress.getShiftsdetail().get(0).getAccountType().equalsIgnoreCase("Company"))
        {
            binding.tvCompanyName.setText(shiftInProgress.getShiftsdetail().get(0).getCompany());
        }else
        {
            binding.tvCompanyName.setText(shiftInProgress.getShiftsdetail().get(0).getUserName());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(13));

        Glide.with(getActivity())
                .load(shiftInProgress.getShiftsdetail().get(0).getUserImage())
                .centerCrop()
                .apply(requestOptions)
                .into(binding.ivProfile);

        binding.rlShiftsNotes.setOnClickListener(v ->
                {
                    //   showShiftsNotes(shiftInProgress.getShiftsdetail().get(0).getShiftNotes());

                    showNotes = !showNotes;

                    if(showNotes)
                    {

                        binding.tvShiftsNotes.setVisibility(View.VISIBLE);
                        binding.plus.setVisibility(View.GONE);
                        binding.minus.setVisibility(View.VISIBLE);

                    }
                    else
                    {

                        binding.tvShiftsNotes.setVisibility(View.GONE);
                        binding.plus.setVisibility(View.VISIBLE);
                        binding.minus.setVisibility(View.GONE);

                    }

                }
        );

        binding.tvLocation.setOnClickListener(v ->
                {


                    String lat = shiftInProgress.getShiftsdetail().get(0).getLocationLat();
                    String lon = shiftInProgress.getShiftsdetail().get(0).getLocationLon();

                    String uri = "http://maps.google.com/maps?q=loc:"+lat+","+lon;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);

                }
        );

        binding.tvWorkerName.setText(shiftInProgress.getShiftsdetail().get(0).getWorkerName());
        binding.tvWorkerDesignation.setText("( "+shiftInProgress.getShiftsdetail().get(0).getWorkerDesignation()+" )");

        Glide.with(getActivity())
                .load(shiftInProgress.getShiftsdetail().get(0).getWorkerImage())
                .centerCrop()
                .apply(requestOptions)
                .into(binding.ivWorker);

    }


    @Override
    public void onPause()
    {
        super.onPause();
        PowerManager powerManager = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();
        if (!isScreenOn) {
            if(hasShiftInProgress)
            {
                runTimer();
            }
        }
    }

    public void showImageSelection(List<String> dates,List<String> startTimeList,List<String> endTimeList) {

        final Dialog dialog = new Dialog(getActivity());
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
        rvDateTime.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDateTime.setAdapter(new ShowDateTimeAdapter(getActivity(),dates,startTimeList,endTimeList));

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
    private boolean updateTime = false;
/*

    private void runTimer()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//                String dateString = "21-12-2021 12:08 pm";shiftDate+" "+shiftStartTime
        String dateString = shiftDate+" "+shiftStartTime;

        try {
            date1 = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            startTim = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Specifying the pattern of input date and time
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//                String dateString2 = "21-12-2021 12:12 pm";
        String dateString2 = shiftDate+" "+shiftEndTime;

        try {
            date2 = sdf2.parse(dateString2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
//                    System.out.println("Given Time in milliseconds :end "+calendar2.getTimeInMillis());
            endTim = calendar2.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        handler = new Handler();

        timerHandler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(runnable, 1000);

                long currentTim = 0;

                try {
                    Date date3 = new Date();//sdf3.parse(dateString2);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(date3);
                    currentTim = calendar3.getTimeInMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (endTim > currentTim) {
                    if (startTim <= currentTim) {
                        updateTime = true;
                        displayTim = currentTim - startTim;
                    } else {
                        updateTime = false;
                        displayTim = startTim - currentTim;
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

                    binding.btnClock.setText(time);

                    if(updateTime)
                    {
                        SharedPreferenceUtility.getInstance(getActivity()).putInt(SEC,(int)totalSeconds);

                        timerHandler.post(new Runnable() {
                            @Override

                            public void run()
                            {
                                updatTimeInShiftsInProgress();
                                handler.postDelayed(this, 1 * 5000);
                            }
                        });

                    }

                } else {

                    updatTimeInShiftsInProgress();

                    long difference = date2.getTime() - date1.getTime();

                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);

                    totalSeconds = diffInSec;

                    handler.removeCallbacks(null);

                    timerHandler.removeCallbacks(null);


                    shiftCompleted();

                }
            }
        };
        handler.postDelayed(runnable, 1000);


//        // Get the text view.
//
//        // Creates a new Handler
//
//        if(handler==null)
//        {
//            handler      = new Handler();
//
//            // Call the post() method,
//            // passing in a new Runnable.
//            // The post() method processes
//            // code without a delay,
//            // so the code in the Runnable
//            // will run almost immediately.
//            handler.post(new Runnable() {
//                @Override
//
//                public void run()
//                {
//                    int hours = seconds / 3600;
//                    int minutes = (seconds % 3600) / 60;
//                    int secs = seconds % 60;
//
//                    // Format the seconds into hours, minutes,
//                    // and seconds.
//                    String time
//                            = String
//                            .format(Locale.getDefault(),
//                                    "%02d:%02d:%02d", hours,
//                                    minutes, secs);
//
//                    // Set the text view text.
//                    binding.btnClock.setText(time);
//
//                    // If running is true, increment the
//                    // seconds variable.
//                    if (running) {
//                        seconds++;
//                    }
//
//                    // Post the code again
//                    // with a delay of 1 second.
//                    handler.postDelayed(this, 1000);
//                }
//            });
//        }

    }
*/


//    private void runTimer()
//    {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
//
//        String dateString = shiftDate+" "+shiftStartTime;
//
//        try {
//            date1 = sdf.parse(dateString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);
//            startTim = calendar.getTimeInMillis();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//
//        String dateString2 = shiftDate+" "+shiftEndTime;
//
//        try {
//            date2 = sdf2.parse(dateString2);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(date2);
////                    System.out.println("Given Time in milliseconds :end "+calendar2.getTimeInMillis());
//            endTim = calendar2.getTimeInMillis();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        handler = new Handler();
//
//        timerHandler = new Handler();
//
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                handler.postDelayed(runnable, 1000);
//
//                long currentTim = 0;
//
//                //Specifying the pattern of input date and time
////        SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm a");
////        String dateString3 = "07:30 pm";
//
//                try {
//                    Date date3 = new Date();//sdf3.parse(dateString2);
//                    Calendar calendar3 = Calendar.getInstance();
//                    calendar3.setTime(date3);
//                    currentTim = calendar3.getTimeInMillis();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (endTim > currentTim) {
//                    if (startTim <= currentTim) {
//                        updateTime = true;
//                        displayTim = currentTim - startTim;
//                    } else {
//                        updateTime = false;
//                        displayTim = startTim - currentTim;
//                    }
//                    totalSeconds = displayTim / 1000;
//
//                    long difference = date1.getTime() - date2.getTime();
//
//                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//
//                    Log.d(TAG, "run: "+diffInSec);
//
//                    int currentSecond = (int) totalSeconds % 60;
//
//                    long totalMinutes = totalSeconds / 60;
//                    int currentMinute = (int) totalMinutes % 60;
//
//                    long totalHours = totalMinutes / 60;
//                    int currentHour = (int) totalHours % 12;
//
//                    String time
//                            = String
//                            .format(Locale.getDefault(),
//                                    "%02d:%02d:%02d", currentHour,
//                                    currentMinute, currentSecond);
//
//                    binding.btnClock.setText(time);
//
//                    if(updateTime)
//                    {
//                        SharedPreferenceUtility.getInstance(getActivity()).putInt(SEC,(int)totalSeconds);
//                        timerHandler.post(new Runnable() {
//                            @Override
//
//                            public void run()
//                            {
//                                updatTimeInShiftsInProgress();
//                                handler.postDelayed(this, 1 * 5000);
//                            }
//                        });
//                    }
//                } else {
//                    updateTime = true;
//                    updatTimeInShiftsInProgress();
//                    long difference = date2.getTime() - date1.getTime();
//                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                    totalSeconds = diffInSec;
//                    handler.removeCallbacks(runnable);
//                    timerHandler.removeCallbacks(null);
////                    shiftCompleted();
//
//                    if(handler!=null)
//                    {
//                        handler.removeCallbacksAndMessages(null);
//                    }else if(timerHandler!=null)
//                    {
//                        timerHandler.removeCallbacksAndMessages(null);
//                    }
//                    getShiftsInProgress();
//
//                }
//            }
//        };
//        handler.postDelayed(runnable, 1000);
//    }




    private void runTimer()
    {
        if(getTimeTodayOrNot(shift24hrStartTime,shift24HrEndTime))
        {


            Log.d(ContentValues.TAG, "runTimer: "+"Today");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            String dateString = shiftDate+" "+shiftStartTime;
            try {
                date1 = sdf.parse(dateString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);
//            startTim = calendar.getTimeInMillis();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

            String newTIme = shiftEndTime;
            String[] splitStr = newTIme.split("\\s+");
            shiftEndTime = splitStr[0]+":00 "+splitStr[1];

            String dateString2 = shiftDate+" "+shiftEndTime;
            try {
                date2 = sdf2.parse(dateString2);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(date2);
//            endTim = calendar2.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            handler = new Handler();
//        timerHandler = new Handler();

            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable, 1000);
                    long currentTim = 0;
                    try {
                        date3 = new Date();//sdf3.parse(dateString2);
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(date3);
                        currentTim = calendar3.getTimeInMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                        Log.d(ContentValues.TAG, "run: "+diffInSec);
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

                        Log.d(LoginAct.TAG, "run: "+time);

                        binding.btnClock.setText(time);

                    } else {
                        updateTime = true;
                        updatTimeInShiftsInProgress();
                        long difference = date2.getTime() - date1.getTime();
                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                        totalSeconds = diffInSec;
                        handler.removeCallbacks(runnable);
//                    timerHandler.removeCallbacks(null);

                        shiftCompleted();
//                    timerHandler.removeCallbacks(null);

//                                        getShiftsInProgress();

//                    if(handler!=null)
//                    {
//                        handler.removeCallbacksAndMessages(null);
//                    }else if(timerHandler!=null)
//                    {
//                        timerHandler.removeCallbacksAndMessages(null);
//                    }else if(negativeHandler!=null)
//                    {
//                        negativeHandler.removeCallbacksAndMessages(null);
//                    }
//                    getShiftsInProgress();
                    }
                }
            };

            handler.postDelayed(runnable, 1000);


          /*  Log.d(TAG, "runTimer: "+"Today");

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            String dateString = shiftDate+" "+shiftStartTime;
            try {
                date1 = sdf.parse(dateString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date1);
                startTim = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
            String dateString2 = shiftDate+" "+shiftEndTime;
            try {
                date2 = sdf2.parse(dateString2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date2);
                endTim = calendar2.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            handler = new Handler();
            timerHandler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable, 1000);
                    long currentTim = 0;
                    try {
                        Date date3 = new Date();//sdf3.parse(dateString2);
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(date3);
                        currentTim = calendar3.getTimeInMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (endTim > currentTim) {
                        if (startTim <= currentTim) {
                            updateTime = true;
                            displayTim = currentTim - startTim;
                        } else {
                            updateTime = false;
                            displayTim = startTim - currentTim;
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
                        binding.btnClock.setText(time);
                        if(updateTime)
                        {
                            SharedPreferenceUtility.getInstance(getActivity()).putInt(SEC,(int)totalSeconds);
                            timerHandler.post(new Runnable() {
                                @Override
                                public void run()
                                {
                                    updatTimeInShiftsInProgress();
                                    handler.postDelayed(this, 1 * 5000);
                                }
                            });
                        }
                    } else {
                        updateTime = true;
                        updatTimeInShiftsInProgress();
                        long difference = date2.getTime() - date1.getTime();
                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                        totalSeconds = diffInSec;
                        handler.removeCallbacks(runnable);
                        timerHandler.removeCallbacks(null);
                        shiftCompleted();
//                    if(handler!=null)
//                    {
//                        handler.removeCallbacksAndMessages(null);
//                    }else if(timerHandler!=null)
//                    {
//                        timerHandler.removeCallbacksAndMessages(null);
//                    }else if(negativeHandler!=null)
//                    {
//                        negativeHandler.removeCallbacksAndMessages(null);
//                    }
//                    getShiftsInProgress();
                    }
                }
            };

            handler.postDelayed(runnable, 1000);*/

        }
        else
        {

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            String dateString = shiftDate+" "+shiftStartTime;
            try {
                date1 = sdf.parse(dateString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date1);
                startTim = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
            String dateString2 = shiftDate+" "+shiftEndTime;

            try {
                date2 = sdf2.parse(dateString2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date2);

//                calendar2.add(Calendar.DATE, 1);

                endTim = calendar2.getTimeInMillis();
                newEndTime = endTim;
                endTim = 1440 * 60 * 1000+endTim;

            } catch (ParseException e) {
                e.printStackTrace();
            }

            handler = new Handler();
            timerHandler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable, 1000);
                    long currentTim = 0;
                    try {
                        Date date3 = new Date();//sdf3.parse(dateString2);
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(date3);
                        currentTim = calendar3.getTimeInMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(displayTim<endTim)
                    {
                        if(currentTim<newEndTime)
                        {
                            currentTim = currentTim + 1440 * 60 * 1000;
                        }

                        if (startTim <= currentTim) {

                            if(endTim<currentTim)
                            {
                                updateTime = true;
                                updatTimeInShiftsInProgress();
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(date2);
                                calendar2.add(Calendar.DATE, 1);
                                date2 = calendar2.getTime();
                                long difference = date2.getTime() - date1.getTime();
                                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                                totalSeconds = diffInSec;
                                handler.removeCallbacks(runnable);
                                timerHandler.removeCallbacks(null);
                                shiftCompleted();
                            }
                            updateTime = true;
                            displayTim = currentTim - startTim;
                        } else {
                            updateTime = false;
                            displayTim = startTim - currentTim;
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

                        binding.btnClock.setText(time);
                        if(updateTime)
                        {
                            SharedPreferenceUtility.getInstance(getActivity()).putInt(SEC,(int)totalSeconds);
                            timerHandler.post(new Runnable() {
                                @Override
                                public void run()
                                {
                                    updatTimeInShiftsInProgress();
                                    handler.postDelayed(this, 1 * 5000);
                                }
                            });
                        }
                    }
                    else
                    {
                        updateTime = true;
                        updatTimeInShiftsInProgress();
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(date2);
                        calendar2.add(Calendar.DATE, 1);
                        date2 = calendar2.getTime();
                        long difference = date2.getTime() - date1.getTime();
                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                        totalSeconds = diffInSec;
                        handler.removeCallbacks(runnable);
                        timerHandler.removeCallbacks(null);
                        shiftCompleted();
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

            handler.postDelayed(runnable, 1000);

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

    }

    RecyclerView rvMessageItem ;

    ChatAdapter chatAdapter;

    EditText etChat;

    private String name = "",id = "", image ="",strChatMessage = "";

    List<SuccessResGetChat.Result> chatList = new LinkedList<>();

    public void fullScreenDialog(String myId)
    {

        id = myId;

        Dialog  dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.fragment_one2_one_chat);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        ImageView ivBack = dialog.findViewById(R.id.ivBack);

        ImageView ivSend = dialog.findViewById(R.id.ivSendMessage);

        ImageView ivAdminChat = dialog.findViewById(R.id.ivAdminChat);

        etChat = dialog.findViewById(R.id.etChat);

        ivAdminChat.setOnClickListener(v ->
                {

                    startActivity(new Intent(getActivity(), ConversationAct.class));

                }
        );

        rvMessageItem = dialog.findViewById(R.id.rvMessageItem);

        ivBack.setOnClickListener(v -> {
            dialog.dismiss();
        });


        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        chatAdapter = new ChatAdapter(getActivity(),chatList,userId);
        rvMessageItem.setHasFixedSize(true);
        rvMessageItem.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage((Activity) getActivity(), getActivity().getString(R.string.please_wait));

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
                        rvMessageItem.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvMessageItem.setAdapter(chatAdapter);
                        rvMessageItem.scrollToPosition(chatList.size()-1);
                    } else if (data.status.equals("0")) {
                        showToast((Activity) getActivity(), data.message);
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
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage((Activity) getActivity(), getActivity().getString(R.string.please_wait));
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
                    } else if (data.status.equals("0")) {
                        showToast((Activity) getActivity(), data.message);
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
    public void shiftCompleted()
    {
        String myStatus = "";
        float totalHoursWorked ;
        int seconds;
        if(updateTime)
        {
            seconds = (int)totalSeconds;
        }
        else
        {
            seconds = 0;
        }
        int hours = seconds / 3600;
        int workedMinutes = (seconds % 3600) / 60;
        if(hours == 0)
        {
            totalHoursWorked = (float) workedMinutes / (float)60;
            Log.d(TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
        } else if(workedMinutes == 0)
        {
            totalHoursWorked = hours;
            Log.d(TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
        }
        else
        {
            totalHoursWorked = (float) (workedMinutes) / (float)60;
            totalHoursWorked = hours + totalHoursWorked;
            Log.d(TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
        }

        if(!isNumber(totalHoursWorked+""))
        {
            totalHoursWorked = Float.parseFloat(String.format("%.2f", totalHoursWorked));
        }
        String str = strUnpaidBreak;
        String[] _arr = str.split("\\s");
        String word = _arr[0];
        if(!word.equalsIgnoreCase("None"))
        {
            float minutes = Float.parseFloat(word);
            float unpaidBreakHours = minutes / 60;
            strUnpaidBreakHours = unpaidBreakHours+"";
        }
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("id",shiftID);
        map.put("status","Completed");
        map.put("total_worked",totalHoursWorked+"");
        map.put("hourly_rate",strHourlyRate);
        map.put("unpaid_break",word);
        map.put("unpaid_break_hour",strUnpaidBreakHours);
        map.put("total_time",strTotalHours);
        map.put("on_time",myStatus);
        Call<SuccessResShiftCompleted> call = apiInterface.shiftCompleted(map);
        call.enqueue(new Callback<SuccessResShiftCompleted>() {
            @Override
            public void onResponse(Call<SuccessResShiftCompleted> call, Response<SuccessResShiftCompleted> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResShiftCompleted data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        if(handler!=null)
                        {
                            handler.removeCallbacksAndMessages(null);
                        }else if(timerHandler!=null)
                        {
                            timerHandler.removeCallbacksAndMessages(null);
                        }
                        getShiftsInProgress();
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResShiftCompleted> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void updatTimeInShiftsInProgress()
    {

        if(!updateTime)
        {
            totalSeconds = 0;
        }

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("id",updateTimeshiftID);
        map.put("total_worked",totalSeconds+"");

        Call<SuccessResUpdateShiftInProgressTime> call = apiInterface.updateShiftInProgressTime(map);
        call.enqueue(new Callback<SuccessResUpdateShiftInProgressTime>() {
            @Override
            public void onResponse(Call<SuccessResUpdateShiftInProgressTime> call, Response<SuccessResUpdateShiftInProgressTime> response) {

                try {

                    SuccessResUpdateShiftInProgressTime data = response.body();
                    if (data.status.equals("1")) {

                    } else {
                        //          showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateShiftInProgressTime> call, Throwable t) {
                call.cancel();
            }
        });

    }

    public static boolean isNumber(String str) {
        try {
            double v = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }



    public static boolean getTimeTodayOrNot(String time1,String time2)
    {

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");

        boolean b = false;
        long myTime1=0;
        long myTime2=0;
        String namepass[] = time1.split(":");
        myTime1 = Long.parseLong(namepass[0]);
        myTime1 = myTime1*60;
        myTime1 = myTime1+Long.parseLong(namepass[1]);
        Log.d(TAG, "getTime24DifferenceOne: "+myTime1);

        String namepass1[] = time2.split(":");
        myTime2 = Long.parseLong(namepass1[0]);
        myTime2 = myTime2*60;
        myTime2 = myTime2+Long.parseLong(namepass1[1]);
        Log.d(TAG, "getTime24DifferenceTwo: "+myTime2);
        if(myTime1>myTime2)
        {
            b =false;
        }
        else
        {
            b=true;
        }
        return  b;
    }

}