package com.technorizen.healthcare.workerSide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.activites.ConversationAct;
import com.technorizen.healthcare.activites.HomeActivity;
import com.technorizen.healthcare.activites.LoginAct;
import com.technorizen.healthcare.retrofit.Constant;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

public class WorkerHomeAct extends AppCompatActivity {

    private DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    private ImageView ivHamNom;
    private Bundle intent;
    private Context context = WorkerHomeAct.this;
    private TextView tvTitleHeader;
    AppCompatButton logout;
    private ImageView ivBack, ivMenu,ivConversation;
    NavigationView myNav;
    private NavController navController;
    RelativeLayout rlHome, rlProfile,rlDocument, rlReference, rlShifts, rlBIlling, rlWallet, rlHiredWorker, rlFaq, rlContactus, rlSetting, rlPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_home);
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
                bundle.putString("senderId",sendId);
                navController.navigateUp();
                navController.navigate(R.id.one2OneChatFragment2,bundle);
                ivBack.setVisibility(View.VISIBLE);
                ivMenu.setVisibility(View.GONE);
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