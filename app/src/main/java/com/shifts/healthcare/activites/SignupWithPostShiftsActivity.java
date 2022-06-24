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
import android.widget.CompoundButton;
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

import com.shifts.healthcare.databinding.ActivitySignupWithPostShiftsBinding;
import com.shifts.healthcare.models.SuccessResGetCountries;
import com.shifts.healthcare.models.SuccessResGetStates;
import com.shifts.healthcare.models.SuccessResSignup;
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

public class SignupWithPostShiftsActivity extends AppCompatActivity {

    private Dialog dialog;

    ActivitySignupWithPostShiftsBinding binding;
    private ArrayAdapter<String> countryArrayAdapter;
    private ArrayAdapter<String> stateArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;
    private HealthInterface apiInterface;

    private ArrayList<Country> countries;
    private ArrayList<City> cities;

    private List<SuccessResGetStates.Result> myStateList = new LinkedList<>();

    private List<SuccessResGetCountries.Result> myCountriesList = new LinkedList<>();
    private ArrayList<String> states;

    private String strRegisterType = "", strfirstName = "", strlastName = "", strCompanyName = "", strCompanyWebsite = "",
            strStreetNumber = "", strStreetName = "", strCountry = "", strState = "", strCity = "", strZip = "", strEmail = "", strCountryCode = "",
            strPhone = "", strPass = "", strConfirmPass = "", strClientDesc = "",strLat = "",strLong ="";

    private String strAddress= "",myCountry = "", myState="";
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_with_post_shifts);

        Places.initialize(getApplicationContext(), getString(R.string.api_key1));
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        strRegisterType = "Company";
        strCountryCode = "CA";

        binding.header.imgHeader.setOnClickListener(v ->
                {

                    AddAddressAct.myAddress = "";
                    finish();
                }
                );

        binding.header.tvHeader.setText(R.string.sign_up_to_post_shifts);

        binding.btnSignup.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupWithPostShiftsActivity.this, LoginAct.class));
                }
        );

        binding.etAddress.setOnClickListener(v ->

                {
                    // return after the user has made a selection.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(SignupWithPostShiftsActivity.this);
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                }
                );

        binding.rlbottom.setOnClickListener(v ->
                {
                    startActivity(new Intent(SignupWithPostShiftsActivity.this, LoginAct.class));
                }
        );


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

    //    getCountries();

//        SpannableStringBuilder ssb=new SpannableStringBuilder();
//        ssb.append("By clicking Signup, you agree to CareShift's Terms of Service and Privacy Policy .");
//
//        ssb.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View v) {
//                //Eredmeny2.this is just the context, name of the whole class
//                Toast.makeText(SignupWithPostShiftsActivity.this, "first", Toast.LENGTH_LONG).show();
//            }
//        }, 45, 62, 0);
//
//        ssb.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SignupWithPostShiftsActivity.this, "second", Toast.LENGTH_LONG).show();
//            }
//        }, 7, 10, 0);
//
////        TextView t1=new TextView(this);
//        binding.loggedInRadio.setText(ssb);

