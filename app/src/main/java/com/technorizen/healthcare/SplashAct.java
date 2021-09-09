package com.technorizen.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.technorizen.healthcare.activites.HomeActivity;
import com.technorizen.healthcare.activites.LoginAct;
import com.technorizen.healthcare.retrofit.Constant;
import com.technorizen.healthcare.util.SharedPreferenceUtility;
import com.technorizen.healthcare.workerSide.WorkerHomeAct;

import static com.technorizen.healthcare.retrofit.Constant.USER_TYPE;

public class SplashAct extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        // binding= DataBindingUtil.setContentView(this,R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        isUserLoggedIn = SharedPreferenceUtility.getInstance(SplashAct.this).getBoolean(Constant.IS_USER_LOGGED_IN);
        finds();

    }


    private void finds() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn) {

                    String type = SharedPreferenceUtility.getInstance(SplashAct.this).getString(USER_TYPE);

                    if(type.equalsIgnoreCase("User"))
                    {
                        startActivity(new Intent(SplashAct.this, HomeActivity.class));
                        finish();

                    }else
                    {
                        startActivity(new Intent(SplashAct.this, WorkerHomeAct.class));
                        finish();

                    }

                } else {
                    startActivity(new Intent(SplashAct.this, LoginAct.class));
                    finish();
                }

            }
        },3000);
    }

}