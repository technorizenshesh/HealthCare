package com.shifts.healthcare.retrofit;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class Constant {

    public static final String BASE_URL = "https://www.app.careshifts.net/webservice/";
    public static final String USER_INFO = "user_info";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String driver_id = "driver_id";
    public static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";
    public static final String USER_ID = "userID";
    public static final String USER_TYPE = "userType";
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;
    public static final String SEC = "sec";
    public static final String RUNNING = "running";
    public static final String WASRUNNING = "wasrunning";
    public static boolean isValidEmail(CharSequence target) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }

    public static void showToast(Activity context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static float getMinutesDifference(String time1,String time2,String transit)
    {
        long myTime1=0;
        long myTime2=0;
        String namepass[] = time1.split(":");
        myTime1 = Long.parseLong(namepass[0]);
        myTime1 = myTime1*60;
        myTime1 = myTime1+Long.parseLong(namepass[1]);
        Log.d(TAG, "getTime24DifferenceOne: "+myTime1);
        String namepass1[] = time2.split(":");
        myTime2 = Long.parseLong(namepass1[0]);
        myTime2 = myTime2*60;
        myTime2 = myTime2+Long.parseLong(namepass1[1]);
        Log.d(TAG, "getTime24DifferenceTwo: "+myTime2);
        long remainingTime = 0;
        float timeDifference;
        if(myTime1>myTime2)
        {
            remainingTime = 1440 - myTime1 ;
            timeDifference = remainingTime+myTime2;
            Log.d(TAG, "Time Difference: "+timeDifference);
        }
        else
        {
            if(myTime1==myTime2)
            {
                timeDifference = 1440;
            }
            else
            {
                timeDifference = myTime2 - myTime1;
            }
        }

        return  timeDifference;
    }



}
