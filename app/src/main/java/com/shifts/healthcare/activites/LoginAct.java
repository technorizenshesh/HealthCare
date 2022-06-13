package com.shifts.healthcare.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.shifts.healthcare.R;
import com.shifts.healthcare.databinding.ActivityLoginBinding;
import com.shifts.healthcare.models.SuccessResSignIn;
import com.shifts.healthcare.retrofit.ApiClient;
import com.shifts.healthcare.retrofit.Constant;
import com.shifts.healthcare.retrofit.HealthInterface;
import com.shifts.healthcare.util.DataManager;
import com.shifts.healthcare.util.NetworkAvailablity;
import com.shifts.healthcare.util.SharedPreferenceUtility;
import com.shifts.healthcare.workerSide.WorkerHomeAct;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shifts.healthcare.retrofit.Constant.isValidEmail;
import static com.shifts.healthcare.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {

    private ActivityLoginBinding binding;
    HealthInterface apiInterface;
    public static String TAG = "LoginActivity";
    private String strEmail ="",strPassword= "",deviceToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        apiInterface = ApiClient.getClient().create(HealthInterface.class);
        FirebaseApp.initializeApp(LoginAct.this);
        getToken();
        binding.rlDont.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginAct.this,SignupAct.class));
                }
                );
        binding.tvForgotPass.setOnClickListener(v ->
                {
                    startActivity(new Intent(LoginAct.this,ForgotPassActivity.class));
                }
        );
        binding.btnLogin.setOnClickListener(v -> {
            strEmail = binding.etEmail.getText().toString().trim();
            strPassword = binding.etPass.getText().toString().trim();
            if(isValid())
            {
                if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
                    login();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,  getResources().getString(R.string.on_error), Toast.LENGTH_SHORT).show();
            }
        });
        binding.loggedInRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.loggedInRadio.isSelected()) {
                    binding.loggedInRadio.setChecked(true);
                    binding.loggedInRadio.setSelected(true);
                } else {
                    binding.loggedInRadio.setChecked(false);
                    binding.loggedInRadio.setSelected(false);
                }
            }
        });
    }

    private void login() {
        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();
        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        map.put("password",strPassword);
        map.put("register_id",deviceToken);
        map.put("time_zone",id);
        Call<SuccessResSignIn> call = apiInterface.login(map);
        call.enqueue(new Callback<SuccessResSignIn>() {
            @Override
            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignIn data = response.body();
                    Log.e("data",data.status);
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        if(binding.loggedInRadio.isChecked())
                        {
                            SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        }
                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID,data.getResult().getId());
                        Toast.makeText(LoginAct.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                        if(data.getResult().getType().equalsIgnoreCase("User"))
                        {
                            SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_TYPE,"User");
                            startActivity(new Intent(LoginAct.this, HomeActivity.class).putExtra("key","login").putExtra("admin",data.getResult().getAdminApproval()));
                        } else
                        {
                            SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_TYPE,"");
                            startActivity(new Intent(LoginAct.this, WorkerHomeAct.class));
                        }
                        finish();
                    } else if (data.status.equals("0")) {
                        showToast(LoginAct.this, data.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
    private boolean isValid() {
        if (strEmail.equalsIgnoreCase("")) {
            binding.etEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!isValidEmail(strEmail)) {
            binding.etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }else if (strPassword.equalsIgnoreCase("")) {
            binding.etPass.setError(getString(R.string.enter_pass));
            return false;
        }
        return true;
    }
    private void getToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            deviceToken = token;
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginAct.this, "Error=>" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}