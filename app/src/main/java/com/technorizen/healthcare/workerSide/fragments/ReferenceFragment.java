package com.technorizen.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.adapters.ReferenceAdapter;
import com.technorizen.healthcare.databinding.FragmentReferenceBinding;
import com.technorizen.healthcare.models.SuccessResAddReference;
import com.technorizen.healthcare.models.SuccessResGetReference;
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
 * Use the {@link ReferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReferenceFragment extends Fragment {

    FragmentReferenceBinding binding;

    private HealthInterface apiInterface;

    private ArrayList<SuccessResGetReference.Result> referencesList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReferenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReferenceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReferenceFragment newInstance(String param1, String param2) {
        ReferenceFragment fragment = new ReferenceFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reference, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        binding.btnAddRef.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_referenceFragment_to_addNewReferenceFragment);
                }
                );

        getReferences();

        return binding.getRoot();
    }

    public void getReferences()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResGetReference> call = apiInterface.getReference(map);

        call.enqueue(new Callback<SuccessResGetReference>() {
            @Override
            public void onResponse(Call<SuccessResGetReference> call, Response<SuccessResGetReference> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetReference data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        referencesList.clear();

                        referencesList.addAll(data.getResult());


                        binding.rvReferences.setHasFixedSize(true);
                        binding.rvReferences.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvReferences.setAdapter(new ReferenceAdapter(getActivity(),referencesList));

//                        SessionManager.writeString(RegisterAct.this, Constant.driver_id,data.result.id);
//                        App.showToast(RegisterAct.this, data.message, Toast.LENGTH_SHORT);

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetReference> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}