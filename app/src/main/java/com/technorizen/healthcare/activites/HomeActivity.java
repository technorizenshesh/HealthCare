package com.technorizen.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.ActivityHomeBinding;
import com.technorizen.healthcare.fragments.HomeFragment;
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

import static com.technorizen.healthcare.activites.LoginAct.TAG;
import static com.technorizen.healthcare.retrofit.Constant.USER_ID;

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
    RelativeLayout rlHome,rlProfile,rlPostShifts,rlShifts,rlBIlling,rlWallet,rlHiredWorker,rlFaq,rlContactus,rlSetting,rlPrivacy;
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
//                bundle.putString("senderId",sendId);
//                navController.navigateUp();
//                navController.navigate(R.id.one2OneChatFragment,bundle);
//                ivBack.setVisibility(View.VISIBLE);
//                ivMenu.setVisibility(View.GONE);

                Intent intent1 = new Intent("filter_string_1");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);

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

        logout.setOnClickListener(v ->
                {
                    SharedPreferenceUtility.getInstance(HomeActivity.this.getApplicationContext()).putBoolean(Constant.IS_USER_LOGGED_IN, false);
                    startActivity(new Intent(HomeActivity.this,LoginAct.class));
                    finish();
                }
                );
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