
package com.shifts.healthcare.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.activites.HomeActivity;
import com.shifts.healthcare.adapters.SelectTimeAdapter;
import com.shifts.healthcare.adapters.ShowDateTimeAdapter;

import com.shifts.healthcare.databinding.FragmentPostShiftsBinding;
import com.shifts.healthcare.models.SuccessResAddAddress;
import com.shifts.healthcare.models.SuccessResGetAddress;
import com.shifts.healthcare.models.SuccessResGetAdminFees;
import com.shifts.healthcare.models.SuccessResGetHrRate;
import com.shifts.healthcare.models.SuccessResGetJobPositions;
import com.shifts.healthcare.models.SuccessResGetProfile;
import com.shifts.healthcare.models.SuccessResSignIn;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.Constant;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.GPSTracker;
import com.shifts.healthcare.util.SharedPreferenceUtility;
import com.shifts.healthcare.util.StartTimeAndTimeInterface;
import com.wisnu.datetimerangepickerandroid.CalendarPickerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.shifts.healthcare.activites.LoginAct.TAG;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;
import static com.shifts.healthcare.util.DataManager.subArray;
import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.MULTIPLE;
import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostShiftsFragment extends Fragment implements StartTimeAndTimeInterface {

    GPSTracker gpsTracker;
    String strLat = "", strLng = "",categoryId = "",strAddress="";

    private boolean fromRehire = false;

    private String totalWorked = "";

    private HealthInterface apiInterface;
    private List<SuccessResGetJobPositions.Result> jobPositionsList = new LinkedList<>();
    private List<SuccessResGetAddress.Result> addressList = new LinkedList<>();

    private Dialog dialog;

    private HashMap<String,String> unpaidBreakMap = new HashMap<>();
    private HashMap<String,String> transitMap = new HashMap<>();

    private ArrayAdapter<String> stateArrayAdapter;

    FragmentPostShiftsBinding binding;

    List<String> dates = new LinkedList<>();

    ArrayList<String> selectedDates = new ArrayList<>();

    private String singleSelectedDate = "";

    private Map<String,String> startTime = new HashMap<>();

    private Map<String,String> endTime = new HashMap<>();

    private Map<String,String> startTimeText = new HashMap<>();

    private Map<String,String> endTimeText = new HashMap<>();

    private SelectTimeAdapter adapter;

    private String selectedDatesWithSameTime= "";

    private List<com.shifts.healthcare.models.Date> mySelectedDates;

    boolean visible = false;

    String strJobPosition = "",strNoOfVancancies = "",strHourlyRate = "",strDuty = "",strCovid = "",

    strUnpaid = "", strTransit = "",strShiftsLocation = "",strDayType = "",strShiftsNote = "",
            strShiftsDate = "",strStartTime = "",strEndTIme = "",strNewShiftDate="",strNewShiftStartTime="",strNewShiftSEndTime="";

    private String timeSelection = "Single";

    private boolean multipleSelection = false;

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM"
    };

    String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM"
    };

    String[] vaccine = new String[100];
    ArrayList<String> hrrate = new ArrayList<>();
    String[] duty = {"Floor Duty","One on One"};
    String[] status = {"Negative","Positive"};
    String[] entry = {"Single Day", "Multi Day"};
    String[] day = {"17 july 2021"};
    String[] unpaid = {"None","30 Minutes","45 Minutes","60 Minutes","120 Minutes"};

    String[] unpaid1 = {"None","30","45","60","120"};

    private String workerName = "",workerId = "";

    String[] transit = {"None","1 Hour","2 Hour","3 Hour","4 Hour"};

    ArrayAdapter ad;

    private String workerDesignation = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostShiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostShiftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostShiftsFragment newInstance(String param1, String param2) {
        PostShiftsFragment fragment = new PostShiftsFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_shifts, container, false);
        Bundle myBundle = new Bundle();
        myBundle = this.getArguments();
        if(myBundle!=null)
        {
            workerName = myBundle.getString("name");
            workerId = myBundle.getString("id");
            workerDesignation = myBundle.getString("designation");
            binding.btnRecruitment.setVisibility(View.GONE);
            binding.btnRehireWorker.setVisibility(View.VISIBLE);
            binding.btnPost.setVisibility(View.GONE);
            binding.btnRehireWorker.setText(getResources().getString(R.string.post_shift_to)+" ( "+workerName+" )");
            fromRehire = true;
            binding.spinnerVaccine.setClickable(false);
            binding.spinnerVaccine.setEnabled(false);
            binding.tvShift.setText(getString(R.string.post_rehire_shifts));
        }

        Places.initialize(getActivity().getApplicationContext(), getString(R.string.api_key1));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity());

        binding.nestedScrollView.setNestedScrollingEnabled(true);

        gpsTracker = new GPSTracker(getActivity());

        getLocation();

        unpaidBreakMap.put("None","None");
        unpaidBreakMap.put("30 Minutes","30");
        unpaidBreakMap.put("45 Minutes","45");
        unpaidBreakMap.put("60 Minutes","60");
        unpaidBreakMap.put("120 Minutes","120");

        transitMap.put("None","None");
        transitMap.put("1 Hour","1");
        transitMap.put("2 Hour","2");
        transitMap.put("3 Hour","3");
        transitMap.put("4 Hour","4");

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        adapter = new SelectTimeAdapter(selectedDates,getActivity(),this);

        binding.btnPost.setOnClickListener(v ->
                {

                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());
                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();
                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();
                    strHourlyRate = strHourlyRate.substring(1);
                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
                    strDayType = binding.spinnerEnter.getSelectedItem().toString();
                    strShiftsNote = binding.etShiftsNote.getText().toString();
                    if(multipleSelection)
                    {
                        if(binding.checkboxMultipledateSelect.isChecked())
                        {
                            strStartTime = setCommaForStartDate();
                            strEndTIme = setCommaForEndDate();
                        } else
                        {
                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();

                        }
                    }
                    else
                    {
//                        sasdfpj
                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
                        strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();


                    }
                    if (isValid())
                    {

                        Bundle bundle = new Bundle();
                        bundle.putString("JobPosition",strJobPosition);
                        bundle.putString("NoOfVancancies",strNoOfVancancies);
                        bundle.putString("HourlyRate",strHourlyRate);
                        bundle.putString("Duty",strDuty);
                        bundle.putString("Covid",strCovid);
                        bundle.putString("Unpaid",strUnpaid);
                        bundle.putString("Transit",strTransit);
                        bundle.putString("ShiftsLocation",strShiftsLocation);
                        bundle.putString("DayType",strDayType);
                        bundle.putString("ShiftsNote",strShiftsNote);
                        bundle.putString("ShiftsDate",strShiftsDate);
                        bundle.putString("StartTime",strStartTime);
                        bundle.putString("EndTIme",strEndTIme);

                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);

                        fullScreenDialog("Directshift");

                    }

                }
        );

        binding.btnRecruitment.setOnClickListener(v ->
                {

                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());

                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();

                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();

                    strHourlyRate = strHourlyRate.substring(1);

                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
                    strDayType = binding.spinnerEnter.getSelectedItem().toString();

                    strShiftsNote = binding.etShiftsNote.getText().toString();

                    if(multipleSelection)
                    {

                        if(binding.checkboxMultipledateSelect.isChecked())
                        {

                            strStartTime = setCommaForStartDate();
                            strEndTIme = setCommaForEndDate();

                        } else
                        {

                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();

                        }

                    }
                    else
                    {

                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
                        strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();

                    }
                    if (isValid())
                    {

                        Bundle bundle = new Bundle();
                        bundle.putString("JobPosition",strJobPosition);
                        bundle.putString("NoOfVancancies",strNoOfVancancies);
                        bundle.putString("HourlyRate",strHourlyRate);
                        bundle.putString("Duty",strDuty);
                        bundle.putString("Covid",strCovid);
                        bundle.putString("Unpaid",strUnpaid);
                        bundle.putString("Transit",strTransit);
                        bundle.putString("ShiftsLocation",strShiftsLocation);
                        bundle.putString("DayType",strDayType);
                        bundle.putString("ShiftsNote",strShiftsNote);
                        bundle.putString("ShiftsDate",strShiftsDate);
                        bundle.putString("StartTime",strStartTime);
                        bundle.putString("EndTIme",strEndTIme);

                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);

                        fullScreenDialog("Recruitmentshift");

                    }

                }
        );


        binding.btnRehireWorker.setOnClickListener(v ->
                {

                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());

                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();

                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();

                    strHourlyRate = strHourlyRate.substring(1);

                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
                    strDayType = binding.spinnerEnter.getSelectedItem().toString();

                    strShiftsNote = binding.etShiftsNote.getText().toString();

                    if(multipleSelection)
                    {

                        if(binding.checkboxMultipledateSelect.isChecked())
                        {

                            strStartTime = setCommaForStartDate();
                            strEndTIme = setCommaForEndDate();

                        } else
                        {

                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();

                        }

                    }
                    else
                    {
//                        sasdfpj

                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
                        strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();

                    }
                    if (isValid())
                    {

                        Bundle bundle = new Bundle();
                        bundle.putString("JobPosition",strJobPosition);
                        bundle.putString("NoOfVancancies",strNoOfVancancies);
                        bundle.putString("HourlyRate",strHourlyRate);
                        bundle.putString("Duty",strDuty);
                        bundle.putString("Covid",strCovid);
                        bundle.putString("Unpaid",strUnpaid);
                        bundle.putString("Transit",strTransit);
                        bundle.putString("ShiftsLocation",strShiftsLocation);
                        bundle.putString("DayType",strDayType);
                        bundle.putString("ShiftsNote",strShiftsNote);
                        bundle.putString("ShiftsDate",strShiftsDate);
                        bundle.putString("StartTime",strStartTime);
                        bundle.putString("EndTIme",strEndTIme);

                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);

                        fullScreenDialog("Rehire");

                    }

                }
        );


        binding.tvAddAddress.setOnClickListener(v ->
                {

                    // return after the user has made a selection.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(getActivity());
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

                }
        );

        int i=0;
        while (i<100)
        {
            vaccine[i] = (i+1)+"";
            i++;
        }

        int z=17;
        int m=0;
        while (z<=150)
        {
            hrrate.add("$"+z+"");
            z++;
            m++;
        }

        setSpinner();

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        binding.spinnerStartTime.setAdapter(ad);

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerEndTime.setAdapter(ad);

        dates.clear();
//Date Selection Android
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today1 = new Date();
        binding.calendarSingleDate.init(today1, nextYear.getTime())
                .withSelectedDate(today1).inMode(SINGLE);

        dates.add("12/4/2021");
        Date date = Calendar.getInstance().getTime();
        String pattern = "MM/dd/yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
        DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
        Date today = Calendar.getInstance().getTime();

        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        String compareDate = dateFormat.format(date);

        Log.d(TAG, "onCreateView: "+compareDate.toUpperCase()+" Current Position :"+getTodayTimePosition(compareDate.toUpperCase()));

// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        String todayAsString = df.format(date);

        binding.tvSingleDate.setText(todayAsString);

        strShiftsDate = todayAsString;

        binding.calendarSingleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                String pattern = "MM/dd/yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
                Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(date);

                strShiftsDate = todayAsString;

                binding.tvSingleDate.setText(todayAsString);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        binding.calendarMultipleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                String pattern = "MM/dd/yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                DateFormat df = new SimpleDateFormat(pattern);
// Get the today date using Calendar object.
                Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(date);

                selectedDates.add(todayAsString);

                startTime.put(todayAsString,"12:00 AM");

                endTime.put(todayAsString,"12:00 AM");

                adapter.notifyDataSetChanged();

                setTextForMultipleDate();

            }

            @Override
            public void onDateUnselected(Date date) {
                String pattern = "MM/dd/yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
                Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(date);

                selectedDates.remove(todayAsString);
                startTime.remove(todayAsString);
                endTime.remove(todayAsString);
                adapter.notifyDataSetChanged();
                setTextForMultipleDate();

            }
        });

        binding.rvDate.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvDate.setHasFixedSize(true);
        binding.rvDate.setAdapter(adapter);

        if(jobPositionsList.size()==0)
        {
            getJobPositions();
        }

        if(addressList.size()==0)
        {
            getAddress();
        }

        getAdminFees();

        return binding.getRoot();
    }


    public void getAdminFees()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        Call<SuccessResGetAdminFees> call = apiInterface.getAdminFees(map);
        call.enqueue(new Callback<SuccessResGetAdminFees>() {
            @Override
            public void onResponse(Call<SuccessResGetAdminFees> call, Response<SuccessResGetAdminFees> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetAdminFees data = response.body();

                    if (data.status.equals("1")) {
                        String datas = "Please note that "+data.getResult().get(0).getAdminFee()+"% admin fee will be added to the rate.";
                        binding.tvAdminFees.setText(datas);
                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetAdminFees> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }




    private void setSpinner() {

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                vaccine);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerVaccine.setAdapter(ad);

        binding.spinnerVaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    getHourlyRate();



        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                duty);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerDuty.setAdapter(ad);


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                status);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerStatus.setAdapter(ad);



        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                entry);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerEnter.setAdapter(ad);

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                unpaid);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerUnpaidBreak.setAdapter(ad);


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                transit);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerTransit.setAdapter(ad);

        binding.spinnerTransit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerEnter.setOnItemSelectedListener(date_selection_listener);

