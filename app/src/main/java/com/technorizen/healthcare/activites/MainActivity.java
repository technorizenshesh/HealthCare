package com.technorizen.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.ActivityMain2Binding;

import java.util.Calendar;
import java.util.Date;

import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;

public class MainActivity extends AppCompatActivity {

    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        binding.calendarView.init(today, nextYear.getTime())
                .withSelectedDate(today).inMode(SINGLE);


    }
}