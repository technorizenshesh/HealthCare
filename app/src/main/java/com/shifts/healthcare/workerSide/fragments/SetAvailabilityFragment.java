package com.shifts.healthcare.workerSide.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.shifts.healthcare.R;
import com.shifts.healthcare.databinding.FragmentSetAvailabilityBinding;
import com.shifts.healthcare.models.SuccessResAddGetWorkerAvail;
import com.shifts.healthcare.models.SuccessResUpdateScheduleTime;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetAvailabilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetAvailabilityFragment extends Fragment {

    private HealthInterface apiInterface ;

    private SuccessResAddGetWorkerAvail.Result workerAvailabilty;

    FragmentSetAvailabilityBinding binding;

    private String strMonday = "0", strMondayStart="",strMondayEnd="",strTues = "0", strTuesStart="",strTuesEnd="",
            strWed = "0", strWedStart="",strWedEnd="",strThus = "0", strThusStart="",strThusEnd="",
            strFri = "0", strFriStart="",strFriEnd="",strSat = "0", strSatStart="",strSatEnd="",
            strSun = "0", strSunStart="",strSunEnd="";


    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM","11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM","11:15 PM","11:30 PM","11:45 PM"

    };
    String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM","11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM","11:15 PM","11:30 PM","11:45 PM"};

    ArrayAdapter ad;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SetAvailabilityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetAvailabilityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetAvailabilityFragment newInstance(String param1, String param2) {
        SetAvailabilityFragment fragment = new SetAvailabilityFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_set_availability, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        setSpinner();

        binding.switchWeeklySchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    binding.llSchedule.setVisibility(View.VISIBLE);

                    binding.btnSaveChanges.setVisibility(View.VISIBLE);

                }
                else
                {
                    binding.llSchedule.setVisibility(View.GONE);
                    binding.btnSaveChanges.setVisibility(View.GONE);
                }
            }
        });


        binding.switchMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    strMonday = "1";

                }
                else
                {

                    strMonday = "0";

                }
            }
        });

        binding.switchTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    strTues = "1";

                }
                else
                {

                    strTues = "0";

                }
            }
        });
        binding.switchWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    strWed = "1";

                }
                else
                {

                    strWed = "0";

                }
            }
        });
        binding.switchThurs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    strThus = "1";

                }
                else
                {

                    strThus = "0";

                }
            }
        });

        binding.switchFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    strFri = "1";

                }
                else
                {

                    strFri = "0";

                }
            }
        });
        binding.switchSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    strSat = "1";

                }
                else
                {

                    strSat = "0";

                }
            }
        });
        binding.switchSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    strSun = "1";

                }
                else
                {

                    strSun = "0";

                }
            }
        });


        binding.btnSaveChanges.setOnClickListener(v ->
                {

                    strMondayStart = binding.spinnerMonStartTime.getSelectedItem().toString();
                    strMondayEnd = binding.spinnerMonEndTime.getSelectedItem().toString();

                    strTuesStart = binding.spinnerTueStartTime.getSelectedItem().toString();
                    strTuesEnd = binding.spinnerTueStartTime.getSelectedItem().toString();

                    strWedStart = binding.spinnerWedStartTime.getSelectedItem().toString();
                    strWedEnd = binding.spinnerWedEndTime.getSelectedItem().toString();

                    strThusStart = binding.spinnerThusStartTime.getSelectedItem().toString();
                    strThusEnd = binding.spinnerThusEndTime.getSelectedItem().toString();

                    strFriStart = binding.spinnerFriStartTime.getSelectedItem().toString();
                    strFriEnd = binding.spinnerFriEndTime.getSelectedItem().toString();

                    strSatStart = binding.spinnerSatStartTime.getSelectedItem().toString();
                    strSatEnd = binding.spinnerSatEndTime.getSelectedItem().toString();

                    strSunStart = binding.spinnerSunStartTime.getSelectedItem().toString();
                    strSunEnd = binding.spinnerSunEndTime.getSelectedItem().toString();

                    updateAvailability();

                }
                );

        return binding.getRoot();
    }


    public void updateAvailability()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("worker_availability","1");
        map.put("monday_from",strMondayStart);
        map.put("monday_to",strMondayEnd);
        map.put("monday",strMonday);
        map.put("tuesday_from",strTuesStart);
        map.put("tuesday_to",strTuesEnd);
        map.put("tuesday",strTues);
        map.put("wednesday_from",strWedStart);
        map.put("wednesday_to",strWedEnd);
        map.put("wednesday",strWed);
        map.put("thursday_from",strThusStart);
        map.put("thursday_to",strThusEnd);
        map.put("thursday",strThus);
        map.put("friday_from",strFriStart);
        map.put("friday_to",strFriEnd);
        map.put("friday",strFri);
        map.put("saturday_form",strSatStart);
        map.put("saturday_to",strSatEnd);
        map.put("saturday",strSat);
        map.put("sunday_form",strSunStart);
        map.put("sunday_to",strSunEnd);
        map.put("sunday",strSun);

        Call<SuccessResUpdateScheduleTime> call = apiInterface.updateScheduleTime(map);
        call.enqueue(new Callback<SuccessResUpdateScheduleTime>() {
            @Override
            public void onResponse(Call<SuccessResUpdateScheduleTime> call, Response<SuccessResUpdateScheduleTime> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResUpdateScheduleTime data = response.body();
                    if (data.status.equals("1")) {
                        showToast(getActivity(), data.message);


                    } else {

                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateScheduleTime> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }


    public void setSpinner()
    {
        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerMonStartTime.setAdapter(ad);

        binding.spinnerMonStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerMonEndTime.setAdapter(ad);

        binding.spinnerMonEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerTueStartTime.setAdapter(ad);

        binding.spinnerTueStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerTueEndTime.setAdapter(ad);

        binding.spinnerTueEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerWedStartTime.setAdapter(ad);

        binding.spinnerWedStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerWedEndTime.setAdapter(ad);

        binding.spinnerWedEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerThusStartTime.setAdapter(ad);

        binding.spinnerThusStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerThusEndTime.setAdapter(ad);

        binding.spinnerThusEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerFriStartTime.setAdapter(ad);

        binding.spinnerFriStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerFriEndTime.setAdapter(ad);

        binding.spinnerFriEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerSatStartTime.setAdapter(ad);

        binding.spinnerSatStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerSatEndTime.setAdapter(ad);

        binding.spinnerSatEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerSunStartTime.setAdapter(ad);

        binding.spinnerSunStartTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerSunEndTime.setAdapter(ad);

        binding.spinnerSunEndTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     getWorkerAvail();
    }

    public void getWorkerAvail()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResAddGetWorkerAvail> call = apiInterface.getWorkerAvailability(map);
        call.enqueue(new Callback<SuccessResAddGetWorkerAvail>() {
            @Override
            public void onResponse(Call<SuccessResAddGetWorkerAvail> call, Response<SuccessResAddGetWorkerAvail> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResAddGetWorkerAvail data = response.body();

                    if (data.status.equals("1")) {

                        workerAvailabilty = data.getResult().get(0);

                        setAvailability();

                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResAddGetWorkerAvail> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    public void setAvailability()
    {

        if(workerAvailabilty.getWorkerAvailability().equalsIgnoreCase("1"))
        {

            binding.switchWeeklySchedule.setChecked(true);

        }
        else
        {
            binding.switchWeeklySchedule.setChecked(false);
        }

        if(workerAvailabilty.getMonday().equalsIgnoreCase("1"))
        {

            binding.switchMonday.setChecked(true);

        }
        else
        {
            binding.switchMonday.setChecked(false);
        }

        int position = 0;
        position  = getTimePosition(workerAvailabilty.getMondayFrom());
        position++;
        binding.spinnerMonStartTime.setSelection(getTimePosition(workerAvailabilty.getMondayFrom()));
        position  = getTimePosition(workerAvailabilty.getMondayTo());
        position++;
        binding.spinnerMonEndTime.setSelection(getTimePosition(workerAvailabilty.getMondayTo()));

        if(workerAvailabilty.getTuesday().equalsIgnoreCase("1"))
        {

            binding.switchTuesday.setChecked(true);

        }
        else
        {
            binding.switchTuesday.setChecked(false);
        }

        binding.spinnerTueStartTime.setSelection(getTimePosition(workerAvailabilty.getTuesdayFrom()));
        binding.spinnerTueEndTime.setSelection(getTimePosition(workerAvailabilty.getTuesdayTo()));

        if(workerAvailabilty.getWednesday().equalsIgnoreCase("1"))
        {

            binding.switchWed.setChecked(true);

        }
        else
        {
            binding.switchWed.setChecked(false);
        }

        binding.spinnerWedStartTime.setSelection(getTimePosition(workerAvailabilty.getWednesdayFrom()));
        binding.spinnerWedEndTime.setSelection(getTimePosition(workerAvailabilty.getWednesdayTo()));

        if(workerAvailabilty.getThursday().equalsIgnoreCase("1"))
        {

            binding.switchThurs.setChecked(true);

        }
        else
        {
            binding.switchThurs.setChecked(false);
        }

        binding.spinnerThusStartTime.setSelection(getTimePosition(workerAvailabilty.getThursdayFrom()));
        binding.spinnerThusEndTime.setSelection(getTimePosition(workerAvailabilty.getThursdayTo()));

        if(workerAvailabilty.getFriday().equalsIgnoreCase("1"))
        {

            binding.switchFri.setChecked(true);

        }
        else
        {
            binding.switchFri.setChecked(false);
        }

        binding.spinnerFriStartTime.setSelection(getTimePosition(workerAvailabilty.getFridayFrom()));
        binding.spinnerFriEndTime.setSelection(getTimePosition(workerAvailabilty.getFridayTo()));


        if(workerAvailabilty.getSaturday().equalsIgnoreCase("1"))
        {

            binding.switchSat.setChecked(true);

        }
        else
        {
            binding.switchSat.setChecked(false);
        }

        binding.spinnerSatStartTime.setSelection(getTimePosition(workerAvailabilty.getSaturdayForm()));
        binding.spinnerSatEndTime.setSelection(getTimePosition(workerAvailabilty.getSaturdayTo()));

        if(workerAvailabilty.getSaturday().equalsIgnoreCase("1"))
        {

            binding.switchSat.setChecked(true);

        }
        else
        {
            binding.switchSat.setChecked(false);
        }

        binding.spinnerSatStartTime.setSelection(getTimePosition(workerAvailabilty.getSaturdayForm()));
        binding.spinnerSatEndTime.setSelection(getTimePosition(workerAvailabilty.getSaturdayTo()));

        if(workerAvailabilty.getSunday().equalsIgnoreCase("1"))
        {

            binding.switchSun.setChecked(true);

        }
        else
        {
            binding.switchSun.setChecked(false);
        }

        binding.spinnerSunStartTime.setSelection(getTimePosition(workerAvailabilty.getSundayForm()));
        binding.spinnerSunEndTime.setSelection(getTimePosition(workerAvailabilty.getSundayTo()));


    }


    public int getTimePosition(String date)
    {

        for(int i=0;i<start.length;i++)
        {

            if(start[i].equalsIgnoreCase(date))
            {
                return i;
            }

        }
        return 0;
    }

}