package com.technorizen.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
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
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private DrawerLayout drawer;

    private ImageView ivHamNom;

    private TextView tvTitleHeader;
    AppCompatButton logout;
    private ImageView ivBack,ivMenu;

//    Toolbar toolbarNormal,toolbarHead;

    NavigationView myNav;

    private NavController navController;

    RelativeLayout rlHome,rlProfile,rlPostShifts,rlShifts,rlBIlling,rlWallet,rlHiredWorker,rlFaq,rlContactus,rlSetting,rlPrivacy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawer);

        rlHome = findViewById(R.id.rlHome);
        rlProfile = findViewById(R.id.rlProfile);
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

/*

        toolbarHead = findViewById(R.id.toolbar);
        toolbarNormal = findViewById(R.id.toolbarNormal);
*/

        myNav = findViewById(R.id.navigation);

         ivMenu = findViewById(R.id.iv_hamburgur);
//        ivHamNom = findViewById(R.id.iv_hamburgur_normal);
        tvTitleHeader = findViewById(R.id.tvtitle);
        ivBack = findViewById(R.id.img_header);


      /*  ivHamNom.setOnClickListener(v ->
                {

//                    drawer.openDrawer(Gravity.LEFT);
                    if (drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                    else drawer.openDrawer(GravityCompat.START);
                }
        );*/


        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_edit_profile)
                .setDrawerLayout(drawer)
                .build();

         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
     //   NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);


        NavigationUI.setupWithNavController(navigationView, navController);

        setUpNavigationDrawer();


        ivBack.setOnClickListener(v ->
                {

                    onBackPressed();
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
        );

        ivMenu.setOnClickListener(v ->
                {

//                    drawer.openDrawer(Gravity.LEFT);
                    if (drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                    else drawer.openDrawer(GravityCompat.START);
                }
        );

    }

    private void setUpNavigationDrawer() {

        rlHome.setOnClickListener(v -> {

            tvTitleHeader.setText(R.string.careshifts);
            ivBack.setVisibility(View.GONE);
            ivMenu.setVisibility(View.VISIBLE);

            navController.navigate(R.id.nav_home);
            drawer.closeDrawer(GravityCompat.START);

        });

        rlProfile.setOnClickListener(v -> {
//            toolbarNormal.setVisibility(View.VISIBLE);
//            toolbarHead.setVisibility(View.GONE);
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);

            navController.navigate(R.id.nav_profile);
            drawer.closeDrawer(GravityCompat.START);

        });

        rlShifts.setOnClickListener(v -> {

            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);

            navController.navigate(R.id.shiftsFragment);
            drawer.closeDrawer(GravityCompat.START);

        });

        rlPostShifts.setOnClickListener(v -> {

            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);

            navController.navigate(R.id.postShiftsFragment);
            drawer.closeDrawer(GravityCompat.START);
        });


        rlBIlling.setOnClickListener(v -> {

            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.billingFragment);
            drawer.closeDrawer(GravityCompat.START);

        });

        rlWallet.setOnClickListener(v -> {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.walletFragment);
            drawer.closeDrawer(GravityCompat.START);
        });


        rlHiredWorker.setOnClickListener(v -> {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.hiresWorkerFragment);
            drawer.closeDrawer(GravityCompat.START);

        });

        rlFaq.setOnClickListener(v -> {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.FAQSFragment);
            drawer.closeDrawer(GravityCompat.START);
        });

        rlContactus.setOnClickListener(v -> {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);

            navController.navigate(R.id.contactUsFragment);

            drawer.closeDrawer(GravityCompat.START);

        });

        rlSetting.setOnClickListener(v -> {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.settingsFragment);
            drawer.closeDrawer(GravityCompat.START);

        });

        rlPrivacy.setOnClickListener(v -> {
            ivBack.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.GONE);
            navController.navigate(R.id.privacyAndLegacyFragment);
            drawer.closeDrawer(GravityCompat.START);

        });

        logout.setOnClickListener(v ->
                {
                    startActivity(new Intent(HomeActivity.this,LoginAct.class));
                    finish();
                }
                );

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
}