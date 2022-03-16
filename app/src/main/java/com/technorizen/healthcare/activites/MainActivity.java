package com.technorizen.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.ActivityMain2Binding;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.technorizen.healthcare.activites.LoginAct.TAG;
import static com.technorizen.healthcare.retrofit.Constant.SEC;
import static com.technorizen.healthcare.retrofit.Constant.getMinutesDifference;
import static com.wisnu.datetimerangepickerandroid.CalendarPickerView.SelectionMode.SINGLE;

public class MainActivity extends AppCompatActivity {

    ActivityMain2Binding binding;
    private long totalSeconds;
    long displayTim = 0;
    long startTim = 0;
    long endTim = 0;
    long newEndTime = 0;
    Date date2 = null;
    Date date = null;
    Date endDate =null;
    Date date1 = null;
    Handler handler = null;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
//        String dateString = "03/08/2022"+" "+"06:00:00 PM";
//        try {
//            date1 = sdf.parse(dateString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);
//            startTim = calendar.getTimeInMillis();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//
//        String dateString2 = "03/08/2022"+" "+"12:00 AM";
//
//        try {
//            date2 = sdf2.parse(dateString2);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(date2);
//
//            calendar2.add(Calendar.DATE, 1);
//
//            date2 = calendar2.getTime();
//
//            endTim = calendar2.getTimeInMillis();
//            newEndTime = endTim;
//            Log.d(TAG, "onCreate: New End Time "+newEndTime);
//            endTim = 1440 * 60 * 1000+endTim;
//            Log.d(TAG, "onCreate: End Time "+endTim);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long difference = date2.getTime() - date1.getTime();
//
//        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//
//        Log.d(TAG, "onCreate: "+diffInSec);



//
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
//        String dateString = "03/08/2022"+" "+"08:00:00 AM";
//        try {
//            date1 = sdf.parse(dateString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);
//            startTim = calendar.getTimeInMillis();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//
//        String dateString2 = "03/09/2022"+" "+"08:40 PM";
//
//        try {
//            date2 = sdf2.parse(dateString2);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(date2);
////            calendar2.add(Calendar.DATE, 1);
//            endTim = calendar2.getTimeInMillis();

//            newEndTime = endTim;
//            Log.d(TAG, "onCreate: New End Time "+newEndTime);
//            endTim = (1440 * 60 * 1000)+endTim;
//
//            Log.d(TAG, "onCreate: End Time "+endTim);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        handler = new Handler();
//
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(runnable, 1000);
//                long currentTim = 0;
//                try {
//                    Date date3 = new Date();//sdf3.parse(dateString2);
//                    Calendar calendar3 = Calendar.getInstance();
//                    calendar3.setTime(date3);
//                    currentTim = calendar3.getTimeInMillis();
//                    Log.d(TAG, "onCreate: Current Time "+currentTim);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if(displayTim<endTim)
//                {
//
//                    if(currentTim<newEndTime)
//                    {
////                        currentTim = currentTim + (1440 * 60 * 1000);
//
//                        currentTim = currentTim + Long.parseLong("1646764200000");
//
//                        Log.d(TAG, "onCreate: Current Time IF "+currentTim);
//                    }
//
//                    if (startTim <= currentTim) {
//
//                        if(endTim<currentTim)
//                        {
//                            long difference = date2.getTime() - date1.getTime();
//                            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                            totalSeconds = diffInSec;
//                            handler.removeCallbacks(runnable);
//                        }
//
//                        displayTim = currentTim - startTim;
//
//                    } else {
//                        displayTim = startTim - currentTim;
//                    }
//
//                    totalSeconds = displayTim / 1000;
//                    long difference = date1.getTime() - date2.getTime();
//                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                    Log.d(ContentValues.TAG, "run: "+diffInSec);
//                    int currentSecond = (int) totalSeconds % 60;
//                    long totalMinutes = totalSeconds / 60;
//                    int currentMinute = (int) totalMinutes % 60;
//                    long totalHours = totalMinutes / 60;
//                    int currentHour = (int) totalHours % 12;
//                    String time
//                            = String
//                            .format(Locale.getDefault(),
//                                    "%02d:%02d:%02d", currentHour,
//                                    currentMinute, currentSecond);
//                    Log.d(TAG, "run: "+time);
//                    binding.btnTIme.setText(time);
//
//                }
//
//                else
//                {
//
//                    long difference = date2.getTime() - date1.getTime();
//                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//                    totalSeconds = diffInSec;
//                    handler.removeCallbacks(runnable);
//                    Log.d(TAG, "run: "+"shiftCompleted");
//                }
//
//            }
//        };
//        handler.postDelayed(runnable, 1000);
//        print();
//


