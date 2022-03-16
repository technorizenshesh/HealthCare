


////////////Original/////////////////////////



package com.technorizen.healthcare.fragments;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.activites.HomeActivity;
import com.technorizen.healthcare.adapters.SelectEditTimeAdapter;
import com.technorizen.healthcare.adapters.SelectTimeAdapter;
import com.technorizen.healthcare.adapters.ShowDateTimeAdapter;
import com.technorizen.healthcare.databinding.FragmentPostShiftsBinding;
import com.technorizen.healthcare.models.SuccessResAcceptRejectRecruitment;
import com.technorizen.healthcare.models.SuccessResAddAddress;
import com.technorizen.healthcare.models.SuccessResEditShift;
import com.technorizen.healthcare.models.SuccessResGetAddress;
import com.technorizen.healthcare.models.SuccessResGetCurrentSchedule;
import com.technorizen.healthcare.models.SuccessResGetJobPositions;
import com.technorizen.healthcare.models.SuccessResSignIn;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.Constant;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.GPSTracker;
import com.technorizen.healthcare.util.SharedPreferenceUtility;
import com.technorizen.healthcare.util.StartTimeAndTimeInterface;
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
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.technorizen.healthcare.activites.LoginAct.TAG;
import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;
import static com.technorizen.healthcare.util.DataManager.subArray;
import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.MULTIPLE;
import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmEditRecruitmentShiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ConfirmEditRecruitmentShiftsFragment extends Fragment implements StartTimeAndTimeInterface {

    GPSTracker gpsTracker;
    String strLat = "", strLng = "",categoryId = "",strAddress="";
    private boolean goOnly = false;
    private String strShiftId,strWorkerId,strUserId,strStatus;
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

    private SelectEditTimeAdapter adapter;

    private List<com.technorizen.healthcare.models.Date> mySelectedDates;

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
    private String workerName = "",workerId = "";
    String[] transit = {"None","1 Hour","2 Hour","3 Hour","4 Hour"};
    ArrayAdapter ad;
    private List<Date> myAlreadySelectedDates = new LinkedList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmEditRecruitmentShiftsFragment() {
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
    public static ConfirmEditRecruitmentShiftsFragment newInstance(String param1, String param2) {
        ConfirmEditRecruitmentShiftsFragment fragment = new ConfirmEditRecruitmentShiftsFragment();
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
        binding.btnConfirm.setVisibility(View.VISIBLE);
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
        Bundle myBundle = new Bundle();
        myBundle = this.getArguments();
        if(myBundle!=null)
        {
            String result = myBundle.getString("result");
            SuccessResGetCurrentSchedule.Result result1 = new Gson().fromJson(result,SuccessResGetCurrentSchedule.Result.class);
            Log.e("sdffsdfds","REsult = " +result1.getShiftsdetail().get(0).getJobPosition());
            strShiftId = result1.getShiftId();
            strWorkerId = result1.getWorkerId();
            strUserId = result1.getUserId();
            strStatus = "Accepted";
            strJobPosition = result1.getShiftsdetail().get(0).getJobPosition();
            strNoOfVancancies = result1.getShiftsdetail().get(0).getNoVacancies();
            strHourlyRate = result1.getShiftsdetail().get(0).getHourlyRate();
            strHourlyRate = strHourlyRate;
            strDuty = result1.getShiftsdetail().get(0).getDutyOfWorker();
            strCovid = result1.getShiftsdetail().get(0).getCovidStatus();
            if(result1.getShiftsdetail().get(0).getUnpaidBreak().equalsIgnoreCase("None"))
            {
                strUnpaid = result1.getShiftsdetail().get(0).getUnpaidBreak();
            }
            else
            {
                strUnpaid = result1.getShiftsdetail().get(0).getUnpaidBreak()+" Minutes";
            }

            if(result1.getShiftsdetail().get(0).getTransitAllowance().equalsIgnoreCase("None"))
            {
                strTransit = result1.getShiftsdetail().get(0).getTransitAllowance();
            }
            else
            {
                strTransit = result1.getShiftsdetail().get(0).getTransitAllowance()+" Hour";
            }

            strShiftsLocation = result1.getShiftsdetail().get(0).getShiftLocation();

            strDayType = result1.getShiftsdetail().get(0).getDayType()+" Day";

            timeSelection = result1.getShiftsdetail().get(0).getTimeType();

            binding.etShiftsNote.setText(result1.getShiftsdetail().get(0).getShiftNotes());

            strShiftsNote = result1.getShiftsdetail().get(0).getShiftNotes();

            List<SuccessResGetCurrentSchedule.PostshiftTime> postshiftTimeList = new LinkedList<>();

            postshiftTimeList = result1.getPostshiftTime();

            for (SuccessResGetCurrentSchedule.PostshiftTime timeItem:postshiftTimeList)
            {
                startTime.put(timeItem.getShiftDate(),timeItem.getStartTime());
                endTime.put(timeItem.getShiftDate(),timeItem.getEndTime());
                selectedDates.add(timeItem.getShiftDate());
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date1 = null;
                try {
                    String dt = timeItem.getShiftDate()+" 8:20";
                    date1 = formatter.parse(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myAlreadySelectedDates.add(date1);
            }

            if(strDayType.equalsIgnoreCase("Single Day"))
            {

                binding.calendarSingleDate.setVisibility(View.VISIBLE);
                binding.calendarMultipleDate.setVisibility(View.GONE);

                String dt1 = result1.getPostshiftTime().get(0).getShiftDate()+" 8:20";

                strShiftsDate = result1.getPostshiftTime().get(0).getShiftDate();

                strStartTime = result1.getPostshiftTime().get(0).getStartTime();

                strEndTIme = result1.getPostshiftTime().get(0).getEndTime();

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

                Date date1 = null;

                try {

                   date1 = formatter.parse(dt1);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);

                Date datee = new Date();

                binding.calendarSingleDate.init(datee, nextYear.getTime())
                        .withSelectedDate(date1).inMode(SINGLE);
                binding.tvSingleDate.setText(strShiftsDate);

            }
            else
            {

                binding.calendarSingleDate.setVisibility(View.GONE);
                binding.calendarMultipleDate.setVisibility(View.VISIBLE);

                if(timeSelection.equalsIgnoreCase("Single"))
                {

                    binding.rvDate.setVisibility(View.GONE);
                    binding.llSingleDate.setVisibility(View.VISIBLE);
                    Date today1 = new Date();
                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);

                    binding.calendarMultipleDate.init(today1, nextYear.getTime())
                            .inMode(MULTIPLE)
                            .withSelectedDates(myAlreadySelectedDates)
                    ;

                    setTextForMultipleDate();

                    strStartTime = result1.getPostshiftTime().get(0).getStartTime();
                    strEndTIme = result1.getPostshiftTime().get(0).getEndTime();

                }
                else
                {

                    binding.rvDate.setVisibility(View.VISIBLE);
                    binding.llSingleDate.setVisibility(View.GONE);
                    Date today1 = new Date();
                    Calendar nextYear = Calendar.getInstance();
                    nextYear.add(Calendar.YEAR, 1);

                    binding.calendarMultipleDate.init(today1, nextYear.getTime())
                            .inMode(MULTIPLE)
                            .withSelectedDates(myAlreadySelectedDates)
                    ;

                    setTextForMultipleDate();

                    strStartTime = result1.getPostshiftTime().get(0).getStartTime();
                    strEndTIme = result1.getPostshiftTime().get(0).getEndTime();

                    binding.checkboxMultipledateSelect.setChecked(true);
                    multipleSelection = true;
                }

            }

          //  fullScreenDialog("only");

        }

        Places.initialize(getActivity().getApplicationContext(), "AIzaSyA1zVQsDeyYQJbE64CmQVSfzNO-AwFoUNk");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity());

        binding.nestedScrollView.setNestedScrollingEnabled(true);

        gpsTracker = new GPSTracker(getActivity());

        getLocation();

        adapter = new SelectEditTimeAdapter(selectedDates,getActivity(),this,startTime,endTime);


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

        setSpinner();

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                start);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerStartTime.setAdapter(ad);

        binding.spinnerStartTime.setSelection(getStartDatePosition(strStartTime));

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                end);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerEndTime.setAdapter(ad);

        binding.spinnerEndTime.setSelection(getEndDatePosition(strEndTIme));

        binding.btnPost.setVisibility(View.GONE);
        binding.btnRehireWorker.setVisibility(View.GONE);
        binding.btnRecruitment.setVisibility(View.GONE);


        binding.calendarSingleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
             @Override
             public void onDateSelected(Date date) {

                 String pattern = "MM/dd/yyyy";

                 DateFormat df = new SimpleDateFormat(pattern);

                 Date today = Calendar.getInstance().getTime();

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

     //   fullScreenDialog("Directshift");

        return binding.getRoot();
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

        binding.spinnerVaccine.setSelection(getSpinnerVacanciesPosition(strNoOfVancancies));

        binding.spinnerVaccine.setClickable(false);
        binding.spinnerVaccine.setEnabled(false);


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                hrrate);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerRate.setAdapter(ad);

        binding.spinnerRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerRate.setSelection(getSpinnerHrRatePosition(strHourlyRate));

        binding.spinnerRate.setClickable(false);
        binding.spinnerRate.setEnabled(false);


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                duty);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerDuty.setAdapter(ad);

        binding.spinnerDuty.setSelection(getSpinnerDutyPosition(strDuty));

        binding.spinnerDuty.setClickable(false);
        binding.spinnerDuty.setEnabled(false);


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                status);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerStatus.setAdapter(ad);

        binding.spinnerStatus.setSelection(getSpinnerCovidStatusPosition(strCovid));

        binding.spinnerStatus.setClickable(false);
        binding.spinnerStatus.setEnabled(false);


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                unpaid);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerUnpaidBreak.setAdapter(ad);

        binding.spinnerUnpaidBreak.setSelection(getSpinnerUnpaidBreakPosition(strUnpaid));

        binding.spinnerUnpaidBreak.setClickable(false);
        binding.spinnerUnpaidBreak.setEnabled(false);


        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                transit);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerTransit.setAdapter(ad);

        binding.spinnerTransit.setSelection(getSpinnerTransitPosition(strTransit));

        binding.spinnerTransit.setClickable(false);
        binding.spinnerTransit.setEnabled(false);

        ad = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item,
                entry);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        binding.spinnerEnter.setAdapter(ad);

        binding.spinnerEnter.setSelection(getSpinnerEnterPosition(strDayType));

        binding.spinnerEnter.setOnItemSelectedListener(date_selection_listener);

    }

    CalendarPickerView calendar;

    private AdapterView.OnItemSelectedListener date_selection_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {

                    binding.rvDate.setVisibility(View.GONE);
                    binding.llSingleDate.setVisibility(View.VISIBLE);

                        multipleSelection = false;

                        binding.calendarSingleDate.setVisibility(View.VISIBLE);

                        binding.calendarMultipleDate.setVisibility(View.GONE);

                        if(goOnly)
                        {

                            selectedDates.clear();

                            binding.spinnerStartTime.setSelection(0);
                            binding.spinnerEndTime.setSelection(0);

                            binding.checkboxMultipledateSelect.setChecked(false);

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

                            DateFormat df = new SimpleDateFormat(pattern);
                            String todayAsString = df.format(date);
                            binding.tvSingleDate.setText(todayAsString);

                            selectedDates.add(todayAsString);

                            setTextForMultipleDate();
                        }
                        goOnly = true ;
                } else
                {
                    multipleSelection = true;
                    binding.calendarSingleDate.setVisibility(View.GONE);
                    binding.calendarMultipleDate.setVisibility(View.VISIBLE);
                    if(goOnly)
                    {
                        if(binding.checkboxMultipledateSelect.isChecked())
                        {
                            binding.rvDate.setVisibility(View.VISIBLE);
                            binding.llSingleDate.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.rvDate.setVisibility(View.GONE);
                            binding.llSingleDate.setVisibility(View.VISIBLE);
                        }

                        selectedDates.clear();
                        binding.spinnerStartTime.setSelection(0);
                        binding.spinnerEndTime.setSelection(0);

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

                        setTextForMultipleDate();

                        adapter.notifyDataSetChanged();

                    }

                    goOnly = true;

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

/*
        for(Map.Entry<String, String> e : startTime.entrySet()) {
            myStrStartTime = myStrStartTime +e.getValue()+",";
        }*/

        if (myStrStartTime.endsWith(","))
        {
            myStrStartTime = myStrStartTime.substring(0, myStrStartTime.length() - 1);
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

      /*  for(Map.Entry<String, String> e : endTime.entrySet()) {
            myStrEndTime = myStrEndTime +e.getValue()+",";
        }
*/

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

  private void editPost() throws ParseException {
      if(strDayType.equalsIgnoreCase("Single Day"))
      {
          strDayType = "Single";
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
      map.put("shift_id",strShiftId);
      map.put("worker_id",strWorkerId);
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
      map.put("time_type",timeSelection);
      map.put("shift_date",strShiftsDate);
      map.put("start_time",strStartTime);
      map.put("end_time",strEndTIme);
      map.put("shift_date_new",strNewShiftDate);
      map.put("start_time_new",strNewShiftStartTime);
      map.put("end_time_new",strNewShiftSEndTime);
      map.put("type","Recruitmentshift");

      Call<SuccessResEditShift> call = apiInterface.editPost(map);

      call.enqueue(new Callback<SuccessResEditShift>() {
          @Override
          public void onResponse(Call<SuccessResEditShift> call, Response<SuccessResEditShift> response) {
              DataManager.getInstance().hideProgressMessage();
              try {
                  SuccessResEditShift data = response.body();
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
          public void onFailure(Call<SuccessResEditShift> call, Throwable t) {
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
            tvHeader.setText(getResources().getString(R.string.confirm_recruitment_post));
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
        String jobPosition = strJobPosition;
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
                 confirm();
                }
                );

        binding.btnConfirm.setOnClickListener(v ->
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
                    if (isValid()) {

                        if(binding.checkboxMultipledateSelect.isChecked())
                        {
                            timeSelection = "Multi";
                        }
                        else
                        {
                            timeSelection = "Single";
                        }

                        try {
                            editPost();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
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

        binding.spinnerJobPosition.setSelection(getSpinnerJobPositionPosition(strJobPosition));
        binding.spinnerJobPosition.setClickable(false);
        binding.spinnerJobPosition.setEnabled(false);

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
        binding.spinnerAddressEvent.setSelection(getSpinnerAddressPosition(strShiftsLocation));
        binding.spinnerAddressEvent.setClickable(false);
        binding.spinnerAddressEvent.setEnabled(false);

        fullScreenDialog("");

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

//
//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//
//                    }
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

    public int getSpinnerVacanciesPosition(String code)
    {
        int i=0;

        while (i<vaccine.length)
        {
            if(vaccine[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public int getSpinnerHrRatePosition(String code)
    {
        int i=0;
        for (String rate:hrrate)
        {
            if(rate.equalsIgnoreCase(code))
            {
                return i;
            }

            i++;
        }

        return 0;
    }


    public int getSpinnerDutyPosition(String code)
    {
        int i=0;

        while (i<duty.length)
        {
            if(duty[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public int getSpinnerCovidStatusPosition(String code)
    {
        int i=0;

        while (i<status.length)
        {
            if(status[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public int getSpinnerUnpaidBreakPosition(String code)
    {

        int i=0;

        while (i<unpaid.length)
        {
            if(unpaid[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public int getSpinnerTransitPosition(String code)
    {
        int i=0;

        while (i<transit.length)
        {
            if(transit[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public int getSpinnerAddressPosition(String code)
    {
        int i=1;
        for (SuccessResGetAddress.Result state:addressList)
        {
            if(state.getAddress().equalsIgnoreCase(code))
            {
                return i;
            }

            i++;
        }

        return 0;
    }

    public int getSpinnerEnterPosition(String code)
    {
        int i=0;

        while (i<entry.length)
        {
            if(entry[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }


    public int getStartDatePosition(String code)
    {
        int i=0;

        while (i<start.length)
        {
            if(start[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public int getEndDatePosition(String code)
    {
        int i=0;

        while (i<end.length)
        {
            if(end[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public void confirm()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("shift_id",strShiftId);
        map.put("worker_id",strWorkerId);
        map.put("status",strStatus);
        map.put("user_id",strUserId);

        Call<SuccessResAcceptRejectRecruitment> call = apiInterface.acceptRejectRecruitment(map);
        call.enqueue(new Callback<SuccessResAcceptRejectRecruitment>() {
            @Override
            public void onResponse(Call<SuccessResAcceptRejectRecruitment> call, Response<SuccessResAcceptRejectRecruitment> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAcceptRejectRecruitment data = response.body();
                    if (data.status.equals("1")) {

                        startActivity(new Intent(getActivity(),HomeActivity.class));
                        getActivity().finish();
                        showToast(getActivity(), data.message);

                    } else {
                        showToast(getActivity(), data.message);
                        startActivity(new Intent(getActivity(),HomeActivity.class));
                        getActivity().finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResAcceptRejectRecruitment> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }


    public void setNewTimeDate() throws ParseException {

        List<String> newShiftDate = Arrays.asList(strShiftsDate.split("\\s*,\\s*"));

        List<String> newShiftStartTime = Arrays.asList(strStartTime.split("\\s*,\\s*"));

        List<String> newShiftEndTime = Arrays.asList(strEndTIme.split("\\s*,\\s*"));

        strNewShiftDate = "";
        strNewShiftSEndTime ="";
        strNewShiftStartTime = "";

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

        // String pattern = "MM/dd/yyyy";

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

      /*  //Date/time pattern of input date (12 Hours format - hh used for 12 hours)
        DateFormat df = new SimpleDateFormat("hh:mm:ssaa");

        //Date/time pattern of desired output date (24 Hours format HH - Used for 24 hours)
        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        String output = null;

        //Returns Date object
        date = df.parse(twelveHoursTime);

        //old date format to new date format
        output = outputformat.format(date);
        System.out.println(output);

        return output;*/
    }



}

//
//package com.technorizen.healthcare.fragments;
//
//        import android.Manifest;
//        import android.app.Dialog;
//        import android.content.Intent;
//        import android.content.pm.PackageManager;
//        import android.os.Bundle;
//        import android.util.Log;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.view.WindowManager;
//        import android.widget.AdapterView;
//        import android.widget.ArrayAdapter;
//        import android.widget.CompoundButton;
//        import android.widget.ImageView;
//        import android.widget.LinearLayout;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import androidx.annotation.Nullable;
//        import androidx.appcompat.widget.AppCompatButton;
//        import androidx.core.app.ActivityCompat;
//        import androidx.databinding.DataBindingUtil;
//        import androidx.fragment.app.Fragment;
//        import androidx.recyclerview.widget.LinearLayoutManager;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import com.google.android.gms.common.api.Status;
//        import com.google.android.gms.maps.model.LatLng;
//        import com.google.android.libraries.places.api.Places;
//        import com.google.android.libraries.places.api.model.Place;
//        import com.google.android.libraries.places.api.net.PlacesClient;
//        import com.google.android.libraries.places.widget.Autocomplete;
//        import com.google.android.libraries.places.widget.AutocompleteActivity;
//        import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
//        import com.google.gson.Gson;
//        import com.technorizen.healthcare.R;
//        import com.technorizen.healthcare.activites.HomeActivity;
//        import com.technorizen.healthcare.adapters.SelectEditTimeAdapter;
//        import com.technorizen.healthcare.adapters.SelectTimeAdapter;
//        import com.technorizen.healthcare.adapters.ShowDateTimeAdapter;
//        import com.technorizen.healthcare.databinding.FragmentPostShiftsBinding;
//        import com.technorizen.healthcare.models.SuccessResAcceptRejectRecruitment;
//        import com.technorizen.healthcare.models.SuccessResAddAddress;
//        import com.technorizen.healthcare.models.SuccessResEditShift;
//        import com.technorizen.healthcare.models.SuccessResGetAddress;
//        import com.technorizen.healthcare.models.SuccessResGetCurrentSchedule;
//        import com.technorizen.healthcare.models.SuccessResGetJobPositions;
//        import com.technorizen.healthcare.models.SuccessResSignIn;
//        import com.technorizen.healthcare.retrofit.ApiClient;
//        import com.technorizen.healthcare.retrofit.Constant;
//        import com.technorizen.healthcare.retrofit.HealthInterface;
//        import com.technorizen.healthcare.util.DataManager;
//        import com.technorizen.healthcare.util.GPSTracker;
//        import com.technorizen.healthcare.util.SharedPreferenceUtility;
//        import com.technorizen.healthcare.util.StartTimeAndTimeInterface;
//        import com.wisnu.datetimerangepickerandroid.CalendarPickerView;
//
//        import java.text.DateFormat;
//        import java.text.ParseException;
//        import java.text.SimpleDateFormat;
//        import java.util.ArrayList;
//        import java.util.Arrays;
//        import java.util.Calendar;
//        import java.util.Date;
//        import java.util.HashMap;
//        import java.util.LinkedList;
//        import java.util.List;
//        import java.util.Locale;
//        import java.util.Map;
//
//        import retrofit2.Call;
//        import retrofit2.Callback;
//        import retrofit2.Response;
//
//        import static android.app.Activity.RESULT_CANCELED;
//        import static android.app.Activity.RESULT_OK;
//        import static com.technorizen.healthcare.activites.LoginAct.TAG;
//        import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
//        import static com.technorizen.healthcare.retrofit.Constant.showToast;
//        import static com.technorizen.healthcare.util.DataManager.subArray;
//        import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.MULTIPLE;
//        import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ConfirmEditRecruitmentShiftsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class ConfirmEditRecruitmentShiftsFragment extends Fragment implements StartTimeAndTimeInterface {
//
//    GPSTracker gpsTracker;
//    String strLat = "", strLng = "",categoryId = "",strAddress="";
//
//    private boolean goOnly = false;
//
//    private String strShiftId,strWorkerId,strUserId,strStatus;
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
//    private SelectEditTimeAdapter adapter;
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
//    private String workerName = "",workerId = "";
//    String[] transit = {"None","1 Hour","2 Hour","3 Hour","4 Hour"};
//    ArrayAdapter ad;
//    private List<Date> myAlreadySelectedDates = new LinkedList<>();
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public ConfirmEditRecruitmentShiftsFragment() {
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
//
//    // TODO: Rename and change types and number of parameters
//    public static ConfirmEditRecruitmentShiftsFragment newInstance(String param1, String param2) {
//        ConfirmEditRecruitmentShiftsFragment fragment = new ConfirmEditRecruitmentShiftsFragment();
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
//        binding.btnConfirm.setVisibility(View.VISIBLE);
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
//        Bundle myBundle = new Bundle();
//
//        myBundle = this.getArguments();
//
//        if(myBundle!=null)
//        {
//
//            String result = myBundle.getString("result");
//            SuccessResGetCurrentSchedule.Result result1 = new Gson().fromJson(result,SuccessResGetCurrentSchedule.Result.class);
//            Log.e("sdffsdfds","REsult = " +result1.getShiftsdetail().get(0).getJobPosition());
//            strShiftId = result1.getShiftId();
//            strWorkerId = result1.getWorkerId();
//            strUserId = result1.getUserId();
//            strStatus = "Accepted";
//            strJobPosition = result1.getShiftsdetail().get(0).getJobPosition();
//            strNoOfVancancies = result1.getShiftsdetail().get(0).getNoVacancies();
//            strHourlyRate = result1.getShiftsdetail().get(0).getHourlyRate();
//            strHourlyRate = strHourlyRate;
//            strDuty = result1.getShiftsdetail().get(0).getDutyOfWorker();
//            strCovid = result1.getShiftsdetail().get(0).getCovidStatus();
//            if(result1.getShiftsdetail().get(0).getUnpaidBreak().equalsIgnoreCase("None"))
//            {
//                strUnpaid = result1.getShiftsdetail().get(0).getUnpaidBreak();
//            }
//            else
//            {
//                strUnpaid = result1.getShiftsdetail().get(0).getUnpaidBreak()+" Minutes";
//            }
//
//            if(result1.getShiftsdetail().get(0).getTransitAllowance().equalsIgnoreCase("None"))
//            {
//                strTransit = result1.getShiftsdetail().get(0).getTransitAllowance();
//            }
//            else
//            {
//                strTransit = result1.getShiftsdetail().get(0).getTransitAllowance()+" Hour";
//            }
//
//            strShiftsLocation = result1.getShiftsdetail().get(0).getShiftLocation();
//
//            strDayType = result1.getShiftsdetail().get(0).getDayType()+" Day";
//
//            timeSelection = result1.getShiftsdetail().get(0).getTimeType();
//
//            binding.etShiftsNote.setText(result1.getShiftsdetail().get(0).getShiftNotes());
//
//            strShiftsNote = result1.getShiftsdetail().get(0).getShiftNotes();
//
//            List<SuccessResGetCurrentSchedule.PostshiftTime> postshiftTimeList = new LinkedList<>();
//
//            postshiftTimeList = result1.getPostshiftTime();
//
//            for (SuccessResGetCurrentSchedule.PostshiftTime timeItem:postshiftTimeList)
//            {
//                startTime.put(timeItem.getShiftDate(),timeItem.getStartTime());
//                endTime.put(timeItem.getShiftDate(),timeItem.getEndTime());
//                selectedDates.add(timeItem.getShiftDate());
//                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//                Date date1 = null;
//                try {
//                    String dt = timeItem.getShiftDate()+" 8:20";
//                    date1 = formatter.parse(dt);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                myAlreadySelectedDates.add(date1);
//            }
//
//            if(strDayType.equalsIgnoreCase("Single Day"))
//            {
//
//                binding.calendarSingleDate.setVisibility(View.VISIBLE);
//                binding.calendarMultipleDate.setVisibility(View.GONE);
//
//                String dt1 = result1.getPostshiftTime().get(0).getShiftDate()+" 8:20";
//
//                strShiftsDate = result1.getPostshiftTime().get(0).getShiftDate();
//
//                strStartTime = result1.getPostshiftTime().get(0).getStartTime();
//
//                strEndTIme = result1.getPostshiftTime().get(0).getEndTime();
//
//                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//
//                Date date1 = null;
//
//                try {
//
//                    date1 = formatter.parse(dt1);
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Calendar nextYear = Calendar.getInstance();
//                nextYear.add(Calendar.YEAR, 1);
//
//                Date datee = new Date();
//
//                binding.calendarSingleDate.init(datee, nextYear.getTime())
//                        .withSelectedDate(date1).inMode(SINGLE);
//                binding.tvSingleDate.setText(strShiftsDate);
//
//            }
//            else
//            {
//
//                binding.calendarSingleDate.setVisibility(View.GONE);
//                binding.calendarMultipleDate.setVisibility(View.VISIBLE);
//
//                if(timeSelection.equalsIgnoreCase("Single"))
//                {
//
//                    binding.rvDate.setVisibility(View.GONE);
//                    binding.llSingleDate.setVisibility(View.VISIBLE);
//                    Date today1 = new Date();
//                    Calendar nextYear = Calendar.getInstance();
//                    nextYear.add(Calendar.YEAR, 1);
//
//                    binding.calendarMultipleDate.init(today1, nextYear.getTime())
//                            .inMode(MULTIPLE)
//                            .withSelectedDates(myAlreadySelectedDates)
//                    ;
//
//                    setTextForMultipleDate();
//
//                    strStartTime = result1.getPostshiftTime().get(0).getStartTime();
//                    strEndTIme = result1.getPostshiftTime().get(0).getEndTime();
//
//                }
//                else
//                {
//
//                    binding.rvDate.setVisibility(View.VISIBLE);
//                    binding.llSingleDate.setVisibility(View.GONE);
//                    Date today1 = new Date();
//                    Calendar nextYear = Calendar.getInstance();
//                    nextYear.add(Calendar.YEAR, 1);
//
//                    binding.calendarMultipleDate.init(today1, nextYear.getTime())
//                            .inMode(MULTIPLE)
//                            .withSelectedDates(myAlreadySelectedDates)
//                    ;
//
//                    setTextForMultipleDate();
//
//                    strStartTime = result1.getPostshiftTime().get(0).getStartTime();
//                    strEndTIme = result1.getPostshiftTime().get(0).getEndTime();
//
//                    binding.checkboxMultipledateSelect.setChecked(true);
//                    multipleSelection = true;
//                }
//
//            }
//
//            //  fullScreenDialog("only");
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
//        adapter = new SelectEditTimeAdapter(selectedDates,getActivity(),this,startTime,endTime);
//
//
//        binding.tvAddAddress.setOnClickListener(v ->
//                {
//
//                    // return after the user has made a selection.
//                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
//                    // Start the autocomplete intent.
//                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//                            .build(getActivity());
//                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//                }
//        );
//        setSpinner();
//
//        binding.spinnerStartTime.setOnItemSelectedListener(startTimeSelectListener);
//
//
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                start);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerStartTime.setAdapter(ad);
//        binding.spinnerStartTime.setSelection(getStartDatePosition(strStartTime));
//        int endEndPosition = start.length-1;
//        int myPosition1 = getEndDatePosition(strEndTIme);
//        end = subArray(start, myPosition1, endEndPosition);
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                end);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerEndTime.setAdapter(ad);
//        binding.spinnerEndTime.setSelection(getEndDatePosition(strEndTIme));
//        binding.btnPost.setVisibility(View.GONE);
//        binding.btnRehireWorker.setVisibility(View.GONE);
//        binding.btnRecruitment.setVisibility(View.GONE);
//        binding.calendarSingleDate.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Date date) {
//
//                String pattern = "MM/dd/yyyy";
//
//                DateFormat df = new SimpleDateFormat(pattern);
//
//                Date today = Calendar.getInstance().getTime();
//
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
//
//// Get the today date using Calendar object.
//                Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                String todayAsString = df.format(date);
//
//                selectedDates.add(todayAsString);
//
//                startTime.put(todayAsString,"12:00 AM");
//                endTime.put(todayAsString,"12:15 AM");
//
//                adapter.notifyDataSetChanged();
//
//                setTextForMultipleDate();
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
//        //   fullScreenDialog("Directshift");
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
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerVaccine.setAdapter(ad);
//
//        binding.spinnerVaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
//        binding.spinnerVaccine.setSelection(getSpinnerVacanciesPosition(strNoOfVancancies));
//        binding.spinnerVaccine.setClickable(false);
//        binding.spinnerVaccine.setEnabled(false);
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                hrrate);
//
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerRate.setAdapter(ad);
//        binding.spinnerRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        binding.spinnerRate.setSelection(getSpinnerHrRatePosition(strHourlyRate));
//        binding.spinnerRate.setClickable(false);
//        binding.spinnerRate.setEnabled(false);
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                duty);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerDuty.setAdapter(ad);
//        binding.spinnerDuty.setSelection(getSpinnerDutyPosition(strDuty));
//        binding.spinnerDuty.setClickable(false);
//        binding.spinnerDuty.setEnabled(false);
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                status);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerStatus.setAdapter(ad);
//        binding.spinnerStatus.setSelection(getSpinnerCovidStatusPosition(strCovid));
//        binding.spinnerStatus.setClickable(false);
//        binding.spinnerStatus.setEnabled(false);
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                unpaid);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerUnpaidBreak.setAdapter(ad);
//        binding.spinnerUnpaidBreak.setSelection(getSpinnerUnpaidBreakPosition(strUnpaid));
//        binding.spinnerUnpaidBreak.setClickable(false);
//        binding.spinnerUnpaidBreak.setEnabled(false);
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                transit);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerTransit.setAdapter(ad);
//        binding.spinnerTransit.setSelection(getSpinnerTransitPosition(strTransit));
//        binding.spinnerTransit.setClickable(false);
//        binding.spinnerTransit.setEnabled(false);
//        ad = new ArrayAdapter(
//                getActivity(),
//                android.R.layout.simple_spinner_item,
//                entry);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        binding.spinnerEnter.setAdapter(ad);
//        binding.spinnerEnter.setSelection(getSpinnerEnterPosition(strDayType));
//        binding.spinnerEnter.setOnItemSelectedListener(date_selection_listener);
//    }
//
//    CalendarPickerView calendar;
//    private AdapterView.OnItemSelectedListener date_selection_listener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            if(position == 0)
//            {
//                binding.rvDate.setVisibility(View.GONE);
//                binding.llSingleDate.setVisibility(View.VISIBLE);
//                multipleSelection = false;
//                binding.calendarSingleDate.setVisibility(View.VISIBLE);
//                binding.calendarMultipleDate.setVisibility(View.GONE);
//                if(goOnly)
//                {
//                    selectedDates.clear();
//                    first = true;
//                    binding.spinnerStartTime.setSelection(0);
//                    binding.spinnerEndTime.setSelection(0);
//                    binding.checkboxMultipledateSelect.setChecked(false);
//                    Calendar nextYear = Calendar.getInstance();
//                    nextYear.add(Calendar.YEAR, 1);
//                    Date today = new Date();
//                    binding.calendarSingleDate.init(today, nextYear.getTime())
//                            .withSelectedDate(today).inMode(SINGLE);
//                    binding.rvDate.setVisibility(View.GONE);
//                    binding.llSingleDate.setVisibility(View.VISIBLE);
//                    binding.checkboxMultipledateSelect.setVisibility(View.GONE);
//                    Date date = Calendar.getInstance().getTime();
//                    String pattern = "MM/dd/yyyy";
//                    DateFormat df = new SimpleDateFormat(pattern);
//                    String todayAsString = df.format(date);
//                    binding.tvSingleDate.setText(todayAsString);
//                    selectedDates.add(todayAsString);
//                    setTextForMultipleDate();
//                }
//                goOnly = true ;
//            } else
//            {
//                multipleSelection = true;
//                binding.calendarSingleDate.setVisibility(View.GONE);
//                binding.calendarMultipleDate.setVisibility(View.VISIBLE);
//                if(goOnly)
//                {
//                    if(binding.checkboxMultipledateSelect.isChecked())
//                    {
//                        binding.rvDate.setVisibility(View.VISIBLE);
//                        binding.llSingleDate.setVisibility(View.GONE);
//                    }
//                    else
//                    {
//                        binding.rvDate.setVisibility(View.GONE);
//                        binding.llSingleDate.setVisibility(View.VISIBLE);
//                    }
//                    selectedDates.clear();
//                    binding.spinnerStartTime.setSelection(0);
//                    binding.spinnerEndTime.setSelection(0);
//                    Date date = Calendar.getInstance().getTime();
//                    String pattern = "MM/dd/yyyy";
//// Create an instance of SimpleDateFormat used for formatting
//// the string representation of date according to the chosen pattern
//                    DateFormat df = new SimpleDateFormat(pattern);
//// Get the today date using Calendar object.
//                    Date today = Calendar.getInstance().getTime();
//// Using DateFormat format method we can create a string
//// representation of a date with the defined format.
//                    String todayAsString = df.format(date);
//                    selectedDates.add(todayAsString);
//                    startTime.put(todayAsString,"12:00 AM");
//                    endTime.put(todayAsString,"12:15 AM");
//                    binding.checkboxMultipledateSelect.setVisibility(View.VISIBLE);
//                    Calendar nextYear = Calendar.getInstance();
//                    nextYear.add(Calendar.YEAR, 1);
//                    binding.checkboxMultipledateSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            if(isChecked)
//                            {
//                                binding.rvDate.setVisibility(View.VISIBLE);
//                                binding.llSingleDate.setVisibility(View.GONE);
//                                adapter.notifyDataSetChanged();
//                            }
//                            else
//                            {
//                                binding.rvDate.setVisibility(View.GONE);
//                                binding.llSingleDate.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });
//                    Date today1 = new Date();
//                    binding.calendarMultipleDate.init(today1, nextYear.getTime())
//                            .withSelectedDate(today1).inMode(MULTIPLE);
//                    setTextForMultipleDate();
//                    adapter.notifyDataSetChanged();
//                }
//                goOnly = true;
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };
//    private boolean first = false;
//    private AdapterView.OnItemSelectedListener startTimeSelectListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            if (first)
//            {
//                int subEnd = start.length-1;
//                int myPosition = position + 1;
//                String[] subarray = subArray(start, myPosition, subEnd);
//                ad = new ArrayAdapter(
//                        getActivity(),
//                        android.R.layout.simple_spinner_item,
//                        subarray);
//                ad.setDropDownViewResource(
//                        android.R.layout
//                                .simple_spinner_dropdown_item);
//                binding.spinnerEndTime.setAdapter(ad);
//            }
//            first = true;
//        }
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };
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
//            myStrStartTime = myStrStartTime.substring(0, myStrStartTime.length() - 1);
//        }
//        return myStrStartTime;
//    }
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
//    private void editPost() throws ParseException {
//        if(strDayType.equalsIgnoreCase("Single Day"))
//        {
//            strDayType = "Single";
//        }
//        else
//        {
//            strDayType = "Multi";
//        }
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//        String myBreak = unpaidBreakMap.get(strUnpaid);
//        String myTransit = transitMap.get(strTransit);
//        setNewTimeDate();
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String,String> map = new HashMap<>();
//        map.put("shift_id",strShiftId);
//        map.put("worker_id",strWorkerId);
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
//        map.put("time_type",timeSelection);
//        map.put("shift_date",strShiftsDate);
//        map.put("start_time",strStartTime);
//        map.put("end_time",strEndTIme);
//        map.put("shift_date_new",strNewShiftDate);
//        map.put("start_time_new",strNewShiftStartTime);
//        map.put("end_time_new",strNewShiftSEndTime);
//        map.put("type","Recruitmentshift");
//        Call<SuccessResEditShift> call = apiInterface.editPost(map);
//        call.enqueue(new Callback<SuccessResEditShift>() {
//            @Override
//            public void onResponse(Call<SuccessResEditShift> call, Response<SuccessResEditShift> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//                try {
//                    SuccessResEditShift data = response.body();
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
//            public void onFailure(Call<SuccessResEditShift> call, Throwable t) {
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
//            tvHeader.setText(getResources().getString(R.string.confirm_recruitment_post));
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
//        String jobPosition = strJobPosition;
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
//            if (binding.checkboxMultipledateSelect.isChecked())
//            {
//                llSingleDate.setVisibility(View.GONE);
//                rvMultipleDates.setVisibility(View.VISIBLE);
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
//                strEndTIme = "";
//                for (String date:selectedDates)
//                {
//                    strEndTIme = strEndTIme + binding.spinnerEndTime.getSelectedItem().toString()+",";
//                }
//                if (strEndTIme.endsWith(","))
//                {
//                    strEndTIme = strEndTIme.substring(0, strEndTIme.length() - 1);
//                }
//            }
//        }
//        else
//        {
//            llSingleDate.setVisibility(View.VISIBLE);
//            rvMultipleDates.setVisibility(View.GONE);
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
//                    confirm();
//                }
//        );
//
//        binding.btnConfirm.setOnClickListener(v ->
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
//                            if(strEndTIme.equalsIgnoreCase(""))
//                            {
//                                return;
//                            }
//                        } else
//                        {
//                            strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                            strEndTIme="";
//                            if(binding.spinnerEndTime.getSelectedItem()!=null)
//                            {
//                                strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                            }
//                        }
//                    }
//                    else
//                    {
//                        strStartTime = binding.spinnerStartTime.getSelectedItem().toString();
//                        strEndTIme="";
//                        if(binding.spinnerEndTime.getSelectedItem()!=null)
//                        {
//                            strEndTIme = binding.spinnerEndTime.getSelectedItem().toString();
//                        }
//                    }
//                    if (isValid()) {
//
//                        if(binding.checkboxMultipledateSelect.isChecked())
//                        {
//                            timeSelection = "Multi";
//                        }
//                        else
//                        {
//                            timeSelection = "Single";
//                        }
//                        try {
//                            editPost();
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        //          Navigation.findNavController(v).navigate(R.id.action_postShiftsFragment_to_confirmPostShiftsFragment,bundle);
//                    }
//
//                }
//        );
//
//        dialog.show();
//
//    }
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
//
//                        jobPositionsList.clear();
//
//                        jobPositionsList.addAll(data.getResult());
//
//                        setJobPositionSpinner();
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
//
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
//
//        binding.spinnerJobPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
//        binding.spinnerJobPosition.setSelection(getSpinnerJobPositionPosition(strJobPosition));
//        binding.spinnerJobPosition.setClickable(false);
//        binding.spinnerJobPosition.setEnabled(false);
//
//    }
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
//        binding.spinnerAddressEvent.setSelection(getSpinnerAddressPosition(strShiftsLocation));
//        binding.spinnerAddressEvent.setClickable(false);
//        binding.spinnerAddressEvent.setEnabled(false);
//
//        fullScreenDialog("");
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
//            Toast.makeText(getActivity(), "Please select End date.", Toast.LENGTH_SHORT).show();
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
//        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//
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
//    public int getSpinnerVacanciesPosition(String code)
//    {
//        int i=0;
//
//        while (i<vaccine.length)
//        {
//            if(vaccine[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//    public int getSpinnerHrRatePosition(String code)
//    {
//        int i=0;
//        for (String rate:hrrate)
//        {
//            if(rate.equalsIgnoreCase(code))
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
//    public int getSpinnerDutyPosition(String code)
//    {
//        int i=0;
//
//        while (i<duty.length)
//        {
//            if(duty[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//    public int getSpinnerCovidStatusPosition(String code)
//    {
//        int i=0;
//
//        while (i<status.length)
//        {
//            if(status[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//    public int getSpinnerUnpaidBreakPosition(String code)
//    {
//
//        int i=0;
//
//        while (i<unpaid.length)
//        {
//            if(unpaid[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//    public int getSpinnerTransitPosition(String code)
//    {
//        int i=0;
//
//        while (i<transit.length)
//        {
//            if(transit[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//    public int getSpinnerAddressPosition(String code)
//    {
//        int i=1;
//        for (SuccessResGetAddress.Result state:addressList)
//        {
//            if(state.getAddress().equalsIgnoreCase(code))
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
//    public int getSpinnerEnterPosition(String code)
//    {
//        int i=0;
//
//        while (i<entry.length)
//        {
//            if(entry[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//
//    public int getStartDatePosition(String code)
//    {
//        int i=0;
//
//        while (i<start.length)
//        {
//            if(start[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//    public int getEndDatePosition(String code)
//    {
//        int i=0;
//
//        while (i<end.length)
//        {
//            if(end[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//
//        return 0;
//    }
//
//    public void confirm()
//    {
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//        map.put("shift_id",strShiftId);
//        map.put("worker_id",strWorkerId);
//        map.put("status",strStatus);
//        map.put("user_id",strUserId);
//
//        Call<SuccessResAcceptRejectRecruitment> call = apiInterface.acceptRejectRecruitment(map);
//        call.enqueue(new Callback<SuccessResAcceptRejectRecruitment>() {
//            @Override
//            public void onResponse(Call<SuccessResAcceptRejectRecruitment> call, Response<SuccessResAcceptRejectRecruitment> response) {
//
//                DataManager.getInstance().hideProgressMessage();
//                try {
//                    SuccessResAcceptRejectRecruitment data = response.body();
//                    if (data.status.equals("1")) {
//
//                        startActivity(new Intent(getActivity(),HomeActivity.class));
//                        getActivity().finish();
//                        showToast(getActivity(), data.message);
//
//                    } else {
//                        showToast(getActivity(), data.message);
//                        startActivity(new Intent(getActivity(),HomeActivity.class));
//                        getActivity().finish();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SuccessResAcceptRejectRecruitment> call, Throwable t) {
//
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//
//            }
//        });
//
//    }
//
//
//    public void setNewTimeDate() throws ParseException {
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
//        for (String myDate:newShiftDate)
//        {
//            String date = convertDateFormate(myDate);
//            strNewShiftDate = strNewShiftDate + date+",";
//        }
//
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
//
//        if (strNewShiftDate.endsWith(","))
//        {
//            strNewShiftDate = strNewShiftDate.substring(0, strNewShiftDate.length() - 1);
//        }
//
//        if (strNewShiftStartTime.endsWith(","))
//        {
//            strNewShiftStartTime = strNewShiftStartTime.substring(0, strNewShiftStartTime.length() - 1);
//
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
//        // String pattern = "MM/dd/yyyy";
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
//      /*  //Date/time pattern of input date (12 Hours format - hh used for 12 hours)
//        DateFormat df = new SimpleDateFormat("hh:mm:ssaa");
//
//        //Date/time pattern of desired output date (24 Hours format HH - Used for 24 hours)
//        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
//        Date date = null;
//        String output = null;
//
//        //Returns Date object
//        date = df.parse(twelveHoursTime);
//
//        //old date format to new date format
//        output = outputformat.format(date);
//        System.out.println(output);
//
//        return output;*/
//    }
//
//
//
//}
