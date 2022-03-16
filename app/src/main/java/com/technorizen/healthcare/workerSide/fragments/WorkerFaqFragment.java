package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.FaqsAdapter;
import com.technorizen.healthcare.databinding.FragmentWorkerFaqBinding;
import com.technorizen.healthcare.models.SuccessResGetFaqs;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerFaqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerFaqFragment extends Fragment {

    FragmentWorkerFaqBinding binding;

    private HealthInterface apiInterface;

    private ArrayList<SuccessResGetFaqs.Result> faqsList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerFaqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerFaqFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static WorkerFaqFragment newInstance(String param1, String param2) {
        WorkerFaqFragment fragment = new WorkerFaqFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_faq, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        getFaqs();

        return binding.getRoot();
    }

    public void getFaqs()

    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();


        Call<SuccessResGetFaqs> call = apiInterface.getWorkerFaqs(map);

        call.enqueue(new Callback<SuccessResGetFaqs>() {
            @Override
            public void onResponse(Call<SuccessResGetFaqs> call, Response<SuccessResGetFaqs> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetFaqs data = response.body();
                    if (data.status.equals("1")) {

                        faqsList.clear();
                        faqsList.addAll(data.getResult());

                        binding.rvScheduleTime.setHasFixedSize(true);
                        binding.rvScheduleTime.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvScheduleTime.setAdapter(new FaqsAdapter(getActivity(),faqsList));

                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetFaqs> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }


}