package com.shifts.healthcare.workerSide.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.activites.ConversationAct;
import com.shifts.healthcare.activites.LoginAct;
import com.shifts.healthcare.activites.One2OneChatAct;
import com.shifts.healthcare.activites.PaymentAct;
import com.shifts.healthcare.adapters.ChatAdapter;
import com.shifts.healthcare.adapters.ConfirmRecruitmentAdapter;
import com.shifts.healthcare.adapters.CurrentScheduleShiftsAdapter;
import com.shifts.healthcare.adapters.ShiftsAdapter;
import com.shifts.healthcare.adapters.ShowDateTimeAdapter;
import com.shifts.healthcare.databinding.FragmentWorkerHomeBinding;
import com.shifts.healthcare.models.SuccessResAcceptRejectRecruitment;
import com.shifts.healthcare.models.SuccessResAcceptShift;
import com.shifts.healthcare.models.SuccessResDeleteCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetChat;
import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetPost;
import com.shifts.healthcare.models.SuccessResGetShiftInProgress;
import com.shifts.healthcare.models.SuccessResGetWorkerProfile;
import com.shifts.healthcare.models.SuccessResInsertChat;
import com.shifts.healthcare.models.SuccessResShiftCompleted;
import com.shifts.healthcare.models.SuccessResUpdateShiftInProgressTime;
import com.shifts.healthcare.models.SuccessResWorkerAcceptedShift;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.Constant;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.DeleteShifts;
import com.shifts.healthcare.util.GPSTracker;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.RecruitmentShiftConfirmationInterface;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.POWER_SERVICE;
import static com.shifts.healthcare.adapters.ShiftsAdapter.getTime24Difference;
import static com.shifts.healthcare.retrofit.Constant.RUNNING;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.WASRUNNING;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class WorkerHomeFragment extends Fragment implements DeleteShifts, RecruitmentShiftConfirmationInterface {

    LocalBroadcastManager lbm;
    private int seconds = 0;
    public  Date endDate = null;
    Runnable runnable;
    long displayTim = 0;
    private String newCurrentStartTime = "";
    private boolean late = false;
    private long totalSeconds;
    private boolean updateTime = false;
    private boolean shiftLate = false;
    FragmentWorkerHomeBinding binding;
    HealthInterface apiInterface;
    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>();
    private ArrayList<SuccessResGetPost.Result> newPostedList = new ArrayList<>();
    private ArrayList<SuccessResWorkerAcceptedShift.Result> workerAccceptedShiftList = new ArrayList<>();
    private ArrayList<SuccessResGetPost.Result> removeDataPostedList = new ArrayList<>();
    private CurrentScheduleShiftsAdapter currentScheduleShiftsAdapter;
    private boolean shiftShowLess  = false;
    private boolean showLess = false;
    Timer timer = new Timer();
    private ArrayList<SuccessResGetCurrentSchedule.Result> confirmRecruitmentList = new ArrayList<>();
    private String strNewShiftDate="",strNewShiftStartTime="",strNewShiftSEndTime="";
    private ArrayList<SuccessResGetPost.Result> searchedShiftList = new ArrayList<>();
    private ShiftsAdapter shiftsAdapter;
    private String shiftStartTime = "";
    private String shift24hrStartTime = "";
    private String shiftEndTime = "";
    private String shift24HrEndTime = "";
    private String shiftDate = "";
    private String strTotalWorked = "";
    private String strHourlyRate = "";
    private String strUnpaidBreak = "";
    private String strTotalHours = "";
    private String strUnpaidBreakHours = "0";
    private ArrayList<SuccessResGetWorkerProfile.Result> userList = new ArrayList<>();
    private ArrayList<SuccessResGetCurrentSchedule.Result> currentScheduleList = new ArrayList<>();
    private ArrayList<SuccessResGetCurrentSchedule.Result> newCurrentScheduleList = new ArrayList<>();
     GPSTracker gpsTracker;
     String strLat = "",strLng = "";
     Timer timershifts = new Timer();
     Handler handler = null;
//     Handler timerHandler = null;
     Handler negativeHandler = null;
     public int i;
     private String sort = "Date";
     private boolean hasShiftInProgress = false;
     RecyclerView rvShifts;
     private String shiftID = "";
     private String updateTimeshiftID = "";
     List<String> monthsList = new LinkedList<>();
     private Dialog dialog;
     private ArrayList<SuccessResGetShiftInProgress.Result> shiftInProgressList = new ArrayList<>();
     private SuccessResGetShiftInProgress.Result shiftInProgress;
     private boolean running;
     private boolean wasRunning;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerHomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WorkerHomeFragment newInstance(String param1, String param2) {
        WorkerHomeFragment fragment = new WorkerHomeFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_home, container, false);
        gpsTracker = new GPSTracker(getActivity());
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        sort = "Date";
        getLocation();

        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("filter_string_1"));


        shiftsAdapter = new ShiftsAdapter(getActivity(),newPostedList,false, WorkerHomeFragment.this);
        shiftsAdapter.addList(newPostedList);
        binding.rvShifts.setHasFixedSize(true);
        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvShifts.setAdapter(shiftsAdapter);
        currentScheduleShiftsAdapter = new CurrentScheduleShiftsAdapter(getActivity(),newCurrentScheduleList,false, WorkerHomeFragment.this,"workerhome");
        currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
        binding.rvCurrentShifts.setHasFixedSize(true);
        binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvCurrentShifts.setAdapter(currentScheduleShiftsAdapter);
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchWorkerShifts(binding.etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

          timershifts.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLocation();
                getWorkerShifts(sort,false);
                getCurrentShifts(false);
            }
        },0,60000);

        binding.tvSortByDistance.setOnClickListener(v ->
                {
                    shiftShowLess = false;
                    binding.tvSortByDistance.setBackgroundResource(R.drawable.button_bg);
                    binding.tvSortByTime.setBackgroundResource(0);
                    binding.tvSortByTime.setTextColor(getResources().getColor(R.color.black));
                    binding.tvSortByDistance.setTextColor(getResources().getColor(R.color.white));
                    sort = "Distance";
                    getWorkerShifts(sort,true);
                }
                );
        binding.tvSortByTime.setOnClickListener(v ->
                {
                    shiftShowLess = false;
                    sort = "Date";
                    getWorkerShifts(sort,true);
                    binding.tvSortByTime.setBackgroundResource(R.drawable.button_bg);
                    binding.tvSortByDistance.setBackgroundResource(0);
                    binding.tvSortByDistance.setTextColor(getResources().getColor(R.color.black));
                    binding.tvSortByTime.setTextColor(getResources().getColor(R.color.white));
                }
        );
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getShiftsInProgress();
            getCurrentShifts(true);
            getWorkerShiftsReloads(sort);
            getProfile(true);
            getRecruitmentPendingShifts();
            rehireToDirectShift();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLess = false;
                shiftShowLess = false;
                binding.btnLoadMore.setVisibility(View.GONE);
                binding.btnLoadLess.setVisibility(View.GONE);
                if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
                    getLocation();
                    getShiftsInProgress();
                    getCurrentShifts(true);
                    getWorkerShifts(sort,true);
                    getProfile(true);
                    getRecruitmentPendingShifts();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
