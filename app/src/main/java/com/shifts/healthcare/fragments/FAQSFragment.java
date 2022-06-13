package com.shifts.healthcare.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.FaqsAdapter;
import com.shifts.healthcare.databinding.FragmentFAQSBinding;
import com.shifts.healthcare.models.SuccessResGetFaqs;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FAQSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQSFragment extends Fragment {

    private HealthInterface apiInterface;

    FragmentFAQSBinding binding ;

    private ArrayList<SuccessResGetFaqs.Result> faqsList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FAQSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FAQSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQSFragment newInstance(String param1, String param2) {
        FAQSFragment fragment = new FAQSFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_f_a_q_s, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        getFaqs();

        return binding.getRoot();
    }

    public void getFaqs()

    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetFaqs> call = apiInterface.getUserFaqs(map);

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