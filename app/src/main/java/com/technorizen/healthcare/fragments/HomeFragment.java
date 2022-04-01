package com.technorizen.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.activites.HomeActivity;
import com.technorizen.healthcare.activites.SignupWithPostShiftsActivity;
import com.technorizen.healthcare.adapters.ConfirmRecruitmentAdapter;
import com.technorizen.healthcare.adapters.CurrentScheduleShiftsAdapter;
import com.technorizen.healthcare.adapters.PostedShiftsAdapter;
import com.technorizen.healthcare.adapters.ShiftsAdapter;
import com.technorizen.healthcare.adapters.ShiftsInProgressAdapter;
import com.technorizen.healthcare.adapters.UserShiftsInProgressAdapter;
import com.technorizen.healthcare.databinding.FragmentHomeBinding;
import com.technorizen.healthcare.models.SuccessResAcceptRejectRecruitment;
import com.technorizen.healthcare.models.SuccessResDeleteCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResDeleteShifts;
import com.technorizen.healthcare.models.SuccessResGetCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.models.SuccessResGetProfile;
import com.technorizen.healthcare.models.SuccessResGetShiftInProgress;
import com.technorizen.healthcare.models.SuccessResGetStates;
import com.technorizen.healthcare.models.SuccessResUpdateRehireShift;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.Constant;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.DeleteShifts;
import com.technorizen.healthcare.util.NetworkAvailablity;
import com.technorizen.healthcare.util.RecruitmentShiftConfirmationInterface;
import com.technorizen.healthcare.util.SharedPreferenceUtility;
import com.technorizen.healthcare.util.ShiftCompletedClick;
import com.technorizen.healthcare.workerSide.fragments.WorkerHomeFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.activites.HomeActivity.ivBack;
import static com.technorizen.healthcare.activites.HomeActivity.ivMenu;
import static com.technorizen.healthcare.retrofit.Constant.RUNNING;
import static com.technorizen.healthcare.retrofit.Constant.SEC;
import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.WASRUNNING;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment implements DeleteShifts, RecruitmentShiftConfirmationInterface, ShiftCompletedClick {

    FragmentHomeBinding binding;
    HealthInterface apiInterface;
    private boolean showLess = false;
    private CurrentScheduleShiftsAdapter currentScheduleShiftsAdapter;
    private ArrayList<SuccessResGetCurrentSchedule.Result> newCurrentScheduleList = new ArrayList<>();
    Timer timer = new Timer();
    private boolean shiftShowLess  = false;
    private ShiftsAdapter shiftsAdapter;
    private ArrayList<SuccessResGetCurrentSchedule.Result> confirmRecruitmentList = new ArrayList<>();
    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>();
    private ArrayList<SuccessResGetPost.Result> newPostedList = new ArrayList<>();
    private List<SuccessResGetProfile.Result> userList = new LinkedList<>();
    private ArrayList<SuccessResGetCurrentSchedule.Result> currentScheduleList = new ArrayList<>();
    private ArrayList<SuccessResGetShiftInProgress.Result> shiftInProgressList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        shiftsAdapter = new ShiftsAdapter(getActivity(),newPostedList,true,HomeFragment.this);
        shiftsAdapter.addList(newPostedList);
        binding.rvShifts.setHasFixedSize(true);
        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvShifts.setAdapter(shiftsAdapter);
        currentScheduleShiftsAdapter = new CurrentScheduleShiftsAdapter(getActivity(),newCurrentScheduleList,true,HomeFragment.this,"userhome");
        currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
        binding.rvCurrentShifts.setHasFixedSize(true);
        binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvCurrentShifts.setAdapter(currentScheduleShiftsAdapter);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
             getShifts(false);
             getCurrentShifts();
            }
        },0,60000);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getProfile();
            getShiftsInProgress();
            getCurrentShifts();
            getShiftReload();
            getRecruitmentPendingShifts();
            rehireToDirectShift();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               binding.btnLoadMore.setVisibility(View.GONE);
               binding.btnLoadLess.setVisibility(View.GONE);
               if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
                   shiftShowLess = false;
                   showLess = false;
                   getShiftsInProgress();
                   getCurrentShifts();
                   getShifts(true);
                   getRecruitmentPendingShifts();
               } else {
                   Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
               }
               binding.srlRefreshContainer.setRefreshing(false);
           }
       });
        return binding.getRoot();
    }

    public void getProfile()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetProfile> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetProfile data = response.body();
                    if (data.status.equals("1")) {
                        userList.addAll(data.getResult());
                        setData();
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
    public void setData()
    {
        SuccessResGetProfile.Result user = userList.get(0);
        if (user.getAccountType().equalsIgnoreCase("Company"))
        {
            binding.tvCompany.setText(user.getCompany());
        }else
        {
            binding.tvCompany.setText(user.getFirstName()+" "+user.getLastName());
        }
        String location = user.getPostshiftTime().get(0).getAddress();
        binding.tvLocation.setText(user.getPostshiftTime().get(0).getAddress());
        binding.tvHoursPurchased.setText(getActivity().getString(R.string.hours_purchased)+user.getPurchasedHour());
        binding.tvHiredCount.setText(getActivity().getString(R.string.hired_counts_0)+user.getHiredCount());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(13));
        Glide
                .with(getActivity())
                .load(user.getImage())
                .centerCrop()
                .apply(requestOptions)
                .into(binding.ivProfile);
    }
    public void getShifts(boolean val)
    {

        if(val)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetPost> call = apiInterface.getPostedShifts(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {
                        postedList.clear();
                        postedList.addAll(data.getResult());
                        setShiftList();
                    } else {
                        postedList.clear();
                        newPostedList.clear();
                        shiftsAdapter.notifyDataSetChanged();

//                        binding.rvShifts.setHasFixedSize(true);
//                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,true,HomeFragment.this));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetPost> call, Throwable t) {
                call.cancel();DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    @Override
    public void onClick(String shiftsId,String onerId,List<SuccessResGetPost.PostshiftTime> postshiftTime,String type) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftsId);
        map.put("status","Deleted");
        Call<SuccessResDeleteShifts> call = apiInterface.deleteShifts(map);
        call.enqueue(new Callback<SuccessResDeleteShifts>() {
            @Override
            public void onResponse(Call<SuccessResDeleteShifts> call, Response<SuccessResDeleteShifts> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeleteShifts data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        shiftShowLess = false;
                        getShifts(true);
                        getRecruitmentPendingShifts();
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResDeleteShifts> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    @Override
    public void rejectSHift(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {
    }

    @Override
    public void deleteCurrentShiftsClick(String shiftsId, String userId,SuccessResGetCurrentSchedule.PostshiftTime dateTime) {
        String cancellationChanrge = "";
        int j;
        String status = "";
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
        String newTIme = dateTime.getStartTime();
        long diffInHours = TimeUnit.MILLISECONDS.toHours(difference);
        if(diffInHours<2)
        {
            cancellationChanrge = "Yes";
        }
        else
        {
            cancellationChanrge = "No";
        }
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("id",shiftsId);
        map.put("status","Cancelled_By_User");
        map.put("on_time","");
        map.put("cancellation_charges",cancellationChanrge);
        Call<SuccessResDeleteCurrentSchedule> call = apiInterface.deleteCurrentShifts(map);
        call.enqueue(new Callback<SuccessResDeleteCurrentSchedule>() {
            @Override
            public void onResponse(Call<SuccessResDeleteCurrentSchedule> call, Response<SuccessResDeleteCurrentSchedule> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResDeleteCurrentSchedule data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        showLess = false;
                        shiftShowLess = false;
                        getCurrentShifts();
                        getShifts(true);
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
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftIds);
        Call<SuccessResUpdateRehireShift> call = apiInterface.updateRehiredShifts(map);
        call.enqueue(new Callback<SuccessResUpdateRehireShift>() {
            @Override
            public void onResponse(Call<SuccessResUpdateRehireShift> call, Response<SuccessResUpdateRehireShift> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateRehireShift data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);
                        shiftShowLess = false;
                        showLess = false;
                        getShifts(true);
                        getCurrentShifts();
                        getRecruitmentPendingShifts();
                        getShiftsInProgress();
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateRehireShift> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    @Override
    public void userCancelRecruit(String shiftIds,String userId,String workerId,String status) {
    }

    public void getCurrentShifts()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCurrentSchedule> call = apiInterface.getCurrentShifts(map);
        call.enqueue(new Callback<SuccessResGetCurrentSchedule>() {
            @Override
            public void onResponse(Call<SuccessResGetCurrentSchedule> call, Response<SuccessResGetCurrentSchedule> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetCurrentSchedule data = response.body();
                    if (data.status.equalsIgnoreCase("1")) {
                        binding.tvCurrentSchedule.setVisibility(View.VISIBLE);
                        binding.rvCurrentShifts.setVisibility(View.VISIBLE);
                        currentScheduleList.clear();
                        currentScheduleList.addAll(data.getResult());
                        Collections.sort(currentScheduleList, new Comparator<SuccessResGetCurrentSchedule.Result>(){
                            public int compare(SuccessResGetCurrentSchedule.Result obj1, SuccessResGetCurrentSchedule.Result obj2) {

                                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                String  dtDate1 = obj1.getPostshiftTime().get(0).getShiftDate()+" 8:20";
                                String  dtDate2 = obj2.getPostshiftTime().get(0).getShiftDate()+" 8:20";
                                Date  date1 = null;
                                Date  date2 = null;
                                try {
                                    date1 = format.parse(dtDate1);
                                    date2 = format.parse(dtDate2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                return date1.compareTo(date2);

                            }
                        });

                        setCurrentScheduleList();

                    } else {

                        binding.tvCurrentSchedule.setVisibility(View.GONE);
                        binding.btnLoadMore.setVisibility(View.GONE);
                        binding.rvCurrentShifts.setVisibility(View.GONE);
                        postedList.clear();
                        binding.rvCurrentShifts.setHasFixedSize(true);
                        binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true,HomeFragment.this,"userhome"));

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

        if(currentScheduleList.size()>3 && !showLess)
        {

            ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>(currentScheduleList.subList(0,3));
//            binding.rvCurrentShifts.setHasFixedSize(true);
//            binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,true, HomeFragment.this,"userhome"));

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
//            binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true, HomeFragment.this,"userhome"));

            newCurrentScheduleList = currentScheduleList;
            currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
            currentScheduleShiftsAdapter.notifyDataSetChanged();
            binding.btnLoadMore.setVisibility(View.GONE);
        }

        binding.btnLoadMore.setOnClickListener(v ->
                {
                    showLess = true;

                    binding.btnLoadMore.setVisibility(View.GONE);
                    binding.btnLoadLess.setVisibility(View.VISIBLE);
//                    binding.rvCurrentShifts.setHasFixedSize(true);
//                    binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true, HomeFragment.this,"userhome"));
//                    binding.btnLoadMore.setVisibility(View.GONE);

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
                    ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>(currentScheduleList.subList(0,3));
//                    binding.rvCurrentShifts.setHasFixedSize(true);
//                    binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,true, HomeFragment.this,"userhome"));

                    newCurrentScheduleList = subList;
                    currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
                    currentScheduleShiftsAdapter.notifyDataSetChanged();

                }
        );

    }

    public void getShiftsInProgress()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetShiftInProgress> call = apiInterface.getUserShiftsInProgress(map);
        call.enqueue(new Callback<SuccessResGetShiftInProgress>() {
            @Override
            public void onResponse(Call<SuccessResGetShiftInProgress> call, Response<SuccessResGetShiftInProgress> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetShiftInProgress data = response.body();
                    if (data.status.equals("1")) {
                        binding.tvShiftInProgress.setVisibility(View.VISIBLE);
                        binding.rvShiftInProgress.setVisibility(View.VISIBLE);
                        shiftInProgressList.clear();
                        shiftInProgressList.addAll(data.getResult());
                        binding.rvShiftInProgress.setHasFixedSize(true);
                        binding.rvShiftInProgress.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShiftInProgress.setAdapter(new UserShiftsInProgressAdapter(getActivity(),shiftInProgressList,true,"workershiftInProgress",HomeFragment.this::getClick));
                    } else {
                        binding.tvShiftInProgress.setVisibility(View.GONE);
                        binding.rvShiftInProgress.setVisibility(View.GONE);
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

    public void setShiftList()
    {

//        shiftsAdapter = new ShiftsAdapter(getActivity(),newPostedList,true,HomeFragment.this);
//        shiftsAdapter.addList(newPostedList);
//        binding.rvShifts.setHasFixedSize(true);
//        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rvShifts.setAdapter(shiftsAdapter);

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
//            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,true,HomeFragment.this));

            newPostedList.clear();
            newPostedList.addAll(subList);
            shiftsAdapter.addList(newPostedList);
            shiftsAdapter.notifyDataSetChanged();
            binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);
            binding.btnLoadLessShifts.setVisibility(View.GONE);

        }
        else
        {
//            binding.rvShifts.setHasFixedSize(true);
//            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,true,HomeFragment.this));

            newPostedList = new ArrayList<>();
            newPostedList.addAll(postedList);
            shiftsAdapter.addList(newPostedList);
            shiftsAdapter.notifyDataSetChanged();
            binding.btnLoadMoreShifts.setVisibility(View.GONE);

        }

        binding.btnLoadMoreShifts.setOnClickListener(v ->
                {

                    shiftShowLess = true;

                    binding.btnLoadMoreShifts.setVisibility(View.GONE);
                    binding.btnLoadLessShifts.setVisibility(View.VISIBLE);

//                    binding.rvShifts.setHasFixedSize(true);
//                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,true,HomeFragment.this));

                    newPostedList.clear();
                    newPostedList.addAll(postedList);
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
                     newPostedList.clear();
                     newPostedList.addAll(subList);
                    shiftsAdapter.addList(newPostedList);
                    shiftsAdapter.notifyDataSetChanged();

                }
        );

    }

    public void getRecruitmentPendingShifts()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCurrentSchedule> call = apiInterface.getPendingRecruitmentShifts(map);
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
                        binding.rvRecruitmentConfirmation.setAdapter(new ConfirmRecruitmentAdapter(getActivity(),confirmRecruitmentList,HomeFragment.this,true));
                    } else {
                        binding.tvPendingRecruitmentShift.setVisibility(View.GONE);
                        binding.rvRecruitmentConfirmation.setVisibility(View.GONE);
                        confirmRecruitmentList.clear();
                        binding.rvRecruitmentConfirmation.setHasFixedSize(true);
                        binding.rvRecruitmentConfirmation.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvRecruitmentConfirmation.setAdapter(new ConfirmRecruitmentAdapter(getActivity(),confirmRecruitmentList,HomeFragment.this,true));
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
    public void onResume() {
        super.onResume();
        getShifts(true);
        getShiftsInProgress();
    }

    @Override
    public void recruitConfirmation(View v,String shiftIds, String userId, String workerId, String status,SuccessResGetCurrentSchedule.Result result) {
        Bundle bundle = new Bundle();
        Log.e("sdffsdfds","REsult = " + new Gson().toJson(result));
        bundle.putString("result",new Gson().toJson(result));
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_confirmEditRecruitmentShiftsFragment,bundle);
        ivBack.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.GONE);

/*

        if(result.getShiftsdetail().get(0).getDayType().equalsIgnoreCase("Single"))
        {
            Bundle bundle = new Bundle();
            Log.e("sdffsdfds","REsult = " + new Gson().toJson(result));
            bundle.putString("result",new Gson().toJson(result));
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_confirmEditRecruitmentShiftsFragment,bundle);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);

        }
*/

      /*  DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftIds);
        map.put("worker_id",workerId);
        map.put("status",status);
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
                        getShifts();
                        getCurrentShifts();
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
        });*/

    }

    @Override
    public void cancelRecruitShift(String shiftIds, String userId, String workerId, String status) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",shiftIds);
        map.put("worker_id",workerId);
        map.put("status","Rejected");
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
                        getShifts(true);
                        getRecruitmentPendingShifts();

                    } else {
                        showToast(getActivity(), data.message);
                        getShifts(true);
                        getRecruitmentPendingShifts();

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

    public void removeShiftAfter30()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();

        Call<SuccessResAcceptRejectRecruitment> call = apiInterface.acceptRejectRecruitment(map);
        call.enqueue(new Callback<SuccessResAcceptRejectRecruitment>() {
            @Override
            public void onResponse(Call<SuccessResAcceptRejectRecruitment> call, Response<SuccessResAcceptRejectRecruitment> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResAcceptRejectRecruitment data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);

                        getShifts(true);
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

    public void getShiftReload()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetPost> call = apiInterface.getPostedShifts(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {

                        getShifts(true);

                    } else {

                        getShifts(true);

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
    public void getClick() {

        getShiftsInProgress();

    }
}