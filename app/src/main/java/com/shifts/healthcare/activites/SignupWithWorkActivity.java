package com.shifts.healthcare.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.shifts.healthcare.databinding.ActivitySignupWithWorkBinding;
import com.shifts.healthcare.models.SuccessResGetCountries;
import com.shifts.healthcare.models.SuccessResGetJobPositions;
import com.shifts.healthcare.models.SuccessResGetStates;
import com.shifts.healthcare.models.SuccessResWorkerSignup;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.activites.LoginAct.TAG;
import static com.shifts.healthcare.retrofit.Constant.isValidEmail;
import static com.shifts.healthcare.retrofit.Constant.showToast;

public class SignupWithWorkActivity extends AppCompatActivity {

    private HealthInterface apiInterface;
    private Dialog dialog;

    ActivitySignupWithWorkBinding binding;
    private ArrayAdapter<String> countryArrayAdapter;
    private ArrayAdapter<String> stateArrayAdapter;
    private List<SuccessResGetStates.Result> myStateList = new LinkedList<>();
    private List<SuccessResGetCountries.Result> myCountriesList = new LinkedList<>();
    private List<SuccessResGetJobPositions.Result> jobPositionsList = new LinkedList<>();
    private ArrayList<String> states;
    private String strRegisterType = "", strfirstName = "", strlastName = "",strAppartmentNum= "",
            strStreetNumber = "", strStreetName = "", strCountry = "", strState = "", strCity = "", strZip = "", strEmail = "", strCountryCode = "",
            strPhone = "", strPass = "", strConfirmPass = "", strJobPosition = "";
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    private String strAddress = "",strLat = "",strLong = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup_with_work);

        Places.initialize(getApplicationContext(), getString(R.string.api_key1));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        strCountryCode = "CA";

        binding.header.imgHeader.setOnClickListener(v -> finish());

        binding.header.tvHeader.setText(R.string.sign_up_to_work_shifts);

        binding.btnSignup.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupWithWorkActivity.this,LoginAct.class));
                }
        );

        binding.rlbottom.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupWithWorkActivity.this,LoginAct.class));
                }
        );

        binding.etAddress.setOnClickListener(v ->

                {
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(SignupWithWorkActivity.this);
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                }
        );

        binding.ccp.setOnClickListener(v ->
                {
                    final Dialog dialog = new Dialog(SignupWithWorkActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.ccp);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout rlUs, rlCA;

                    rlUs = dialog.findViewById(R.id.rlUS);
                    rlUs.setOnClickListener(v1 ->
                            {

                                binding.ivCanada.setImageResource(R.drawable.flag_united_states_of_america);
                                binding.tvCanada.setText("US +1");
                                strCountryCode = "US";
                                dialog.dismiss();

                            }
                    );
                    rlCA = dialog.findViewById(R.id.rlCA);
                    rlCA.setOnClickListener(v1 ->
                            {
                                binding.ivCanada.setImageResource(R.drawable.ic_canada);
                                binding.tvCanada.setText("CA +1");
                                strCountryCode = "CA";
                                dialog.dismiss();
                            }
                    );

                    dialog.show();
                }
        );

        getCountries();
        getJobPositions();

        SpannableString SpanString = new SpannableString(
                "By clicking Signup, you agree to CareShifts' Terms of Service and Privacy Policy .");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = "https://www.app.careshifts.net/termsofuse";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        };

        // Character starting from 32 - 45 is Terms and condition.
        // Character starting from 49 - 63 is privacy policy.

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                String url = "https://www.app.careshifts.net/privacypolicy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        };

        SpanString.setSpan(teremsAndCondition, 45, 61, 0);
        SpanString.setSpan(privacy, 66, 80, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLUE), 45, 61, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLUE), 66, 80, 0);
        SpanString.setSpan(new UnderlineSpan(), 45, 61, 0);
        SpanString.setSpan(new UnderlineSpan(), 66, 80, 0);

        binding.loggedInRadio.setMovementMethod(LinkMovementMethod.getInstance());
        binding.loggedInRadio.setText(SpanString, TextView.BufferType.SPANNABLE);
        binding.loggedInRadio.setSelected(true);



        binding.btnSignup.setOnClickListener(v ->
                {

                    strfirstName = binding.etFirstName.getText().toString().trim();
                    strlastName = binding.etLast.getText().toString().trim();
                    strStreetNumber = "test";
                    strStreetName = "test";
                    strCountry = "23";
                    strState = "23";
                    strCity = "test";
                    strZip = "test";
                    strEmail = binding.etEmail.getText().toString().trim();
                    strPhone = binding.etPhone.getText().toString().trim();
                    strPass = binding.etPass.getText().toString().trim();
                    strConfirmPass = binding.etConfirmPass.getText().toString().trim();
                    strAppartmentNum = "test";
                    strJobPosition = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());

                    binding.labelFirst.setError(null);
                    binding.labelLast.setError(null);
                    binding.labelStreetNum.setError(null);
                    binding.labelStreetName.setError(null);
                    binding.rlCountry.setBackgroundResource(R.drawable.et_bg);
                    binding.rlState.setBackgroundResource(R.drawable.et_bg);
                    binding.labelCity.setError(null);
                    binding.labelZipCode.setError(null);
                    binding.labelEmail.setError(null);
                    binding.tvPhoneError.setVisibility(View.GONE);
                    binding.llPhone.setBackgroundResource(R.drawable.et_bg);
                    binding.rlSpinnerJob.setBackgroundResource(R.drawable.et_bg);
                    binding.labelPass.setError(null);
                    binding.labelConfirmPass.setError(null);
                    binding.labelAppartNum.setError(null);

                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {

                            signup();

                        } else {
                            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();

                    }

                }
        );

    }


    private boolean isValid() {
        if (strfirstName.equalsIgnoreCase("")) {
            binding.labelFirst.setError(getString(R.string.enter_first));
            return false;
        } else if (strlastName.equalsIgnoreCase("")) {
            binding.labelLast.setError(getString(R.string.enter_last));
            return false;
        }else if (strEmail.equalsIgnoreCase("")) {
            binding.labelEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.labelEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strAddress.equalsIgnoreCase("")) {
            Toast.makeText(SignupWithWorkActivity.this, "Please add address.", Toast.LENGTH_SHORT).show();
            return false;
        }   else if (strPhone.equalsIgnoreCase("")) {
            binding.tvPhoneError.setVisibility(View.VISIBLE);
            binding.llPhone.setBackgroundResource(R.drawable.red_et_bg);
            return false;
        } else if (strJobPosition.equalsIgnoreCase("")) {
            binding.rlSpinnerJob.setBackgroundResource(R.drawable.red_et_bg);
            Toast.makeText(SignupWithWorkActivity.this, "" + getString(R.string.select_job_position), Toast.LENGTH_SHORT).show();
            return false;
        }else if (strPass.equalsIgnoreCase("")) {
            binding.labelPass.setError(getString(R.string.enter_pass));
            return false;
        } else if (strConfirmPass.equalsIgnoreCase("")) {
            binding.labelConfirmPass.setError(getString(R.string.enter_confirm_pass));
            return false;
        } else if (!strPass.equalsIgnoreCase(strConfirmPass)) {
            binding.labelConfirmPass.setError(getString(R.string.pass_and_confirm_pass_not_macthed));
            return false;
        }
        return true;
    }

    private void initializeUI() {
        ArrayList<String> spinnerListCountry = new ArrayList<>();

        states = new ArrayList<>();

//        createLists();
        spinnerListCountry.add("Select Country");
        for (SuccessResGetCountries.Result country : myCountriesList) {
            spinnerListCountry.add(country.getName());
        }

        countryArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerListCountry);
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCountry.setAdapter(countryArrayAdapter);
        binding.spinnerCountry.setOnItemSelectedListener(country_listener);

        states.add("Province / States");

        stateArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, states);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerState.setAdapter(stateArrayAdapter);

    }

    private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {

                String countryCode = getCountryCode(binding.spinnerCountry.getSelectedItem().toString());

                getState(countryCode);

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void getCountries() {
        DataManager.getInstance().showProgressMessage(SignupWithWorkActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetCountries> call = apiInterface.getCountries(map);
        call.enqueue(new Callback<SuccessResGetCountries>() {
            @Override
            public void onResponse(Call<SuccessResGetCountries> call, Response<SuccessResGetCountries> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCountries data = response.body();
                    if (data.status.equals("1")) {

                        myCountriesList.addAll(data.getResult());
                        initializeUI();

                    } else {
                        showToast(SignupWithWorkActivity.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetCountries> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    public String getCountryCode(String strCountryName) {
        for (SuccessResGetCountries.Result country : myCountriesList) {
            if (country.getName().equalsIgnoreCase(strCountryName)) {
                return country.getId();
            }
        }

        return "";
    }

    public String getSelectedJobPositionCode(String strCountryName) {
        for (SuccessResGetJobPositions.Result job : jobPositionsList) {
            if (job.getName().equalsIgnoreCase(strCountryName)) {
                return job.getId();
            }
        }
        return "";
    }

    public void getState(String contryCode)
    {

        DataManager.getInstance().showProgressMessage(SignupWithWorkActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("country_id",contryCode);

        Call<SuccessResGetStates> call = apiInterface.getStates(map);
        call.enqueue(new Callback<SuccessResGetStates>() {
            @Override
            public void onResponse(Call<SuccessResGetStates> call, Response<SuccessResGetStates> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetStates data = response.body();
                    if (data.status.equals("1")) {

                        myStateList.addAll(data.getResult());
                        setStatesSpinner();

                    } else {
                        showToast(SignupWithWorkActivity.this, data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetStates> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    public void setStatesSpinner()
    {

        List<String> tempStates = new LinkedList<>();

        tempStates.add("Province / State");

        for (SuccessResGetStates.Result state:myStateList)
        {
            tempStates.add(state.getName());
        }

        stateArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempStates);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerState.setAdapter(stateArrayAdapter);

    }

    public void getJobPositions() {
        DataManager.getInstance().showProgressMessage(SignupWithWorkActivity.this, getString(R.string.please_wait));
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
                        showToast(SignupWithWorkActivity.this, data.message);
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
        stateArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempStates);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerJobPosition.setAdapter(stateArrayAdapter);
    }

    private String getStateID(String name)

    {
        for (SuccessResGetStates.Result state:myStateList)
        {
            if(state.getName().equalsIgnoreCase(name))
            {
                return state.getId();
            }
        }
        return "";
    }

    public void signup() {

        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();
        DataManager.getInstance().showProgressMessage(SignupWithWorkActivity.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("first_name",strfirstName);
        map.put("last_name",strlastName);
        map.put("email",strEmail);
        map.put("street_no",strStreetNumber);
        map.put("street_name",strStreetName);
        map.put("apartment_no",strAppartmentNum);
        map.put("couttry_code",strCountryCode);
        map.put("country",strCountry);
        map.put("state",strState);
        map.put("city",strCity);
        map.put("zipcode",strZip);
        map.put("phone",strPhone);
        map.put("password",strPass);
        map.put("worker_designation",strJobPosition);
        map.put("register_id","kjdfk");
        map.put("address",strAddress);
        map.put("lat",strLat);
        map.put("lon",strLong);
        map.put("time_zone",id);

        Call<SuccessResWorkerSignup> signupCall = apiInterface.workerSignup(map);
        signupCall.enqueue(new Callback<SuccessResWorkerSignup>() {
            @Override
            public void onResponse(Call<SuccessResWorkerSignup> call, Response<SuccessResWorkerSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResWorkerSignup data = response.body();
                    if (data.status.equals("1")) {
                        showToast(SignupWithWorkActivity.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        fullScreenDialog();
                    } else if (data.status.equals("0")) {
                        showToast(SignupWithWorkActivity.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResWorkerSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void fullScreenDialog() {
        dialog = new Dialog(this, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.activity_register_success);
        TextView tvEmail;
        ImageView ivBack;
        ivBack = dialog.findViewById(R.id.ivBack);
        tvEmail = dialog.findViewById(R.id.tvEmail);
        tvEmail.setText(strEmail);
        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                strAddress = place.getAddress();
                LatLng latLng = place.getLatLng();

                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                strLat = Double.toString(latitude);
                strLong = Double.toString(longitude);

                String address = place.getAddress();

                strAddress = address;

                binding.etAddress.setText(address);

                binding.etAddress.post(new Runnable(){
                    @Override
                    public void run() {
                        binding.etAddress.setText(address);
                    }
                });

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


}