package com.shifts.healthcare.workerSide.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.gson.Gson;
import com.shifts.healthcare.BuildConfig;
import com.shifts.healthcare.R;
import com.shifts.healthcare.activites.LoginAct;
import com.shifts.healthcare.databinding.FragmentWorkerEditProfileBinding;
import com.shifts.healthcare.models.SuccessResGetCountries;
import com.shifts.healthcare.models.SuccessResGetJobPositions;
import com.shifts.healthcare.models.SuccessResGetProfile;
import com.shifts.healthcare.models.SuccessResGetStates;
import com.shifts.healthcare.models.SuccessResGetWorkerProfile;
import com.shifts.healthcare.models.SuccessResUpdateProfile;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.isValidEmail;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerEditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerEditProfileFragment extends Fragment {

    private ArrayAdapter<String> stateArrayAdapter;
    private ArrayAdapter<String> countryArrayAdapter;
    private List<SuccessResGetJobPositions.Result> jobPositionsList = new LinkedList<>();
    private ArrayList<SuccessResGetWorkerProfile.Result> userList = new ArrayList<>();

    FragmentWorkerEditProfileBinding binding;
    private String  strfirstName = "", strlastName = "",
            strStreetNumber = "", strStreetName = "", strAppartNumber = "",  strCity = "", strZip = "", strEmail = "", strCountryCode = "",
            strPhone = "",  strClientDesc = "",strWorkerDesignation = "",strLat = "",strLong ="";

    private String strAddress= "";

    String str_image_path="";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Uri uriSavedImage;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private String selectedStateCode = "";
    private String selectedCountryCode = "",selectedJobPosition = "";
    HealthInterface apiInterface;
    private List<SuccessResGetStates.Result> myStateList = new LinkedList<>();
    private List<SuccessResGetCountries.Result> myCountriesList = new LinkedList<>();
    ArrayList<SuccessResGetProfile.Result> transactionList = new ArrayList<>();

    private static int AUTOCOMPLETE_REQUEST_CODE = 9;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerEditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerEditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerEditProfileFragment newInstance(String param1, String param2) {
        WorkerEditProfileFragment fragment = new WorkerEditProfileFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_worker_edit_profile, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        Places.initialize(getActivity().getApplicationContext(), getString(R.string.api_key1));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity());

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getProfile();

            getCountries();
            getJobPositions();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }



        binding.ivCamera.setOnClickListener(v ->
                {
                    if(checkPermisssionForReadStorage())
                        showImageSelection();
                }
        );

        binding.ccp.setOnClickListener(v ->
                {
                    final Dialog dialog = new Dialog(getActivity());
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

        binding.btnSave.setOnClickListener(v ->
                {
                    strfirstName = binding.etFirst.getText().toString().trim();
                    strlastName = binding.etLast.getText().toString().trim();
                    strEmail = binding.etEmail.getText().toString().trim();
                    strStreetNumber = binding.etStreetNum.getText().toString();
                    strStreetName = binding.etStreetName.getText().toString();;
                    strAppartNumber = binding.etAppart.getText().toString().trim();
//                    strCountry = getCountryCode(binding.spinnerCountry.getSelectedItem().toString());
//                    strState = getStateID(binding.spinnerState.getSelectedItem().toString());
                    strCity = binding.etCity.getText().toString().trim();
                    strZip = binding.etZipCode.getText().toString().trim();
                    strPhone = binding.etPhone.getText().toString().trim();
                    strWorkerDesignation = getSelectedJobPositionCode(binding.spinnerJobPosition.getSelectedItem().toString());


                    binding.labelFirst.setError(null);
                    binding.labelLast.setError(null);
                    binding.labelStreetNum.setError(null);
                    binding.labelStreetName.setError(null);
                    binding.rlCountry.setBackgroundResource(R.drawable.et_bg);
                    binding.rlState.setBackgroundResource(R.drawable.et_bg);
                    binding.rlSpinnerJob.setBackgroundResource(R.drawable.et_bg);
                    binding.labelCity.setError(null);
                    binding.labelZipCode.setError(null);
                    binding.labelEmail.setError(null);
                    binding.tvPhoneError.setVisibility(View.GONE);
                    binding.llPhone.setBackgroundResource(R.drawable.et_bg);
                    binding.labelAppartNum.setError(null);


                    if (isValid()) {

                        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {

                            updateProfile();

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();

                    }

                }
        );

//        binding.etAddress.setOnClickListener(v ->
//                {
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


        return binding.getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(LoginAct.TAG, "Place: " + place.getName() + ", " + place.getId());

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
                Log.i(LoginAct.TAG, status.getStatusMessage());


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }

        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivProfile);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Micasa/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Micasa/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(getActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, REQUEST_CAMERA);

    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)

            ) {

                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {
            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
/*

                    Log.e("Latittude====", gpsTracker.getLatitude() + "");

                    strLat = Double.toString(gpsTracker.getLatitude()) ;
                    strLng = Double.toString(gpsTracker.getLongitude()) ;
*/

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
//                    }
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);

        ImageView ivCancel = dialog.findViewById(R.id.cancel);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
                );

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



    public String getCountryCode(String strCountryName) {
        for (SuccessResGetCountries.Result country : myCountriesList) {
            if (country.getName().equalsIgnoreCase(strCountryName)) {
                return country.getId();
            }
        }

        return "";
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

    public int getSpinnerCountryPosition(String code)
    {
        int i=1;
        for (SuccessResGetCountries.Result country : myCountriesList) {
            if (country.getId().equalsIgnoreCase(code)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    public int getSpinnerStatePosition(String code)
    {
        int i=1;
        for (SuccessResGetStates.Result state:myStateList)
        {
            if(state.getId().equalsIgnoreCase(code))
            {
                return i;
            }
            i++;
        }
        return 0;
    }
    public int getSpinnerJobPositionPosition(String code)
    {
        int i=1;
        for (SuccessResGetJobPositions.Result state:jobPositionsList)
        {
            if(state.getId().equalsIgnoreCase(code))
            {
                return i;
            }
            i++;
        }
        return 0;
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

    public void setStatesSpinner()
    {

        List<String> tempStates = new LinkedList<>();

        tempStates.add("Province / State");

        for (SuccessResGetStates.Result state:myStateList)
        {
            tempStates.add(state.getName());
        }

        stateArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tempStates);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerState.setAdapter(stateArrayAdapter);
        int position = getSpinnerStatePosition(selectedStateCode);
        binding.spinnerState.setSelection(position);
    }

    private void setCountrySpinner()
    {
        ArrayList<String> spinnerListCountry = new ArrayList<>();
//        createLists();
        spinnerListCountry.add("Select Country");
        for (SuccessResGetCountries.Result country : myCountriesList) {
            spinnerListCountry.add(country.getName());
        }
        countryArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerListCountry);
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCountry.setAdapter(countryArrayAdapter);
        binding.spinnerCountry.setOnItemSelectedListener(country_listener);
        binding.spinnerCountry.setSelection(getSpinnerCountryPosition(selectedCountryCode));
    }

    public void getCountries() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();

        Call<SuccessResGetCountries> call = apiInterface.getCountries(map);
        call.enqueue(new Callback<SuccessResGetCountries>() {
            @Override
            public void onResponse(Call<SuccessResGetCountries> call, Response<SuccessResGetCountries> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetCountries data = response.body();
                    if (data.status.equals("1")) {
                        myCountriesList.clear();
                        myCountriesList.addAll(data.getResult());
                        setCountrySpinner();
                    } else {
//                        showToast(getActivity(), data.message);
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

    public void getState(String contryCode)
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
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
                        myStateList.clear();
                        myStateList.addAll(data.getResult());
                        setStatesSpinner();
                    } else {
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
        binding.spinnerJobPosition.setSelection(getSpinnerJobPositionPosition(selectedJobPosition));
    }

    private void updateProfile()
    {
        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            if(file!=null)
            {
                filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
            else
            {
                filePart = null;
            }
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody workerId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), strfirstName);
        RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), strlastName);
        RequestBody streetNo = RequestBody.create(MediaType.parse("text/plain"), strStreetNumber);
        RequestBody streetName = RequestBody.create(MediaType.parse("text/plain"), strStreetName);
        RequestBody appartNum = RequestBody.create(MediaType.parse("text/plain"), strAppartNumber);
        RequestBody countryCode = RequestBody.create(MediaType.parse("text/plain"), strCountryCode);
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), strCity);
        RequestBody zipCode = RequestBody.create(MediaType.parse("text/plain"), strZip);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmail);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), strPhone);
        RequestBody designation = RequestBody.create(MediaType.parse("text/plain"), strWorkerDesignation);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), strAddress);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), strLat);
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), strLong);
        Call<SuccessResUpdateProfile> loginCall = apiInterface.updateWorkerProfile(workerId,firstName,lastName,streetNo,
                streetName,appartNum,countryCode,country,state,city,zipCode,email,phone,designation,
                address,lat,lon
                ,filePart
        );
        loginCall.enqueue(new Callback<SuccessResUpdateProfile>() {
            @Override
            public void onResponse(Call<SuccessResUpdateProfile> call, Response<SuccessResUpdateProfile> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateProfile data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);
                    getActivity().getWindow().getDecorView().clearFocus();
                    getActivity().onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
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
        } else if(strAddress.equalsIgnoreCase(""))
        {
            Toast.makeText(getActivity(), "Please add address.", Toast.LENGTH_SHORT).show();
            return false;
        }
            else if (strPhone.equalsIgnoreCase("")) {
            binding.tvPhoneError.setVisibility(View.VISIBLE);
            binding.llPhone.setBackgroundResource(R.drawable.red_et_bg);
            return false;
        }else if (strWorkerDesignation.equalsIgnoreCase("")) {
            binding.rlSpinnerJob.setBackgroundResource(R.drawable.red_et_bg);
            Toast.makeText(getActivity(), "" + getString(R.string.select_job_position), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                        setUserDetails(userList);

                    } else {
//                        showToast(getActivity(), data.message);
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


    public void setUserDetails(ArrayList<SuccessResGetWorkerProfile.Result> userList)

    {

        SuccessResGetWorkerProfile.Result user = userList.get(0);

        binding.etFirst.setText(user.getFirstName());
        binding.etLast.setText(user.getLastName());
        binding.etStreetNum.setText(user.getStreetNo());
        binding.etStreetName.setText(user.getStreetName());
        binding.etCity.setText(user.getCity());
        binding.etZipCode.setText(user.getZipcode());
        binding.etEmail.setText(user.getEmail());
        binding.etPhone.setText(user.getPhone());
        binding.etAppart.setText(user.getApartmentNo());

        selectedStateCode = user.getState();
        selectedCountryCode = user.getCountry();
        selectedJobPosition = user.getWorkerDesignation();

        binding.etAddress.setText(user.getAddress());

        strAddress = user.getAddress();
        strLat = user.getLat();
        strLong = user.getLon();

        Glide.with(getActivity())
                .load(user.getImage())
                .centerCrop()
                .into(binding.ivProfile);

        int countryPosition = getSpinnerCountryPosition(user.getCountry());

        if(user.getCouttryCode().equalsIgnoreCase("CA"))
        {
            binding.ivCanada.setImageResource(R.drawable.ic_canada);
            binding.tvCanada.setText("CA +1");
        } else

        {
            binding.ivCanada.setImageResource(R.drawable.flag_united_states_of_america);
            binding.tvCanada.setText("US +1");
        }

        getState(user.getCountry());

    }


    public String getSelectedJobPositionCode(String strCountryName) {
        for (SuccessResGetJobPositions.Result job : jobPositionsList) {
            if (job.getName().equalsIgnoreCase(strCountryName)) {
                return job.getId();
            }
        }
        return "";
    }




}