package com.technorizen.healthcare.workerSide.fragments;

import android.app.Dialog;
import android.bluetooth.BluetoothHealthCallback;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ShiftsAdapter;
import com.technorizen.healthcare.databinding.FragmentWorkerAvailableJobsFragmentBinding;
import com.technorizen.healthcare.models.SuccessResAcceptShift;
import com.technorizen.healthcare.models.SuccessResGetCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.models.SuccessResWorkerAcceptedShift;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.DeleteShifts;
import com.technorizen.healthcare.util.NetworkAvailablity;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

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
import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;
import static com.technorizen.healthcare.workerSide.fragments.WorkerShiftsInProgressFragment.getTimeTodayOrNot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerAvailableJobsFragmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerAvailableJobsFragmentFragment extends Fragment implements DeleteShifts {

    FragmentWorkerAvailableJobsFragmentBinding binding;
    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>();

    private ArrayList<SuccessResGetPost.Result> searchedShiftList = new ArrayList<>();
    private ArrayList<SuccessResGetPost.Result> newPostedList = new ArrayList<>();
    private ShiftsAdapter shiftsAdapter;
    private boolean shiftShowLess  = false;
    private ArrayList<SuccessResGetPost.Result> removeDataPostedList = new ArrayList<>();
    private ArrayList<SuccessResWorkerAcceptedShift.Result> workerAccceptedShiftList = new ArrayList<>();

    HealthInterface apiInterface;

    private String sort = "Date";
    Timer timershifts = new Timer();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerAvailableJobsFragmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerAvailableJobsFragmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerAvailableJobsFragmentFragment newInstance(String param1, String param2) {
        WorkerAvailableJobsFragmentFragment fragment = new WorkerAvailableJobsFragmentFragment();
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
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_worker_available_jobs_fragment, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        shiftsAdapter = new ShiftsAdapter(getActivity(),newPostedList,false, WorkerAvailableJobsFragmentFragment.this);
        shiftsAdapter.addList(newPostedList);
        binding.rvShifts.setHasFixedSize(true);
        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvShifts.setAdapter(shiftsAdapter);

        timershifts.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getWorkerShifts(sort,false);
            }
        },0,60000);

        binding.tvSortByDistance.setOnClickListener(v ->
                {
                    shiftShowLess  = false;
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
                    shiftShowLess  = false;
                    binding.tvSortByTime.setBackgroundResource(R.drawable.button_bg);
                    binding.tvSortByDistance.setBackgroundResource(0);
                    binding.tvSortByDistance.setTextColor(getResources().getColor(R.color.black));
                    binding.tvSortByTime.setTextColor(getResources().getColor(R.color.white));
                    sort = "Date";
                    getWorkerShifts(sort,true);
                }
        );

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

            getWorkerShifts(sort,true);

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }


        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.btnLoadLessShifts.setVisibility(View.GONE);
                binding.btnLoadMoreShifts.setVisibility(View.GONE);

                if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                    shiftShowLess = false;
                    getWorkerShifts(sort,true);

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }

                binding.srlRefreshContainer.setRefreshing(false);
            }
        });


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

        return binding.getRoot();
    }

    public void getWorkerShifts(String sortBy,boolean show)
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        if (show)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        }
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

                        getWorkerAccepted("",show);

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
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false,WorkerAvailableJobsFragmentFragment.this));

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
    public void onClick(String shiftsId, String onerId, List<SuccessResGetPost.PostshiftTime> postshiftTime,String type) {

        String myType = "";

        if(type.equalsIgnoreCase("Directshift"))
        {
            myType = "Accepted";
        }
        else
        {

            myType = "Accepted_By_Worker";

        }

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
                        showToast(getActivity(), data.message);
                        shiftShowLess  = false;
                        getWorkerShifts(sort,true);

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
    public void rejectSHift(String shiftsId, String onerId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {

        String myType = "Rejected";

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

                        showToast(getActivity(), data.message);
                        shiftShowLess  = false;
                        getWorkerShifts(sort,true);

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

    }

    @Override
    public void shiftConfimation(String shiftIds, String userId, String workerId, String status) {

    }

    @Override
    public void userCancelRecruit(String shiftIds,String userId,String workerId,String status) {

    }

    public void searchWorkerShifts(String title)
    {

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
                        binding.etSearch.clearFocus();
                        postedList.clear();
                        removeDataPostedList.clear();
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

      /*  if(postedList.size()>10)
        {

            ArrayList<SuccessResGetPost.Result> subList = new ArrayList<>(postedList.subList(0,10));
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,false, WorkerAvailableJobsFragmentFragment.this));
            binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);

        }
        else
        {
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false, WorkerAvailableJobsFragmentFragment.this));
            binding.btnLoadMoreShifts.setVisibility(View.GONE);

        }

        binding.btnLoadMoreShifts.setOnClickListener(v ->
                {

                    binding.btnLoadMoreShifts.setVisibility(View.GONE);
                    binding.btnLoadLessShifts.setVisibility(View.VISIBLE);
                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false, WorkerAvailableJobsFragmentFragment.this));

                }
        );

        binding.btnLoadLessShifts.setOnClickListener(v ->
                {

                    binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);
                    binding.btnLoadLessShifts.setVisibility(View.GONE);
                    ArrayList<SuccessResGetPost.Result> subList = new ArrayList<>(postedList.subList(0,10));
                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,false, WorkerAvailableJobsFragmentFragment.this));

                }
        );
*/

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

            newPostedList = subList;
            shiftsAdapter.addList(newPostedList);
            shiftsAdapter.notifyDataSetChanged();
            binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);
            binding.btnLoadLessShifts.setVisibility(View.GONE);
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

                    shiftShowLess = true;

                    binding.btnLoadMoreShifts.setVisibility(View.GONE);
                    binding.btnLoadLessShifts.setVisibility(View.VISIBLE);

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
    @Override
    public void onDestroy() {
        super.onDestroy();
        timershifts.cancel();
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


}