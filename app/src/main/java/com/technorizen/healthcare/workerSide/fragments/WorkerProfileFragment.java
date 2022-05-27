package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentWorkerProfileBinding;
import com.technorizen.healthcare.models.SuccessResGetProfile;
import com.technorizen.healthcare.models.SuccessResGetWorkerProfile;
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
 * Use the {@link WorkerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerProfileFragment extends Fragment {

    FragmentWorkerProfileBinding binding;
    HealthInterface apiInterface;
    private ArrayList<SuccessResGetWorkerProfile.Result> userList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerProfileFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static WorkerProfileFragment newInstance(String param1, String param2) {
        WorkerProfileFragment fragment = new WorkerProfileFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_profile, container, false);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        getProfile();
        binding.btnEdit.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_workerProfileFragment_to_workerEditProfileFragment);
                }
        );
        return binding.getRoot();
    }

    public void getProfile()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
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
                        setData();
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

    public void setData()
    {

        SuccessResGetWorkerProfile.Result user = userList.get(0);
        if(user.getCompany().equalsIgnoreCase(""))
        {
            binding.tvName.setText(user.getFirstName()+" "+user.getLastName());
        }
        else
        {
            binding.tvName.setText(user.getCompany());
        }

        binding.tvLocation.setText(user.getAddress());
        binding.tvEmail.setText(user.getEmail());
        binding.tvPhone.setText("+1"+" "+user.getPhone());

        if(user.getAdminApproval().equalsIgnoreCase("Approved"))
        {
            binding.tvApprove.setVisibility(View.VISIBLE);
        } else
        {
            binding.tvApprove.setVisibility(View.GONE);
        }

        binding.tvRating.setText(userList.get(0).getRating());
        binding.ratingBar.setRating(Float.parseFloat(String.valueOf(userList.get(0).getRating())));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms( new RoundedCorners(13));
        Glide.with(getActivity())
                .load(user.getImage())
                .apply(requestOptions)
                .into(binding.ivProfile);
        binding.tvJobPosition.setText("("+user.getDesignation()+")");
    }
}