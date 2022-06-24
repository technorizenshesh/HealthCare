package com.shifts.healthcare.workerSide.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.CurrentScheduleShiftsAdapter;
import com.shifts.healthcare.databinding.FragmentWorkerCurrentScheduleBinding;
import com.shifts.healthcare.models.SuccessResDeleteCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetPost;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.Constant;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.DeleteShifts;
import com.shifts.healthcare.util.GPSTracker;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import static android.app.Activity.RESULT_OK;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerCurrentScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerCurrentScheduleFragment extends Fragment implements DeleteShifts {

    FragmentWorkerCurrentScheduleBinding binding;
    private String newCurrentStartTime = "";
    HealthInterface apiInterface;
    private boolean showLess = false;
    private ArrayList<SuccessResGetCurrentSchedule.Result> postedList = new ArrayList<>();
    GPSTracker gpsTracker;
    private ArrayList<SuccessResGetCurrentSchedule.Result> newpostedList = new ArrayList<>();
    String strLat = "",strLng = "";
    Timer timershifts = new Timer();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerCurrentScheduleFragment() {
        // Required empty public constructor
    }

    private CurrentScheduleShiftsAdapter currentScheduleShiftsAdapter;

    // TODO: Rename and change types and number of parameters
    public static WorkerCurrentScheduleFragment newInstance(String param1, String param2) {
        WorkerCurrentScheduleFragment fragment = new WorkerCurrentScheduleFragment();
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
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_worker_current_schedule, container, false);
        gpsTracker = new GPSTracker(getActivity());
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        currentScheduleShiftsAdapter = new CurrentScheduleShiftsAdapter(getActivity(),newpostedList,false, WorkerCurrentScheduleFragment.this,"workerhome");
        currentScheduleShiftsAdapter.addList(newpostedList);
        binding.rvShifts.setHasFixedSize(true);
        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvShifts.setAdapter(currentScheduleShiftsAdapter);
        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getShifts(true);
            getLocation();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        timershifts.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLocation();
                getShifts(false);
            }
        },0,60000);

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLess = false;
                getLocation();
                getShifts(true);
                binding.srlRefreshContainer.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }

    public void getShifts(boolean show)
    {
        if(strLat.contains("-"))
        {
            strLat = strLat.replace("-","");
        }
        if(strLng.contains("-"))
        {
            strLng = strLng.replace("-","");
        }

        if (show)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }
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

                        postedList.clear();
                        postedList.addAll(data.getResult());
                            Collections.sort(postedList, new Comparator<SuccessResGetCurrentSchedule.Result>(){
                            public int compare(SuccessResGetCurrentSchedule.Result obj1, SuccessResGetCurrentSchedule.Result obj2) {
                                // ## Ascending order

                                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

                                String  dtDate1 = obj1.getPostshiftTime().get(0).getShiftDate()+" 8:20";
                                String  dtDate2 = obj2.getPostshiftTime().get(0).getShiftDate()+" 8:20";

                                Date date1 = null;
                                Date  date2 = null;

                                try {
                                    date1 = format.parse(dtDate1);
                                    date2 = format.parse(dtDate2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                return date1.compareTo(date2); // To compare string values
                                // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                                // ## Descending order
                                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                                // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                            }
                        });

                            setpostedList();

                    } else {
//                        showToast(getActivity(), data.message);
                        postedList.clear();
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),postedList,false,WorkerCurrentScheduleFragment.this,"workercurrent"));

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

            if(diffInHours<24)
            {
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

                        showToast(getActivity(), data.message);

                        binding.btnLoadMore.setVisibility(View.GONE);

                        binding.btnLoadLess.setVisibility(View.GONE);

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


//
//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//
//                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }


        }
    }


    public void setpostedList()
    {

    /*    if(postedList.size()>1)
        {

            ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>();
            subList.clear();
            subList.add(postedList.get(0));
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,false, WorkerCurrentScheduleFragment.this,"workercurrent"));
            binding.btnLoadMore.setVisibility(View.VISIBLE);

        }
        else
        {
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),postedList,false, WorkerCurrentScheduleFragment.this,"workercurrent"));
            binding.btnLoadMore.setVisibility(View.GONE);

        }

        binding.btnLoadMore.setOnClickListener(v ->
                {

                    binding.btnLoadMore.setVisibility(View.GONE);
                    binding.btnLoadLess.setVisibility(View.VISIBLE);

                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),postedList,false, WorkerCurrentScheduleFragment.this,"workercurrent"));
                    binding.btnLoadMore.setVisibility(View.GONE);

                }
        );

        binding.btnLoadLess.setOnClickListener(v ->
                {

                    binding.btnLoadMore.setVisibility(View.VISIBLE);
                    binding.btnLoadLess.setVisibility(View.GONE);
                    ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>();
                    subList.clear();
                    subList.add(postedList.get(0));
                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,false, WorkerCurrentScheduleFragment.this,"workercurrent"));

                }
        );
*/
        if(!showLess)
        {
            binding.btnLoadMore.setVisibility(View.GONE);
            binding.btnLoadLess.setVisibility(View.GONE);
        }

        if(postedList.size()>1 && !showLess)
        {

            ArrayList<SuccessResGetCurrentSchedule.Result> subList = new ArrayList<>();
            subList.clear();
            subList.add(postedList.get(0));

//            binding.rvCurrentShifts.setHasFixedSize(true);
//            binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,false, WorkerHomeFragment.this,"workerhome"));

            newpostedList = subList;

            currentScheduleShiftsAdapter.addList(newpostedList);
            currentScheduleShiftsAdapter.notifyDataSetChanged();

            binding.btnLoadMore.setVisibility(View.VISIBLE);

        }
        else
        {
//            binding.rvCurrentShifts.setHasFixedSize(true);
//            binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),postedList,false, WorkerHomeFragment.this,"workerhome"));

            newpostedList = postedList;

            currentScheduleShiftsAdapter.addList(newpostedList);
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
//                    binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),postedList,false, WorkerHomeFragment.this,"workerhome"));
                    newpostedList = postedList;
                    currentScheduleShiftsAdapter.addList(newpostedList);
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
                    subList.add(postedList.get(0));

                    newpostedList = subList;
//                    binding.rvCurrentShifts.setHasFixedSize(true);
//                    binding.rvCurrentShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvCurrentShifts.setAdapter(new CurrentScheduleShiftsAdapter(getActivity(),subList,false, WorkerHomeFragment.this,"workerhome"));

                    currentScheduleShiftsAdapter.addList(newpostedList);
                    currentScheduleShiftsAdapter.notifyDataSetChanged();

                }
        );
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timershifts.cancel();
    }


}