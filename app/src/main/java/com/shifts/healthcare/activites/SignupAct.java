package com.shifts.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.shifts.healthcare.R;
import com.shifts.healthcare.databinding.ActivitySignupBinding;

public class SignupAct extends AppCompatActivity {

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);

        binding.header.imgHeader.setOnClickListener(v -> finish());

        binding.header.tvHeader.setText(R.string.signup);

        binding.btnSignupPost.setOnClickListener(v ->
                {

                    startActivity(new Intent(SignupAct.this,SignupWithPostShiftsActivity.class));
                }
        );
        binding.btnSignupWork.setOnClickListener(v ->
                {

                    startActivity(new Intent(SignupAct.this,SignupWithWorkActivity.class));
                }
        );

        binding.rlbottom.setOnClickListener(v ->
                {

                    startActivity(new Intent(SignupAct.this,LoginAct.class));
                }
        );
    }
}