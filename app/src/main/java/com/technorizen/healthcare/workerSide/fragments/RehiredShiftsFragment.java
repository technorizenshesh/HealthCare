package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ShiftsAdapter;
import com.technorizen.healthcare.databinding.FragmentRehiredShiftsBinding;
import com.technorizen.healthcare.models.SuccessResAcceptShift;
import com.technorizen.healthcare.models.SuccessResGetCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.DeleteShifts;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RehiredShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RehiredShiftsFragment extends Fragment implements DeleteShifts {

    FragmentRehiredShiftsBinding binding;

    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>();

    HealthInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RehiredShiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RehiredShiftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RehiredShiftsFragment newInstance(String param1, String param2) {
        RehiredShiftsFragment fragment = new RehiredShiftsFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_rehired_shifts, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

      // getWorkerShifts();

        return binding.getRoot();
    }


    public void getWorkerShifts()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResGetPost> call = apiInterface.getRehireShiftForWorker(map);
        call.enqueue(new Callback<SuccessResGetPost>() {
            @Override
            public void onResponse(Call<SuccessResGetPost> call, Response<SuccessResGetPost> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetPost data = response.body();
                    if (data.status.equals("1")) {
                        postedList.clear();
                        postedList.addAll(data.getResult());

                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false, RehiredShiftsFragment.this));

                    } else {
                        showToast(getActivity(), data.message);
                        postedList.clear();
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false,RehiredShiftsFragment.this));

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
    public void onClick(String shiftsId, String onerId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {

        String myType = "";

        if(type.equalsIgnoreCase("Directshift") || type.equalsIgnoreCase("Rehire"))
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

                       getWorkerShifts();

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

                        showToast(getActivity(), data.message);

                     getWorkerShifts();

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
    public void deleteCurrentShiftsClick(String shiftsId, String userId, SuccessResGetCurrentSchedule.PostshiftTime dateTime) {

    }

    @Override
    public void shiftConfimation(String shiftIds, String userId, String workerId, String status) {

    }

    @Override
    public void userCancelRecruit(String shiftIds, String userId, String workerId, String status) {

    }





}