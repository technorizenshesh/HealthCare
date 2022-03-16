package com.technorizen.healthcare.workerSide.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.activites.HomeActivity;
import com.technorizen.healthcare.activites.LoginAct;
import com.technorizen.healthcare.databinding.FragmentAddNewReferenceBinding;
import com.technorizen.healthcare.models.SuccessResAddReference;
import com.technorizen.healthcare.models.SuccessResSignIn;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.Constant;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.NetworkAvailablity;
import com.technorizen.healthcare.util.SharedPreferenceUtility;
import com.technorizen.healthcare.workerSide.WorkerHomeAct;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.isValidEmail;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewReferenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewReferenceFragment extends Fragment {

    FragmentAddNewReferenceBinding binding;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private HealthInterface apiInterface;
    
    private String companyName = "",contactPerson = "",companyEmailAddress="",strJobFunction = "",strStartDate = "",strEndDate = "";
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewReferenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewReferenceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewReferenceFragment newInstance(String param1, String param2) {
        AddNewReferenceFragment fragment = new AddNewReferenceFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_new_reference, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);


        binding.tvStart.setOnClickListener(v ->
                {
                    showDialog(1);
                }
                );

        binding.tvEnd.setOnClickListener(v ->
                {
                    showDialog(2);
                }
        );

        binding.btnLogin.setOnClickListener(v -> {

            companyName = binding.etCompanyName.getText().toString().trim();
            contactPerson = binding.etContactPerson.getText().toString().trim();
            companyEmailAddress = binding.etEmail.getText().toString().trim();
            strJobFunction = binding.etJobFunction.getText().toString().trim();

            if(isValid())
            {

                if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                    addReference();

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(),  getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
            }

        });


        return binding.getRoot();


    }

    private void showDialog(int i)
    {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        if(i==1)
                        {
                            binding.tvStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            
                            strStartDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            
                        }
                        else {
                            binding.tvEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            strEndDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;


                        }

                    }
                }, mYear, mMonth, mDay);
            datePickerDialog.show();

    }
    
    public void addReference()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("company_name",companyName);
        map.put("contact_person",contactPerson);
        map.put("company_email",companyEmailAddress);
        map.put("job_function",strJobFunction);
        map.put("start_date",strStartDate);
        map.put("end_date",strEndDate);

        Call<SuccessResAddReference> call = apiInterface.addReference(map);

        call.enqueue(new Callback<SuccessResAddReference>() {
            @Override
            public void onResponse(Call<SuccessResAddReference> call, Response<SuccessResAddReference> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddReference data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        showToast(getActivity(), data.message);

                        getActivity().onBackPressed();

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
            public void onFailure(Call<SuccessResAddReference> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
        
    }

    private boolean isValid() {
        if (companyName.equalsIgnoreCase("")) {
            binding.etCompanyName.setError(getString(R.string.enter_company_name));
            return false;
        }else if (contactPerson.equalsIgnoreCase("")) {
            binding.etContactPerson.setError(getString(R.string.enter_contact_person));
            return false;
        }else if (companyEmailAddress.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(companyEmailAddress)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strJobFunction.equalsIgnoreCase("")) {
            binding.etJobFunction.setError(getString(R.string.enter_job));
            return false;
        }else if (strStartDate.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(),"Please select start date.",Toast.LENGTH_SHORT).show();
            return false;
        }else if (strEndDate.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(),"Please select end date.",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}