//        binding.spinnerStartTime.setOnItemSelectedListener(startTimeSelectListener);
    }
    CalendarPickerView calendar;

    private AdapterView.OnItemSelectedListener startTimeSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            int subEnd = start.length-1;
            int myPosition = position + 1;
            String[] subarray = subArray(end, myPosition, subEnd);
            ad = new ArrayAdapter(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    subarray);

            ad.setDropDownViewResource(
                    android.R.layout
                            .simple_spinner_dropdown_item);

            binding.spinnerEndTime.setAdapter(ad);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener date_selection_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(position == 0)
            {

                multipleSelection = false;
                binding.calendarSingleDate.setVisibility(View.VISIBLE);
                binding.calendarMultipleDate.setVisibility(View.GONE);
                selectedDates.clear();
                startTime.clear();
                endTime.clear();

                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                Date today = new Date();
                binding.calendarSingleDate.init(today, nextYear.getTime())
                        .withSelectedDate(today).inMode(SINGLE);

                binding.rvDate.setVisibility(View.GONE);
                binding.llSingleDate.setVisibility(View.VISIBLE);
                binding.checkboxMultipledateSelect.setVisibility(View.GONE);

                Date date = Calendar.getInstance().getTime();
                String pattern = "MM/dd/yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern

                DateFormat df = new SimpleDateFormat(pattern);
// Get the today date using Calendar object.
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(date);

                binding.tvSingleDate.setText(todayAsString);

                strShiftsDate = todayAsString;

                selectedDates.add(todayAsString);

                startTime.put(todayAsString,"12:00 AM");
                endTime.put(todayAsString,"12:00 AM");

            } else

            {
                multipleSelection = true;
                binding.calendarSingleDate.setVisibility(View.GONE);
                binding.calendarMultipleDate.setVisibility(View.VISIBLE);
                selectedDates.clear();

                Date date = Calendar.getInstance().getTime();
                String pattern = "MM/dd/yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
                Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(date);

                selectedDates.add(todayAsString);
                startTime.put(todayAsString,"12:00 AM");
                endTime.put(todayAsString,"12:00 AM");

                binding.checkboxMultipledateSelect.setVisibility(View.VISIBLE);

                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                binding.checkboxMultipledateSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {

                            binding.rvDate.setVisibility(View.VISIBLE);
                            binding.llSingleDate.setVisibility(View.GONE);

                            Date date = Calendar.getInstance().getTime();
                            String pattern = "MM/dd/yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                            DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
                            Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                            String todayAsString = df.format(date);

                            adapter.notifyDataSetChanged();
                        }
                        else
                        {

                            binding.rvDate.setVisibility(View.GONE);
                            binding.llSingleDate.setVisibility(View.VISIBLE);

                        }
                    }
                });

                Date today1 = new Date();
                binding.calendarMultipleDate.init(today1, nextYear.getTime())
                        .withSelectedDate(today1).inMode(MULTIPLE);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void setTextForMultipleDate()
    {
        String text = "";
        for (String date:selectedDates)
        {
            text = text+date+",";
        }
        if (text.endsWith(",")) {
            text = text.substring(0, text.length() - 1);
        }
        strShiftsDate = text;
        binding.tvSingleDate.setText(text);
    }

    private String setCommaForStartDate()
    {
        String myStrStartTime = "";
        if(startTime.size()==0)
        {
            return myStrStartTime;
        }
        for (String date:selectedDates)
        {
            myStrStartTime = myStrStartTime +startTime.get(date)+",";
        }

        if (strStartTime.endsWith(","))
        {
            myStrStartTime = strStartTime.substring(0, strStartTime.length() - 1);
        }
        return myStrStartTime;
    }

    private String setCommaForEndDate()
    {
        String myStrEndTime = "";
        if(endTime.size()==0)
        {
            return myStrEndTime;
        }

        for (String date:selectedDates)
        {
            myStrEndTime = myStrEndTime +endTime.get(date)+",";
        }

        if (myStrEndTime.endsWith(","))
        {
            myStrEndTime = myStrEndTime.substring(0, myStrEndTime.length() - 1);
        }
        return myStrEndTime;
    }

    @Override
    public void startTime(String key,String startTime1) {
        startTime.put(key,startTime1);
    }

    @Override
    public void endTime(String key,String startTime1) {
        endTime.put(key,startTime1);
    }

    private void addPost(String fromWhere) throws ParseException {

        if(strDayType.equalsIgnoreCase("Single Day"))
        {
            strDayType = "Single";
            timeSelection = "Single";
        }
        else
        {
            strDayType = "Multi";
        }

        setNewTimeDate();
        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        String myBreak = unpaidBreakMap.get(strUnpaid);
        String myTransit = transitMap.get(strTransit);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("job_position",strJobPosition);
        map.put("no_vacancies",strNoOfVancancies);
        map.put("hourly_rate",strHourlyRate);
        map.put("duty_of_worker",strDuty);
        map.put("covid_status",strCovid);
        map.put("unpaid_break",myBreak);
        map.put("transit_allowance",myTransit);
        map.put("shift_location",strShiftsLocation);
        map.put("day_type",strDayType);
        map.put("shift_notes",strShiftsNote);
        map.put("shift_date",strShiftsDate);
        map.put("start_time",strStartTime);
        map.put("end_time",strEndTIme);
        map.put("time_type",timeSelection);
        map.put("shift_date_new",strNewShiftDate);
        map.put("start_time_new",strNewShiftStartTime);
        map.put("end_time_new",strNewShiftSEndTime);
        map.put("type",fromWhere);
        map.put("total_worked",totalWorked);

        Call<SuccessResSignIn> call = apiInterface.directPost(map);

        call.enqueue(new Callback<SuccessResSignIn>() {
            @Override
            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignIn data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        startActivity(new Intent(getActivity(),HomeActivity.class));
                        getActivity().finish();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void rehireWorker() throws ParseException {

        if(strDayType.equalsIgnoreCase("Single Day"))
        {
            strDayType = "Single";
            timeSelection = "Single";
        }
        else
        {
            strDayType = "Multi";
        }

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        String myBreak = unpaidBreakMap.get(strUnpaid);
        String myTransit = transitMap.get(strTransit);

        setNewTimeDate();

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("worker_id",workerId);
        map.put("job_position",strJobPosition);
        map.put("no_vacancies",strNoOfVancancies);
        map.put("hourly_rate",strHourlyRate);
        map.put("duty_of_worker",strDuty);
        map.put("covid_status",strCovid);
        map.put("unpaid_break",myBreak);
        map.put("transit_allowance",myTransit);
        map.put("shift_location",strShiftsLocation);
        map.put("day_type",strDayType);
        map.put("shift_notes",strShiftsNote);
        map.put("shift_date",strShiftsDate);
        map.put("start_time",strStartTime);
        map.put("end_time",strEndTIme);
        map.put("time_type",timeSelection);
        map.put("shift_date_new",strNewShiftDate);
        map.put("start_time_new",strNewShiftStartTime);
        map.put("end_time_new",strNewShiftSEndTime);
        map.put("type","Rehire");
        map.put("total_worked",totalWorked);

        Call<SuccessResSignIn> call = apiInterface.rehireWorker(map);

        call.enqueue(new Callback<SuccessResSignIn>() {
            @Override
            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignIn data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        startActivity(new Intent(getActivity(),HomeActivity.class));
                        getActivity().finish();

                    } else if (data.status.equals("0")) {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void fullScreenDialog(String fromWhere) {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.fragment_confirm_post_shifts);

        TextView tvHeader = dialog.findViewById(R.id.tvHeader);

        ImageView ivBack = dialog.findViewById(R.id.ivBack);

        TextView singleDate,singleStartDate,singleEndDate;

        LinearLayout llSingleDate;

        RecyclerView rvMultipleDates;

        if(fromWhere.equalsIgnoreCase("Directshift"))
        {
            tvHeader.setText(getResources().getString(R.string.confirm_direct_post));
        }else
        {

            if(fromRehire)
            {
                tvHeader.setText(getResources().getString(R.string.confirm_rehire_post));
            }
            else
            {
                tvHeader.setText(getResources().getString(R.string.confirm_recruitment_post));
            }

        }

        ivBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        singleDate = dialog.findViewById(R.id.tvDate);
        singleStartDate = dialog.findViewById(R.id.tvStartTime);
        singleEndDate = dialog.findViewById(R.id.tvEndTime);

        AppCompatButton btnPost =  dialog.findViewById(R.id.btnPostShift);
        AppCompatButton btnEdit =  dialog.findViewById(R.id.btnEdit);
        TextView tvjobPosition,tvVacancies,tvHourlyRate,tvDuty,tvCovid,tvUnpaid,tvTransit,tvShiftsLocation,tvDayType,tvShifstNotes;
        tvjobPosition = dialog.findViewById(R.id.tvJobPosition);
        tvVacancies = dialog.findViewById(R.id.tvVacancies);
        tvHourlyRate = dialog.findViewById(R.id.tvHrlRate);
        tvDuty = dialog.findViewById(R.id.tvDuty);
        tvCovid = dialog.findViewById(R.id.tvCovid);
        tvUnpaid = dialog.findViewById(R.id.tvUnpaid);
        tvTransit = dialog.findViewById(R.id.tvTransit);
        tvShiftsLocation = dialog.findViewById(R.id.tvLocation);
        tvDayType = dialog.findViewById(R.id.tvDayType);
        tvShifstNotes = dialog.findViewById(R.id.tvShiftsNotes);
        llSingleDate = dialog.findViewById(R.id.llSingleDate);
        rvMultipleDates = dialog.findViewById(R.id.rvDate);

        String jobPosition = binding.spinnerJobPosition.getSelectedItem().toString();

        tvjobPosition.setText(jobPosition);
        tvVacancies.setText(strNoOfVancancies);
        tvHourlyRate.setText("$ "+strHourlyRate);
        tvDuty.setText(strDuty);
        tvCovid.setText(strCovid);
        tvUnpaid.setText(strUnpaid);
        tvTransit.setText(strTransit);
        tvShiftsLocation.setText(binding.spinnerAddressEvent.getSelectedItem().toString());
        tvDayType.setText(strDayType);
        tvShifstNotes.setText(strShiftsNote);

        if(multipleSelection)
        {

            if (binding.checkboxMultipledateSelect.isChecked())
            {
                llSingleDate.setVisibility(View.GONE);
                rvMultipleDates.setVisibility(View.VISIBLE);

                List<String> dates = new LinkedList<>();
                List<String> startTimeList = new LinkedList<>();
                List<String> endTimeList = new LinkedList<>();

                dates.addAll(selectedDates);

                for (String date:selectedDates)
                {
                    startTimeList.add(startTime.get(date));
                    endTimeList.add(endTime.get(date));
                }

                rvMultipleDates.setHasFixedSize(true);
                rvMultipleDates.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvMultipleDates.setAdapter(new ShowDateTimeAdapter(getActivity(),dates,startTimeList,endTimeList));

            }
            else
            {

                llSingleDate.setVisibility(View.VISIBLE);
                rvMultipleDates.setVisibility(View.GONE);
                singleDate.setText(strShiftsDate);
                singleStartDate.setText(strStartTime);
                singleEndDate.setText(strEndTIme);

                strStartTime = "";

                for (String date:selectedDates)
                {
                    strStartTime = strStartTime + binding.spinnerStartTime.getSelectedItem().toString()+",";
                }

                if (strStartTime.endsWith(","))
                {
                    strStartTime = strStartTime.substring(0, strStartTime.length() - 1);
                }

                strEndTIme = "";

                for (String date:selectedDates)
                {
                    strEndTIme = strEndTIme + binding.spinnerEndTime.getSelectedItem().toString()+",";
                }

                if (strEndTIme.endsWith(","))
                {
                    strEndTIme = strEndTIme.substring(0, strEndTIme.length() - 1);
                }

            }
        }
        else
        {

            llSingleDate.setVisibility(View.VISIBLE);
            rvMultipleDates.setVisibility(View.GONE);
            singleDate.setText(strShiftsDate);
            singleStartDate.setText(strStartTime);
            singleEndDate.setText(strEndTIme);

        }

        btnEdit.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        btnPost.setOnClickListener(v ->
                {

                    if(binding.checkboxMultipledateSelect.isChecked())
                    {
                        timeSelection = "Multi";
                    }
                    else
                    {
                        timeSelection = "Single";
                    }

                    if(fromWhere.equalsIgnoreCase("Rehire"))
                    {

                        try {
                            rehireWorker();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        try {
                            addPost(fromWhere);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
        );

        dialog.show();

    }


    public void getJobPositions() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetJobPositions> call = apiInterface.getJobPositions(map);
        call.enqueue(new Callback<SuccessResGetJobPositions>() {
            @Override
            public void onResponse(Call<SuccessResGetJobPositions> call, Response<SuccessResGetJobPositions> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetJobPositions data = response.body();
                    if (data.status.equals("1")) {
                        jobPositionsList.clear();
                        jobPositionsList.addAll(data.getResult());
                        setJobPositionSpinner();
                    } else {
                        showToast(getActivity(), data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetJobPositions> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void setJobPositionSpinner()
    {
        List<String> tempStates = new LinkedList<>();

        tempStates.add(getString(R.string.select_job_position));

        for (SuccessResGetJobPositions.Result state:jobPositionsList)
        {
            tempStates.add(state.getName());
        }

        stateArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tempStates);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerJobPosition.setAdapter(stateArrayAdapter);
        binding.spinnerJobPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(fromRehire)
        {

            binding.spinnerJobPosition.setSelection(getSpinnerJobPositionPosition(workerDesignation));
            binding.spinnerJobPosition.setClickable(false);
            binding.spinnerJobPosition.setEnabled(false);
        }

    }

    public int getSpinnerJobPositionPosition(String code)
    {
        int i=1;
        for (SuccessResGetJobPositions.Result state:jobPositionsList)
        {
            if(state.getName().equalsIgnoreCase(code))
            {
                return i;
            }

            i++;
        }

        return 0;
    }


    public String getSelectedJobPositionCode(String strCountryName) {
        for (SuccessResGetJobPositions.Result job : jobPositionsList) {
            if (job.getName().equalsIgnoreCase(strCountryName)) {
                return job.getId();
            }
        }
        return "";
    }


    public void getAddress() {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetAddress> call = apiInterface.getAddress(map);
        call.enqueue(new Callback<SuccessResGetAddress>() {
            @Override
            public void onResponse(Call<SuccessResGetAddress> call, Response<SuccessResGetAddress> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetAddress data = response.body();
                    if (data.status.equals("1")) {

                        addressList.clear();

                        addressList.addAll(data.getResult());
                        setAddressSpinner();

                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetAddress> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void setAddressSpinner()
    {
        List<String> tempStates = new LinkedList<>();

        tempStates.add(getString(R.string.select_shifts_location));

        if(addressList.size()>0)
        {
            for (SuccessResGetAddress.Result state:addressList)
            {
                tempStates.add(state.getAddress());
            }
        }

        stateArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tempStates);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAddressEvent.setAdapter(stateArrayAdapter);

    }

    public String getSelectedAddressCode(String address) {
        for (SuccessResGetAddress.Result job : addressList) {
            if (job.getAddress().equalsIgnoreCase(address)) {
                return job.getId();
            }
        }
        return "";
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
            strLat = Double.toString(gpsTracker.getLatitude()) ;
            strLng = Double.toString(gpsTracker.getLongitude()) ;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("Latittude====", gpsTracker.getLatitude() + "");

                    strLat = Double.toString(gpsTracker.getLatitude()) ;
                    strLng = Double.toString(gpsTracker.getLongitude()) ;

                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }


    private boolean isValid() {
        if (strJobPosition.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please select job position.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strShiftsLocation.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please select shift location.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strShiftsDate.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please select shift dates.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (strEndTIme.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please select End Time.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (strShiftsNote.equalsIgnoreCase("")) {
            binding.etShiftsNote.setError(getString(R.string.enter_shiftsNotes));
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                strAddress = place.getAddress();

                LatLng latLng = place.getLatLng();

                Double latitude = latLng.latitude;

                Double longitude = latLng.longitude;

                strLat = Double.toString(latitude);

                strLng = Double.toString(longitude);

                String address = place.getAddress();

                strAddress = address;

                addAddress();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addAddress()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("address",strAddress);
        map.put("lat",strLat);
        map.put("long",strLng);
        map.put("city","");
        map.put("state","");
        map.put("country","");
        Call<SuccessResAddAddress> call = apiInterface.addAddress(map);
        call.enqueue(new Callback<SuccessResAddAddress>() {
            @Override
            public void onResponse(Call<SuccessResAddAddress> call, Response<SuccessResAddAddress> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddAddress data = response.body();
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
            public void onFailure(Call<SuccessResAddAddress> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public int getTodayTimePosition(String time)
    {
        for (int i=0;i<start.length;i++)
        {
            if(time.equalsIgnoreCase(start[i]));
            {
                return i;
            }
        }
        return 0;
    }

    public void setNewTimeDate() throws ParseException {

        //strShiftsDate strStartTime strEndTIme

        List<String> newShiftDate = Arrays.asList(strShiftsDate.split("\\s*,\\s*"));

        List<String> newShiftStartTime = Arrays.asList(strStartTime.split("\\s*,\\s*"));

        List<String> newShiftEndTime = Arrays.asList(strEndTIme.split("\\s*,\\s*"));

        strNewShiftDate = "";
        strNewShiftSEndTime ="";
        strNewShiftStartTime = "";

        int totalHous = 0;

        long differenceINSec = 0;

        for(int i=0;i<newShiftDate.size();i++)
        {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

            Date  date1 = simpleDateFormat.parse(newShiftStartTime.get(i));
            Date  date2 = simpleDateFormat.parse(newShiftEndTime.get(i));

            long difference = date2.getTime() - date1.getTime();

            long diff = TimeUnit.MILLISECONDS.toSeconds(difference);

            differenceINSec = diff+differenceINSec;

//            int   days = (int) (difference / (1000*60*60*24));
//          int  hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
//          int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
//          hours = (hours < 0 ? -hours : hours);
//          Log.i("======= Hours"," :: "+hours);
//          totalHous = totalHous+ hours;
        }

        float totalHoursWorked;

        double hours = differenceINSec / 3600;
        double workedMinutes = (differenceINSec % 3600) / 60;
        if(hours == 0)
        {
            totalHoursWorked = (float) workedMinutes / (float)60;
            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
        } else if(workedMinutes == 0)
        {
            totalHoursWorked = (float) hours;
            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
        }
        else
        {
            totalHoursWorked = (float) (workedMinutes) / (float)60;
            totalHoursWorked =(float) hours + totalHoursWorked;
            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
        }
        if(!isNumber(totalHoursWorked+""))
        {
            totalHoursWorked = Float.parseFloat(String.format("%.2f", totalHoursWorked));
        }

        totalWorked = totalHoursWorked+"";

        Log.d(TAG, "setNewTimeDate: Total Hours"+totalHoursWorked);

        for (String myDate:newShiftDate)
        {
            String date = convertDateFormate(myDate);
            strNewShiftDate = strNewShiftDate + date+",";
        }

        for (String myStartTime:newShiftStartTime)
        {
            String startTime = timeCoversion12to24(myStartTime);
            strNewShiftStartTime = strNewShiftStartTime + startTime+",";
        }

        for (String myStartTime:newShiftEndTime)
        {
            String startTime = timeCoversion12to24(myStartTime);
            strNewShiftSEndTime = strNewShiftSEndTime + startTime+",";
        }

        if (strNewShiftDate.endsWith(","))
        {
            strNewShiftDate = strNewShiftDate.substring(0, strNewShiftDate.length() - 1);
        }

        if (strNewShiftStartTime.endsWith(","))
        {
            strNewShiftStartTime = strNewShiftStartTime.substring(0, strNewShiftStartTime.length() - 1);
        }

        if (strNewShiftSEndTime.endsWith(","))
        {
            strNewShiftSEndTime = strNewShiftSEndTime.substring(0, strNewShiftSEndTime.length() - 1);
        }

    }

    public String convertDateFormate(String d1)
    {
        String convertedDate = "";

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        String  dtDate1 = d1 +" 8:20";

        Date  date1 = null;

        try {
            date1 = format.parse(dtDate1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String pattern = "yyyy-MM-dd";

        DateFormat df = new SimpleDateFormat(pattern);

        String todayAsString = df.format(date1);

        return todayAsString;
    }

    public static String timeCoversion12to24(String twelveHoursTime) throws ParseException {

        String time = twelveHoursTime;

        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");

        String newTime = date24Format.format(date12Format.parse(time));

        return  newTime;

    }


    public static boolean isNumber(String str) {
        try {
            double v = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

private ArrayList<SuccessResGetHrRate.Result> succHrList = new ArrayList<>();

    private void getHourlyRate() {

        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResGetHrRate> call = apiInterface.getHourlyRate(map);

        call.enqueue(new Callback<SuccessResGetHrRate>() {
            @Override
            public void onResponse(Call<SuccessResGetHrRate> call, Response<SuccessResGetHrRate> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetHrRate data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        succHrList.clear();

                        hrrate.clear();

                        succHrList.addAll(data.getResult());

                        for(SuccessResGetHrRate.Result result:succHrList)
                        {

                            hrrate.add("$"+result.getRate()+"");

                        }

                        ad = new ArrayAdapter(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                hrrate);

                        ad.setDropDownViewResource(
                                android.R.layout
                                        .simple_spinner_dropdown_item);

                        binding.spinnerRate.setAdapter(ad);

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
            public void onFailure(Call<SuccessResGetHrRate> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }





}









//
//
//
//package com.technorizen.healthcare.fragments;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.core.app.ActivityCompat;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.AutocompleteActivity;
//import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
//import com.google.android.material.datepicker.SingleDateSelector;
//import com.google.gson.Gson;
//import com.technorizen.healthcare.R;
//import com.technorizen.healthcare.activites.HomeActivity;
//import com.technorizen.healthcare.activites.LoginAct;
//import com.technorizen.healthcare.activites.SignupWithPostShiftsActivity;
//import com.technorizen.healthcare.activites.SignupWithWorkActivity;
//import com.technorizen.healthcare.adapters.SelectTimeAdapter;
//import com.technorizen.healthcare.adapters.ShowDateTimeAdapter;
//
//import com.technorizen.healthcare.databinding.FragmentPostShiftsBinding;
//import com.technorizen.healthcare.models.SuccessResAddAddress;
//import com.technorizen.healthcare.models.SuccessResGetAddress;
//import com.technorizen.healthcare.models.SuccessResGetJobPositions;
//import com.technorizen.healthcare.models.SuccessResSignIn;
//import com.technorizen.healthcare.retrofit.ApiClient;
//import com.technorizen.healthcare.retrofit.Constant;
//import com.technorizen.healthcare.retrofit.HealthInterface;
//import com.technorizen.healthcare.util.DataManager;
//import com.technorizen.healthcare.util.GPSTracker;
//import com.technorizen.healthcare.util.SharedPreferenceUtility;
//import com.technorizen.healthcare.util.StartTimeAndTimeInterface;
//import com.technorizen.healthcare.workerSide.WorkerHomeAct;
//import com.wisnu.datetimerangepickerandroid.CalendarPickerView;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static android.app.Activity.RESULT_CANCELED;
//import static android.app.Activity.RESULT_OK;
//import static com.technorizen.healthcare.activites.LoginAct.TAG;
//import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
//import static com.technorizen.healthcare.retrofit.Constant.isValidEmail;
//import static com.technorizen.healthcare.retrofit.Constant.showToast;
//import static com.technorizen.healthcare.util.DataManager.subArray;
//import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.MULTIPLE;
//import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.RANGE;
//import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;
//
//public class PostShiftsFragment extends Fragment implements StartTimeAndTimeInterface {
//
//    GPSTracker gpsTracker;
//    String strLat = "", strLng = "",categoryId = "",strAddress="";
//
//    private boolean fromRehire = false;
//
//    private String totalWorked = "";
//
//    private HealthInterface apiInterface;
//    private List<SuccessResGetJobPositions.Result> jobPositionsList = new LinkedList<>();
//    private List<SuccessResGetAddress.Result> addressList = new LinkedList<>();
//
//    private Dialog dialog;
//
//    private HashMap<String,String> unpaidBreakMap = new HashMap<>();
//    private HashMap<String,String> transitMap = new HashMap<>();
//
//    private ArrayAdapter<String> stateArrayAdapter;
//
//    FragmentPostShiftsBinding binding;
//
//    List<String> dates = new LinkedList<>();
//
//    ArrayList<String> selectedDates = new ArrayList<>();
//
//    private String singleSelectedDate = "";
//
//    private Map<String,String> startTime = new HashMap<>();
//
//    private Map<String,String> endTime = new HashMap<>();
//
//    private Map<String,String> startTimeText = new HashMap<>();
//
//    private Map<String,String> endTimeText = new HashMap<>();
//
//    private SelectTimeAdapter adapter;
//
//    private String selectedDatesWithSameTime= "";
//
//    private List<com.technorizen.healthcare.models.Date> mySelectedDates;
//
//    boolean visible = false;
//
//    String strJobPosition = "",strNoOfVancancies = "",strHourlyRate = "",strDuty = "",strCovid = "",
//
//    strUnpaid = "", strTransit = "",strShiftsLocation = "",strDayType = "",strShiftsNote = "",
//            strShiftsDate = "",strStartTime = "",strEndTIme = "",strNewShiftDate="",strNewShiftStartTime="",strNewShiftSEndTime="";
//
//    private String timeSelection = "Single";
//
//    private boolean multipleSelection = false;
//
//    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
//
//    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//            "11:15 AM","11:30 AM","11:45 AM",
//            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//            "11:15 PM","11:30 PM","11:45 PM"
//    };
//
//    String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//            "11:15 AM","11:30 AM","11:45 AM",
//            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//            "11:15 PM","11:30 PM","11:45 PM"
//    };
//
//    String[] vaccine = new String[100];
//    ArrayList<String> hrrate = new ArrayList<>();
//    String[] duty = {"Floor Duty","One on One"};
//    String[] status = {"Negative","Positive"};
//    String[] entry = {"Single Day", "Multi Day"};
//    String[] day = {"17 july 2021"};
//    String[] unpaid = {"None","30 Minutes","45 Minutes","60 Minutes","120 Minutes"};
//
//    String[] unpaid1 = {"None","30","45","60","120"};
//
//    private String workerName = "",workerId = "";
//
//    String[] transit = {"None","1 Hour","2 Hour","3 Hour","4 Hour"};
//
//    ArrayAdapter ad;
//
//    private String workerDesignation = "";
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public PostShiftsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PostShiftsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static PostShiftsFragment newInstance(String param1, String param2) {
//        PostShiftsFragment fragment = new PostShiftsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_shifts, container, false);
//
//        Bundle myBundle = new Bundle();
//
//        myBundle = this.getArguments();
//
//        if(myBundle!=null)
//        {
//            workerName = myBundle.getString("name");
//            workerId = myBundle.getString("id");
//            workerDesignation = myBundle.getString("designation");
//            binding.btnRecruitment.setVisibility(View.GONE);
//            binding.btnRehireWorker.setVisibility(View.VISIBLE);
//            binding.btnPost.setVisibility(View.GONE);
//            binding.btnRehireWorker.setText(getResources().getString(R.string.post_shift_to)+" ( "+workerName+" )");
//
//            fromRehire = true;
//
//            binding.spinnerVaccine.setClickable(false);
//
//            binding.spinnerVaccine.setEnabled(false);
//
//            binding.tvShift.setText(getString(R.string.post_rehire_shifts));
//
//        }
//
//        Places.initialize(getActivity().getApplicationContext(), "AIzaSyA1zVQsDeyYQJbE64CmQVSfzNO-AwFoUNk");
//
//        // Create a new PlacesClient instance
//        PlacesClient placesClient = Places.createClient(getActivity());
//
//        binding.nestedScrollView.setNestedScrollingEnabled(true);
//
//        gpsTracker = new GPSTracker(getActivity());
//
//        getLocation();
//
//        unpaidBreakMap.put("None","None");
//        unpaidBreakMap.put("30 Minutes","30");
//        unpaidBreakMap.put("45 Minutes","45");
//        unpaidBreakMap.put("60 Minutes","60");
//        unpaidBreakMap.put("120 Minutes","120");
//
//        transitMap.put("None","None");
//        transitMap.put("1 Hour","1");
//        transitMap.put("2 Hour","2");
//        transitMap.put("3 Hour","3");
//        transitMap.put("4 Hour","4");
//
//        apiInterface = ApiClient.getClient().create(HealthInterface.class);
//
//        adapter = new SelectTimeAdapter(selectedDates,getActivity(),this);
//
//        binding.btnPost.setOnClickListener(v ->
//                {
//
//                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());
//                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();
//                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();
//                    strHourlyRate = strHourlyRate.substring(1);
//                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
//                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
//                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
//                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
//                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
//                    strDayType = binding.spinnerEnter.getSelectedItem().toString();
//                    strShiftsNote = binding.etShiftsNote.getText().toString();
//                    if(multipleSelection)
//                    {
//                        if(binding.checkboxMultipledateSelect.isChecked())
//                        {
//                            strStartTime = setCommaForStartDate();
//                            strEndTIme = setCommaForEndDate();
//
//                            if(strEndTIme.equalsIgnoreCase(""))
//                            {
//                                return;
//                            }
//
//                        } else
//                        {
//                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                            strEndTIme = "";
//                            if(binding.spinnerEndTime.getSelectedItem()!=null)
//                            {
//                                strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                            }
//                        }
//                    }
//                    else
//                    {
////                        sasdfpj
//                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//
//                        strEndTIme = "";
//                        if(binding.spinnerEndTime.getSelectedItem()!=null)
//                        {
//                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                        }
//
//                    }
//                    if (isValid())
//                    {
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("JobPosition",strJobPosition);
//                        bundle.putString("NoOfVancancies",strNoOfVancancies);
//                        bundle.putString("HourlyRate",strHourlyRate);
//                        bundle.putString("Duty",strDuty);
//                        bundle.putString("Covid",strCovid);
//                        bundle.putString("Unpaid",strUnpaid);
//                        bundle.putString("Transit",strTransit);
//                        bundle.putString("ShiftsLocation",strShiftsLocation);
//                        bundle.putString("DayType",strDayType);
//                        bundle.putString("ShiftsNote",strShiftsNote);
//                        bundle.putString("ShiftsDate",strShiftsDate);
//                        bundle.putString("StartTime",strStartTime);
//                        bundle.putString("EndTIme",strEndTIme);
//
//                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
//
//                        fullScreenDialog("Directshift");
//
//                    }
//
//                }
//        );
//
//        binding.btnRecruitment.setOnClickListener(v ->
//                {
//
//                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());
//
//                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();
//
//                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();
//
//                    strHourlyRate = strHourlyRate.substring(1);
//
//                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
//                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
//                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
//                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
//                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
//                    strDayType = binding.spinnerEnter.getSelectedItem().toString();
//
//                    strShiftsNote = binding.etShiftsNote.getText().toString();
//
//                    if(multipleSelection)
//                    {
//
//                        if(binding.checkboxMultipledateSelect.isChecked())
//                        {
//
//                            strStartTime = setCommaForStartDate();
//                            strEndTIme = setCommaForEndDate();
//                            if(strEndTIme.equalsIgnoreCase(""))
//                            {
//                                return;
//                            }
//                        } else
//                        {
//
//                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                            strEndTIme = "";
//                            if(binding.spinnerEndTime.getSelectedItem()!=null)
//                            {
//                                strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                            }
//
//                        }
//
//                    }
//                    else
//                    {
//
//                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                        strEndTIme = "";
//                        if(binding.spinnerEndTime.getSelectedItem()!=null)
//                        {
//                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                        }
//
//                    }
//                    if (isValid())
//                    {
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("JobPosition",strJobPosition);
//                        bundle.putString("NoOfVancancies",strNoOfVancancies);
//                        bundle.putString("HourlyRate",strHourlyRate);
//                        bundle.putString("Duty",strDuty);
//                        bundle.putString("Covid",strCovid);
//                        bundle.putString("Unpaid",strUnpaid);
//                        bundle.putString("Transit",strTransit);
//                        bundle.putString("ShiftsLocation",strShiftsLocation);
//                        bundle.putString("DayType",strDayType);
//                        bundle.putString("ShiftsNote",strShiftsNote);
//                        bundle.putString("ShiftsDate",strShiftsDate);
//                        bundle.putString("StartTime",strStartTime);
//                        bundle.putString("EndTIme",strEndTIme);
//
//                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
//
//                        fullScreenDialog("Recruitmentshift");
//
//                    }
//
//                }
//        );
//
//
//        binding.btnRehireWorker.setOnClickListener(v ->
//                {
//
//                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());
//
//                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();
//
//                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();
//
//                    strHourlyRate = strHourlyRate.substring(1);
//
//                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
//                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
//                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
//                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
//                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
//                    strDayType = binding.spinnerEnter.getSelectedItem().toString();
//
//                    strShiftsNote = binding.etShiftsNote.getText().toString();
//
//                    if(multipleSelection)
//                    {
//
//                        if(binding.checkboxMultipledateSelect.isChecked())
//                        {
//
//                            strStartTime = setCommaForStartDate();
//                            strEndTIme = setCommaForEndDate();
//                            if(strEndTIme.equalsIgnoreCase(""))
//                            {
//                                return;
//                            }
//                        } else
//                        {
//                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                            strEndTIme = "";
//                            if(binding.spinnerEndTime.getSelectedItem()!=null)
//                            {
//                                strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                            }
//                        }
//                    }
//                    else
//                    {
//                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                        strEndTIme = "";
//                        if(binding.spinnerEndTime.getSelectedItem()!=null)
//                        {
//                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                        }
//                    }
//                    if (isValid())
//                    {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("JobPosition",strJobPosition);
//                        bundle.putString("NoOfVancancies",strNoOfVancancies);
//                        bundle.putString("HourlyRate",strHourlyRate);
//                        bundle.putString("Duty",strDuty);
//                        bundle.putString("Covid",strCovid);
//                        bundle.putString("Unpaid",strUnpaid);
//                        bundle.putString("Transit",strTransit);
//                        bundle.putString("ShiftsLocation",strShiftsLocation);
//                        bundle.putString("DayType",strDayType);
//                        bundle.putString("ShiftsNote",strShiftsNote);
//                        bundle.putString("ShiftsDate",strShiftsDate);
//                        bundle.putString("StartTime",strStartTime);
//                        bundle.putString("EndTIme",strEndTIme);
//
//                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
//
//                        fullScreenDialog("Rehire");
//
//                    }
//
//                }
//        );
//
//
//        binding.tvAddAddress.setOnClickListener(v ->
//                {
//
//                    // return after the user has made a selection.
//                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
//
//                    // Start the autocomplete intent.
//                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                            .build(getActivity());
//                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//
//                }
//        );
//
//        int i=0;
//        while (i<100)
//        {
//            vaccine[i] = (i+1)+"";
//            i++;
//        }
//
//        int z=17;
//        int m=0;
//        while (z<=150)
//        {
//            hrrate.add("$"+z+"");
//            z++;
//            m++;
//        }
//
//        setSpinner();
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                start);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerStartTime.setAdapter(ad);
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                end);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerEndTime.setAdapter(ad);
//
//        dates.clear();
////Date Selection Android
//        Calendar nextYear = Calendar.getInstance();
//        nextYear.add(Calendar.YEAR, 1);
//
//        Date today1 = new Date();
//        binding.calendarSingleDate.init(today1, nextYear.getTime())
//                .withSelectedDate(today1).inMode(SINGLE);
//
//        dates.add("12/4/2021");
//        Date date = Calendar.getInstance().getTime();
//        String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//        DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//        Date today = Calendar.getInstance().getTime();
//
//        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//
//        String compareDate = dateFormat.format(date);
//
//        Log.d(TAG, "onCreateView: "+compareDate.toUpperCase()+" Current Position :"+getTodayTimePosition(compareDate.toUpperCase()));
//
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//        String todayAsString = df.format(date);
//
//        binding.tvSingleDate.setText(todayAsString);
//
//        strShiftsDate = todayAsString;
//
//        binding.calendarSingleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Date date) {
//
//                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                strShiftsDate = todayAsString;
//
//                binding.tvSingleDate.setText(todayAsString);
//            }
//
//            @Override
//            public void onDateUnselected(Date date) {
//
//            }
//        });
//
//        binding.calendarMultipleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Date date) {
//
//                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                DateFormat df = new SimpleDateFormat(pattern);
//// Get the today date using Calendar object.
//                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                selectedDates.add(todayAsString);
//
//                startTime.put(todayAsString,"12:00 AM");
//
//                endTime.put(todayAsString,"12:00 AM");
//
//                adapter.notifyDataSetChanged();
//
//                setTextForMultipleDate();
//
//            }
//
//            @Override
//            public void onDateUnselected(Date date) {
//                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                selectedDates.remove(todayAsString);
//                startTime.remove(todayAsString);
//                endTime.remove(todayAsString);
//                adapter.notifyDataSetChanged();
//                setTextForMultipleDate();
//
//            }
//        });
//
//        binding.rvDate.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rvDate.setHasFixedSize(true);
//        binding.rvDate.setAdapter(adapter);
//
//        if(jobPositionsList.size()==0)
//        {
//            getJobPositions();
//        }
//
//        if(addressList.size()==0)
//        {
//            getAddress();
//        }
//
//        return binding.getRoot();
//    }
//
//    private void setSpinner() {
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                vaccine);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerVaccine.setAdapter(ad);
//
//        binding.spinnerVaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                hrrate);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerRate.setAdapter(ad);
//
//        binding.spinnerRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                duty);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerDuty.setAdapter(ad);
//
//        binding.spinnerDuty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                status);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerStatus.setAdapter(ad);
//
//        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                entry);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerEnter.setAdapter(ad);
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                unpaid);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerUnpaidBreak.setAdapter(ad);
//
//        binding.spinnerUnpaidBreak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                transit);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerTransit.setAdapter(ad);
//
//        binding.spinnerTransit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        binding.spinnerEnter.setOnItemSelectedListener(date_selection_listener);
//
//        binding.spinnerStartTime.setOnItemSelectedListener(startTimeSelectListener);
//    }
//
//    CalendarPickerView calendar;
//
//    private AdapterView.OnItemSelectedListener startTimeSelectListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            int subEnd = start.length-1;
//            int myPosition = position + 1;
//            String[] subarray = subArray(end, myPosition, subEnd);
//            ad = new ArrayAdapter(
//                    getActivity(),
//                    android.R.layout.simple_spinner_item,
//                    subarray);
//
//            ad.setDropDownViewResource(
//                    android.R.layout
//                            .simple_spinner_dropdown_item);
//
//            binding.spinnerEndTime.setAdapter(ad);
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };
//
//    private AdapterView.OnItemSelectedListener date_selection_listener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            if(position == 0)
//            {
//
//                multipleSelection = false;
//                binding.calendarSingleDate.setVisibility(View.VISIBLE);
//                binding.calendarMultipleDate.setVisibility(View.GONE);
//                selectedDates.clear();
//                startTime.clear();
//                endTime.clear();
//
//                Calendar nextYear = Calendar.getInstance();
//                nextYear.add(Calendar.YEAR, 1);
//
//                Date today = new Date();
//                binding.calendarSingleDate.init(today, nextYear.getTime())
//                        .withSelectedDate(today).inMode(SINGLE);
//
//                binding.rvDate.setVisibility(View.GONE);
//                binding.llSingleDate.setVisibility(View.VISIBLE);
//                binding.checkboxMultipledateSelect.setVisibility(View.GONE);
//
//                Date date = Calendar.getInstance().getTime();
//                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//
//                DateFormat df = new SimpleDateFormat(pattern);
//// Get the today date using Calendar object.
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                binding.tvSingleDate.setText(todayAsString);
//
//                strShiftsDate = todayAsString;
//
//                selectedDates.add(todayAsString);
//
//                startTime.put(todayAsString,"12:00 AM");
//                endTime.put(todayAsString,"12:00 AM");
//
//            } else
//
//            {
//                multipleSelection = true;
//                binding.calendarSingleDate.setVisibility(View.GONE);
//                binding.calendarMultipleDate.setVisibility(View.VISIBLE);
//                selectedDates.clear();
//
//                Date date = Calendar.getInstance().getTime();
//                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                selectedDates.add(todayAsString);
//                startTime.put(todayAsString,"12:00 AM");
//                endTime.put(todayAsString,"12:00 AM");
//
//                binding.checkboxMultipledateSelect.setVisibility(View.VISIBLE);
//
//                Calendar nextYear = Calendar.getInstance();
//                nextYear.add(Calendar.YEAR, 1);
//
//                binding.checkboxMultipledateSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if(isChecked)
//                        {
//                            binding.rvDate.setVisibility(View.VISIBLE);
//                            binding.llSingleDate.setVisibility(View.GONE);
//                            Date date = Calendar.getInstance().getTime();
//                            String pattern = "MM/dd/yyyy";
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                            DateFormat df = new SimpleDateFormat(pattern);
//// Get the today date using Calendar object.
//                            Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                            String todayAsString = df.format(date);
//                            adapter.notifyDataSetChanged();
//                        }
//                        else
//                        {
//                            binding.rvDate.setVisibility(View.GONE);
//                            binding.llSingleDate.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//                Date today1 = new Date();
//                binding.calendarMultipleDate.init(today1, nextYear.getTime())
//                        .withSelectedDate(today1).inMode(MULTIPLE);
//            }
//              /*
//                final Country country = (Country) binding.spinnerCountry.getItemAtPosition(position);
//                Log.d("SpinnerCountry", "onItemSelected: country: "+country.getCountryID());
//                ArrayList<State> tempStates = new ArrayList<>();
//                tempStates.add(new State(0, new Country(0, "Choose a Country"), "Choose a State"));
//                for (State singleState : states) {
//                    if (singleState.getCountry().getCountryID() == country.getCountryID()) {
//                        tempStates.add(singleState);
//                    }
//                }
//                stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempStates);
//                stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                binding.spinnerState.setAdapter(stateArrayAdapter);
//    */
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };
//
//    private void setTextForMultipleDate()
//    {
//        String text = "";
//        for (String date:selectedDates)
//        {
//            text = text+date+",";
//        }
//        if (text.endsWith(",")) {
//            text = text.substring(0, text.length() - 1);
//        }
//        strShiftsDate = text;
//        binding.tvSingleDate.setText(text);
//    }
//
//    private String setCommaForStartDate()
//    {
//        String myStrStartTime = "";
//        if(startTime.size()==0)
//        {
//            return myStrStartTime;
//        }
//        for (String date:selectedDates)
//        {
//            myStrStartTime = myStrStartTime +startTime.get(date)+",";
//        }
//        if (strStartTime.endsWith(","))
//        {
//            myStrStartTime = strStartTime.substring(0, strStartTime.length() - 1);
//        }
//        return myStrStartTime;
//    }
//
//    private String setCommaForEndDate()
//    {
//        String myStrEndTime = "";
//        if(endTime.size()==0)
//        {
//            return myStrEndTime;
//        }
//        for (String date:selectedDates)
//        {
//            if(endTime.get(date).equalsIgnoreCase(""))
//            {
//                Toast.makeText(getActivity(),"Please select end time.",Toast.LENGTH_SHORT).show();
//                return "";
//            }
//            myStrEndTime = myStrEndTime +endTime.get(date)+",";
//        }
//        if (myStrEndTime.endsWith(","))
//        {
//            myStrEndTime = myStrEndTime.substring(0, myStrEndTime.length() - 1);
//        }
//        return myStrEndTime;
//    }
//
//    @Override
//    public void startTime(String key,String startTime1) {
//        startTime.put(key,startTime1);
//    }
//
//    @Override
//    public void endTime(String key,String startTime1) {
//        endTime.put(key,startTime1);
//    }
//
//    private void addPost(String fromWhere) throws ParseException {
//
//        if(strDayType.equalsIgnoreCase("Single Day"))
//        {
//            strDayType = "Single";
//            timeSelection = "Single";
//        }
//        else
//        {
//            strDayType = "Multi";
//        }
//
//        setNewTimeDate();
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//        String myBreak = unpaidBreakMap.get(strUnpaid);
//        String myTransit = transitMap.get(strTransit);
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String,String> map = new HashMap<>();
//        map.put("user_id",userId);
//        map.put("job_position",strJobPosition);
//        map.put("no_vacancies",strNoOfVancancies);
//        map.put("hourly_rate",strHourlyRate);
//        map.put("duty_of_worker",strDuty);
//        map.put("covid_status",strCovid);
//        map.put("unpaid_break",myBreak);
//        map.put("transit_allowance",myTransit);
//        map.put("shift_location",strShiftsLocation);
//        map.put("day_type",strDayType);
//        map.put("shift_notes",strShiftsNote);
//        map.put("shift_date",strShiftsDate);
//        map.put("start_time",strStartTime);
//        map.put("end_time",strEndTIme);
//        map.put("time_type",timeSelection);
//        map.put("shift_date_new",strNewShiftDate);
//        map.put("start_time_new",strNewShiftStartTime);
//        map.put("end_time_new",strNewShiftSEndTime);
//        map.put("type",fromWhere);
//        map.put("total_worked",totalWorked);
//        Call<SuccessResSignIn> call = apiInterface.directPost(map);
//        call.enqueue(new Callback<SuccessResSignIn>() {
//            @Override
//            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {
//                DataManager.getInstance().hideProgressMessage();
//                try {
//                    SuccessResSignIn data = response.body();
//                    Log.e("data",data.status);
//                    if (data.status.equals("1")) {
//                        String dataResponse = new Gson().toJson(response.body());
//                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//                        startActivity(new Intent(getActivity(),HomeActivity.class));
//                        getActivity().finish();
//                    } else if (data.status.equals("0")) {
//                        showToast(getActivity(), data.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//            }
//        });
//
//    }
//
//    private void rehireWorker() throws ParseException {
//
//        if(strDayType.equalsIgnoreCase("Single Day"))
//        {
//            strDayType = "Single";
//            timeSelection = "Single";
//        }
//        else
//        {
//            strDayType = "Multi";
//        }
//
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        String myBreak = unpaidBreakMap.get(strUnpaid);
//        String myTransit = transitMap.get(strTransit);
//
//        setNewTimeDate();
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String,String> map = new HashMap<>();
//        map.put("user_id",userId);
//        map.put("worker_id",workerId);
//        map.put("job_position",strJobPosition);
//        map.put("no_vacancies",strNoOfVancancies);
//        map.put("hourly_rate",strHourlyRate);
//        map.put("duty_of_worker",strDuty);
//        map.put("covid_status",strCovid);
//        map.put("unpaid_break",myBreak);
//        map.put("transit_allowance",myTransit);
//        map.put("shift_location",strShiftsLocation);
//        map.put("day_type",strDayType);
//        map.put("shift_notes",strShiftsNote);
//        map.put("shift_date",strShiftsDate);
//        map.put("start_time",strStartTime);
//        map.put("end_time",strEndTIme);
//        map.put("time_type",timeSelection);
//        map.put("shift_date_new",strNewShiftDate);
//        map.put("start_time_new",strNewShiftStartTime);
//        map.put("end_time_new",strNewShiftSEndTime);
//        map.put("type","Rehire");
//        map.put("total_worked",totalWorked);
//
//        Call<SuccessResSignIn> call = apiInterface.rehireWorker(map);
//
//        call.enqueue(new Callback<SuccessResSignIn>() {
//            @Override
//            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//                try {
//                    SuccessResSignIn data = response.body();
//                    Log.e("data",data.status);
//                    if (data.status.equals("1")) {
//                        String dataResponse = new Gson().toJson(response.body());
//                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//
//                        startActivity(new Intent(getActivity(),HomeActivity.class));
//                        getActivity().finish();
//
//                    } else if (data.status.equals("0")) {
//                        showToast(getActivity(), data.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//            }
//        });
//    }
//
//    private void fullScreenDialog(String fromWhere) {
//
//        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
//
//        dialog.setContentView(R.layout.fragment_confirm_post_shifts);
//
//        TextView tvHeader = dialog.findViewById(R.id.tvHeader);
//
//        ImageView ivBack = dialog.findViewById(R.id.ivBack);
//
//        TextView singleDate,singleStartDate,singleEndDate;
//
//        LinearLayout llSingleDate;
//
//        RecyclerView rvMultipleDates;
//
//        if(fromWhere.equalsIgnoreCase("Directshift"))
//        {
//            tvHeader.setText(getResources().getString(R.string.confirm_direct_post));
//        }else
//        {
//
//            if(fromRehire)
//            {
//                tvHeader.setText(getResources().getString(R.string.confirm_rehire_post));
//            }
//            else
//            {
//                tvHeader.setText(getResources().getString(R.string.confirm_recruitment_post));
//            }
//
//
//        }
//
//        ivBack.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//
//        singleDate = dialog.findViewById(R.id.tvDate);
//        singleStartDate = dialog.findViewById(R.id.tvStartTime);
//        singleEndDate = dialog.findViewById(R.id.tvEndTime);
//
//        AppCompatButton btnPost =  dialog.findViewById(R.id.btnPostShift);
//        AppCompatButton btnEdit =  dialog.findViewById(R.id.btnEdit);
//        TextView tvjobPosition,tvVacancies,tvHourlyRate,tvDuty,tvCovid,tvUnpaid,tvTransit,tvShiftsLocation,tvDayType,tvShifstNotes;
//        tvjobPosition = dialog.findViewById(R.id.tvJobPosition);
//        tvVacancies = dialog.findViewById(R.id.tvVacancies);
//        tvHourlyRate = dialog.findViewById(R.id.tvHrlRate);
//        tvDuty = dialog.findViewById(R.id.tvDuty);
//        tvCovid = dialog.findViewById(R.id.tvCovid);
//        tvUnpaid = dialog.findViewById(R.id.tvUnpaid);
//        tvTransit = dialog.findViewById(R.id.tvTransit);
//        tvShiftsLocation = dialog.findViewById(R.id.tvLocation);
//        tvDayType = dialog.findViewById(R.id.tvDayType);
//        tvShifstNotes = dialog.findViewById(R.id.tvShiftsNotes);
//        llSingleDate = dialog.findViewById(R.id.llSingleDate);
//        rvMultipleDates = dialog.findViewById(R.id.rvDate);
//
//        String jobPosition = binding.spinnerJobPosition.getSelectedItem().toString();
//
//        tvjobPosition.setText(jobPosition);
//        tvVacancies.setText(strNoOfVancancies);
//        tvHourlyRate.setText("$ "+strHourlyRate);
//        tvDuty.setText(strDuty);
//        tvCovid.setText(strCovid);
//        tvUnpaid.setText(strUnpaid);
//        tvTransit.setText(strTransit);
//        tvShiftsLocation.setText(binding.spinnerAddressEvent.getSelectedItem().toString());
//        tvDayType.setText(strDayType);
//        tvShifstNotes.setText(strShiftsNote);
//
//        if(multipleSelection)
//        {
//
//            if (binding.checkboxMultipledateSelect.isChecked())
//            {
//                llSingleDate.setVisibility(View.GONE);
//                rvMultipleDates.setVisibility(View.VISIBLE);
//
//                List<String> dates = new LinkedList<>();
//                List<String> startTimeList = new LinkedList<>();
//                List<String> endTimeList = new LinkedList<>();
//
//                dates.addAll(selectedDates);
//
//                for (String date:selectedDates)
//                {
//                    startTimeList.add(startTime.get(date));
//                    endTimeList.add(endTime.get(date));
//                }
//
//                rvMultipleDates.setHasFixedSize(true);
//                rvMultipleDates.setLayoutManager(new LinearLayoutManager(getActivity()));
//                rvMultipleDates.setAdapter(new ShowDateTimeAdapter(getActivity(),dates,startTimeList,endTimeList));
//
//            }
//            else
//            {
//
//                llSingleDate.setVisibility(View.VISIBLE);
//                rvMultipleDates.setVisibility(View.GONE);
//                singleDate.setText(strShiftsDate);
//                singleStartDate.setText(strStartTime);
//                singleEndDate.setText(strEndTIme);
//
//                strStartTime = "";
//
//                for (String date:selectedDates)
//                {
//                    strStartTime = strStartTime + binding.spinnerStartTime.getSelectedItem().toString()+",";
//                }
//
//                if (strStartTime.endsWith(","))
//                {
//                    strStartTime = strStartTime.substring(0, strStartTime.length() - 1);
//                }
//
//                strEndTIme = "";
//
//                for (String date:selectedDates)
//                {
//                    strEndTIme = strEndTIme + binding.spinnerEndTime.getSelectedItem().toString()+",";
//                }
//
//                if (strEndTIme.endsWith(","))
//                {
//                    strEndTIme = strEndTIme.substring(0, strEndTIme.length() - 1);
//                }
//
//            }
//        }
//        else
//        {
//
//            llSingleDate.setVisibility(View.VISIBLE);
//            rvMultipleDates.setVisibility(View.GONE);
//            singleDate.setText(strShiftsDate);
//            singleStartDate.setText(strStartTime);
//            singleEndDate.setText(strEndTIme);
//
//        }
//
//        btnEdit.setOnClickListener(v ->
//                {
//                    dialog.dismiss();
//                }
//        );
//
//        btnPost.setOnClickListener(v ->
//                {
//
//                    if(binding.checkboxMultipledateSelect.isChecked())
//                    {
//                        timeSelection = "Multi";
//                    }
//                    else
//                    {
//                        timeSelection = "Single";
//                    }
//
//                    if(fromWhere.equalsIgnoreCase("Rehire"))
//                    {
//
//                        try {
//                            rehireWorker();
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    else
//                    {
//                        try {
//                            addPost(fromWhere);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//        );
//
//        dialog.show();
//
//    }
//
//  /*  private void fullScreenDialogDate() {
//
//        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
//
//        dialog.setContentView(R.layout.fragment_confirm_post_shifts);
//
//        ImageView ivBack = dialog.findViewById(R.id.ivBack);
//
//        TextView singleDate,singleStartDate,singleEndDate;
//
//        LinearLayout llSingleDate;
//
//        RecyclerView rvMultipleDates;
//
//        ivBack.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//
//        singleDate = dialog.findViewById(R.id.tvDate);
//        singleStartDate = dialog.findViewById(R.id.tvStartTime);
//        singleEndDate = dialog.findViewById(R.id.tvEndTime);
//
//        AppCompatButton btnPost =  dialog.findViewById(R.id.btnPostShift);
//        AppCompatButton btnEdit =  dialog.findViewById(R.id.btnEdit);
//        TextView tvjobPosition,tvVacancies,tvHourlyRate,tvDuty,tvCovid,tvUnpaid,tvTransit,tvShiftsLocation,tvDayType,tvShifstNotes;
//        tvjobPosition = dialog.findViewById(R.id.tvJobPosition);
//        tvVacancies = dialog.findViewById(R.id.tvVacancies);
//        tvHourlyRate = dialog.findViewById(R.id.tvHrlRate);
//        tvDuty = dialog.findViewById(R.id.tvDuty);
//        tvCovid = dialog.findViewById(R.id.tvCovid);
//        tvUnpaid = dialog.findViewById(R.id.tvUnpaid);
//        tvTransit = dialog.findViewById(R.id.tvTransit);
//        tvShiftsLocation = dialog.findViewById(R.id.tvLocation);
//        tvDayType = dialog.findViewById(R.id.tvDayType);
//        tvShifstNotes = dialog.findViewById(R.id.tvShiftsNotes);
//        llSingleDate = dialog.findViewById(R.id.llSingleDate);
//        rvMultipleDates = dialog.findViewById(R.id.rvDate);
//
//        String jobPosition = binding.spinnerJobPosition.getSelectedItem().toString();
//
//        tvjobPosition.setText(jobPosition);
//        tvVacancies.setText(strNoOfVancancies);
//        tvHourlyRate.setText(strHourlyRate);
//        tvDuty.setText(strDuty);
//        tvCovid.setText(strCovid);
//        tvUnpaid.setText(strUnpaid);
//        tvTransit.setText(strTransit);
//        tvShiftsLocation.setText(binding.spinnerAddressEvent.getSelectedItem().toString());
//        tvDayType.setText(strDayType);
//        tvShifstNotes.setText(strShiftsNote);
//
//        if(multipleSelection)
//        {
//
//            if (binding.checkboxMultipledateSelect.isChecked())
//
//            {
//
//                llSingleDate.setVisibility(View.GONE);
//                rvMultipleDates.setVisibility(View.VISIBLE);
//
//                List<String> dates = new LinkedList<>();
//                List<String> startTimeList = new LinkedList<>();
//                List<String> endTimeList = new LinkedList<>();
//
//                dates.addAll(selectedDates);
//
//                for (String date:selectedDates)
//
//                {
//                    startTimeList.add(startTime.get(date));
//                    endTimeList.add(endTime.get(date));
//                }
//
//                rvMultipleDates.setHasFixedSize(true);
//                rvMultipleDates.setLayoutManager(new LinearLayoutManager(getActivity()));
//                rvMultipleDates.setAdapter(new ShowDateTimeAdapter(getActivity(),dates,startTimeList,endTimeList));
//
//            }
//            else
//            {
//
//                llSingleDate.setVisibility(View.VISIBLE);
//                rvMultipleDates.setVisibility(View.GONE);
//                singleDate.setText(strShiftsDate);
//                singleStartDate.setText(strStartTime);
//                singleEndDate.setText(strEndTIme);
//
//            }
//
//        }
//        else
//        {
//
//            llSingleDate.setVisibility(View.VISIBLE);
//            rvMultipleDates.setVisibility(View.GONE);
//
//            singleDate.setText(strShiftsDate);
//            singleStartDate.setText(strStartTime);
//            singleEndDate.setText(strEndTIme);
//        }
//
//        btnEdit.setOnClickListener(v ->
//                {
//                    dialog.dismiss();
//                }
//        );
//
//        btnPost.setOnClickListener(v ->
//                {
//                    addPost();
//                }
//        );
//        dialog.show();
//
//    }
//
//*/
//
//    public void getJobPositions() {
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//
//        Call<SuccessResGetJobPositions> call = apiInterface.getJobPositions(map);
//        call.enqueue(new Callback<SuccessResGetJobPositions>() {
//            @Override
//            public void onResponse(Call<SuccessResGetJobPositions> call, Response<SuccessResGetJobPositions> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResGetJobPositions data = response.body();
//                    if (data.status.equals("1")) {
//                        jobPositionsList.clear();
//                        jobPositionsList.addAll(data.getResult());
//                        setJobPositionSpinner();
//                    } else {
//                        showToast(getActivity(), data.message);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResGetJobPositions> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//    }
//
//    private void setJobPositionSpinner()
//    {
//        List<String> tempStates = new LinkedList<>();
//
//        tempStates.add(getString(R.string.select_job_position));
//
//        for (SuccessResGetJobPositions.Result state:jobPositionsList)
//        {
//            tempStates.add(state.getName());
//        }
//
//        stateArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tempStates);
//        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinnerJobPosition.setAdapter(stateArrayAdapter);
//        binding.spinnerJobPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        if(fromRehire)
//        {
//
//            binding.spinnerJobPosition.setSelection(getSpinnerJobPositionPosition(workerDesignation));
//            binding.spinnerJobPosition.setClickable(false);
//            binding.spinnerJobPosition.setEnabled(false);
//        }
//
//    }
//
//    public int getSpinnerJobPositionPosition(String code)
//    {
//        int i=1;
//        for (SuccessResGetJobPositions.Result state:jobPositionsList)
//        {
//            if(state.getName().equalsIgnoreCase(code))
//            {
//                return i;
//            }
//
//            i++;
//        }
//
//        return 0;
//    }
//
//
//    public String getSelectedJobPositionCode(String strCountryName) {
//        for (SuccessResGetJobPositions.Result job : jobPositionsList) {
//            if (job.getName().equalsIgnoreCase(strCountryName)) {
//                return job.getId();
//            }
//        }
//        return "";
//    }
//
//
//    public void getAddress() {
//
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id",userId);
//
//        Call<SuccessResGetAddress> call = apiInterface.getAddress(map);
//        call.enqueue(new Callback<SuccessResGetAddress>() {
//            @Override
//            public void onResponse(Call<SuccessResGetAddress> call, Response<SuccessResGetAddress> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResGetAddress data = response.body();
//                    if (data.status.equals("1")) {
//
//                        addressList.clear();
//
//                        addressList.addAll(data.getResult());
//                        setAddressSpinner();
//
//                    } else {
//                        showToast(getActivity(), data.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResGetAddress> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//    }
//
//    private void setAddressSpinner()
//    {
//        List<String> tempStates = new LinkedList<>();
//
//        tempStates.add(getString(R.string.select_shifts_location));
//
//        if(addressList.size()>0)
//        {
//            for (SuccessResGetAddress.Result state:addressList)
//            {
//                tempStates.add(state.getAddress());
//            }
//        }
//
//        stateArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tempStates);
//        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinnerAddressEvent.setAdapter(stateArrayAdapter);
//
//    }
//
//    public String getSelectedAddressCode(String address) {
//        for (SuccessResGetAddress.Result job : addressList) {
//            if (job.getAddress().equalsIgnoreCase(address)) {
//                return job.getId();
//            }
//        }
//        return "";
//    }
//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    Constant.LOCATION_REQUEST);
//        } else {
//            Log.e("Latittude====",gpsTracker.getLatitude()+"");
//            strLat = Double.toString(gpsTracker.getLatitude()) ;
//            strLng = Double.toString(gpsTracker.getLongitude()) ;
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case 1000: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                    strLat = Double.toString(gpsTracker.getLatitude()) ;
//                    strLng = Double.toString(gpsTracker.getLongitude()) ;
//
////
////                    if (isContinue) {
////                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                            // TODO: Consider calling
////                            //    ActivityCompat#requestPermissions
////                            // here to request the missing permissions, and then overriding
////                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                            //                                          int[] grantResults)
////                            // to handle the case where the user grants the permission. See the documentation
////                            // for ActivityCompat#requestPermissions for more details.
////                            return;
////                        }
////                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
////                    } else {
////                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
////
////                        strLat = Double.toString(gpsTracker.getLatitude()) ;
////                        strLng = Double.toString(gpsTracker.getLongitude()) ;
////
////                    }
//                } else {
//                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//
//        }
//    }
//
//
//    private boolean isValid() {
//        if (strJobPosition.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select job position.", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (strShiftsLocation.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select shift location.", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (strShiftsDate.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select shift dates.", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (strEndTIme.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select End Time.", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (strShiftsNote.equalsIgnoreCase("")) {
//            binding.etShiftsNote.setError(getString(R.string.enter_shiftsNotes));
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//
//                strAddress = place.getAddress();
//
//                LatLng latLng = place.getLatLng();
//
//                Double latitude = latLng.latitude;
//
//                Double longitude = latLng.longitude;
//
//                strLat = Double.toString(latitude);
//
//                strLng = Double.toString(longitude);
//
//                String address = place.getAddress();
//
//                strAddress = address;
//
//                addAddress();
//
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i(TAG, status.getStatusMessage());
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public void addAddress()
//    {
//
////        if(strLat.contains("-"))
////        {
////            strLat = strLat.replace("-","");
////        }
////        if(strLng.contains("-"))
////        {
////            strLng = strLng.replace("-","");
////        }
//
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id",userId);
//        map.put("address",strAddress);
//        map.put("lat",strLat);
//        map.put("long",strLng);
//        map.put("city","");
//        map.put("state","");
//        map.put("country","");
//
//        Call<SuccessResAddAddress> call = apiInterface.addAddress(map);
//        call.enqueue(new Callback<SuccessResAddAddress>() {
//            @Override
//            public void onResponse(Call<SuccessResAddAddress> call, Response<SuccessResAddAddress> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResAddAddress data = response.body();
//                    if (data.status.equals("1")) {
//
//                        showToast(getActivity(), data.message);
//
//                    } else {
//                        showToast(getActivity(), data.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResAddAddress> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//    }
//
//    public int getTodayTimePosition(String time)
//    {
//        for (int i=0;i<start.length;i++)
//        {
//            if(time.equalsIgnoreCase(start[i]));
//            {
//                return i;
//            }
//        }
//        return 0;
//    }
//
//    public void setNewTimeDate() throws ParseException {
//
//        //strShiftsDate strStartTime strEndTIme
//
//        List<String> newShiftDate = Arrays.asList(strShiftsDate.split("\\s*,\\s*"));
//
//        List<String> newShiftStartTime = Arrays.asList(strStartTime.split("\\s*,\\s*"));
//
//        List<String> newShiftEndTime = Arrays.asList(strEndTIme.split("\\s*,\\s*"));
//
//        strNewShiftDate = "";
//        strNewShiftSEndTime ="";
//        strNewShiftStartTime = "";
//
//        int totalHous = 0;
//
//        long differenceINSec = 0;
//
//        for(int i=0;i<newShiftDate.size();i++)
//        {
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
//
//            Date  date1 = simpleDateFormat.parse(newShiftStartTime.get(i));
//            Date  date2 = simpleDateFormat.parse(newShiftEndTime.get(i));
//
//            long difference = date2.getTime() - date1.getTime();
//
//            long diff = TimeUnit.MILLISECONDS.toSeconds(difference);
//
//            differenceINSec = diff+differenceINSec;
//
////            int   days = (int) (difference / (1000*60*60*24));
////          int  hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
////          int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
////          hours = (hours < 0 ? -hours : hours);
////          Log.i("======= Hours"," :: "+hours);
////          totalHous = totalHous+ hours;
//        }
//
//        float totalHoursWorked;
//
//        double hours = differenceINSec / 3600;
//        double workedMinutes = (differenceINSec % 3600) / 60;
//        if(hours == 0)
//        {
//            totalHoursWorked = (float) workedMinutes / (float)60;
//            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
//        } else if(workedMinutes == 0)
//        {
//            totalHoursWorked = (float) hours;
//            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
//        }
//        else
//        {
//            totalHoursWorked = (float) (workedMinutes) / (float)60;
//            totalHoursWorked =(float) hours + totalHoursWorked;
//            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
//        }
//        if(!isNumber(totalHoursWorked+""))
//        {
//            totalHoursWorked = Float.parseFloat(String.format("%.2f", totalHoursWorked));
//        }
//
//        totalWorked = totalHoursWorked+"";
//
//
//        Log.d(TAG, "setNewTimeDate: Total Hours"+totalHoursWorked);
//
//        for (String myDate:newShiftDate)
//        {
//            String date = convertDateFormate(myDate);
//            strNewShiftDate = strNewShiftDate + date+",";
//        }
//
//        for (String myStartTime:newShiftStartTime)
//        {
//            String startTime = timeCoversion12to24(myStartTime);
//            strNewShiftStartTime = strNewShiftStartTime + startTime+",";
//        }
//
//        for (String myStartTime:newShiftEndTime)
//        {
//            String startTime = timeCoversion12to24(myStartTime);
//            strNewShiftSEndTime = strNewShiftSEndTime + startTime+",";
//        }
//
//        if (strNewShiftDate.endsWith(","))
//        {
//            strNewShiftDate = strNewShiftDate.substring(0, strNewShiftDate.length() - 1);
//        }
//
//        if (strNewShiftStartTime.endsWith(","))
//        {
//            strNewShiftStartTime = strNewShiftStartTime.substring(0, strNewShiftStartTime.length() - 1);
//        }
//
//        if (strNewShiftSEndTime.endsWith(","))
//        {
//            strNewShiftSEndTime = strNewShiftSEndTime.substring(0, strNewShiftSEndTime.length() - 1);
//        }
//
//    }
//
//    public String convertDateFormate(String d1)
//    {
//        String convertedDate = "";
//
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//
//        String  dtDate1 = d1 +" 8:20";
//
//        Date  date1 = null;
//
//        try {
//            date1 = format.parse(dtDate1);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        String pattern = "yyyy-MM-dd";
//
//        DateFormat df = new SimpleDateFormat(pattern);
//
//        String todayAsString = df.format(date1);
//
//        return todayAsString;
//    }
//
//    public static String timeCoversion12to24(String twelveHoursTime) throws ParseException {
//
//        String time = twelveHoursTime;
//
//        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
//
//        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
//
//        String newTime = date24Format.format(date12Format.parse(time));
//
//        return  newTime;
//
//    }
//
//    public static boolean isNumber(String str) {
//        try {
//            double v = Integer.parseInt(str);
//            return true;
//        } catch (NumberFormatException nfe) {
//        }
//        return false;
//    }
//
//
//
//}
//







//package com.technorizen.healthcare.fragments;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.core.app.ActivityCompat;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.AutocompleteActivity;
//import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
//import com.google.android.material.datepicker.SingleDateSelector;
//import com.google.gson.Gson;
//import com.technorizen.healthcare.R;
//import com.technorizen.healthcare.activites.HomeActivity;
//import com.technorizen.healthcare.activites.LoginAct;
//import com.technorizen.healthcare.activites.SignupWithPostShiftsActivity;
//import com.technorizen.healthcare.activites.SignupWithWorkActivity;
//import com.technorizen.healthcare.adapters.SelectTimeAdapter;
//import com.technorizen.healthcare.adapters.ShowDateTimeAdapter;
//
//import com.technorizen.healthcare.databinding.FragmentPostShiftsBinding;
//import com.technorizen.healthcare.models.SuccessResAddAddress;
//import com.technorizen.healthcare.models.SuccessResGetAddress;
//import com.technorizen.healthcare.models.SuccessResGetJobPositions;
//import com.technorizen.healthcare.models.SuccessResSignIn;
//import com.technorizen.healthcare.retrofit.ApiClient;
//import com.technorizen.healthcare.retrofit.Constant;
//import com.technorizen.healthcare.retrofit.HealthInterface;
//import com.technorizen.healthcare.util.DataManager;
//import com.technorizen.healthcare.util.GPSTracker;
//import com.technorizen.healthcare.util.SharedPreferenceUtility;
//import com.technorizen.healthcare.util.StartTimeAndTimeInterface;
//import com.technorizen.healthcare.workerSide.WorkerHomeAct;
//import com.wisnu.datetimerangepickerandroid.CalendarPickerView;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static android.app.Activity.RESULT_CANCELED;
//import static android.app.Activity.RESULT_OK;
//import static com.technorizen.healthcare.activites.LoginAct.TAG;
//import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
//import static com.technorizen.healthcare.retrofit.Constant.isValidEmail;
//import static com.technorizen.healthcare.retrofit.Constant.showToast;
//import static com.technorizen.healthcare.util.DataManager.subArray;
//import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.MULTIPLE;
//import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.RANGE;
//import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link PostShiftsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class PostShiftsFragment extends Fragment implements StartTimeAndTimeInterface {
//
//    GPSTracker gpsTracker;
//    String strLat = "", strLng = "",categoryId = "",strAddress="";
//
//    private boolean fromRehire = false;
//
//    private String totalWorked = "";
//
//    private HealthInterface apiInterface;
//    private List<SuccessResGetJobPositions.Result> jobPositionsList = new LinkedList<>();
//    private List<SuccessResGetAddress.Result> addressList = new LinkedList<>();
//
//    private Dialog dialog;
//
//    private HashMap<String,String> unpaidBreakMap = new HashMap<>();
//    private HashMap<String,String> transitMap = new HashMap<>();
//
//    private ArrayAdapter<String> stateArrayAdapter;
//
//    FragmentPostShiftsBinding binding;
//
//    List<String> dates = new LinkedList<>();
//
//    ArrayList<String> selectedDates = new ArrayList<>();
//
//    private String singleSelectedDate = "";
//
//    private Map<String,String> startTime = new HashMap<>();
//
//    private Map<String,String> endTime = new HashMap<>();
//
//    private Map<String,String> startTimeText = new HashMap<>();
//
//    private Map<String,String> endTimeText = new HashMap<>();
//
//    private SelectTimeAdapter adapter;
//
//    private String selectedDatesWithSameTime= "";
//
//    private List<com.technorizen.healthcare.models.Date> mySelectedDates;
//
//    boolean visible = false;
//
//    String strJobPosition = "",strNoOfVancancies = "",strHourlyRate = "",strDuty = "",strCovid = "",
//
//    strUnpaid = "", strTransit = "",strShiftsLocation = "",strDayType = "",strShiftsNote = "",
//    strShiftsDate = "",strStartTime = "",strEndTIme = "",strNewShiftDate="",strNewShiftStartTime="",strNewShiftSEndTime="";
//
//    private String timeSelection = "Single";
//
//    private boolean multipleSelection = false;
//
//    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
//
//    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//    "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//            "11:15 AM","11:30 AM","11:45 AM",
//            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//            "11:15 PM","11:30 PM","11:45 PM"
//    };
//
//    String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//            "11:15 AM","11:30 AM","11:45 AM",
//            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//            "11:15 PM","11:30 PM","11:45 PM"
//    };
//
//    String[] vaccine = new String[100];
//    ArrayList<String> hrrate = new ArrayList<>();
//    String[] duty = {"Floor Duty","One on One"};
//    String[] status = {"Negative","Positive"};
//    String[] entry = {"Single Day", "Multi Day"};
//    String[] day = {"17 july 2021"};
//    String[] unpaid = {"None","30 Minutes","45 Minutes","60 Minutes","120 Minutes"};
//
//    String[] unpaid1 = {"None","30","45","60","120"};
//
//    private String workerName = "",workerId = "";
//
//    String[] transit = {"None","1 Hour","2 Hour","3 Hour","4 Hour"};
//
//    ArrayAdapter ad;
//
//    private String workerDesignation = "";
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public PostShiftsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PostShiftsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static PostShiftsFragment newInstance(String param1, String param2) {
//        PostShiftsFragment fragment = new PostShiftsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_shifts, container, false);
//
//        Bundle myBundle = new Bundle();
//
//        myBundle = this.getArguments();
//
//        if(myBundle!=null)
//        {
//            workerName = myBundle.getString("name");
//            workerId = myBundle.getString("id");
//            workerDesignation = myBundle.getString("designation");
//            binding.btnRecruitment.setVisibility(View.GONE);
//            binding.btnRehireWorker.setVisibility(View.VISIBLE);
//            binding.btnPost.setVisibility(View.GONE);
//            binding.btnRehireWorker.setText(getResources().getString(R.string.post_shift_to)+" ( "+workerName+" )");
//
//            fromRehire = true;
//
//            binding.spinnerVaccine.setClickable(false);
//
//            binding.spinnerVaccine.setEnabled(false);
//
//            binding.tvShift.setText(getString(R.string.post_rehire_shifts));
//
//        }
//
//        Places.initialize(getActivity().getApplicationContext(), "AIzaSyA1zVQsDeyYQJbE64CmQVSfzNO-AwFoUNk");
//
//        // Create a new PlacesClient instance
//        PlacesClient placesClient = Places.createClient(getActivity());
//
//        binding.nestedScrollView.setNestedScrollingEnabled(true);
//
//        gpsTracker = new GPSTracker(getActivity());
//
//        getLocation();
//
//        unpaidBreakMap.put("None","None");
//        unpaidBreakMap.put("30 Minutes","30");
//        unpaidBreakMap.put("45 Minutes","45");
//        unpaidBreakMap.put("60 Minutes","60");
//        unpaidBreakMap.put("120 Minutes","120");
//
//        transitMap.put("None","None");
//        transitMap.put("1 Hour","1");
//        transitMap.put("2 Hour","2");
//        transitMap.put("3 Hour","3");
//        transitMap.put("4 Hour","4");
//
//        apiInterface = ApiClient.getClient().create(HealthInterface.class);
//
//        adapter = new SelectTimeAdapter(selectedDates,getActivity(),this);
//
//        binding.btnPost.setOnClickListener(v ->
//                {
//
//                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());
//                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();
//                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();
//                    strHourlyRate = strHourlyRate.substring(1);
//                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
//                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
//                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
//                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
//                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
//                    strDayType = binding.spinnerEnter.getSelectedItem().toString();
//                    strShiftsNote = binding.etShiftsNote.getText().toString();
//                    if(multipleSelection)
//                    {
//                        if(binding.checkboxMultipledateSelect.isChecked())
//                        {
//                            strStartTime = setCommaForStartDate();
//                            strEndTIme = setCommaForEndDate();
//                        } else
//                        {
//                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//
//                            strEndTIme = "";
//                            if(binding.spinnerEndTime.getSelectedItem()!=null)
//                            {
//                                strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                            }
//                        }
//                    }
//                    else
//                    {
////                      sasdfpj
//                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                        strEndTIme = "";
//                        if(binding.spinnerEndTime.getSelectedItem()!=null)
//                        {
//                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                        }
//
//                    }
//                      if (isValid())
//                    {
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("JobPosition",strJobPosition);
//                        bundle.putString("NoOfVancancies",strNoOfVancancies);
//                        bundle.putString("HourlyRate",strHourlyRate);
//                        bundle.putString("Duty",strDuty);
//                        bundle.putString("Covid",strCovid);
//                        bundle.putString("Unpaid",strUnpaid);
//                        bundle.putString("Transit",strTransit);
//                        bundle.putString("ShiftsLocation",strShiftsLocation);
//                        bundle.putString("DayType",strDayType);
//                        bundle.putString("ShiftsNote",strShiftsNote);
//                        bundle.putString("ShiftsDate",strShiftsDate);
//                        bundle.putString("StartTime",strStartTime);
//                        bundle.putString("EndTIme",strEndTIme);
//
//              //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
//
//                        fullScreenDialog("Directshift");
//
//                    }
//
//                }
//                );
//
//        binding.btnRecruitment.setOnClickListener(v ->
//                {
//
//                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());
//
//                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();
//
//                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();
//
//                    strHourlyRate = strHourlyRate.substring(1);
//
//                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
//                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
//                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
//                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
//                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
//                    strDayType = binding.spinnerEnter.getSelectedItem().toString();
//                    strShiftsNote = binding.etShiftsNote.getText().toString();
//
//                    if(multipleSelection)
//                    {
//
//                        if(binding.checkboxMultipledateSelect.isChecked())
//                        {
//                            strStartTime = setCommaForStartDate();
//                            strEndTIme = setCommaForEndDate();
//                        } else
//                        {
//                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                            strEndTIme = "";
//                            if(binding.spinnerEndTime.getSelectedItem()!=null)
//                            {
//                                strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                            }
//                        }
//                    }
//                    else
//                    {
//                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                        strEndTIme = "";
//                        if(binding.spinnerEndTime.getSelectedItem()!=null)
//                        {
//                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                        }
//                    }
//                    if (isValid())
//                    {
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("JobPosition",strJobPosition);
//                        bundle.putString("NoOfVancancies",strNoOfVancancies);
//                        bundle.putString("HourlyRate",strHourlyRate);
//                        bundle.putString("Duty",strDuty);
//                        bundle.putString("Covid",strCovid);
//                        bundle.putString("Unpaid",strUnpaid);
//                        bundle.putString("Transit",strTransit);
//                        bundle.putString("ShiftsLocation",strShiftsLocation);
//                        bundle.putString("DayType",strDayType);
//                        bundle.putString("ShiftsNote",strShiftsNote);
//                        bundle.putString("ShiftsDate",strShiftsDate);
//                        bundle.putString("StartTime",strStartTime);
//                        bundle.putString("EndTIme",strEndTIme);
//
//                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
//
//                        fullScreenDialog("Recruitmentshift");
//
//                    }
//
//                }
//        );
//
//
//        binding.btnRehireWorker.setOnClickListener(v ->
//                {
//
//                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());
//
//                    strNoOfVancancies = binding.spinnerVaccine.getSelectedItem().toString();
//
//                    strHourlyRate = binding.spinnerRate.getSelectedItem().toString();
//
//                    strHourlyRate = strHourlyRate.substring(1);
//
//                    strDuty = binding.spinnerDuty.getSelectedItem().toString();
//                    strCovid = binding.spinnerStatus.getSelectedItem().toString();
//                    strUnpaid = binding.spinnerUnpaidBreak.getSelectedItem().toString();
//                    strTransit = binding.spinnerTransit.getSelectedItem().toString();
//                    strShiftsLocation = getSelectedAddressCode(binding.spinnerAddressEvent.getSelectedItem().toString());
//                    strDayType = binding.spinnerEnter.getSelectedItem().toString();
//
//                    strShiftsNote = binding.etShiftsNote.getText().toString();
//
//                    if(multipleSelection)
//                    {
//
//                        if(binding.checkboxMultipledateSelect.isChecked())
//                        {
//
//                            strStartTime = setCommaForStartDate();
//                            strEndTIme = setCommaForEndDate();
//
//                        } else
//                        {
//
//                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                            strEndTIme = "";
//                            if(binding.spinnerEndTime.getSelectedItem()!=null)
//                            {
//                                strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                            }
//                        }
//
//                    }
//                    else
//                    {
////                        sasdfpj
//
//                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//
//                        strEndTIme = "";
//                        if(binding.spinnerEndTime.getSelectedItem()!=null)
//                        {
//                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                        }
//                    }
//                    if (isValid())
//                    {
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("JobPosition",strJobPosition);
//                        bundle.putString("NoOfVancancies",strNoOfVancancies);
//                        bundle.putString("HourlyRate",strHourlyRate);
//                        bundle.putString("Duty",strDuty);
//                        bundle.putString("Covid",strCovid);
//                        bundle.putString("Unpaid",strUnpaid);
//                        bundle.putString("Transit",strTransit);
//                        bundle.putString("ShiftsLocation",strShiftsLocation);
//                        bundle.putString("DayType",strDayType);
//                        bundle.putString("ShiftsNote",strShiftsNote);
//                        bundle.putString("ShiftsDate",strShiftsDate);
//                        bundle.putString("StartTime",strStartTime);
//                        bundle.putString("EndTIme",strEndTIme);
//
//                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
//
//                        fullScreenDialog("Rehire");
//
//                    }
//
//                }
//        );
//
//
//        binding.tvAddAddress.setOnClickListener(v ->
//                {
//
//                    // return after the user has made a selection.
//                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
//
//                    // Start the autocomplete intent.
//                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                            .build(getActivity());
//                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//
//                }
//        );
//
//        int i=0;
//        while (i<100)
//        {
//            vaccine[i] = (i+1)+"";
//            i++;
//        }
//
//        int z=17;
//        int m=0;
//        while (z<=150)
//        {
//            hrrate.add("$"+z+"");
//            z++;
//            m++;
//        }
//
//        setSpinner();
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                start);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerStartTime.setAdapter(ad);
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                end);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerEndTime.setAdapter(ad);
//
//        dates.clear();
////Date Selection Android
//        Calendar nextYear = Calendar.getInstance();
//        nextYear.add(Calendar.YEAR, 1);
//
//        Date today1 = new Date();
//        binding.calendarSingleDate.init(today1, nextYear.getTime())
//                .withSelectedDate(today1).inMode(SINGLE);
//
//        dates.add("12/4/2021");
//        Date date = Calendar.getInstance().getTime();
//        String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//        DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//        Date today = Calendar.getInstance().getTime();
//
//        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//
//        String compareDate = dateFormat.format(date);
//
//        Log.d(TAG, "onCreateView: "+compareDate.toUpperCase()+" Current Position :"+getTodayTimePosition(compareDate.toUpperCase()));
//
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//        String todayAsString = df.format(date);
//
//        binding.tvSingleDate.setText(todayAsString);
//
//        strShiftsDate = todayAsString;
//
//        binding.calendarSingleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//             @Override
//             public void onDateSelected(Date date) {
//
//                 String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                 DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//                 Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                 String todayAsString = df.format(date);
//
//                 strShiftsDate = todayAsString;
//
//                 binding.tvSingleDate.setText(todayAsString);
//             }
//
//             @Override
//             public void onDateUnselected(Date date) {
//
//             }
//         });
//
//        binding.calendarMultipleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Date date) {
//
//                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                DateFormat df = new SimpleDateFormat(pattern);
//// Get the today date using Calendar object.
//                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                selectedDates.add(todayAsString);
//
//                startTime.put(todayAsString,"12:00 AM");
//
//                endTime.put(todayAsString,"12:00 AM");
//
//                adapter.notifyDataSetChanged();
//
//                setTextForMultipleDate();
//
//            }
//
//            @Override
//            public void onDateUnselected(Date date) {
//                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                selectedDates.remove(todayAsString);
//                startTime.remove(todayAsString);
//                endTime.remove(todayAsString);
//                adapter.notifyDataSetChanged();
//                setTextForMultipleDate();
//
//            }
//        });
//
//        binding.rvDate.setLayoutManager(new LinearLayoutManager(getActivity()));
//        binding.rvDate.setHasFixedSize(true);
//        binding.rvDate.setAdapter(adapter);
//
//        if(jobPositionsList.size()==0)
//        {
//            getJobPositions();
//        }
//
//        if(addressList.size()==0)
//        {
//            getAddress();
//        }
//
//        return binding.getRoot();
//    }
//
//    private void setSpinner() {
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                vaccine);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerVaccine.setAdapter(ad);
//
//        binding.spinnerVaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                hrrate);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerRate.setAdapter(ad);
//
//        binding.spinnerRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                duty);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerDuty.setAdapter(ad);
//
//        binding.spinnerDuty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                status);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerStatus.setAdapter(ad);
//
//        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                entry);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerEnter.setAdapter(ad);
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                unpaid);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerUnpaidBreak.setAdapter(ad);
//
//        binding.spinnerUnpaidBreak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                transit);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//
//        binding.spinnerTransit.setAdapter(ad);
//
//        binding.spinnerTransit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        binding.spinnerEnter.setOnItemSelectedListener(date_selection_listener);
//
//        binding.spinnerStartTime.setOnItemSelectedListener(startTimeSelectListener);
//    }
//    CalendarPickerView calendar;
//
//    private AdapterView.OnItemSelectedListener startTimeSelectListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            int subEnd = start.length-1;
//            int myPosition = position + 1;
//            String[] subarray = subArray(end, myPosition, subEnd);
//            ad = new ArrayAdapter(
//                    getActivity(),
//                    android.R.layout.simple_spinner_item,
//                    subarray);
//
//            ad.setDropDownViewResource(
//                    android.R.layout
//                            .simple_spinner_dropdown_item);
//
//            binding.spinnerEndTime.setAdapter(ad);
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };
//
//    private AdapterView.OnItemSelectedListener date_selection_listener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position == 0)
//                {
//
//                    multipleSelection = false;
//                    binding.calendarSingleDate.setVisibility(View.VISIBLE);
//                    binding.calendarMultipleDate.setVisibility(View.GONE);
//                    selectedDates.clear();
//                    startTime.clear();
//                    endTime.clear();
//
//                    Calendar nextYear = Calendar.getInstance();
//                    nextYear.add(Calendar.YEAR, 1);
//
//                    Date today = new Date();
//                    binding.calendarSingleDate.init(today, nextYear.getTime())
//                            .withSelectedDate(today).inMode(SINGLE);
//
//                    binding.rvDate.setVisibility(View.GONE);
//                    binding.llSingleDate.setVisibility(View.VISIBLE);
//                    binding.checkboxMultipledateSelect.setVisibility(View.GONE);
//
//                    Date date = Calendar.getInstance().getTime();
//                    String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//
//                    DateFormat df = new SimpleDateFormat(pattern);
//// Get the today date using Calendar object.
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                    String todayAsString = df.format(date);
//
//                    binding.tvSingleDate.setText(todayAsString);
//
//                    strShiftsDate = todayAsString;
//
//                    selectedDates.add(todayAsString);
//
//                    startTime.put(todayAsString,"12:00 AM");
//                    endTime.put(todayAsString,"12:00 AM");
//
//                } else
//
//                {
//                    multipleSelection = true;
//                    binding.calendarSingleDate.setVisibility(View.GONE);
//                    binding.calendarMultipleDate.setVisibility(View.VISIBLE);
//                    selectedDates.clear();
//
//                    Date date = Calendar.getInstance().getTime();
//                    String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                    DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//                    Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                    String todayAsString = df.format(date);
//
//                    selectedDates.add(todayAsString);
//                    startTime.put(todayAsString,"12:00 AM");
//                    endTime.put(todayAsString,"12:00 AM");
//
//                    binding.checkboxMultipledateSelect.setVisibility(View.VISIBLE);
//
//                    Calendar nextYear = Calendar.getInstance();
//                    nextYear.add(Calendar.YEAR, 1);
//
//                    binding.checkboxMultipledateSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            if(isChecked)
//                            {
//
//                                binding.rvDate.setVisibility(View.VISIBLE);
//                                binding.llSingleDate.setVisibility(View.GONE);
//
//                                Date date = Calendar.getInstance().getTime();
//                                String pattern = "MM/dd/yyyy";
//
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                                DateFormat df = new SimpleDateFormat(pattern);
//
//// Get the today date using Calendar object.
//                                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                                String todayAsString = df.format(date);
//
//                             adapter.notifyDataSetChanged();
//                            }
//                            else
//                            {
//
//                                binding.rvDate.setVisibility(View.GONE);
//                                binding.llSingleDate.setVisibility(View.VISIBLE);
//
//                            }
//                        }
//                    });
//
//                    Date today1 = new Date();
//                    binding.calendarMultipleDate.init(today1, nextYear.getTime())
//                            .withSelectedDate(today1).inMode(MULTIPLE);
//                }
//
//              /*
//                final Country country = (Country) binding.spinnerCountry.getItemAtPosition(position);
//                Log.d("SpinnerCountry", "onItemSelected: country: "+country.getCountryID());
//                ArrayList<State> tempStates = new ArrayList<>();
//
//                tempStates.add(new State(0, new Country(0, "Choose a Country"), "Choose a State"));
//
//                for (State singleState : states) {
//                    if (singleState.getCountry().getCountryID() == country.getCountryID()) {
//                        tempStates.add(singleState);
//                    }
//                }
//                stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempStates);
//                stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                binding.spinnerState.setAdapter(stateArrayAdapter);
//    */
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };
//
//    private void setTextForMultipleDate()
//    {
//        String text = "";
//        for (String date:selectedDates)
//        {
//            text = text+date+",";
//        }
//        if (text.endsWith(",")) {
//            text = text.substring(0, text.length() - 1);
//        }
//        strShiftsDate = text;
//        binding.tvSingleDate.setText(text);
//    }
//
//    private String setCommaForStartDate()
//    {
//        String myStrStartTime = "";
//        if(startTime.size()==0)
//        {
//            return myStrStartTime;
//        }
//
//        for (String date:selectedDates)
//        {
//            myStrStartTime = myStrStartTime +startTime.get(date)+",";
//        }
//
//        if (strStartTime.endsWith(","))
//        {
//            myStrStartTime = strStartTime.substring(0, strStartTime.length() - 1);
//        }
//        return myStrStartTime;
//    }
//
//    private String setCommaForEndDate()
//    {
//
//        String myStrEndTime = "";
//
//        if(endTime.size()==0)
//        {
//            return myStrEndTime;
//        }
//
//        for (String date:selectedDates)
//        {
//
//            if(endTime.get(date).equalsIgnoreCase(""))
//            {
//                Toast.makeText(getActivity(),"Please select end date.",Toast.LENGTH_SHORT).show();
//                return myStrEndTime;
//            }
//
//            myStrEndTime = myStrEndTime +endTime.get(date)+",";
//        }
//
//        if (myStrEndTime.endsWith(","))
//        {
//            myStrEndTime = myStrEndTime.substring(0, myStrEndTime.length() - 1);
//        }
//        return myStrEndTime;
//    }
//
//    @Override
//    public void startTime(String key,String startTime1) {
//        startTime.put(key,startTime1);
//    }
//
//    @Override
//    public void endTime(String key,String startTime1) {
//        endTime.put(key,startTime1);
//    }
//
//  private void addPost(String fromWhere) throws ParseException {
//
//      if(strDayType.equalsIgnoreCase("Single Day"))
//      {
//          strDayType = "Single";
//          timeSelection = "Single";
//      }
//      else
//      {
//          strDayType = "Multi";
//      }
//
//      setNewTimeDate();
//      String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//      String myBreak = unpaidBreakMap.get(strUnpaid);
//      String myTransit = transitMap.get(strTransit);
//
//      DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//      Map<String,String> map = new HashMap<>();
//      map.put("user_id",userId);
//      map.put("job_position",strJobPosition);
//      map.put("no_vacancies",strNoOfVancancies);
//      map.put("hourly_rate",strHourlyRate);
//      map.put("duty_of_worker",strDuty);
//      map.put("covid_status",strCovid);
//      map.put("unpaid_break",myBreak);
//      map.put("transit_allowance",myTransit);
//      map.put("shift_location",strShiftsLocation);
//      map.put("day_type",strDayType);
//      map.put("shift_notes",strShiftsNote);
//      map.put("shift_date",strShiftsDate);
//      map.put("start_time",strStartTime);
//      map.put("end_time",strEndTIme);
//      map.put("time_type",timeSelection);
//      map.put("shift_date_new",strNewShiftDate);
//      map.put("start_time_new",strNewShiftStartTime);
//      map.put("end_time_new",strNewShiftSEndTime);
//      map.put("type",fromWhere);
//      map.put("total_worked",totalWorked);
//
//      Call<SuccessResSignIn> call = apiInterface.directPost(map);
//
//      call.enqueue(new Callback<SuccessResSignIn>() {
//          @Override
//          public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {
//
//              DataManager.getInstance().hideProgressMessage();
//              try {
//                  SuccessResSignIn data = response.body();
//                  Log.e("data",data.status);
//                  if (data.status.equals("1")) {
//                      String dataResponse = new Gson().toJson(response.body());
//                      Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//
//                      startActivity(new Intent(getActivity(),HomeActivity.class));
//                      getActivity().finish();
//
//                  } else if (data.status.equals("0")) {
//                      showToast(getActivity(), data.message);
//                  }
//
//              } catch (Exception e) {
//                  e.printStackTrace();
//              }
//          }
//
//          @Override
//          public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
//              call.cancel();
//              DataManager.getInstance().hideProgressMessage();
//          }
//      });
//
//  }
//
//    private void rehireWorker() throws ParseException {
//
//        if(strDayType.equalsIgnoreCase("Single Day"))
//        {
//            strDayType = "Single";
//            timeSelection = "Single";
//        }
//        else
//        {
//            strDayType = "Multi";
//        }
//
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        String myBreak = unpaidBreakMap.get(strUnpaid);
//        String myTransit = transitMap.get(strTransit);
//
//        setNewTimeDate();
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String,String> map = new HashMap<>();
//        map.put("user_id",userId);
//        map.put("worker_id",workerId);
//        map.put("job_position",strJobPosition);
//        map.put("no_vacancies",strNoOfVancancies);
//        map.put("hourly_rate",strHourlyRate);
//        map.put("duty_of_worker",strDuty);
//        map.put("covid_status",strCovid);
//        map.put("unpaid_break",myBreak);
//        map.put("transit_allowance",myTransit);
//        map.put("shift_location",strShiftsLocation);
//        map.put("day_type",strDayType);
//        map.put("shift_notes",strShiftsNote);
//        map.put("shift_date",strShiftsDate);
//        map.put("start_time",strStartTime);
//        map.put("end_time",strEndTIme);
//        map.put("time_type",timeSelection);
//        map.put("shift_date_new",strNewShiftDate);
//        map.put("start_time_new",strNewShiftStartTime);
//        map.put("end_time_new",strNewShiftSEndTime);
//        map.put("type","Rehire");
//        map.put("total_worked",totalWorked);
//
//        Call<SuccessResSignIn> call = apiInterface.rehireWorker(map);
//
//        call.enqueue(new Callback<SuccessResSignIn>() {
//            @Override
//            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//                try {
//                    SuccessResSignIn data = response.body();
//                    Log.e("data",data.status);
//                    if (data.status.equals("1")) {
//                        String dataResponse = new Gson().toJson(response.body());
//                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
//
//                        startActivity(new Intent(getActivity(),HomeActivity.class));
//                        getActivity().finish();
//
//                    } else if (data.status.equals("0")) {
//                        showToast(getActivity(), data.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//            }
//        });
//    }
//
//    private void fullScreenDialog(String fromWhere) {
//
//        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
//
//        dialog.setContentView(R.layout.fragment_confirm_post_shifts);
//
//        TextView tvHeader = dialog.findViewById(R.id.tvHeader);
//
//        ImageView ivBack = dialog.findViewById(R.id.ivBack);
//
//        TextView singleDate,singleStartDate,singleEndDate;
//
//        LinearLayout llSingleDate;
//
//        RecyclerView rvMultipleDates;
//
//        if(fromWhere.equalsIgnoreCase("Directshift"))
//        {
//            tvHeader.setText(getResources().getString(R.string.confirm_direct_post));
//        }else
//        {
//
//            if(fromRehire)
//            {
//                tvHeader.setText(getResources().getString(R.string.confirm_rehire_post));
//            }
//            else
//            {
//                tvHeader.setText(getResources().getString(R.string.confirm_recruitment_post));
//            }
//
//
//        }
//
//        ivBack.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//
//        singleDate = dialog.findViewById(R.id.tvDate);
//        singleStartDate = dialog.findViewById(R.id.tvStartTime);
//        singleEndDate = dialog.findViewById(R.id.tvEndTime);
//
//        AppCompatButton btnPost =  dialog.findViewById(R.id.btnPostShift);
//        AppCompatButton btnEdit =  dialog.findViewById(R.id.btnEdit);
//        TextView tvjobPosition,tvVacancies,tvHourlyRate,tvDuty,tvCovid,tvUnpaid,tvTransit,tvShiftsLocation,tvDayType,tvShifstNotes;
//        tvjobPosition = dialog.findViewById(R.id.tvJobPosition);
//        tvVacancies = dialog.findViewById(R.id.tvVacancies);
//        tvHourlyRate = dialog.findViewById(R.id.tvHrlRate);
//        tvDuty = dialog.findViewById(R.id.tvDuty);
//        tvCovid = dialog.findViewById(R.id.tvCovid);
//        tvUnpaid = dialog.findViewById(R.id.tvUnpaid);
//        tvTransit = dialog.findViewById(R.id.tvTransit);
//        tvShiftsLocation = dialog.findViewById(R.id.tvLocation);
//        tvDayType = dialog.findViewById(R.id.tvDayType);
//        tvShifstNotes = dialog.findViewById(R.id.tvShiftsNotes);
//        llSingleDate = dialog.findViewById(R.id.llSingleDate);
//        rvMultipleDates = dialog.findViewById(R.id.rvDate);
//
//        String jobPosition = binding.spinnerJobPosition.getSelectedItem().toString();
//
//        tvjobPosition.setText(jobPosition);
//        tvVacancies.setText(strNoOfVancancies);
//        tvHourlyRate.setText("$ "+strHourlyRate);
//        tvDuty.setText(strDuty);
//        tvCovid.setText(strCovid);
//        tvUnpaid.setText(strUnpaid);
//        tvTransit.setText(strTransit);
//        tvShiftsLocation.setText(binding.spinnerAddressEvent.getSelectedItem().toString());
//        tvDayType.setText(strDayType);
//        tvShifstNotes.setText(strShiftsNote);
//
//        if(multipleSelection)
//        {
//
//            if (binding.checkboxMultipledateSelect.isChecked())
//            {
//                llSingleDate.setVisibility(View.GONE);
//                rvMultipleDates.setVisibility(View.VISIBLE);
//
//                List<String> dates = new LinkedList<>();
//                List<String> startTimeList = new LinkedList<>();
//                List<String> endTimeList = new LinkedList<>();
//
//                dates.addAll(selectedDates);
//
//                for (String date:selectedDates)
//                {
//                    startTimeList.add(startTime.get(date));
//                    endTimeList.add(endTime.get(date));
//                }
//
//                rvMultipleDates.setHasFixedSize(true);
//                rvMultipleDates.setLayoutManager(new LinearLayoutManager(getActivity()));
//                rvMultipleDates.setAdapter(new ShowDateTimeAdapter(getActivity(),dates,startTimeList,endTimeList));
//            }
//            else
//            {
//
//                llSingleDate.setVisibility(View.VISIBLE);
//                rvMultipleDates.setVisibility(View.GONE);
//                singleDate.setText(strShiftsDate);
//                singleStartDate.setText(strStartTime);
//                singleEndDate.setText(strEndTIme);
//
//                strStartTime = "";
//
//                for (String date:selectedDates)
//                {
//                    strStartTime = strStartTime + binding.spinnerStartTime.getSelectedItem().toString()+",";
//                }
//
//                if (strStartTime.endsWith(","))
//                {
//                    strStartTime = strStartTime.substring(0, strStartTime.length() - 1);
//                }
//
//                strEndTIme = "";
//
//                for (String date:selectedDates)
//                {
//                    strEndTIme = strEndTIme + binding.spinnerEndTime.getSelectedItem().toString()+",";
//                }
//
//                if (strEndTIme.endsWith(","))
//                {
//                    strEndTIme = strEndTIme.substring(0, strEndTIme.length() - 1);
//                }
//
//            }
//        }
//        else
//        {
//
//            llSingleDate.setVisibility(View.VISIBLE);
//            rvMultipleDates.setVisibility(View.GONE);
//            singleDate.setText(strShiftsDate);
//            singleStartDate.setText(strStartTime);
//            singleEndDate.setText(strEndTIme);
//
//        }
//
//        btnEdit.setOnClickListener(v ->
//                {
//                    dialog.dismiss();
//                }
//                );
//
//        btnPost.setOnClickListener(v ->
//                {
//
//                    if(binding.checkboxMultipledateSelect.isChecked())
//                    {
//                        timeSelection = "Multi";
//                    }
//                    else
//                    {
//                        timeSelection = "Single";
//                    }
//
//                    if(fromWhere.equalsIgnoreCase("Rehire"))
//                    {
//
//                        try {
//                            rehireWorker();
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    else
//                    {
//                        try {
//                            addPost(fromWhere);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//                );
//
//        dialog.show();
//
//    }
//
//  /*  private void fullScreenDialogDate() {
//
//        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
//
//        dialog.setContentView(R.layout.fragment_confirm_post_shifts);
//
//        ImageView ivBack = dialog.findViewById(R.id.ivBack);
//
//        TextView singleDate,singleStartDate,singleEndDate;
//
//        LinearLayout llSingleDate;
//
//        RecyclerView rvMultipleDates;
//
//        ivBack.setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//
//        singleDate = dialog.findViewById(R.id.tvDate);
//        singleStartDate = dialog.findViewById(R.id.tvStartTime);
//        singleEndDate = dialog.findViewById(R.id.tvEndTime);
//
//        AppCompatButton btnPost =  dialog.findViewById(R.id.btnPostShift);
//        AppCompatButton btnEdit =  dialog.findViewById(R.id.btnEdit);
//        TextView tvjobPosition,tvVacancies,tvHourlyRate,tvDuty,tvCovid,tvUnpaid,tvTransit,tvShiftsLocation,tvDayType,tvShifstNotes;
//        tvjobPosition = dialog.findViewById(R.id.tvJobPosition);
//        tvVacancies = dialog.findViewById(R.id.tvVacancies);
//        tvHourlyRate = dialog.findViewById(R.id.tvHrlRate);
//        tvDuty = dialog.findViewById(R.id.tvDuty);
//        tvCovid = dialog.findViewById(R.id.tvCovid);
//        tvUnpaid = dialog.findViewById(R.id.tvUnpaid);
//        tvTransit = dialog.findViewById(R.id.tvTransit);
//        tvShiftsLocation = dialog.findViewById(R.id.tvLocation);
//        tvDayType = dialog.findViewById(R.id.tvDayType);
//        tvShifstNotes = dialog.findViewById(R.id.tvShiftsNotes);
//        llSingleDate = dialog.findViewById(R.id.llSingleDate);
//        rvMultipleDates = dialog.findViewById(R.id.rvDate);
//
//        String jobPosition = binding.spinnerJobPosition.getSelectedItem().toString();
//
//        tvjobPosition.setText(jobPosition);
//        tvVacancies.setText(strNoOfVancancies);
//        tvHourlyRate.setText(strHourlyRate);
//        tvDuty.setText(strDuty);
//        tvCovid.setText(strCovid);
//        tvUnpaid.setText(strUnpaid);
//        tvTransit.setText(strTransit);
//        tvShiftsLocation.setText(binding.spinnerAddressEvent.getSelectedItem().toString());
//        tvDayType.setText(strDayType);
//        tvShifstNotes.setText(strShiftsNote);
//
//        if(multipleSelection)
//        {
//
//            if (binding.checkboxMultipledateSelect.isChecked())
//
//            {
//
//                llSingleDate.setVisibility(View.GONE);
//                rvMultipleDates.setVisibility(View.VISIBLE);
//
//                List<String> dates = new LinkedList<>();
//                List<String> startTimeList = new LinkedList<>();
//                List<String> endTimeList = new LinkedList<>();
//
//                dates.addAll(selectedDates);
//
//                for (String date:selectedDates)
//
//                {
//                    startTimeList.add(startTime.get(date));
//                    endTimeList.add(endTime.get(date));
//                }
//
//                rvMultipleDates.setHasFixedSize(true);
//                rvMultipleDates.setLayoutManager(new LinearLayoutManager(getActivity()));
//                rvMultipleDates.setAdapter(new ShowDateTimeAdapter(getActivity(),dates,startTimeList,endTimeList));
//
//            }
//            else
//            {
//
//                llSingleDate.setVisibility(View.VISIBLE);
//                rvMultipleDates.setVisibility(View.GONE);
//                singleDate.setText(strShiftsDate);
//                singleStartDate.setText(strStartTime);
//                singleEndDate.setText(strEndTIme);
//
//            }
//
//        }
//        else
//        {
//
//            llSingleDate.setVisibility(View.VISIBLE);
//            rvMultipleDates.setVisibility(View.GONE);
//
//            singleDate.setText(strShiftsDate);
//            singleStartDate.setText(strStartTime);
//            singleEndDate.setText(strEndTIme);
//        }
//
//        btnEdit.setOnClickListener(v ->
//                {
//                    dialog.dismiss();
//                }
//        );
//
//        btnPost.setOnClickListener(v ->
//                {
//                    addPost();
//                }
//        );
//        dialog.show();
//
//    }
//
//*/
//
//    public void getJobPositions() {
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//
//        Call<SuccessResGetJobPositions> call = apiInterface.getJobPositions(map);
//        call.enqueue(new Callback<SuccessResGetJobPositions>() {
//            @Override
//            public void onResponse(Call<SuccessResGetJobPositions> call, Response<SuccessResGetJobPositions> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResGetJobPositions data = response.body();
//                    if (data.status.equals("1")) {
//                        jobPositionsList.clear();
//                        jobPositionsList.addAll(data.getResult());
//                        setJobPositionSpinner();
//                    } else {
//                        showToast(getActivity(), data.message);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResGetJobPositions> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//    }
//
//    private void setJobPositionSpinner()
//    {
//        List<String> tempStates = new LinkedList<>();
//
//        tempStates.add(getString(R.string.select_job_position));
//
//        for (SuccessResGetJobPositions.Result state:jobPositionsList)
//        {
//            tempStates.add(state.getName());
//        }
//
//        stateArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tempStates);
//        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinnerJobPosition.setAdapter(stateArrayAdapter);
//        binding.spinnerJobPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        if(fromRehire)
//        {
//
//            binding.spinnerJobPosition.setSelection(getSpinnerJobPositionPosition(workerDesignation));
//            binding.spinnerJobPosition.setClickable(false);
//            binding.spinnerJobPosition.setEnabled(false);
//        }
//
//    }
//
//    public int getSpinnerJobPositionPosition(String code)
//    {
//        int i=1;
//        for (SuccessResGetJobPositions.Result state:jobPositionsList)
//        {
//            if(state.getName().equalsIgnoreCase(code))
//            {
//                return i;
//            }
//
//            i++;
//        }
//
//        return 0;
//    }
//
//
//    public String getSelectedJobPositionCode(String strCountryName) {
//        for (SuccessResGetJobPositions.Result job : jobPositionsList) {
//            if (job.getName().equalsIgnoreCase(strCountryName)) {
//                return job.getId();
//            }
//        }
//        return "";
//    }
//
//
//    public void getAddress() {
//
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id",userId);
//
//        Call<SuccessResGetAddress> call = apiInterface.getAddress(map);
//        call.enqueue(new Callback<SuccessResGetAddress>() {
//            @Override
//            public void onResponse(Call<SuccessResGetAddress> call, Response<SuccessResGetAddress> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResGetAddress data = response.body();
//                    if (data.status.equals("1")) {
//
//                        addressList.clear();
//
//                        addressList.addAll(data.getResult());
//                        setAddressSpinner();
//
//                    } else {
//                        showToast(getActivity(), data.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResGetAddress> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//    }
//
//    private void setAddressSpinner()
//    {
//        List<String> tempStates = new LinkedList<>();
//
//        tempStates.add(getString(R.string.select_shifts_location));
//
//        if(addressList.size()>0)
//        {
//            for (SuccessResGetAddress.Result state:addressList)
//            {
//                tempStates.add(state.getAddress());
//            }
//        }
//
//        stateArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tempStates);
//        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinnerAddressEvent.setAdapter(stateArrayAdapter);
//
//    }
//
//    public String getSelectedAddressCode(String address) {
//        for (SuccessResGetAddress.Result job : addressList) {
//            if (job.getAddress().equalsIgnoreCase(address)) {
//                return job.getId();
//            }
//        }
//        return "";
//    }
//
//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    Constant.LOCATION_REQUEST);
//        } else {
//            Log.e("Latittude====",gpsTracker.getLatitude()+"");
//            strLat = Double.toString(gpsTracker.getLatitude()) ;
//            strLng = Double.toString(gpsTracker.getLongitude()) ;
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case 1000: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                    strLat = Double.toString(gpsTracker.getLatitude()) ;
//                    strLng = Double.toString(gpsTracker.getLongitude()) ;
//
////
////                    if (isContinue) {
////                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                            // TODO: Consider calling
////                            //    ActivityCompat#requestPermissions
////                            // here to request the missing permissions, and then overriding
////                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                            //                                          int[] grantResults)
////                            // to handle the case where the user grants the permission. See the documentation
////                            // for ActivityCompat#requestPermissions for more details.
////                            return;
////                        }
////                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
////                    } else {
////                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
////
////                        strLat = Double.toString(gpsTracker.getLatitude()) ;
////                        strLng = Double.toString(gpsTracker.getLongitude()) ;
////
////                    }
//                } else {
//                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//
//        }
//    }
//
//    private boolean isValid() {
//        if (strJobPosition.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select job position.", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (strShiftsLocation.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select shift location.", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (strShiftsDate.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select shift dates.", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (strEndTIme.equalsIgnoreCase("")) {
//            Toast.makeText(getActivity(), "Please select End Time.", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (strShiftsNote.equalsIgnoreCase("")) {
//            binding.etShiftsNote.setError(getString(R.string.enter_shiftsNotes));
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//
//                strAddress = place.getAddress();
//
//                LatLng latLng = place.getLatLng();
//
//                Double latitude = latLng.latitude;
//
//                Double longitude = latLng.longitude;
//
//                strLat = Double.toString(latitude);
//
//                strLng = Double.toString(longitude);
//
//                String address = place.getAddress();
//
//                strAddress = address;
//
//                addAddress();
//
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i(TAG, status.getStatusMessage());
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public void addAddress()
//    {
//
////        if(strLat.contains("-"))
////        {
////            strLat = strLat.replace("-","");
////        }
////        if(strLng.contains("-"))
////        {
////            strLng = strLng.replace("-","");
////        }
//
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id",userId);
//        map.put("address",strAddress);
//        map.put("lat",strLat);
//        map.put("long",strLng);
//        map.put("city","");
//        map.put("state","");
//        map.put("country","");
//
//        Call<SuccessResAddAddress> call = apiInterface.addAddress(map);
//        call.enqueue(new Callback<SuccessResAddAddress>() {
//            @Override
//            public void onResponse(Call<SuccessResAddAddress> call, Response<SuccessResAddAddress> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//
//                try {
//
//                    SuccessResAddAddress data = response.body();
//                    if (data.status.equals("1")) {
//
//                        showToast(getActivity(), data.message);
//
//                    } else {
//                        showToast(getActivity(), data.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResAddAddress> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//    }
//
//    public int getTodayTimePosition(String time)
//    {
//        for (int i=0;i<start.length;i++)
//        {
//            if(time.equalsIgnoreCase(start[i]));
//            {
//                return i;
//            }
//        }
//        return 0;
//    }
//
//    public void setNewTimeDate() throws ParseException {
//
//        //strShiftsDate strStartTime strEndTIme
//
//        List<String> newShiftDate = Arrays.asList(strShiftsDate.split("\\s*,\\s*"));
//
//        List<String> newShiftStartTime = Arrays.asList(strStartTime.split("\\s*,\\s*"));
//
//        List<String> newShiftEndTime = Arrays.asList(strEndTIme.split("\\s*,\\s*"));
//
//        strNewShiftDate = "";
//        strNewShiftSEndTime ="";
//        strNewShiftStartTime = "";
//
//        int totalHous = 0;
//
//        long differenceINSec = 0;
//
//        for(int i=0;i<newShiftDate.size();i++)
//        {
//
//          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
//
//          Date  date1 = simpleDateFormat.parse(newShiftStartTime.get(i));
//          Date  date2 = simpleDateFormat.parse(newShiftEndTime.get(i));
//
//           long difference = date2.getTime() - date1.getTime();
//
//            long diff = TimeUnit.MILLISECONDS.toSeconds(difference);
//
//            differenceINSec = diff+differenceINSec;
//
////            int   days = (int) (difference / (1000*60*60*24));
////          int  hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
////          int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
////          hours = (hours < 0 ? -hours : hours);
////          Log.i("======= Hours"," :: "+hours);
////          totalHous = totalHous+ hours;
//        }
//
//        float totalHoursWorked;
//
//        double hours = differenceINSec / 3600;
//        double workedMinutes = (differenceINSec % 3600) / 60;
//        if(hours == 0)
//        {
//            totalHoursWorked = (float) workedMinutes / (float)60;
//            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
//        } else if(workedMinutes == 0)
//        {
//            totalHoursWorked = (float) hours;
//            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
//        }
//        else
//        {
//            totalHoursWorked = (float) (workedMinutes) / (float)60;
//            totalHoursWorked =(float) hours + totalHoursWorked;
//            Log.d(ContentValues.TAG, "onCreate: Hours "+ hours + " minutes :" +workedMinutes + "Total Worked :"+totalHoursWorked);
//        }
//        if(!isNumber(totalHoursWorked+""))
//        {
//            totalHoursWorked = Float.parseFloat(String.format("%.2f", totalHoursWorked));
//        }
//
//        totalWorked = totalHoursWorked+"";
//
//
//        Log.d(TAG, "setNewTimeDate: Total Hours"+totalHoursWorked);
//
//        for (String myDate:newShiftDate)
//        {
//            String date = convertDateFormate(myDate);
//            strNewShiftDate = strNewShiftDate + date+",";
//        }
//
//        for (String myStartTime:newShiftStartTime)
//        {
//            String startTime = timeCoversion12to24(myStartTime);
//            strNewShiftStartTime = strNewShiftStartTime + startTime+",";
//        }
//
//        for (String myStartTime:newShiftEndTime)
//        {
//            String startTime = timeCoversion12to24(myStartTime);
//            strNewShiftSEndTime = strNewShiftSEndTime + startTime+",";
//        }
//
//        if (strNewShiftDate.endsWith(","))
//        {
//            strNewShiftDate = strNewShiftDate.substring(0, strNewShiftDate.length() - 1);
//        }
//
//        if (strNewShiftStartTime.endsWith(","))
//        {
//            strNewShiftStartTime = strNewShiftStartTime.substring(0, strNewShiftStartTime.length() - 1);
//        }
//
//        if (strNewShiftSEndTime.endsWith(","))
//        {
//            strNewShiftSEndTime = strNewShiftSEndTime.substring(0, strNewShiftSEndTime.length() - 1);
//        }
//
//    }
//
//    public String convertDateFormate(String d1)
//    {
//        String convertedDate = "";
//
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//
//        String  dtDate1 = d1 +" 8:20";
//
//        Date  date1 = null;
//
//        try {
//            date1 = format.parse(dtDate1);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        String pattern = "yyyy-MM-dd";
//
//        DateFormat df = new SimpleDateFormat(pattern);
//
//        String todayAsString = df.format(date1);
//
//        return todayAsString;
//    }
//
//    public static String timeCoversion12to24(String twelveHoursTime) throws ParseException {
//
//        String time = twelveHoursTime;
//
//        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
//
//        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");
//
//        String newTime = date24Format.format(date12Format.parse(time));
//
//        return  newTime;
//
//    }
//
//
//    public static boolean isNumber(String str) {
//        try {
//            double v = Integer.parseInt(str);
//            return true;
//        } catch (NumberFormatException nfe) {
//        }
//        return false;
//    }
//
//
//
//}