        playTimer();
    }

    private String endTime;
    private String newEndTime1;

    public void print()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        String dateString = "03/09/2022"+" "+"12:00:00 AM";
        try {
            date1 = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            startTim = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "print: "+startTim);

    }
    Date date3 = null;
    public void playTimer()
    {

        Log.d(ContentValues.TAG, "runTimer: "+"Today");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        String dateString = "03/10/2022"+" "+"06:30:00 PM";
        try {
            date1 = sdf.parse(dateString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);
//            startTim = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        String dateString2 = "03/11/2022"+" "+"12:00:00 AM";
        try {
            date2 = sdf2.parse(dateString2);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(date2);
//            endTim = calendar2.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        handler = new Handler();
//        timerHandler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 1000);
                long currentTim = 0;
                try {
                     date3 = new Date();//sdf3.parse(dateString2);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(date3);
                    currentTim = calendar3.getTimeInMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (date2.compareTo(date3)==1) {

                    if (date1.compareTo(date3) <= 0) {

                        displayTim = date3.getTime() - date1.getTime();

                    } else {

                        displayTim = date1.getTime() - date3.getTime();
                    }

                    totalSeconds = displayTim / 1000;
                    long difference = date1.getTime() - date2.getTime();
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                    Log.d(ContentValues.TAG, "run: "+diffInSec);
                    int currentSecond = (int) totalSeconds % 60;
                    long totalMinutes = totalSeconds / 60;
                    int currentMinute = (int) totalMinutes % 60;
                    long totalHours = totalMinutes / 60;
                    int currentHour = (int) totalHours % 12;
                    String time
                            = String
                            .format(Locale.getDefault(),
                                    "%02d:%02d:%02d", currentHour,
                                    currentMinute, currentSecond);

                    Log.d(TAG, "run: "+time);

                    binding.btnTIme.setText(time);

                } else {
                     long difference = date2.getTime() - date1.getTime();
                    long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
                    totalSeconds = diffInSec;
                    handler.removeCallbacks(runnable);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

 //    int seconds = 0;
//    int i=0;
//    public void myHandler()
//    {
//
//        String time = shiftDate+" "+shiftStartTime;
//        String endTime = shiftDate+" "+shiftEndTime;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
//        try {
//
//            date = dateFormat.parse(time);
//            endDate = dateFormat.parse(endTime);
////            Calendar c = Calendar.getInstance();
////            c.setTime(endDate);
////            c.add(Calendar.DATE, 1);
////            endDate = c.getTime();
//
//        } catch (ParseException e) {
//        }
//        Date currentTime = Calendar.getInstance().getTime();
//        long difference = date.getTime() - currentTime.getTime();
//        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
//         i = (int) diffInSec;
//        if(i < 0 || i==0)
//        {
//
//            if(handler==null)
//            {
//
//                handler      = new Handler();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        int hours = seconds / 3600;
//                        int minutes = (seconds % 3600) / 60;
//                        int secs = seconds % 60;
//                        Date currentTime1 = Calendar.getInstance().getTime();
//                        long difference1 = endDate.getTime() - currentTime1.getTime();
//                        long diffInSec1 = TimeUnit.MILLISECONDS.toSeconds(difference1);
//                        int  j = (int) diffInSec1;
//                        if(j<0)
//                        {
//
//                            handler.removeCallbacks(null);
//                        }
//
////                        Date currentTime = Calendar.getInstance().getTime();
////
////                        long difference = date.getTime() - currentTime.getTime();
////
////                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(difference);
////
////                        i = (int) diffInSec;
////
////                        seconds = Math.abs(i);
//
//                        String time
//                                = String
//                                .format(Locale.getDefault(),
//                                        "%02d:%02d:%02d", hours,
//                                        minutes, secs);
//
//                        Log.d(TAG, "run: "+time);
//
////                        binding.btnClock.setText(time);
//                        handler.postDelayed(this, 1 * 1000);
//                    }
//                });
//
//
//            }
//        }
//        else
//        {
//
//            if(negativeHandler==null)
//            {
//
//                negativeHandler = new Handler();
//                negativeHandler.post(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        if(i==0 || i < 0 )
//                        {
//                            i=-1;
//                            negativeHandler.removeCallbacks(null);
//                            runTimer();
//                        }
//
//                        int hours = i / 3600;
//                        int minutes = (i % 3600) / 60;
//                        int secs = i % 60;
//                        String time
//                                = String
//                                .format(Locale.getDefault(),
//                                        "%02d:%02d:%02d", hours,
//                                        minutes, secs);
//                        if(i>0)
//                        {
//                            binding.btnClock.setText(time);
//                        }
//
//                        if (running) {
//                            i = i-1;
//                        }
//                        negativeHandler.postDelayed(this, 1 * 1000);
//                    }
//                });
//            }
//        }
//
//    }
//


}