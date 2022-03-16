package com.technorizen.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.CurrentScheduleShiftsAdapter;
import com.technorizen.healthcare.adapters.ShiftsAdapter;
import com.technorizen.healthcare.databinding.FragmentCurrentScheduleBinding;
import com.technorizen.healthcare.models.SuccessResDeleteCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResGetCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.DeleteShifts;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentScheduleFragment extends Fragment implements DeleteShifts {

    FragmentCurrentScheduleBinding binding;

    HealthInterface apiInterface;

    Timer timer = new Timer();
    private boolean showLess = false;

    private ArrayList<SuccessResGetCurrentSchedule.Result> currentScheduleList = new ArrayList<>();

    private CurrentScheduleShiftsAdapter currentScheduleShiftsAdapter;
    private ArrayList<SuccessResGetCurrentSchedule.Result> newCurrentScheduleList = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CurrentScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentScheduleFragment newInstance(String param1, String param2) {
        CurrentScheduleFragment fragment = new CurrentScheduleFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_current_schedule, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        currentScheduleShiftsAdapter = new CurrentScheduleShiftsAdapter(getActivity(),newCurrentScheduleList,true, CurrentScheduleFragment.this,"userCurrent");
        currentScheduleShiftsAdapter.addList(newCurrentScheduleList);

        binding.rvShifts.setHasFixedSize(true);
        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvShifts.setAdapter(currentScheduleShiftsAdapter);

        getShifts(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getShifts(false);
            }
        },0,60000);

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.btnLoadMore.setVisibility(View.GONE);
                binding.btnLoadLess.setVisibility(View.VISIBLE);

                showLess = false;

                getShifts(true);

                binding.srlRefreshContainer.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }

    public void getShifts(boolean show)
    {
        if(show)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetCurrentSchedule> call = apiInterface.getCurrentShifts(map);
        call.enqueue(new Callback<SuccessResGetCurrentSchedule>() {
            @Override
            public void onResponse(Call<SuccessResGetCurrentSchedule> call, Response<SuccessResGetCurrentSchedule> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCurrentSchedule data = response.body();
                    if (data.status.equals("1")) {

                        currentScheduleList.clear();
                        currentScheduleList.addAll(data.getResult());
                        
                        setCurrentScheduleList();
                        
                    } else {
//                        showToast(getActivity(), data.message);
                        currentScheduleList.clear();
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true,CurrentScheduleFragment.this,"userCurrent"));

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
    public void onClick(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime,String type) {

    }

    @Override
    public void rejectSHift(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {

    }

    @Override
    public void deleteCurrentShiftsClick(String shiftsId, String userId,SuccessResGetCurrentSchedule.PostshiftTime dateTime) {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("id",shiftsId);
        map.put("status","Deleted_By_User");
        map.put("on_time","No Show");

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

    }

    @Override
    public void userCancelRecruit(String shiftIds,String userId,String workerId,String status) {

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
//            binding.rvShifts.setHasFixedSize(true);
//            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,true, HomeFragment.this,"userhome"));

            newCurrentScheduleList = subList;
            currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
            currentScheduleShiftsAdapter.notifyDataSetChanged();

            binding.btnLoadMore.setVisibility(View.VISIBLE);
            binding.btnLoadLess.setVisibility(View.GONE);

        }
        else
        {
//            binding.rvShifts.setHasFixedSize(true);
//            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true, HomeFragment.this,"userhome"));

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
//                    binding.rvShifts.setHasFixedSize(true);
//                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true, HomeFragment.this,"userhome"));
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
//                    binding.rvShifts.setHasFixedSize(true);
//                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,true, HomeFragment.this,"userhome"));

                    newCurrentScheduleList = subList;
                    currentScheduleShiftsAdapter.addList(newCurrentScheduleList);
                    currentScheduleShiftsAdapter.notifyDataSetChanged();


                }
        );
       /* binding.btnLoadMore.setVisibility(View.GONE);
        binding.btnLoadLess.setVisibility(View.GONE);

        if(currentScheduleList.size()>3)
        {

            ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>(currentScheduleList.subList(0,3));
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,true, CurrentScheduleFragment.this,"userCurrent"));
            binding.btnLoadMore.setVisibility(View.VISIBLE);
            binding.btnLoadLess.setVisibility(View.GONE);
        }
        else
        {
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true, CurrentScheduleFragment.this,"userCurrent"));
            binding.btnLoadMore.setVisibility(View.GONE);

        }

        binding.btnLoadMore.setOnClickListener(v ->
                {

                    binding.btnLoadMore.setVisibility(View.GONE);
                    binding.btnLoadLess.setVisibility(View.VISIBLE);

                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),currentScheduleList,true, CurrentScheduleFragment.this,"userCurrent"));
                    binding.btnLoadMore.setVisibility(View.GONE);

                }
        );

        binding.btnLoadLess.setOnClickListener(v ->
                {

                    binding.btnLoadMore.setVisibility(View.VISIBLE);
                    binding.btnLoadLess.setVisibility(View.GONE);
                    ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>(currentScheduleList.subList(0,3));
                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,true, CurrentScheduleFragment.this,"userCurrent"));

                }
        );*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }


}