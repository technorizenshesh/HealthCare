package com.shifts.healthcare.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.models.SuccessResGetDocuments;
import com.shifts.healthcare.models.SuccessResGetUnseenMessageCount;
import com.shifts.healthcare.models.SuccessResUploadDocument;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.Constant;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.activites.LoginAct.TAG;
import static com.shifts.healthcare.retrofit.Constant.USER_ID;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Bundle intent;
    private DrawerLayout drawer;
    private ImageView ivHamNom;
    private TextView tvTitleHeader,tvMessageCount;
    AppCompatButton logout;
    public static ImageView ivBack,ivMenu,ivConversation;
    NavigationView myNav;
    private NavController navController;
    RelativeLayout rlHome,rlProfile,rlPostShifts,rlShifts,rlBIlling,rlWallet,rlHiredWorker,rlFaq,rlContactus,rlSetting,rlPrivacy,rlGovDocumentID;
    public boolean approved = false;
    private HealthInterface apiInterface;
    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }
    LocalBroadcastManager lbm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        drawer = findViewById(R.id.drawer);
        rlHome = findViewById(R.id.rlHome);
        rlProfile = findViewById(R.id.rl_user);
        rlPostShifts = findViewById(R.id.rlPostShifts);
        rlShifts = findViewById(R.id.rlShifts);
        rlBIlling = findViewById(R.id.rlBilling);
        rlWallet = findViewById(R.id.rlWallet);
        rlHiredWorker = findViewById(R.id.rlHiredWorkers);
        rlFaq = findViewById(R.id.rlFaq);
        rlContactus = findViewById(R.id.rlContactUs);
        rlSetting = findViewById(R.id.rlSettings);
        rlPrivacy = findViewById(R.id.rlPrivacy);
        rlGovDocumentID = findViewById(R.id.rlGovDocumentID);
        logout = findViewById(R.id.btnLogout);
        ivConversation = findViewById(R.id.ivConversation);
        myNav = findViewById(R.id.navigation);
        ivMenu = findViewById(R.id.iv_hamburgur);
        tvTitleHeader = findViewById(R.id.tvtitle);
        ivBack = findViewById(R.id.img_header);
        ivConversation = findViewById(R.id.ivConversation);
        tvMessageCount= findViewById(R.id.tvMessageCount);

        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_edit_profile)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        setUpNavigationDrawer();
        ivBack.setOnClickListener(v ->
                {
                    onBackPressed();
                    NavDestination dat = navController.getCurrentDestination();
                    int id = dat.getId();
                    Log.d(TAG, "onCreate: Home ID:"+R.id.nav_home+" Selected ID :"+id);
                    if(R.id.nav_home ==  id)
                    {
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        else drawer.openDrawer(GravityCompat.START);
                        tvTitleHeader.setText(R.string.careshifts);
                        ivBack.setVisibility(View.GONE);
                        ivMenu.setVisibility(View.VISIBLE);
                    }else
                    {
                        ivBack.setVisibility(View.VISIBLE);
                        ivMenu.setVisibility(View.GONE);
                    }
                }
        );

        ivConversation.setOnClickListener(v ->
                {
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this,ConversationAct.class));
                }
        );

        intent =  getIntent().getExtras();
        if(intent!=null)
        {
            String key = intent.getString("key");

            if (key.equalsIgnoreCase("chat")){

                Bundle bundle = new Bundle();
                String sendId =intent.getString("senderId");

               startActivity(new Intent(HomeActivity.this, One2OneChatAct.class).putExtra("id",sendId));

            }else  if (key.equalsIgnoreCase("adminmessage")){

                HomeActivity.this.startActivity(new Intent(HomeActivity.this,ConversationAct.class));

            }
            else  if (key.equalsIgnoreCase("login")){
                String sendId =intent.getString("admin");
                if(sendId.equalsIgnoreCase("Approved"))
                {
                    approved = true;
                }
                else
                {
                    approved = false;
                }
            }else  if (key.equalsIgnoreCase("invoice")){
                navController.navigateUp();
                Bundle bundle = new Bundle();
                ivBack.setVisibility(View.VISIBLE);
                ivMenu.setVisibility(View.GONE);
                ivConversation.setVisibility(View.VISIBLE);
                navController.navigate(R.id.invoicesFragment,bundle);
                drawer.closeDrawer(GravityCompat.START);

            }
        }

        ivMenu.setOnClickListener(v ->
                {
                    if (drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                    else drawer.openDrawer(GravityCompat.START);
                }
        );

        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
            getUnseenNotificationCount();
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String str = intent.getStringExtra("key");
                getUnseenNotificationCount();
                // get all your data from intent and do what you want
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void setUpNavigationDrawer() {
        rlHome.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            tvTitleHeader.setText(R.string.careshifts);
            ivBack.setVisibility(View.GONE);
            ivMenu.setVisibility(View.VISIBLE);
            navController.navigate(R.id.nav_home);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlProfile.setOnClickListener(v -> {
            Log.d(TAG, "setUpNavigationDrawer: ");
            Log.d(TAG, "setUpNavigationDrawer: ");
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.nav_profile);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlShifts.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.shiftsFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlPostShifts.setOnClickListener(v -> {
            if(approved)
            {
                ivConversation.setVisibility(View.VISIBLE);
                ivBack.setVisibility(View.VISIBLE);
                ivMenu.setVisibility(View.GONE);
                navController.navigate(R.id.postShiftsFragment);
                drawer.closeDrawer(GravityCompat.START);
            }
            else
            {
                Toast.makeText(HomeActivity.this,"Please wait for admin approval.",Toast.LENGTH_SHORT).show();
            }
        });
        rlBIlling.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.billingFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        rlWallet.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.walletFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlHiredWorker.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.hiresWorkerFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlFaq.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.FAQSFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlContactus.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.contactUsFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlSetting.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.settingsFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlPrivacy.setOnClickListener(v -> {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.privacyAndLegacyFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlGovDocumentID.setOnClickListener(v -> {
            uploadDocumentDialog();
            drawer.closeDrawer(GravityCompat.START);
        });

        logout.setOnClickListener(v ->
                {
                    SharedPreferenceUtility.getInstance(HomeActivity.this.getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    startActivity(new Intent(HomeActivity.this,LoginAct.class));
                    finish();
                }
                );
    }

public int gov =  101;
    TextView tvGovPhoto,tvGovPhotoNotUploaded,tvGovPhotoUploaded;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private void uploadDocumentDialog()
    {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_upload_user_document);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        tvGovPhoto = dialog.findViewById(R.id.tvGovPhoto);
        tvGovPhotoNotUploaded = dialog.findViewById(R.id.tvGovPhotoNotUploaded);
        tvGovPhotoUploaded = dialog.findViewById(R.id.tvGovPhotoUploaded);

        ImageView ivCancel = dialog.findViewById(R.id.cancel);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        tvGovPhoto.setOnClickListener(v ->
                {
                   if(checkPermisssionForReadStorage())
                   {
                       Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                       intent.setType("image/*");
                       startActivityForResult(Intent.createChooser(intent, "Select Image"), gov);

                   }
                }
                );
        
        getGovPhotoIDDocuments();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
    MultipartBody.Part filePartGovId = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {

            if(requestCode == gov)
            {

                String  str_image_path = DataManager.getRealPathFromURI(HomeActivity.this, data.getData());

//                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));

                File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));

                if(file!=null)
                {
                    filePartGovId = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));
                }
                else
                {
                    filePartGovId = null;
                }

//                filePartGovId = MultipartBody.Part.createFormData("document", file.getName(), RequestBody.create(MediaType.parse("document/*"), file));

                addDocuments("gove_phot_id",filePartGovId);

            }

        }

    }


    public void addDocuments(String type,MultipartBody.Part filePart )
    {
        String strUserId = SharedPreferenceUtility.getInstance(HomeActivity.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(HomeActivity.this, getString(R.string.please_wait));
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody docType = RequestBody.create(MediaType.parse("text/plain"), "gove_phot_id");

//        Call<SuccessResSignIn> call = apiInterface.login(email,password,registerID);
        Call<SuccessResUploadDocument> loginCall = apiInterface.uploadUserDocuments(userId,docType,filePart );
        loginCall.enqueue(new Callback<SuccessResUploadDocument>() {
            @Override
            public void onResponse(Call<SuccessResUploadDocument> call, Response<SuccessResUploadDocument> response) {
                DataManager.getInstance().hideProgressMessage();
                try {

                    SuccessResUploadDocument data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(ContentValues.TAG,"Test Response :"+responseString);
                    HomeActivity.this.getWindow().getDecorView().clearFocus();

                    getGovPhotoIDDocuments();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(ContentValues.TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResUploadDocument> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(HomeActivity.this,
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(this, getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
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

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), gov);

                    } else {
                        Toast.makeText(HomeActivity.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).
                    show();
                }
                // return;
            }

        }
    }
    private ArrayList<SuccessResGetDocuments.Result> documentsList ;
    private SuccessResGetDocuments.Result documents ;
    public void getGovPhotoIDDocuments()
    {
        String userId =  SharedPreferenceUtility.getInstance(HomeActivity.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(HomeActivity.this, getString(R.string.please_wait));
        
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("type","gove_phot_id");

        Call<SuccessResGetDocuments> call = apiInterface.getUserDocuments(map);
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
                        setDocumentsButtons();
                    } else {
                        // showToast(HomeActivity.this, data.message);
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


    public void setDocumentsButtons() {

        tvGovPhotoNotUploaded.setVisibility(View.GONE);
        tvGovPhotoUploaded.setVisibility(View.VISIBLE);
        tvGovPhotoUploaded.setText(getString(R.string.uploaded) + "(" + documentsList.size() + ")");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
            getUnseenNotificationCount();
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        NavDestination dat = navController.getCurrentDestination();
        int id = dat.getId();

        if(R.id.nav_home ==  id)
        {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            else drawer.openDrawer(GravityCompat.START);
            tvTitleHeader.setText(R.string.careshifts);
            ivBack.setVisibility(View.GONE);
            ivMenu.setVisibility(View.VISIBLE);

        }else
        {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);

        }

    }

    public  void getUnseenNotificationCount()
    {

        String userId = SharedPreferenceUtility.getInstance(HomeActivity.this).getString(USER_ID);
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
    
    
}