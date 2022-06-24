package com.shifts.healthcare.workerSide.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;
//
//import com.stripe.android.ApiResultCallback;
//import com.stripe.android.Stripe;
//import com.stripe.android.model.Card;
//import com.stripe.android.model.Token;
//

import com.shifts.healthcare.R;
import com.shifts.healthcare.activites.ConversationAct;
import com.shifts.healthcare.databinding.FragmentDocumentBinding;
import com.shifts.healthcare.models.SuccessResGetDocuments;
import com.shifts.healthcare.models.SuccessResGetToken;
import com.shifts.healthcare.models.SuccessResGetUnseenMessageCount;
import com.shifts.healthcare.models.SuccessResUploadDocument;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DocumentFragment extends Fragment {

    FragmentDocumentBinding binding;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private SuccessResGetDocuments.Result documents ;
    private String status = "";
    String cardNo ="",expirationDate="",cvv = "",cardType = "",holderName="",expirationMonth = "",expirationYear = "";
    private Dialog dialog;
    public int gov =  101;
    public int degree =  102;
    public int professional =  103;
    public int resume =  104;
    public int firstaid =  105;
    public int covidresult =  106;
    public int covidVaccin =  107;
    public int immune =  108;
    public int tb =  109;
    public int vulnerable =  110;
    public int currentFlu =  111;
    public int othervaccine =  112;
    public int n95 =  113;
    public int cpi =  114;
    public int additional =  115;
    HealthInterface apiInterface;
    RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
    MultipartBody.Part filePartGovId = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filePartDegree = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filePartProfessionalLicences = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filePartResume = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filePartFirstAid = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filePartCovidResult = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filePartCovidVaccine = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filePartImmune = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filPartTB = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filPartVulnerable = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filPartCurrentFlu = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filPartOtherVaccine = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filPartN95 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filPartCPI = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
    MultipartBody.Part filPartAdditionalDocument = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static DocumentFragment newInstance(String param1, String param2) {
        DocumentFragment fragment = new DocumentFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_document, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        checkPermisssionForReadStorage();

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf"};

        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
        intent1.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent1.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent1.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent1.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }

        binding.tvCLickHere.setOnClickListener(v ->
                {
               showImageSelection();
                }
                );

        binding.tvGovPhoto.setOnClickListener(v ->
                {

//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "Select Image"), gov);

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");
                    startActivityForResult(intent, gov);

                }
                );

        binding.tvDegree.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, degree);
                }
        );

        binding.tvProfessionalLicences.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, professional);
                }
        );
        binding.tvResume.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, resume);
                }
        );

        binding.tvFirstAid.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, firstaid);
                }
        );

        binding.tvCurrentCovid.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, covidresult);
                }
        );

        binding.tvCovidVaccine.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, covidVaccin);
                }
        );

        binding.tvImmune.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, immune);
                }
        );

        binding.tvTB.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, tb);
                }
        );

        binding.tvVulnerable.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, vulnerable);
                }
        );

        binding.tvCurrentFlue.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, currentFlu);
                }
        );

        binding.tvOtherVaccination.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, othervaccine);
                }
        );

        binding.tvN95Mask.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, n95);
                }
        );

        binding.tvCpi.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, cpi);
                }
        );

        binding.tvAdditionalDoc.setOnClickListener(v ->
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, additional);
                }
        );

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        getGovPhotoIDDocuments("gove_phot_id");
        getGovPhotoIDDocuments("degree");
        getGovPhotoIDDocuments("pro_licence");
        getGovPhotoIDDocuments("resume");
        getGovPhotoIDDocuments("firstaid_cpr");
        getGovPhotoIDDocuments("covid_result");
        getGovPhotoIDDocuments("covid_vaccination");
        getGovPhotoIDDocuments("immunization_record");
        getGovPhotoIDDocuments("tb_sceeenig");
        getGovPhotoIDDocuments("vulnerable");
        getGovPhotoIDDocuments("c_flu");
        getGovPhotoIDDocuments("other_vaccination");
        getGovPhotoIDDocuments("n95_mask");
        getGovPhotoIDDocuments("cpi");
        getGovPhotoIDDocuments("additional_document");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().hideProgressMessage();

            }
        },6000);

        return binding.getRoot();
    }

    public void addDocuments(String type,MultipartBody.Part filePart )
    {
        String strUserId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody docType = RequestBody.create(MediaType.parse("text/plain"), type);

//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResUploadDocument> loginCall = apiInterface.uploadDocuments(userId,docType,filePart );
        loginCall.enqueue(new Callback<SuccessResUploadDocument>() {
            @Override
            public void onResponse(Call<SuccessResUploadDocument> call, Response<SuccessResUploadDocument> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadDocument data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);
                    getActivity().getWindow().getDecorView().clearFocus();

                    getGovPhotoIDDocuments(type);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadDocument> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {

            if(requestCode == gov)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());

                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePartDegree = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));

                addDocuments("gove_phot_id",filePartGovId);

            }

            else if(requestCode == degree)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());

                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePartDegree = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("degree",filePartDegree);


            }else if(requestCode == professional)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());

                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePartProfessionalLicences = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("pro_licence",filePartProfessionalLicences);

            }else if(requestCode == resume)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());

                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePartResume = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("resume",filePartResume);

            }else if(requestCode == firstaid)
             {
            String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePartFirstAid = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                 addDocuments("firstaid_cpr",filePartFirstAid);

             }

            else if(requestCode == covidresult)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePartCovidResult = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("covid_result",filePartCovidResult);

            }
            else if(requestCode == covidVaccin)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePartCovidVaccine = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("covid_vaccination",filePartCovidVaccine);

            }
            else if(requestCode == immune)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filePartImmune = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("immunization_record",filePartImmune);

            }

            else if(requestCode == tb)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filPartTB = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("tb_sceeenig",filPartTB);

            }

            else if(requestCode == vulnerable)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filPartVulnerable = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("vulnerable",filPartVulnerable);
            }

            else if(requestCode == currentFlu)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filPartCurrentFlu = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("c_flu",filPartCurrentFlu);
            }

            else if(requestCode == othervaccine)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filPartOtherVaccine = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("other_vaccination",filPartOtherVaccine);
            }

            else if(requestCode == n95)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filPartN95 = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("n95_mask",filPartN95);

            }

            else if(requestCode == cpi)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filPartCPI = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("cpi",filPartCPI);

            }

            else if(requestCode == additional)
            {
                String  str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
                filPartAdditionalDocument = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                addDocuments("additional_document",filPartAdditionalDocument);
            }

        }

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

    private ArrayList<SuccessResGetDocuments.Result> documentsList ;

    public void getGovPhotoIDDocuments(String type)
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("type",type);

        Call<SuccessResGetDocuments> call = apiInterface.getWorkerDocuments(map);
        call.enqueue(new Callback<SuccessResGetDocuments>() {
            @Override
            public void onResponse(Call<SuccessResGetDocuments> call, Response<SuccessResGetDocuments> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetDocuments data = response.body();
                    if (data.status.equals("1")) {
                        documentsList = new ArrayList<>();
                        documentsList.addAll(data.getResult());
                        documents = data.getResult().get(0);
                        status = type;
                        setDocumentsButtons();

                    } else {
                       // showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetDocuments> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setDocumentsButtons()
    {

        if(status.equalsIgnoreCase("gove_phot_id"))
        {
            binding.tvGovPhotoNotUploaded.setVisibility(View.GONE);
            binding.tvGovPhotoUploaded.setVisibility(View.VISIBLE);
            binding.tvGovPhotoUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }
        else  if(status.equalsIgnoreCase("degree"))
        {
            binding.tvDegreeNotUploaded.setVisibility(View.GONE);
            binding.tvDegreeUploaded.setVisibility(View.VISIBLE);
            binding.tvDegreeUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        } else  if(status.equalsIgnoreCase("pro_licence"))

        {
            binding.tvProfessionalLicencesNotUploaded.setVisibility(View.GONE);
            binding.tvProfessionalLicencesUploaded.setVisibility(View.VISIBLE);
            binding.tvProfessionalLicencesUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("resume"))

        {
            binding.tvResumeNotUploaded.setVisibility(View.GONE);
            binding.tvResumeUploaded.setVisibility(View.VISIBLE);
            binding.tvResumeUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("firstaid_cpr"))

        {
            binding.tvFirstAidNotUploaded.setVisibility(View.GONE);
            binding.tvFirstAidUploaded.setVisibility(View.VISIBLE);
            binding.tvFirstAidUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("covid_result"))

        {
            binding.tvCurrentCovidNotUploaded.setVisibility(View.GONE);
            binding.tvCurrentCovidUploaded.setVisibility(View.VISIBLE);
            binding.tvCurrentCovidUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("covid_vaccination"))
        {
            binding.tvCovidVaccineNotUploaded.setVisibility(View.GONE);
            binding.tvCovidVaccineUploaded.setVisibility(View.VISIBLE);
            binding.tvCovidVaccineUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("immunization_record"))
        {
            binding.tvImmuneNotUploaded.setVisibility(View.GONE);
            binding.tvImmuneUploaded.setVisibility(View.VISIBLE);
            binding.tvImmuneUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("tb_sceeenig"))
        {
            binding.tvTBNotUploaded.setVisibility(View.GONE);
            binding.tvTBUploaded.setVisibility(View.VISIBLE);
            binding.tvTBUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("vulnerable"))
        {
            binding.tvVulnerableNotUploaded.setVisibility(View.GONE);
            binding.tvVulnerableUploaded.setVisibility(View.VISIBLE);
            binding.tvVulnerableUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("c_flu"))
        {
            binding.tvCurrentFlueNotUploaded.setVisibility(View.GONE);
            binding.tvCurrentFlueUploaded.setVisibility(View.VISIBLE);
            binding.tvCurrentFlueUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("other_vaccination"))
        {
            binding.tvOtherVaccinationNotUploaded.setVisibility(View.GONE);
            binding.tvOtherVaccinationUploaded.setVisibility(View.VISIBLE);
            binding.tvOtherVaccinationUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("n95_mask"))
        {
            binding.tvN95MaskNotUploaded.setVisibility(View.GONE);
            binding.tvN95MaskUploaded.setVisibility(View.VISIBLE);
            binding.tvN95MaskUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("cpi"))
        {
            binding.tvCpiNotUploaded.setVisibility(View.GONE);
            binding.tvCpiUploaded.setVisibility(View.VISIBLE);
            binding.tvCpiUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }else  if(status.equalsIgnoreCase("additional_document"))
        {
            binding.tvAdditionalDocNotUploaded.setVisibility(View.GONE);
            binding.tvAdditionalDocUploaded.setVisibility(View.VISIBLE);
            binding.tvAdditionalDocUploaded.setText(getString(R.string.uploaded)+"("+documentsList.size()+")");
        }
    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_redirect_to_certn);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);
        AppCompatButton btnRedirect = dialog.findViewById(R.id.btnShiftAccepted);
        AppCompatButton btnCncel = dialog.findViewById(R.id.btnReject);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        btnCncel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
                );

        btnRedirect.setOnClickListener(v ->
                {
                    fullScreenDialog();
                    dialog.dismiss();
                }
                );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    TextView tvMessageCount;

    private void fullScreenDialog() {

        dialog = new Dialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_add_card);
        AppCompatButton btnAdd =  dialog.findViewById(R.id.btnAdd);

        MaterialCheckBox checkBox = dialog.findViewById(R.id.defaultCheckBox);

        tvMessageCount = dialog.findViewById(R.id.tvMessageCount);

        RelativeLayout rlChat = dialog.findViewById(R.id.rlChat);

        ImageView ivBack;
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        ivBack = dialog.findViewById(R.id.ivBack);
        CardForm cardForm = dialog.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .expirationRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .cvvRequired(true)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel("Purchase")
                .setup((AppCompatActivity) getActivity());

        rlChat.setOnClickListener(v ->
                {
                    startActivity(new Intent(getActivity(), ConversationAct.class));
                }
                );

        getUnseenNotificationCount();

        tvMessage.setVisibility(View.VISIBLE);

        checkBox.setVisibility(View.GONE);

        btnAdd.setText(getString(R.string.pay));

        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                cardNo = cardForm.getCardNumber();
                expirationDate = cardForm.getExpirationMonth()+"/"+cardForm.getExpirationYear();
                expirationMonth = cardForm.getExpirationMonth();
                expirationYear = cardForm.getExpirationYear();
                cvv =  cardForm.getCvv();
                cardType = "";
                holderName = cardForm.getCardholderName();
                if(cardForm.isValid())
                {
//                  clickOnPayNow();


                    getToken();
//                    DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            paymentSuccess();
//                            dialog.dismiss();
//                            DataManager.getInstance().hideProgressMessage();
//                        }
//                    },5000);

                }else
                {
                    cardForm.validate();
                }
            }
        });

        btnAdd.setOnClickListener(v ->
                {
                    cardNo = cardForm.getCardNumber();
                    expirationDate = cardForm.getExpirationDateEditText().getText().toString();
                    cardType = "";
                    expirationMonth = cardForm.getExpirationMonth();
                    holderName = cardForm.getCardholderName();
                    expirationYear = cardForm.getExpirationYear();
                    cvv = cardForm.getCvv();
                    if(cardForm.isValid())
                    {

                        getToken();

//                        clickOnPayNow();
//                        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                paymentSuccess();
//                                dialog.dismiss();
//                                DataManager.getInstance().hideProgressMessage();
//                            }
//                        },5000);


                    }else
                    {
                        cardForm.validate();
                    }
                }
        );
        ivBack.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );
        dialog.show();
    }

    public  void getUnseenNotificationCount()
    {

        String userId = SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetUnseenMessageCount> call = apiInterface.getUnseenMessage(map);

        call.enqueue(new Callback<SuccessResGetUnseenMessageCount>() {
            @Override
            public void onResponse(Call<SuccessResGetUnseenMessageCount> call, Response<SuccessResGetUnseenMessageCount> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    SuccessResGetUnseenMessageCount data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        int unseenNoti = Integer.parseInt(data.getResult().getTotalUnseenMessage());

                        if(unseenNoti!=0)
                        {

                            tvMessageCount.setVisibility(View.VISIBLE);
                            tvMessageCount.setText(unseenNoti+"");

                        }
                        else
                        {

                            tvMessageCount.setVisibility(View.GONE);

                        }

                    } else if (data.status.equals("0")) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetUnseenMessageCount> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    private String token = "";

    public void getToken()
    {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("card_number",cardNo);
        map.put("expiry_year",expirationYear);
        map.put("expiry_month",expirationMonth);
        map.put("cvc_code",cvv);

        Call<SuccessResGetToken> call = apiInterface.getToken(map);
        call.enqueue(new Callback<SuccessResGetToken>() {
            @Override
            public void onResponse(Call<SuccessResGetToken> call, Response<SuccessResGetToken> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetToken data = response.body();
                    if (data.status == 1) {

                        Log.d(TAG, "onResponse: "+token);

                        token = data.getResult().getId();

                        if(token==null)
                        {
                            showToast(getActivity(),"Invalid card details.");
                        }
                        else
                        {
                            callPaymentApi(token);
                        }

                    } else {
                        showToast(getActivity(), data.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(Call<SuccessResGetToken> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });
    }

    private void clickOnPayNow() {

//        Card.Builder card = new Card.Builder(cardNo,
//                Integer.parseInt(expirationMonth),
//                Integer.parseInt(expirationYear),
//                cvv);
//
//        if (!card.build().validateCard()) {
//            cardNo = "";
//            expirationDate = "";
//            cvv = "";
//            showToast(getActivity(),"Please Enter valid card details.");
//            return;
//        }
//
//        Stripe stripe = new Stripe(getActivity(), "pk_test_51Jl1kpIzhVsEreKHYKdvN0fLZUv3xQaOjf4W73C3qvTAMexMXcbJP5SwioNPbeeh6o2cP2ygdUrlV8oBfH2VAH9f000YseP4ES");
//
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        stripe.createCardToken(
//                card.build(), new ApiResultCallback<Token>() {
//                    @Override
//                    public void onSuccess(@NotNull Token token) {
//                        DataManager.getInstance().hideProgressMessage();
//                        callPaymentApi(token.getId());
//                    }
//
//                    @Override
//                    public void onError(@NotNull Exception e) {
//                        showToast(getActivity(),e.getMessage());
//                        DataManager.getInstance().hideProgressMessage();
//                    }
//                });
    }

    private void callPaymentApi(String token)
    {

        Random rand = new Random();
        int maxNumber = 10000;
        int randomNumber = rand.nextInt(maxNumber) + 1;
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        String userId = SharedPreferenceUtility.getInstance(getContext()).getString(USER_ID);
        Map<String, String> map = new HashMap<>();
        map.put("worker_id", userId);
        map.put("request_id", randomNumber+"");
        map.put("amount", "56.5");
        map.put("currency", "CAD");
        map.put("token", token);

        Call<ResponseBody> call = apiInterface.workerPayment(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());

                    String data = jsonObject.getString("status");

                    String message = jsonObject.getString("message");

                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        dialog.dismiss();
                        paymentSuccess();
//                        getActivity().onBackPressed();
                    } else if (data.equals("0")) {
                        showToast(getActivity(), message);
                    }else if (data.equals("2")) {
                        showToast(getActivity(), jsonObject.getString("result"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void paymentSuccess() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_success);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialog.setCanceledOnTouchOutside(false);

        AppCompatButton appCompatButton = dialog.findViewById(R.id.btnLogin);

        ImageView ivCancel = dialog.findViewById(R.id.cancel);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        appCompatButton.setOnClickListener(v ->
                {

                    String url = "https://lime.certn.co/browse/packages/87dfca5a-25b8-4ede-ae53-1a193209c9e9";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private String getPathFromUri(Uri uri) {
        String[] filePathColumn = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String Path = cursor.getString(columnIndex);
        cursor.close();
        return Path;
    }

    private void copy(File source, File destination) throws IOException {

        FileChannel in = new FileInputStream(source).getChannel();
        FileChannel out = new FileOutputStream(destination).getChannel();

        try {
            in.transferTo(0, in.size(), out);
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }

}