package com.shifts.healthcare.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.shifts.healthcare.R;
import com.shifts.healthcare.adapters.InvoiceUserAdapter;
import com.shifts.healthcare.databinding.FragmentInvoicesBinding;
import com.shifts.healthcare.models.SuccessResGetInvoices;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.DownloadInvoice;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.service.controls.ControlsProviderService.TAG;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;
import static com.shifts.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvoicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class InvoicesFragment extends Fragment implements DownloadInvoice {

    FragmentInvoicesBinding binding;
    private ArrayList<SuccessResGetInvoices.Result> invoicesList = new ArrayList<>();
    private String type = "All";
    HealthInterface apiInterface;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String URL = "http://www.codeplayon.com/samples/resume.pdf";
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (lDownloadID == id) {
                Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };
    static final File fSdcard = new File(Environment.getExternalStorageDirectory().toString(), "VDTEC");
    static final String sUrlManualMult = "http://maven.apache.org/maven-1.x/maven.pdf";
    static final File fManualMult = new File(fSdcard, "CareShiftInvoice.pdf");
    private long lDownloadID;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public InvoicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvoicesFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static InvoicesFragment newInstance(String param1, String param2) {
        InvoicesFragment fragment = new InvoicesFragment();
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_invoices, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        if (NetworkAvailablity.getInstance(getActivity()).checkNetworkStatus()) {
            getShifts();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }

        getActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchInvoice(binding.etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

//
//        binding.tvAll.setOnClickListener(v ->
//                {
//                    type  = "All";
//                    binding.tvAll.setBackground(getResources().getDrawable(R.drawable.button_bg));
//                    binding.tvAll.setTextColor(getResources().getColor(R.color.white));
//                    binding.tvUnpaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
//                    binding.tvUnpaid.setTextColor(getResources().getColor(R.color.black));
//                    binding.tvPaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
//                    binding.tvPaid.setTextColor(getResources().getColor(R.color.black));
//                    getShifts();
//                }
//        );
//
//        binding.tvUnpaid.setOnClickListener(v ->
//                {
//                    type  = "Unpaid";
//                    binding.tvUnpaid.setBackground(getResources().getDrawable(R.drawable.button_bg));
//                    binding.tvUnpaid.setTextColor(getResources().getColor(R.color.white));
//                    binding.tvAll.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
//                    binding.tvAll.setTextColor(getResources().getColor(R.color.black));
//                    binding.tvPaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
//                    binding.tvPaid.setTextColor(getResources().getColor(R.color.black));
//                    getShifts();
//                }
//        );
//        binding.tvPaid.setOnClickListener(v ->
//                {
//                    type  = "Paid";
//                    binding.tvPaid.setBackground(getResources().getDrawable(R.drawable.button_bg));
//                    binding.tvPaid.setTextColor(getResources().getColor(R.color.white));
//                    binding.tvAll.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
//                    binding.tvAll.setTextColor(getResources().getColor(R.color.black));
//                    binding.tvUnpaid.setBackground(getResources().getDrawable(R.drawable.light_grey_button_bg));
//                    binding.tvUnpaid.setTextColor(getResources().getColor(R.color.black));
//                    getShifts();
//                }
//        );
        return binding.getRoot();
    }


    public void searchInvoice(String title)
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type",type);
        map.put("invoice_no",title);
        Call<SuccessResGetInvoices> call = apiInterface.searchUserInvoice(map);
        call.enqueue(new Callback<SuccessResGetInvoices>() {
            @Override
            public void onResponse(Call<SuccessResGetInvoices> call, Response<SuccessResGetInvoices> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInvoices data = response.body();
                    if (data.status.equals("1")) {
                        binding.etSearch.clearFocus();
                        invoicesList.clear();
                        invoicesList.addAll(data.getResult());
                        binding.rvInvoices.setHasFixedSize(true);
                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvInvoices.setAdapter(new InvoiceUserAdapter(getActivity(),invoicesList,InvoicesFragment.this::donwloadInvoice));
                    } else {
//                        showToast(getActivity(), data.message);
                        binding.etSearch.clearFocus();
                        invoicesList.clear();
                        invoicesList.addAll(data.getResult());
                        binding.rvInvoices.setHasFixedSize(true);
                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvInvoices.setAdapter(new InvoiceUserAdapter(getActivity(),invoicesList,InvoicesFragment.this::donwloadInvoice));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetInvoices> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public void getShifts()
    {
        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type",type);
        Call<SuccessResGetInvoices> call = apiInterface.getUserInvoice(map);
        call.enqueue(new Callback<SuccessResGetInvoices>() {
            @Override
            public void onResponse(Call<SuccessResGetInvoices> call, Response<SuccessResGetInvoices> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetInvoices data = response.body();
                    if (data.status.equals("1")) {
                        invoicesList.clear();
                        invoicesList.addAll(data.getResult());
                        binding.rvInvoices.setHasFixedSize(true);
                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvInvoices.setAdapter(new InvoiceUserAdapter(getActivity(),invoicesList,InvoicesFragment.this::donwloadInvoice));
                    } else {
//                        showToast(getActivity(), data.message);
                        invoicesList.clear();
                        binding.rvInvoices.setHasFixedSize(true);
                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.rvInvoices.setAdapter(new InvoiceUserAdapter(getActivity(),invoicesList,InvoicesFragment.this::donwloadInvoice));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetInvoices> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(onDownloadComplete);
    }
    @Override
    public void donwloadInvoice(String companyId, String productId) {
        Log.d(TAG, "donwloadInvoice: ");
//        if(requestPermision())
//        {
//            download(sUrlManualMult);
//        }

    }

    public void download(String url)
    {
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url))
                .setMimeType("application/pdf")
                .setTitle("Dummy File")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(fManualMult))// Uri of the destination file
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        request.allowScanningByMediaScanner();
        DownloadManager downloadManager= (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        lDownloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
    }
    private final static int MY_PERMISSIONS_CONSTANT = 101;
    public boolean requestPermision()
    {
        Context context = getActivity();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
           requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_CONSTANT);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_CONSTANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    download(sUrlManualMult);
                } else {
                    Toast.makeText(getActivity(), "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}


//
//    public void downloadPdf()
//    {
//
//        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);
//        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
//        Map<String, String> map = new HashMap<>();
//        map.put("user_id",userId);
//        map.put("type",type);
//        Call<SuccessResGetInvoices> call = apiInterface.getInvoiceURL(map);
//        call.enqueue(new Callback<SuccessResGetInvoices>() {
//            @Override
//            public void onResponse(Call<SuccessResGetInvoices> call, Response<SuccessResGetInvoices> response) {
//                DataManager.getInstance().hideProgressMessage();
//                try {
//                    SuccessResGetInvoices data = response.body();
//                    if (data.status.equals("1")) {
//                        invoicesList.clear();
//                        invoicesList.addAll(data.getResult());
//                        binding.rvInvoices.setHasFixedSize(true);
//                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        binding.rvInvoices.setAdapter(new InvoiceUserAdapter(getActivity(),invoicesList,InvoicesFragment.this::donwloadInvoice));
//                    } else {
//                        showToast(getActivity(), data.message);
//                        invoicesList.clear();
//                        binding.rvInvoices.setHasFixedSize(true);
//                        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        binding.rvInvoices.setAdapter(new InvoiceUserAdapter(getActivity(),invoicesList,InvoicesFragment.this::donwloadInvoice));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<SuccessResGetInvoices> call, Throwable t) {
//                call.cancel();
//                DataManager.getInstance().hideProgressMessage();
//            }
//        });
//    }
//
//    private boolean writeResponseBodyToDisk(ResponseBody body) {
//        try {
//            // todo change the file location/name according to your needs
//            File futureStudioIconFile = new File(getActivity().getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//            try {
//                byte[] fileReader = new byte[4096];
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(futureStudioIconFile);
//                while (true) {
//                    int read = inputStream.read(fileReader);
//                    if (read == -1) {
//                        break;
//                    }
//                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//                outputStream.flush();
//                return true;
//            } catch (IOException e) {
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            return false;
//        }
//    }