//        binding.tvTermsOfService.setOnClickListener(v ->
//
//                {
//                    String url = "https://www.app.careshifts.net/termsofuse";
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                }
//        );
//
//        binding.tvPP.setOnClickListener(v ->
//                {
//                    String url = "https://www.app.careshifts.net/privacypolicy";
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                }
//        );

        binding.ccp.setOnClickListener(v ->
                {
                    final Dialog dialog = new Dialog(SignupWithPostShiftsActivity.this);
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

        binding.checkboxCompany.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.labelCompanyName.setVisibility(View.VISIBLE);
                    binding.labelCompanyWebsite.setVisibility(View.VISIBLE);
                    binding.checkboxIndividual.setChecked(false);
                    strRegisterType = "Company";
                }
                else
                {
                    binding.checkboxIndividual.setChecked(true);
                    strRegisterType = "Individual";
                }
            }
        });

        binding.checkboxIndividual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.labelCompanyName.setVisibility(View.GONE);
                    binding.labelCompanyWebsite.setVisibility(View.GONE);
                    binding.checkboxCompany.setChecked(false);
                    strRegisterType = "Individual";
                }
                else
                {
                    binding.checkboxCompany.setChecked(true);
                    strRegisterType = "Company";
                }
            }
        });

        binding.btnSignup.setOnClickListener(v ->
                {

                    strfirstName = binding.etFirst.getText().toString().trim();
                    strlastName = binding.etLast.getText().toString().trim();
                    strCompanyName = binding.etCompanyName.getText().toString().trim();
                    strCompanyWebsite = binding.etCompanyWebsite.getText().toString().trim();
                    strStreetNumber = "May be changed";
                    strStreetName = "May be changed";

//                    strCountry = getCountryCode(binding.spinnerCountry.getSelectedItem().toString());
//                    strState = getStateID(binding.spinnerState.getSelectedItem().toString());
//                    strCity = binding.etCity.getText().toString().trim();
//                    strZip = binding.etZipCode.getText().toString().trim();
                    strEmail = binding.etEmail.getText().toString().trim();
                    strPhone = binding.etPhone.getText().toString().trim();
                    strPass = binding.etPass.getText().toString().trim();
                    strConfirmPass = binding.etConfirmPass.getText().toString().trim();
                    strClientDesc = binding.etDesc.getText().toString().trim();

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
                    binding.labelPass.setError(null);
                    binding.labelConfirmPass.setError(null);
                    binding.labelClientDesc.setError(null);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void signup() {

//        if(strLat.contains("-"))
//        {
//            strLat = strLat.replace("-","");
//        }
//        if(strLong.contains("-"))
//        {
//            strLong = strLong.replace("-","");
//        }

        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();
        DataManager.getInstance().showProgressMessage(SignupWithPostShiftsActivity.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("account_type",strRegisterType);
        map.put("first_name",strfirstName);
        map.put("last_name",strlastName);
        map.put("company",strCompanyName);
        map.put("company_website",strCompanyWebsite);
        map.put("street_no",strStreetNumber);
        map.put("street_name",strStreetName);
        map.put("couttry_code",strCountryCode);
        map.put("country",strCountry);
        map.put("state",strState);
        map.put("city",strCity);
        map.put("zipcode",strZip);
        map.put("email",strEmail);
        map.put("phone",strPhone);
        map.put("password",strPass);
        map.put("description",strClientDesc);
        map.put("type","User");
        map.put("register_id","");
        map.put("address",strAddress);
        map.put("lat",strLat);
        map.put("lon",strLong);
        map.put("city_new",strCity);
        map.put("state_new",strState);
        map.put("country_new",strCountry);
        map.put("time_zone",id);

        Call<SuccessResSignup> signupCall = apiInterface.signup(map);
        signupCall.enqueue(new Callback<SuccessResSignup>() {
            @Override
            public void onResponse(Call<SuccessResSignup> call, Response<SuccessResSignup> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignup data = response.body();
                    if (data.status.equals("1")) {
                        showToast(SignupWithPostShiftsActivity.this, data.message);
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        fullScreenDialog();
                    } else if (data.status.equals("0")) {
                        showToast(SignupWithPostShiftsActivity.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignup> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void initializeUI() {
        countries = new ArrayList<>();
        states = new ArrayList<>();
        cities = new ArrayList<>();
        ArrayList<String> spinnerListCountry = new ArrayList<>();
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

    /*    cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cities);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCity.setAdapter(cityArrayAdapter);
*/
//        binding.spinnerCity.setOnItemSelectedListener(city_listener);

    }

    private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {



               String countryCode = getCountryCode(binding.spinnerCountry.getSelectedItem().toString());

               getState(countryCode);

              /*  if (position == 1) {
                    List<String> tempStates = new LinkedList<>();
                    tempStates = Arrays.asList(getResources().getStringArray(R.array.states_canada));
                    stateArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempStates);
                    stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerState.setAdapter(stateArrayAdapter);
                } else {
                    List<String> tempStates = new LinkedList<>();
                    tempStates = Arrays.asList(getResources().getStringArray(R.array.states_us));
                    stateArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempStates);
                    stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerState.setAdapter(stateArrayAdapter);
        }*/


              /*
                final Country country = (Country) binding.spinnerCountry.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: country: "+country.getCountryID());
                ArrayList<State> tempStates = new ArrayList<>();

                tempStates.add(new State(0, new Country(0, "Choose a Country"), "Choose a State"));

                for (State singleState : states) {
                    if (singleState.getCountry().getCountryID() == country.getCountryID()) {
                        tempStates.add(singleState);
                    }
                }

                stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempStates);
                stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerState.setAdapter(stateArrayAdapter);


    */
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
               /* final State state = (State) binding.spinnerState.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: state: "+state.getStateID());
                ArrayList<City> tempCities = new ArrayList<>();

                Country country = new Country(0, "Choose a Country");
                State firstState = new State(0, country, "Choose a State");
                tempCities.add(new City(0, country, firstState, "Choose a City"));

                for (City singleCity : cities) {
                    if (singleCity.getState().getStateID() == state.getStateID()) {
                        tempCities.add(singleCity);
                    }
                }

                cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempCities);
                cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerCity.setAdapter(cityArrayAdapter);*/
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener city_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void createLists() {
        Country country0 = new Country(0, "Choose a Country");
        Country country1 = new Country(1, "Country1");
        Country country2 = new Country(2, "Country2");

        countries.add(new Country(0, "Select Country"));
        countries.add(new Country(1, "Canada"));
        countries.add(new Country(2, "United States"));

        State state0 = new State(0, country0, "Choose a Country");
        State state1 = new State(1, country1, "state1");
        State state2 = new State(2, country1, "state2");
        State state3 = new State(3, country2, "state3");
        State state4 = new State(4, country2, "state4");
/*
        states.add(state0);
        states.add(state1);
        states.add(state2);
        states.add(state3);
        states.add(state4);*/

        states.add("Province / State");
/*
        cities.add(new City(0, country0, state0, "Choose a City"));
        cities.add(new City(1, country1, state1, "City1"));
        cities.add(new City(2, country1, state1, "City2"));
        cities.add(new City(3, country1, state2, "City3"));
        cities.add(new City(4, country2, state2, "City4"));
        cities.add(new City(5, country2, state3, "City5"));
        cities.add(new City(6, country2, state3, "City6"));
        cities.add(new City(7, country2, state4, "City7"));
        cities.add(new City(8, country1, state4, "City8"));*/
    }

    private class Country implements Comparable<Country> {

        private int countryID;
        private String countryName;


        public Country(int countryID, String countryName) {
            this.countryID = countryID;
            this.countryName = countryName;
        }

        public int getCountryID() {
            return countryID;
        }

        public String getCountryName() {
            return countryName;
        }

        @Override
        public String toString() {
            return countryName;
        }

        @Override
        public int compareTo(Country another) {
            return this.getCountryID() - another.getCountryID();//ascending order
//            return another.getCountryID()-this.getCountryID();//descending  order
        }
    }

    private class State implements Comparable<State> {

        private int stateID;
        private Country country;
        private String stateName;

        public State(int stateID, Country country, String stateName) {
            this.stateID = stateID;
            this.country = country;
            this.stateName = stateName;
        }

        public int getStateID() {
            return stateID;
        }

        public Country getCountry() {
            return country;
        }

        public String getStateName() {
            return stateName;
        }

        @Override
        public String toString() {
            return stateName;
        }

        @Override
        public int compareTo(State another) {
            return this.getStateID() - another.getStateID();//ascending order
//            return another.getStateID()-this.getStateID();//descending order
        }
    }

    private class City implements Comparable<City> {

        private int cityID;
        private Country country;
        private State state;
        private String cityName;

        public City(int cityID, Country country, State state, String cityName) {
            this.cityID = cityID;
            this.country = country;
            this.state = state;
            this.cityName = cityName;
        }

        public int getCityID() {
            return cityID;
        }

        public Country getCountry() {
            return country;
        }

        public State getState() {
            return state;
        }

        public String getCityName() {
            return cityName;
        }

        @Override
        public String toString() {
            return cityName;
        }

        @Override
        public int compareTo(City another) {
            return this.cityID - another.getCityID();//ascending order
//            return another.getCityID() - this.cityID;//descending order
        }
    }

    private boolean isValid() {
        if (strfirstName.equalsIgnoreCase("")) {
            binding.labelFirst.setError(getString(R.string.enter_first));
            return false;
        } else if (strlastName.equalsIgnoreCase("")) {
            binding.labelLast.setError(getString(R.string.enter_last));
            return false;
        } else if (strStreetNumber.equalsIgnoreCase("")) {
            binding.labelStreetNum.setError(getString(R.string.enter_street_num));
            return false;
        } else if (strStreetName.equalsIgnoreCase("")) {
            binding.labelStreetName.setError(getString(R.string.enter_street_name));
            return false;
        }else if (strAddress.equalsIgnoreCase("")) {
            Toast.makeText(SignupWithPostShiftsActivity.this, "Please add address.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strEmail.equalsIgnoreCase("")) {
            binding.labelEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.labelEmail.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (strPhone.equalsIgnoreCase("")) {
            binding.tvPhoneError.setVisibility(View.VISIBLE);
            binding.llPhone.setBackgroundResource(R.drawable.red_et_bg);
            return false;
        } else if (strPass.equalsIgnoreCase("")) {
            binding.labelPass.setError(getString(R.string.enter_pass));
            return false;
        } else if (strConfirmPass.equalsIgnoreCase("")) {
            binding.labelConfirmPass.setError(getString(R.string.enter_confirm_pass));
            return false;
        } else if (!strPass.equalsIgnoreCase(strConfirmPass)) {
            binding.labelConfirmPass.setError(getString(R.string.pass_and_confirm_pass_not_macthed));
            return false;
        } else if (strClientDesc.equalsIgnoreCase("")) {
            binding.labelClientDesc.setError(getString(R.string.enter_desc));
            return false;
        }
        return true;
    }

  /*
    else if (strCountry.equalsIgnoreCase("")) {
        binding.rlCountry.setBackgroundResource(R.drawable.red_et_bg);
        Toast.makeText(SignupWithPostShiftsActivity.this, "" + getString(R.string.select_country), Toast.LENGTH_SHORT).show();
        return false;
    } else if (strState.equalsIgnoreCase("")) {
        binding.rlState.setBackgroundResource(R.drawable.red_et_bg);
        Toast.makeText(SignupWithPostShiftsActivity.this, "" + getString(R.string.select_state), Toast.LENGTH_SHORT).show();
        return false;
    } else if (strCity.equalsIgnoreCase("")) {
        binding.labelCity.setError(getString(R.string.enter_City));
        return false;
    } else if (strZip.equalsIgnoreCase("")) {
        binding.labelZipCode.setError(getString(R.string.enter_zip));
        return false;
    }
*/
    public void getCountries() {
        DataManager.getInstance().showProgressMessage(SignupWithPostShiftsActivity.this, getString(R.string.please_wait));
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
                        showToast(SignupWithPostShiftsActivity.this, data.message);
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


    public void getState(String contryCode)
    {

        DataManager.getInstance().showProgressMessage(SignupWithPostShiftsActivity.this, getString(R.string.please_wait));
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
                        showToast(SignupWithPostShiftsActivity.this, data.message);
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


    private void fullScreenDialog() {

        dialog = new Dialog(this, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(R.layout.activity_register_success);

        TextView tvEmail ;


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