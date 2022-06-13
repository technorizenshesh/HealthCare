package com.shifts.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.ShiftsAdapter;
import com.shifts.healthcare.databinding.FragmentSearchShiftBinding;
import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetPost;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.DeleteShifts;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchShiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchShiftFragment extends Fragment implements DeleteShifts {

    FragmentSearchShiftBinding binding;
    HealthInterface apiInterface;
    private ArrayList<SuccessResGetPost.Result> postedList = new ArrayList<>() ;

    private String searchText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchShiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchShiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchShiftFragment newInstance(String param1, String param2) {
        SearchShiftFragment fragment = new SearchShiftFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_shift, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        binding.imgHeader.setOnClickListener(v ->
                {

                    getActivity().onBackPressed();

                }
                );

        binding.etSearch.requestFocus();

        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getWorkerShifts(binding.etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        return binding.getRoot();
    }


    public void getWorkerShifts(String title)
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
                        postedList.clear();
                        postedList.addAll(data.getResult());
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false, SearchShiftFragment.this));
                    } else {
                        showToast(getActivity(), data.message);
                        postedList.clear();
                        binding.rvShifts.setHasFixedSize(true);
                        binding.rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvShifts.setAdapter(new ShiftsAdapter(getActivity(),postedList,false,SearchShiftFragment.this));
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
    public void onClick(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime,String type) {

    }

    @Override
    public void rejectSHift(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime, String type) {

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
}