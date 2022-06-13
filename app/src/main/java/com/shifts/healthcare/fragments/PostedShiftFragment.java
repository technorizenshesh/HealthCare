package com.shifts.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.PostedShiftsAdapter;
import com.shifts.healthcare.adapters.ShiftsAdapter;
import com.shifts.healthcare.databinding.FragmentPostedShiftBinding;
import com.shifts.healthcare.models.SuccessResDeleteShifts;
import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetPost;
import com.shifts.healthcare.models.SuccessResUpdateRehireShift;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.DeleteShifts;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostedShiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostedShiftFragment extends Fragment implements DeleteShifts {

    FragmentPostedShiftBinding binding;
    HealthInterface apiInterface;
    private boolean shiftShowLess  = false;

    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>();
    private ArrayList<SuccessResGetPost.Result> newPostedList = new ArrayList<>();
    Timer timer = new Timer();

    private ShiftsAdapter shiftsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostedShiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostedShiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostedShiftFragment newInstance(String param1, String param2) {
        PostedShiftFragment fragment = new PostedShiftFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_posted_shift, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        shiftsAdapter = new ShiftsAdapter(getActivity(),newPostedList,true,PostedShiftFragment.this);
        shiftsAdapter.addList(newPostedList);
        binding.rvShifts.setHasFixedSize(true);
        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvShifts.setAdapter(shiftsAdapter);


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getShifts(false);
            }
        },0,60000);

        getShifts(true);

        binding.srlRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShifts(true);
                binding.btnLoadLessShifts.setVisibility(View.GONE);
                binding.btnLoadMoreShifts.setVisibility(View.GONE);
                binding.srlRefreshContainer.setRefreshing(false);
            }
        });

        return binding.getRoot();
    }

    public void getShifts(boolean show)
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        if(show)
        {
            DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        }
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
//                        showToast(getActivity(), data.message);
                        postedList.clear();
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new PostedShiftsAdapter(getActivity(),postedList,PostedShiftFragment.this));
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

                        getShifts(true);

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

    public void setShiftList()
    {
/*

        if(postedList.size()>10)
        {

            ArrayList<SuccessResGetPost.Result> subList = new ArrayList<>(postedList.subList(0,10));
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,true,PostedShiftFragment.this));
            binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);

        }
        else
        {
            binding.rvShifts.setHasFixedSize(true);
            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,true,PostedShiftFragment.this));
            binding.btnLoadMoreShifts.setVisibility(View.GONE);

        }

        binding.btnLoadMoreShifts.setOnClickListener(v ->
                {

                    binding.btnLoadMoreShifts.setVisibility(View.GONE);
                    binding.btnLoadLessShifts.setVisibility(View.VISIBLE);

                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,true,PostedShiftFragment.this));

                }
        );

        binding.btnLoadLessShifts.setOnClickListener(v ->
                {

                    binding.btnLoadMoreShifts.setVisibility(View.VISIBLE);
                    binding.btnLoadLessShifts.setVisibility(View.GONE);
                    ArrayList<SuccessResGetPost.Result> subList = new ArrayList<>(postedList.subList(0,10));
                    binding.rvShifts.setHasFixedSize(true);
                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,true,PostedShiftFragment.this));

                }
        );
*/

        if(!shiftShowLess)
        {
            binding.btnLoadMoreShifts.setVisibility(View.GONE);
            binding.btnLoadLessShifts.setVisibility(View.GONE);
        }

        if(postedList.size()>10  && !shiftShowLess)
        {

            ArrayList<SuccessResGetPost.Result> subList = new ArrayList<>(postedList.subList(0,10));

//            binding.rvShifts.setHasFixedSize(true);
//            binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,true,HomeFragment.this));

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
//            binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,true,HomeFragment.this));

            newPostedList = postedList;
            shiftsAdapter.addList(newPostedList);
            shiftsAdapter.notifyDataSetChanged();
            binding.btnLoadMoreShifts.setVisibility(View.GONE);

        }

        binding.btnLoadMoreShifts.setOnClickListener(v ->
                {

                    binding.btnLoadMoreShifts.setVisibility(View.GONE);
                    binding.btnLoadLessShifts.setVisibility(View.VISIBLE);
                    shiftShowLess = true;

//                    binding.rvShifts.setHasFixedSize(true);
//                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,true,HomeFragment.this));

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
//                    binding.rvShifts.setHasFixedSize(true);
//                    binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),subList,true,HomeFragment.this));
                    newPostedList = subList;
                    shiftsAdapter.addList(newPostedList);
                    shiftsAdapter.notifyDataSetChanged();

                }
        );


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();

    }

}