package com.technorizen.healthcare.workerSide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.activites.ConversationAct;
import com.technorizen.healthcare.activites.HomeActivity;
import com.technorizen.healthcare.activites.LoginAct;
import com.technorizen.healthcare.activites.One2OneChatAct;
import com.technorizen.healthcare.models.SuccessResGetUnseenMessageCount;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.Constant;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.NetworkAvailablity;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;

public class WorkerHomeAct extends AppCompatActivity {

    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    private ImageView ivHamNom;
    private Bundle intent;
    private Context context = WorkerHomeAct.this;
    private TextView tvTitleHeader,tvMessageCount;
    AppCompatButton logout;
    private ImageView ivBack, ivMenu,ivConversation;
    NavigationView myNav;
    private NavController navController;
    RelativeLayout rlHome, rlProfile,rlDocument, rlReference, rlShifts, rlBIlling, rlWallet, rlHiredWorker, rlFaq, rlContactus, rlSetting, rlPrivacy;
    LocalBroadcastManager lbm;

    private HealthInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_home);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        drawer = findViewById(R.id.drawer);
        rlHome = findViewById(R.id.rlHome);
        rlProfile = findViewById(R.id.rl_user);
        rlDocument = findViewById(R.id.rlDocument);
        rlReference = findViewById(R.id.rlRerence);
        rlShifts = findViewById(R.id.rlShifts);
        rlBIlling = findViewById(R.id.rlBilling);
        rlWallet = findViewById(R.id.rlWallet);
        rlHiredWorker = findViewById(R.id.rlHiredWorkers);
        rlFaq = findViewById(R.id.rlFaq);
        rlContactus = findViewById(R.id.rlContactUs);
        rlSetting = findViewById(R.id.rlSettings);
        rlPrivacy = findViewById(R.id.rlPrivacy);
        logout = findViewById(R.id.btnLogout);
        myNav = findViewById(R.id.navigation);
        ivMenu = findViewById(R.id.iv_hamburgur1);
        tvTitleHeader = findViewById(R.id.tvtitle);
        ivBack = findViewById(R.id.img_header1);
        ivConversation = findViewById(R.id.ivConversation);
        tvMessageCount= findViewById(R.id.tvMessageCount);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_worker_home, R.id.nav_profile, R.id.nav_edit_profile)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        ivBack.setOnClickListener(v ->
                {
                    onBackPressed();
                    NavDestination dat = navController.getCurrentDestination();
                    int id = dat.getId();
                    if (R.id.nav_worker_home == id) {
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        else drawer.openDrawer(GravityCompat.START);
                        tvTitleHeader.setText(R.string.careshifts);
                        ivBack.setVisibility(View.GONE);
                        ivMenu.setVisibility(View.VISIBLE);
                    } else {
                        ivBack.setVisibility(View.VISIBLE);
                        ivMenu.setVisibility(View.GONE);
                    }
                }
        );
        ivConversation.setOnClickListener(v ->
                {
                    WorkerHomeAct.this.startActivity(new Intent(WorkerHomeAct.this, ConversationAct.class));
                }
        );
        intent =  getIntent().getExtras();
        if(intent!=null)
        {
            String key = intent.getString("key");
            if (key.equalsIgnoreCase("chat")){
                Bundle bundle = new Bundle();
                String sendId =intent.getString("senderId");
//                bundle.putString("senderId",sendId);
//                navController.navigateUp();
//                navController.navigate(R.id.one2OneChatFragment2,bundle);
//                ivBack.setVisibility(View.VISIBLE);
//                ivMenu.setVisibility(View.GONE);

                Intent intent1 = new Intent("filter_string_1");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);

                startActivity(new Intent(WorkerHomeAct.this, One2OneChatAct.class).putExtra("id",sendId));

            } else  if (key.equalsIgnoreCase("adminmessage")){
                WorkerHomeAct.this.startActivity(new Intent(WorkerHomeAct.this,ConversationAct.class));
            }
        }
        ivMenu.setOnClickListener(v ->
                {
                    if (drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                    else drawer.openDrawer(GravityCompat.START);
                }
        );

        setUpNavigation();

        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
            getUnseenNotificationCount();
        } else {
            Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
        }
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

    public  void getUnseenNotificationCount()
    {

        String userId = SharedPreferenceUtility.getInstance(WorkerHomeAct.this).getString(USER_ID);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavDestination dat = navController.getCurrentDestination();
        int id = dat.getId();
        if (R.id.nav_worker_home == id) {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            else drawer.openDrawer(GravityCompat.START);
            tvTitleHeader.setText(R.string.careshifts);
            ivBack.setVisibility(View.GONE);
            ivMenu.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
        }
    }
    public void setUpNavigation()
    {
        rlHome.setOnClickListener(v ->
    {
        ivConversation.setVisibility(View.VISIBLE);
        tvTitleHeader.setText(R.string.careshifts);
        ivBack.setVisibility(View.GONE);
        ivMenu.setVisibility(View.VISIBLE);
        navController.navigate(R.id.nav_worker_home);
        drawer.closeDrawer(GravityCompat.START);
    });
        rlProfile.setOnClickListener(v ->
    {
        ivBack.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.GONE);
        ivConversation.setVisibility(View.VISIBLE);
        navController.navigate(R.id.workerProfileFragment);
        drawer.closeDrawer(GravityCompat.START);
    });
        rlReference.setOnClickListener(v ->
    {
        ivConversation.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivMenu.setVisibility(View.GONE);
        navController.navigate(R.id.referenceFragment);
        drawer.closeDrawer(GravityCompat.START);
    });
        rlDocument.setOnClickListener(v ->
        {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.documentFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        rlShifts.setOnClickListener(v ->
        {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.workerShiftsFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        rlBIlling.setOnClickListener(v ->
        {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.workerBillingFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        rlContactus.setOnClickListener(v ->
        {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.workerContactUsFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        rlSetting.setOnClickListener(v ->
        {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.workerSettingsFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        rlFaq.setOnClickListener(v ->
        {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.workerFaqFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        rlPrivacy.setOnClickListener(v ->
        {
            ivConversation.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.workerPrivacyAndLegalFragment);
            drawer.closeDrawer(GravityCompat.START);
        });
        logout.setOnClickListener(v ->
                {
                    SharedPreferenceUtility.getInstance(WorkerHomeAct.this.getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    startActivity(new Intent(WorkerHomeAct.this, LoginAct.class));
                    finish();
                }
        );
    }

}