//                String str = intent.getStringExtra("key");
//                getUnseenNotificationCount();
                // get all your data from intent and do what you want
                getShiftsInProgress();
                getCurrentShifts(false);
                getRecruitmentPendingShifts();
            }
        }
    };

    @Override
    public void onPause()
    {

        super.onPause();
        timer.cancel();
        PowerManager powerManager = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();
        if (!isScreenOn) {
            if(hasShiftInProgress)
            {
                runTimer();
            }
        }
    }

    public void getWorkerShifts(String sortBy,boolean show)
    {

        if (show)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("sortby",sortBy);
        Call<SuccessResGetPost> call = apiInterface.getWorkerShift(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {
                        postedList.clear();
                        removeDataPostedList.clear();
                        removeDataPostedList.addAll(data.getResult());

                        Log.d(TAG, "onResponse: removeDataPostedList"+removeDataPostedList.size());

                        getWorkerAccepted("",show);

//                        for (SuccessResGetPost.Result result:removeDataPostedList)
//                        {
//                            boolean add = true;
//                            List<SuccessResGetPost.PostshiftTime> postshiftTimeList =  new LinkedList<>();
//                            postshiftTimeList.addAll(result.getPostshiftTime());
//                            for (SuccessResGetPost.PostshiftTime postshiftTime: postshiftTimeList)
//                            {
//                                if(postshiftTime.getShiftareaccepted().equalsIgnoreCase("Yes"))
//                                {
//                                    add =  false;
//                                    break;
//                                }
//                            }
//                            if(add)
//                            {
//                                postedList.add(result);
//                            }
//                        }
//                        setShiftList();

                        }
                    else {
                        if (show)
                        {
//                            showToast(getActivity(), data.message);
                        }
                        postedList.clear();
                        newPostedList.clear();
                        shiftsAdapter.notifyDataSetChanged();
                       }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetPost> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    String strTimeId = "",strShiftDate="",strStartTime="",strEndTime="";

    @Override
    public void onClick(String shiftsId,String onerId,List<SuccessResGetPost.PostshiftTime> postshiftTime,String type) throws ParseException {

        if(shiftsId.equalsIgnoreCase("Unapproved"))
        {

            showCertnSelection();

        }
        else
        {
            String myType = "";

            if(type.equalsIgnoreCase("Directshift") || type.equalsIgnoreCase("Rehire"))
            {
                myType = "Accepted";
            }
            else
            {
                myType = "Accepted_By_Worker";
            }
            strTimeId = ""; strShiftDate=""; strStartTime=""; strEndTime="";
            for (SuccessResGetPost.PostshiftTime postshiftTime1:postshiftTime)
            {
                strTimeId = strTimeId + postshiftTime1.getId()+",";
                strShiftDate = strShiftDate + postshiftTime1.getShiftDate()+",";
                strStartTime = strStartTime + postshiftTime1.getStartTime()+",";
                strEndTime = strEndTime + postshiftTime1.getEndTime()+",";
            }
            if (strTimeId.endsWith(","))
            {
                strTimeId = strTimeId.substring(0, strTimeId.length() - 1);
            }
            if (strShiftDate.endsWith(","))
            {
                strShiftDate = strShiftDate.substring(0, strShiftDate.length() - 1);
            }
            if (strStartTime.endsWith(","))
            {
                strStartTime = strStartTime.substring(0, strStartTime.length() - 1);
            }
            if (strEndTime.endsWith(","))
            {
                strEndTime = strEndTime.substring(0, strEndTime.length() - 1);
            }
            try {
                setNewTimeDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
            Map<String, String> map = new HashMap<>();
            map.put("shift_id",shiftsId);
            map.put("worker_id",userId);
            map.put("status",myType);
            map.put("user_id",onerId);
            map.put("time_id",strTimeId);
            map.put("shift_date",strShiftDate);
            map.put("start_time",strStartTime);
            map.put("end_time",strEndTime);
            map.put("shift_date_new",strNewShiftDate);
            map.put("start_time_new",strNewShiftStartTime);
            map.put("end_time_new",strNewShiftSEndTime);
            Call<SuccessResAcceptShift> call = apiInterface.acceptShift(map);
            call.enqueue(new Callback<SuccessResAcceptShift>() {
                @Override
                public void onResponse(Call<SuccessResAcceptShift> call, Response<SuccessResAcceptShift> response) {
                    DataManager.getInstance().hideProgressMessage();
                    try {
                        SuccessResAcceptShift data = response.body();
                        if (data.status.equals("1")) {
                            showToast(getActivity(), data.message);
                            showLess = false;
                            shiftShowLess = false;
                            binding.btnLoadMore.setVisibility(View.GONE);
                            binding.btnLoadLess.setVisibility(View.GONE);
                            getWorkerShifts(sort,true);
                            getCurrentShifts(true);
                            getRecruitmentPendingShifts();
                        } else {
                            showToast(getActivity(), data.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<SuccessResAcceptShift> call, Throwable t) {
                    call.cancel();
                    DataManager.getInstance().hideProgressMessage();
                }
            });
        }

    }

    @Override
    public void rejectSHift(String shiftsId, String onerId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {
        String myType = "Rehire_Rejected";
        String strTimeId = "",strShiftDate="",strStartTime="",strEndTime="";
        for (SuccessResGetPost.PostshiftTime postshiftTime1:postshiftTime)
        {
            strTimeId = strTimeId + postshiftTime1.getId()+",";
            strShiftDate = strShiftDate + postshiftTime1.getShiftDate()+",";
            strStartTime = strStartTime + postshiftTime1.getStartTime()+",";
            strEndTime = strEndTime + postshiftTime1.getEndTime()+",";
        }

        if (strTimeId.endsWith(","))
        {
            strTimeId = strTimeId.substring(0, strTimeId.length() - 1);
        }

        if (strShiftDate.endsWith(","))
        {
            strShiftDate = strShiftDate.substring(0, strShiftDate.length() - 1);
        }

        if (strStartTime.endsWith(","))
        {
            strStartTime = strStartTime.substring(0, strStartTime.length() - 1);
        }

        if (strEndTime.endsWith(","))
        {
            strEndTime = strEndTime.substring(0, strEndTime.length() - 1);
        }
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftsId);
        map.put("worker_id",userId);
        map.put("status",myType);
        map.put("user_id",onerId);
        map.put("time_id",strTimeId);
        map.put("shift_date",strShiftDate);
        map.put("start_time",strStartTime);
        map.put("end_time",strEndTime);
        Call<SuccessResAcceptShift> call = apiInterface.acceptShift(map);
        call.enqueue(new Callback<SuccessResAcceptShift>() {
            @Override
            public void onResponse(Call<SuccessResAcceptShift> call, Response<SuccessResAcceptShift> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAcceptShift data = response.body();
                    if (data.status.equals("1")) {
                        showLess = false;
                        shiftShowLess = false;
                        showToast(getActivity(), data.message);
                        getWorkerShifts(sort,true);
                        getCurrentShifts(true);
                        getRecruitmentPendingShifts();
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResAcceptShift> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void deleteCurrentShiftsClick(String shiftsId, String userId,SuccessResGetCurrentSchedule.PostshiftTime dateTime) {
        String cancellationChanrge = "";
        int j;
        String status1 = "";
        String time = dateTime.getShiftDate()+" "+dateTime.getStartTime();
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        try {
            date = dateFormat.parse(time);
            String out = dateFormat2.format(date);
            Log.e("Time", out);
        } catch (ParseException e) {
        }
        Date currentTime = Calendar.getInstance().getTime();
        long difference = date.getTime() - currentTime.getTime();
        int  days = (int) (difference / (1000*60*60*24));
        int  hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int   min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
        String newTIme = dateTime.getStartTime();
        String[] splitStr = newTIme.split("\\s+");
        newCurrentStartTime = splitStr[0]+":00 "+splitStr[1];
        if(userId.equalsIgnoreCase("Progress"))
        {
            j = (int) diffInSec;
            if(j < 0 ) {
                status1 = "Late";
                late   = true;
                String pattern = "hh:mm:ss a";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String todayAsString = df.format(today);
                newCurrentStartTime   = todayAsString;
                newCurrentStartTime.toUpperCase();
            } else
            {
                status1 = "Punctual";
            }
            }
        else
        {
            long diffInHours = TimeUnit.MILLISECONDS.toHours(difference);
            if(diffInHours<24) {
                cancellationChanrge = "Yes";
            }
            else
            {
                cancellationChanrge = "No";
            }
        }
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("id",shiftsId);
        map.put("status",userId);
        map.put("on_time",status1);
        map.put("clock_in_time",newCurrentStartTime);
        map.put("cancellation_charges",cancellationChanrge);
        Call<SuccessResDeleteCurrentSchedule> call = apiInterface.deleteCurrentShifts(map);
        call.enqueue(new Callback<SuccessResDeleteCurrentSchedule>() {
            @Override
            public void onResponse(Call<SuccessResDeleteCurrentSchedule> call, Response<SuccessResDeleteCurrentSchedule> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeleteCurrentSchedule data = response.body();
                    if (data.status.equals("1")) {
                        showLess = false;
                        shiftShowLess = false;
                        showToast(getActivity(), data.message);
                        binding.btnLoadMore.setVisibility(View.GONE);
                        binding.btnLoadLess.setVisibility(View.GONE);
                        getCurrentShifts(true);
                        getShiftsInProgress();
                        getWorkerShifts(sort,true);
                        getProfile(true);
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResDeleteCurrentSchedule> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void shiftConfimation(String shiftIds, String userId, String workerId, String status) {
    }

    @Override
    public void userCancelRecruit(String shiftIds,String userId,String workerId,String status) {
    }

    public void getProfile(boolean showLoader)
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        if(showLoader)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }

        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        Call<SuccessResGetWorkerProfile> call = apiInterface.getWorkerProfile(map);
        call.enqueue(new Callback<SuccessResGetWorkerProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetWorkerProfile> call, Response<SuccessResGetWorkerProfile> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetWorkerProfile data = response.body();
                    if (data.status.equals("1")) {
                        userList.clear();
                        userList.addAll(data.getResult());
                        setUserDetails(userList);
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetWorkerProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setUserDetails(ArrayList<SuccessResGetWorkerProfile.Result> userList)
    {
        binding.tvName.setText(userList.get(0).getFirstName()+" "+userList.get(0).getLastName());
        binding.tvJobPosition.setText("["+userList.get(0).getDesignation()+"]");
        binding.tvShiftWorked.setText(userList.get(0).getShiftsWorked());
        binding.tvTotalEarning.setText("$ "+userList.get(0).getTotalEarning()+"");
        if(userList.get(0).getPunctualShifs() != null)
        {
            binding.tvPunctual.setText(userList.get(0).getPunctualShifs()+"");
        }
        if(userList.get(0).getLateShifts() != null)
        {
            binding.tvLate.setText(userList.get(0).getLateShifts()+"");
        }
        binding.tvCancel.setText(userList.get(0).getCancelShifts());
        binding.tvNoShows.setText(userList.get(0).getNoShows());
        binding.tvRating.setText(userList.get(0).getRating());
        binding.ratingBar.setRating(Float.parseFloat(String.valueOf(userList.get(0).getRating())));
        binding.tvFromToDate.setText(userList.get(0).getApprovalDate()+" - "+userList.get(0).getCurentDate());
        Log.d(TAG, "setUserDetails: ");
        Glide.with(getActivity())
                .load(userList.get(0).getImage())
                .into(binding.ivWorkerProfile);
    }

    public void getCurrentShifts(boolean show)
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("lat",strLat);
        map.put("long",strLng);
        Call<SuccessResGetCurrentSchedule> call = apiInterface.getWorkerCurrentShifts(map);
        call.enqueue(new Callback<SuccessResGetCurrentSchedule>() {
            @Override
            public void onResponse(Call<SuccessResGetCurrentSchedule> call, Response<SuccessResGetCurrentSchedule> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetCurrentSchedule data = response.body();
                    if (data.status.equals("1")) {
                        getProfile(false);
                        binding.tvCurrentSchedule.setVisibility(View.VISIBLE);
                        binding.rvCurrentShifts.setVisibility(View.VISIBLE);
                        currentScheduleList.clear();
                        currentScheduleList.addAll(data.getResult());

//                        Collections.sort(currentScheduleList, new Comparator<SuccessResGetCurrentSchedule.Result>(){
//                            public int compare(SuccessResGetCurrentSchedule.Result obj1, SuccessResGetCurrentSchedule.Result obj2) {
//                                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//                                String  dtDate1 = obj1.getPostshiftTime().get(0).getShiftDate()+" 8:20";
//                                String  dtDate2 = obj2.getPostshiftTime().get(0).getShiftDate()+" 8:20";
//                                Date  date1 = null;
//                                Date  date2 = null;
//                                try {
//                                    date1 = format.parse(dtDate1);
//                                    date2 = format.parse(dtDate2);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                return date1.compareTo(date2); // To compare string values
//                                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values
//                                // ## Descending order
//                                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
//                                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
//                            }
//                        });

                        setCurrentScheduleList();
                    } else {
                        binding.tvCurrentSchedule.setVisibility(View.GONE);
                        binding.btnLoadMore.setVisibility(View.GONE);
                        binding.rvCurrentShifts.setVisibility(View.GONE);
                        currentScheduleList.clear();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCurrentSchedule> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setCurrentScheduleList()
    {

        if(!showLess)
        {
            binding.btnLoadMore.setVisibility(View.GONE);
            binding.btnLoadLess.setVisibility(View.GONE);
        }

        if(currentScheduleList.size()>1 && !showLess)
        {

            ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>();
            subList.clear();
            subList.add(currentScheduleList.get(0));

//            binding.rvCurrentShifts.setHasFixedSize(true);
//            binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,false, WorkerHomeFragment.this,"workerhome"));

            newCurrentScheduleList = subList;
            currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
            currentScheduleShiftsAdapter.notifyDataSetChanged();
            binding.btnLoadMore.setVisibility(View.VISIBLE);
            binding.btnLoadLess.setVisibility(View.GONE);

        }
        else
        {
//            binding.rvCurrentShifts.setHasFixedSize(true);
//            binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,false, WorkerHomeFragment.this,"workerhome"));

            newCurrentScheduleList = currentScheduleList;

            currentScheduleShiftsAdapter.addList(newCurrentScheduleList);

            currentScheduleShiftsAdapter.notifyDataSetChanged();

            binding.btnLoadMore.setVisibility(View.GONE);

        }

        binding.btnLoadMore.setOnClickListener(v ->
                {

                    binding.btnLoadMore.setVisibility(View.GONE);
                    binding.btnLoadLess.setVisibility(View.VISIBLE);

                    showLess = true;

//                    binding.rvCurrentShifts.setHasFixedSize(true);
//                    binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,false, WorkerHomeFragment.this,"workerhome"));
                    newCurrentScheduleList = currentScheduleList;
                    currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
                    currentScheduleShiftsAdapter.notifyDataSetChanged();

                }
        );

        binding.btnLoadLess.setOnClickListener(v ->
                {
                    showLess = false;
                    binding.btnLoadMore.setVisibility(View.VISIBLE);
                    binding.btnLoadLess.setVisibility(View.GONE);
                    ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>();
                    subList.clear();
                    subList.add(currentScheduleList.get(0));

                    newCurrentScheduleList = subList;
//                    binding.rvCurrentShifts.setHasFixedSize(true);
//                    binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,false, WorkerHomeFragment.this,"workerhome"));

                    currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
                    currentScheduleShiftsAdapter.notifyDataSetChanged();

                }
        );
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
            strLat = Double.toString(gpsTracker.getLatitude()) ;
            strLng = Double.toString(gpsTracker.getLongitude()) ;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode == Constant.GPS_REQUEST) {
//                isGPS = true; // flag maintain before get location
//            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Latittude====", gpsTracker.getLatitude() + "");
                    strLat = Double.toString(gpsTracker.getLatitude()) ;
                    strLng = Double.toString(gpsTracker.getLongitude()) ;
//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getgetActivity()(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void getShiftsInProgress()
    {
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
                        binding.tvShiftInProgress.setVisibility(View.VISIBLE);
                        binding.cvSHiftInProgress.setVisibility(View.VISIBLE);
                        shiftInProgressList.clear();
                        shiftInProgressList.addAll(data.getResult());
                        shiftInProgress = shiftInProgressList.get(0);
                        updateTimeshiftID = shiftInProgress.getSid();
                        shiftID = shiftInProgressList.get(0).getSid();
                        setShiftInProgress();
                        shiftStartTime = shiftInProgressList.get(0).getClockInTime();
                        shift24hrStartTime = shiftInProgressList.get(0).getPostshiftTime().get(0).getStartTimeNew();

                        shift24HrEndTime = shiftInProgressList.get(0).getPostshiftTime().get(0).getEndTimeNew();

                        shiftEndTime = shiftInProgressList.get(0).getPostshiftTime().get(0).getEndTime();
                        shiftDate = shiftInProgressList.get(0).getPostshiftTime().get(0).getShiftDate();
                        strHourlyRate = shiftInProgressList.get(0).getShiftsdetail().get(0).getHourlyRate();
                        strUnpaidBreak = shiftInProgressList.get(0).getShiftsdetail().get(0).getUnpaidBreak();
//                        strTotalHours = shiftInProgressList.get(0).getPostshiftTime().get(0).getTotalHours()+"";sdf
                        strTotalHours = getTime24Difference(shiftInProgressList.get(0).getPostshiftTime().get(0).getStartTimeNew(),shiftInProgressList.get(0).getPostshiftTime().get(0).getEndTimeNew(),shiftInProgress.getShiftsdetail().get(0).getTransitAllowance());
                        SharedPreferenceUtility.getInstance(getActivity()).putBoolean(RUNNING,true);
                        SharedPreferenceUtility.getInstance(getActivity()).putBoolean(WASRUNNING,true);

                        running
                                = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(RUNNING);
                        wasRunning
                                = SharedPreferenceUtility.getInstance(getActivity()).getBoolean(WASRUNNING);
                        runTimer();

                    } else {
                        binding.tvShiftInProgress.setVisibility(View.GONE);
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

    if(!shiftInProgress.getTotalUnseenMessage().equalsIgnoreCase("0"))
    {
        binding.tvWorkerMsg.setVisibility(View.VISIBLE);
        binding.tvWorkerMsg.setText(shiftInProgress.getTotalUnseenMessage());
    }else
    {
        binding.tvWorkerMsg.setVisibility(View.GONE);
    }

    if(shiftInProgress.getShiftsdetail().get(0).getNoVacancies().equalsIgnoreCase("1"))
    {
        binding.tvID.setVisibility(View.GONE);
    }
    else
    {
        binding.tvID.setVisibility(View.VISIBLE);
    }
    if(shiftInProgress.getShiftsdetail().get(0).getType().equalsIgnoreCase("Directshift"))
    {
        binding.tvShiftType.setVisibility(View.GONE);
    }
    else
    {
        binding.tvShiftType.setVisibility(View.VISIBLE);
        binding.tvShiftType.setText(R.string.recruitment);
    }
    binding.tvID.setText(shiftInProgress.getVacanciesNo());
    binding.tvDistance.setText(getString(R.string.distance)+shiftInProgress.getShiftsdetail().get(0).getDistance());
    if(shiftInProgress.getShiftsdetail().get(0).getDayType().equalsIgnoreCase("Single"))
    {
        binding.tvShiftNumber.setText(shiftInProgress.getShiftNo());
    }
    else
    {
        binding.tvShiftNumber.setText(shiftInProgress.getShiftSubNo());
    }
    String date ="";
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

    binding.tvSignleTime.setText(text);
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

/*
    if(shiftInProgress.getShiftsdetail().get(0).getTimeType().equalsIgnoreCase("Single"))
    {
        binding.tvSignleTime.setVisibility(View.VISIBLE);
        binding.tvMutlipleTime.setVisibility(View.GONE);
        String startTime = postshiftTimeList.get(0).getStartTime();
        String endTime = postshiftTimeList.get(0).getEndTime();
        String text = getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+postshiftTimeList.get(0).getTotalHours()+" hrs)";
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
            String pattern = "EEE,MMM d, yyyy";
// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
            DateFormat df = new SimpleDateFormat(pattern);
// Get the today date using Calendar object.
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
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
    }*/

    binding.tvShiftsNotes.setText(shiftInProgress.getShiftsdetail().get(0).getShiftNotes());

    binding.tvDuty.setText("("+shiftInProgress.getShiftsdetail().get(0).getDutyOfWorker()+")");
    if(shiftInProgress.getShiftsdetail().get(0).getUnpaidBreak().equalsIgnoreCase("None"))
    {
        binding.tvBreak.setText(getString(R.string.unpaid_break)+shiftInProgress.getShiftsdetail().get(0).getUnpaidBreak());
    }
    else
    {
        binding.tvBreak.setText(getActivity().getString(R.string.unpaid_break)+shiftInProgress.getShiftsdetail().get(0).getUnpaidBreak()+" Minutes");
    }
    if(shiftInProgress.getShiftsdetail().get(0).getTransitAllowance().equalsIgnoreCase("None"))
    {
        binding.tvTransit.setText(getActivity().getString(R.string.transit_allowance)+shiftInProgress.getShiftsdetail().get(0).getTransitAllowance());
    }
    else
    {
        binding.tvTransit.setText(getActivity().getString(R.string.transit_allowance)+shiftInProgress.getShiftsdetail().get(0).getTransitAllowance()+" Hour");
    }
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
    requestOptions = requestOptions.transforms( new RoundedCorners(15));

    Glide.with(getActivity())
            .load(shiftInProgress.getShiftsdetail().get(0).getUserImage())
            .apply(requestOptions)
            .into(binding.ivProfile);

    binding.rlShiftsNotes.setOnClickListener(v ->
            {
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
    binding.rlChat.setOnClickListener(v ->
            {
//                fullScreenDialog(shiftInProgress.getUserId());
                getActivity().startActivity(new Intent(getActivity(), One2OneChatAct.class).putExtra("id",shiftInProgress.getUserId()));
            }
            );
    Glide.with(getActivity())
            .load(shiftInProgress.getShiftsdetail().get(0).getWorkerImage())
            .centerCrop()
            .apply(requestOptions)
            .into(binding.ivWorker);
}
    public void showImageSelection(List<String> dates,List<String> startTimeList,List<String> endTimeList) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.multiple_date_recycler_view_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
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

    long startTim = 0;
    long endTim = 0;
    long newEndTime = 0;
    Date date2 = null;
    Date date = null;
    Date date1 = null;
    Date date3 = null;
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
            } catch (ParseException e) {
                e.printStackTrace();
            }

            handler = new Handler();
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
                            updateTime = true;

                        } else {

                            updateTime = false;

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
//                        shiftCompleted();
                        getShiftsInProgress();

                    }
                }
            };

            handler.postDelayed(runnable, 1000);

       /* Log.d(TAG, "runTimer: "+"Today");
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
//        timerHandler = new Handler();
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
//                        timerHandler.post(new Runnable() {
//                            @Override
//                            public void run()
//                            {
//                                updatTimeInShiftsInProgress();
//                                handler.postDelayed(this, 1 * 5000);
//                            }
//                        });
                    }
                } else {
                    updateTime = true;
                    updatTimeInShiftsInProgress();
                    long difference = date2.getTime() - date1.getTime();
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                    totalSeconds = diffInSec;
                    handler.removeCallbacks(runnable);
//                    timerHandler.removeCallbacks(null);

                    shiftCompleted();
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

        handler.postDelayed(runnable, 1000);*/

        }
        else
        {
            Log.d(TAG, "runTimer: "+"Next Day");
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

            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

            String newTIme = shiftEndTime;
            String[] splitStr = newTIme.split("\\s+");
            shiftEndTime = splitStr[0]+":00 "+splitStr[1];

            String dateString2 = shiftDate+" "+shiftEndTime;

            try {
                date2 = sdf2.parse(dateString2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(date2);
                calendar2.add(Calendar.DATE, 1);
                date2 = calendar2.getTime();
                endTim = calendar2.getTimeInMillis();
                newEndTime = endTim;
//                endTim = 1440 * 60 * 1000+endTim;

            } catch (ParseException e) {
                e.printStackTrace();
            }

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable, 1000);
                    long currentTim = 0;
                    try {
                        date3 = new Date();
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
//                                updateTime = true;
//                                updatTimeInShiftsInProgress();
//                                Calendar calendar2 = Calendar.getInstance();
//                                calendar2.setTime(date2);
//                                date2 = calendar2.getTime();
//                                long difference = date2.getTime() - date1.getTime();
//                                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                                totalSeconds = diffInSec;
//                                handler.removeCallbacks(runnable);
//                                shiftCompleted();
//                            }
//                            updateTime = true;
//                            displayTim = currentTim - startTim;
//                        } else {
//                            updateTime = false;
//                            displayTim = startTim - currentTim;
//                        }
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
//                        binding.btnClock.setText(time);
//
//                    }

                    if (date2.compareTo(date3)==1) {
//                    if (startTim <= currentTim) {
                        if (date1.compareTo(date3) <= 0) {
                            updateTime = true;
                            displayTim = date3.getTime() - date1.getTime();
                        } else {
                            updateTime = false;
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

                    }
                    else
                    {
                        updateTime = true;
                        long difference = date2.getTime() - date1.getTime();
                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                        totalSeconds = diffInSec;
                        handler.removeCallbacks(runnable);
//                        shiftCompleted();

                        getShiftsInProgress();

                    }

//                    if (endTim > currentTim) {
//                    } else {
//                        updateTime = true;
//                        updatTimeInShiftsInProgress();
//                        long difference = date2.getTime() - date1.getTime();
//                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                        totalSeconds = diffInSec;
//                        handler.removeCallbacks(runnable);
//                        timerHandler.removeCallbacks(null);
////                        shiftCompleted();
//                    }

                }
            };

            handler.postDelayed(runnable, 1000);

//        String time = shiftDate+" "+shiftStartTime;
//        String endTime = shiftDate+" "+shiftEndTime;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
//        try {
//            date = dateFormat.parse(time);
//            endDate = dateFormat.parse(endTime);
////            Calendar c = Calendar.getInstance();
////            c.setTime(endDate);
////            c.add(Calendar.DATE, 1);
////            endDate = c.getTime();
//        } catch (ParseException e) {
//        }
//        Date currentTime = Calendar.getInstance().getTime();
//        long difference = date.getTime() - currentTime.getTime();
//        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//         i = (int) diffInSec;
//        if(i < 0 || i==0)
//        {

//            if(handler==null)
//            {

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

////                        Date currentTime = Calendar.getInstance().getTime();

////                        long difference = date.getTime() - currentTime.getTime();
////
////                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);

////                        i = (int) diffInSec;

////                        seconds = Math.abs(i);
//
//                        String time
//                                = String
//                                .format(Locale.getDefault(),
//                                        "%02d:%02d:%02d", hours,
//                                        minutes, secs);

//                        binding.btnClock.setText(time);

//                        if (running) {
//                            seconds++;
//                            SharedPreferenceUtility.getInstance(getActivity()).putInt(SEC,seconds);
//                        }

//                        handler.postDelayed(this, 1 * 1000);
//                    }
//                });

//                timerHandler.post(new Runnable() {
//                    @Override

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

    @Override
    public void onResume() {
        super.onResume();
        getShiftsInProgress();
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(hasShiftInProgress)
        {
            updatTimeInShiftsInProgress();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(hasShiftInProgress)
        {
            updatTimeInShiftsInProgress();
        }
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
                        }
//                        else if(timerHandler!=null)
//                        {
//                            timerHandler.removeCallbacksAndMessages(null);
//                        }
                        else if(negativeHandler!=null)
                        {
                            negativeHandler.removeCallbacksAndMessages(null);
                        }
                        showLess = false;
                        shiftShowLess = false;
                        getShiftsInProgress();
                        getWorkerShifts(sort,true);
                        getCurrentShifts(true);
                        getProfile(true);
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

    public void searchWorkerShifts(String title)
    {
        shiftShowLess = false;
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("company",title);
        Call<SuccessResGetPost> call = apiInterface.getShiftsByCompanyName(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {
                        postedList.clear();
                        removeDataPostedList.clear();
                        binding.etSearch.clearFocus();
//                        postedList.addAll(data.getResult());
                        removeDataPostedList.addAll(data.getResult());
                        getWorkerAccepted("",true);


//                        for (SuccessResGetPost.Result result:removeDataPostedList)
//                        {
//
//                            boolean add = true;
//
//                            List<SuccessResGetPost.PostshiftTime> postshiftTimeList =  new LinkedList<>();
//                            postshiftTimeList.addAll(result.getPostshiftTime());
//
//                            for (SuccessResGetPost.PostshiftTime postshiftTime: postshiftTimeList)
//                            {
//                                if(postshiftTime.getShiftareaccepted().equalsIgnoreCase("Yes"))
//                                {
//                                    add =  false;
//                                    break;
//                                }
//                            }
//
//                            if(add)
//                            {
//                                postedList.add(result);
//                            }
//
//                        }
//
//                        setShiftList();

                          } else {
                        showToast(getActivity(), data.message);
                       postedList.clear();
                       binding.etSearch.clearFocus();
                       shiftsAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetPost> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    public void setShiftList()
    {

        if(!shiftShowLess)
        {

            binding.btnLoadMoreShifts.setVisibility(View.GONE);
            binding.btnLoadLessShifts.setVisibility(View.GONE);

        }

        if(postedList.size()>10 && !shiftShowLess)
        {

            ArrayList<SuccessResGetPost.Result> subList = new ArrayList<>(postedList.subList(0,10));
//            binding.rvShifts.setHasFixedSize(true);
//            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,false, WorkerHomeFragment.this));
//
            newPostedList = subList;

            shiftsAdapter.addList(newPostedList);
            shiftsAdapter.notifyDataSetChanged();

            binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);

        }
        else
        {
//            binding.rvShifts.setHasFixedSize(true);
//            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false, WorkerHomeFragment.this));

            newPostedList = postedList;

            shiftsAdapter.addList(newPostedList);
            shiftsAdapter.notifyDataSetChanged();

            binding.btnLoadMoreShifts.setVisibility(View.GONE);

        }

        binding.btnLoadMoreShifts.setOnClickListener(v ->
                {

                    binding.btnLoadMoreShifts.setVisibility(View.GONE);
                    binding.btnLoadLessShifts.setVisibility(View.VISIBLE);
//                    binding.rvShifts.setHasFixedSize(true);
//                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false, WorkerHomeFragment.this));

                    shiftShowLess = true;

                    newPostedList = postedList;
                    shiftsAdapter.addList(newPostedList);
                    shiftsAdapter.notifyDataSetChanged();

                }
        );

        binding.btnLoadLessShifts.setOnClickListener(v ->
                {

                    shiftShowLess = false;

                    binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);
                    binding.btnLoadLessShifts.setVisibility(View.GONE);
                    ArrayList<SuccessResGetPost.Result> subList = new ArrayList<>(postedList.subList(0,10));

                    newPostedList = subList;
                    shiftsAdapter.addList(newPostedList);
                    shiftsAdapter.notifyDataSetChanged();

                }
        );

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
            timer.cancel();
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
                        //chatAdapter.notifyDataSetChanged();
                        rvMessageItem.scrollToPosition(chatList.size()-1);

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

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
       /*
        RequestBody senderId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody receiverID = RequestBody.create(MediaType.parse("text/plain"),receiverId);
        RequestBody chatMessage = RequestBody.create(MediaType.parse("text/plain"), strChatMessage);
        RequestBody itemId = RequestBody.create(MediaType.parse("text/plain"),itemID);
*/

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

    public void getRecruitmentPendingShifts()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResGetCurrentSchedule> call = apiInterface.getPendingRecruitmentShiftForWorker(map);
        call.enqueue(new Callback<SuccessResGetCurrentSchedule>() {
            @Override
            public void onResponse(Call<SuccessResGetCurrentSchedule> call, Response<SuccessResGetCurrentSchedule> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCurrentSchedule data = response.body();
                    if (data.status.equals("1")) {

                        binding.tvPendingRecruitmentShift.setVisibility(View.VISIBLE);
                        binding.rvRecruitmentConfirmation.setVisibility(View.VISIBLE);

                        confirmRecruitmentList.clear();
                        confirmRecruitmentList.addAll(data.getResult());
                        binding.rvRecruitmentConfirmation.setHasFixedSize(true);
                        binding.rvRecruitmentConfirmation.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvRecruitmentConfirmation.setAdapter(new ConfirmRecruitmentAdapter(getActivity(),confirmRecruitmentList,WorkerHomeFragment.this,false));

                    } else {

                        binding.tvPendingRecruitmentShift.setVisibility(View.GONE);
                        binding.rvRecruitmentConfirmation.setVisibility(View.GONE);
                        confirmRecruitmentList.clear();
                        binding.rvRecruitmentConfirmation.setHasFixedSize(true);
                        binding.rvRecruitmentConfirmation.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvRecruitmentConfirmation.setAdapter(new ConfirmRecruitmentAdapter(getActivity(),confirmRecruitmentList,WorkerHomeFragment.this,false));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetCurrentSchedule> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    @Override
    public void recruitConfirmation(View v,String shiftIds, String userId, String workerId, String status,SuccessResGetCurrentSchedule.Result result) {

    }

    @Override
    public void cancelRecruitShift(String shiftIds, String userId, String workerId, String status) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftIds);
        map.put("worker_id",workerId);
        map.put("status","Cancelled_By_Worker");
        map.put("user_id",userId);

        Call<SuccessResAcceptRejectRecruitment> call = apiInterface.acceptRejectRecruitment(map);
        call.enqueue(new Callback<SuccessResAcceptRejectRecruitment>() {
            @Override
            public void onResponse(Call<SuccessResAcceptRejectRecruitment> call, Response<SuccessResAcceptRejectRecruitment> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResAcceptRejectRecruitment data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        shiftShowLess = false;
                        getWorkerShifts(sort,true);
                        getRecruitmentPendingShifts();
                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResAcceptRejectRecruitment> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }


    public void rehireToDirectShift()
    {


        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetPost> call = apiInterface.convertRehireToDirectShifts(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {

                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetPost> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    public void setNewTimeDate() throws ParseException {

        List<String> newShiftDate = Arrays.asList(strShiftDate.split("\\s*,\\s*"));

        List<String> newShiftStartTime = Arrays.asList(strStartTime.split("\\s*,\\s*"));

        List<String> newShiftEndTime = Arrays.asList(strEndTime.split("\\s*,\\s*"));

        strNewShiftDate = "";
        strNewShiftSEndTime ="";
        strNewShiftStartTime = "";

        for (String myDate:newShiftDate)
        {
            String date = convertDateFormate(myDate);
            strNewShiftDate = strNewShiftDate + date+",";
        }

        for (String myStartTime:newShiftStartTime)
        {
            String startTime = timeCoversion12to24(myStartTime);
            strNewShiftStartTime = strNewShiftStartTime + startTime+",";
        }

        for (String myStartTime:newShiftEndTime)
        {
            String startTime = timeCoversion12to24(myStartTime);
            strNewShiftSEndTime = strNewShiftSEndTime + startTime+",";
        }

        if (strNewShiftDate.endsWith(","))
        {
            strNewShiftDate = strNewShiftDate.substring(0, strNewShiftDate.length() - 1);
        }

        if (strNewShiftStartTime.endsWith(","))
        {
            strNewShiftStartTime = strNewShiftStartTime.substring(0, strNewShiftStartTime.length() - 1);
        }

        if (strNewShiftSEndTime.endsWith(","))
        {
            strNewShiftSEndTime = strNewShiftSEndTime.substring(0, strNewShiftSEndTime.length() - 1);
        }
    }

    public String convertDateFormate(String d1)
    {
        String convertedDate = "";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        String  dtDate1 = d1 +" 8:20";

        Date  date1 = null;

        try {
            date1 = format.parse(dtDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(date1);
        return todayAsString;
    }

    public static String timeCoversion12to24(String twelveHoursTime) throws ParseException {
        String time = twelveHoursTime;
        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
        String newTime = date24Format.format(date12Format.parse(time));
        return  newTime;
    }

    public void getWorkerShiftsReloads(String sortBy)
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("sortby",sortBy);

        Call<SuccessResGetPost> call = apiInterface.getWorkerShift(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {
                        getWorkerShifts(sort,true);
                    }
                    else {
                        postedList.clear();
                        getWorkerShifts(sort,true);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetPost> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timershifts.cancel();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    public void getWorkerAccepted(String sortBy,boolean show)
    {

        if (show)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResWorkerAcceptedShift> call = apiInterface.getWorkerAcceptedShifts(map);
        call.enqueue(new Callback<SuccessResWorkerAcceptedShift>() {
            @Override
            public void onResponse(Call<SuccessResWorkerAcceptedShift> call, Response<SuccessResWorkerAcceptedShift> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResWorkerAcceptedShift data = response.body();
                    if (data.status.equals("1")) {

                        workerAccceptedShiftList.clear();
                        postedList.clear();
                        workerAccceptedShiftList.addAll(data.getResult());
                        postedList.addAll(removeDataPostedList);

                        for (SuccessResWorkerAcceptedShift.Result acceptedShift : workerAccceptedShiftList)
                        {

                            String strDate1  = acceptedShift.getShiftDate();
                            String strDateStart1 = acceptedShift.getStartTimeNew();
                            String strDateEnd1 = acceptedShift.getEndTimeNew();
                            String strNewStartDate = acceptedShift.getStartTimeNew();
                            String strNewEndDate = acceptedShift.getEndTimeNew();

                            for (SuccessResGetPost.Result result:removeDataPostedList)
                            {

                                boolean add = true;
                                List<SuccessResGetPost.PostshiftTime> postshiftTimeList =  new LinkedList<>();
                                postshiftTimeList.addAll(result.getPostshiftTime());
                                for (SuccessResGetPost.PostshiftTime postshiftTime: postshiftTimeList)
                                {

                                    String strNewStartDate1 = postshiftTime.getStartTimeNew();
                                    String strNewEndDate1 = postshiftTime.getEndTimeNew();

                                    String strDate2  =  postshiftTime.getShiftDate();
                                    String strDateStart2 =postshiftTime.getStartTimeNew();
                                    String strDateEnd2 = postshiftTime.getEndTimeNew();
                                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                    String  dtDate1 = strDate1+" 8:30";
                                    String  dtDate2 = strDate2+" 8:30";
                                    Date  date1 = null;
                                    Date  date2 = null;
                                    try {
                                        date1 = format.parse(dtDate1);
                                        date2 = format.parse(dtDate2);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if(date1.compareTo(date2) == 0)
                                    {

                                        String[] seperatedTime= strDateStart1.split(":");
                                        int hours = Integer.parseInt(seperatedTime[0]);
                                        int minutes = Integer.parseInt(seperatedTime[1]);
                                        int SelectedTimeInMinutesStart1 = hours * 60 + minutes;

                                        String[] seperatedTimeEnd1= strDateEnd1.split(":");
                                        int hoursEnd1 = Integer.parseInt(seperatedTimeEnd1[0]);
                                        int minutesEnd1 = Integer.parseInt(seperatedTimeEnd1[1]);
                                        int SelectedTimeInMinutesEnd1 = hoursEnd1 * 60 + minutesEnd1;

                                        String[] seperatedTimeStart2= strDateStart2.split(":");
                                        int hoursStart2 = Integer.parseInt(seperatedTimeStart2[0]);
                                        int minutesStart2 = Integer.parseInt(seperatedTimeStart2[1]);
                                        int SelectedTimeInMinutesStart2 = hoursStart2 * 60 + minutesStart2;

                                        String[] seperatedTimeEnd2= strDateEnd2.split(":");
                                        int hoursEnd2 = Integer.parseInt(seperatedTimeEnd2[0]);
                                        int minutesEnd2 = Integer.parseInt(seperatedTimeEnd2[1]);
                                        int SelectedTimeInMinutesEnd2 = hoursEnd2 * 60 + minutesEnd2;

                                        if(!getTimeTodayOrNot(strNewStartDate,strNewEndDate))
                                        {
                                            SelectedTimeInMinutesEnd1 = SelectedTimeInMinutesEnd1 +1440;
                                        }

                                        if(!getTimeTodayOrNot(strNewStartDate1,strNewEndDate1))
                                        {
                                            SelectedTimeInMinutesEnd2 = SelectedTimeInMinutesEnd2 +1440;
                                        }

                                        if ((SelectedTimeInMinutesStart2>SelectedTimeInMinutesStart1 && SelectedTimeInMinutesStart2 < SelectedTimeInMinutesEnd1)
                                                || (SelectedTimeInMinutesEnd2>SelectedTimeInMinutesStart1 && SelectedTimeInMinutesEnd2<SelectedTimeInMinutesEnd1)
                                                || (SelectedTimeInMinutesStart2<SelectedTimeInMinutesStart1 && SelectedTimeInMinutesEnd2>SelectedTimeInMinutesEnd1)
                                                || (SelectedTimeInMinutesStart2==SelectedTimeInMinutesStart1 && SelectedTimeInMinutesEnd2==SelectedTimeInMinutesEnd1)
                                                || (SelectedTimeInMinutesStart2==SelectedTimeInMinutesStart1 && SelectedTimeInMinutesEnd1<SelectedTimeInMinutesEnd2)
                                        )
                                        {
                                            postedList.remove(result);
                                        }

                                    }

                                }

                            }

                        }

                        setShiftList();

                    }
                    else {

                     postedList.clear();
                     postedList.addAll(removeDataPostedList);
                     setShiftList();

                            }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResWorkerAcceptedShift> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

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

    public void showCertnSelection() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_redirect_to_certn);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);
        AppCompatButton btnRedirect = dialog.findViewById(R.id.btnShiftAccepted);
        AppCompatButton btnCncel = dialog.findViewById(R.id.btnReject);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        btnCncel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        btnRedirect.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), PaymentAct.class));
                    dialog.dismiss();
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



}