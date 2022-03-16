package com.technorizen.healthcare.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.technorizen.healthcare.R;

import java.io.File;
import java.util.Currency;
import java.util.Locale;

public class One2OneChatAct extends AppCompatActivity {

    static final File fSdcard = new File(Environment.getExternalStorageDirectory().toString(), "VDTEC");
    static final File fManualMult = new File(fSdcard, "CareShiftInvoice.pdf");
    private long lDownloadID;
    int MY_PERMISSIONS_CONSTANT = 101;
    static final String sUrlManualMult = "http://maven.apache.org/maven-1.x/maven.pdf";
    AppCompatButton btnDownload;
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (lDownloadID == id) {
                Toast.makeText(One2OneChatAct.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one2_one_chat);
        Currency currency = Currency.getInstance(Locale.getDefault());
//        String symbol = currency.get(currency.getCurrencyCode());

        Locale defaultLocale = Locale.getDefault();
//        displayCurrencyInfoForLocale(defaultLocale);
//       String locale = this.getResources().getConfiguration().locale.getCountry();
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String locale = tm.getNetworkCountryIso();
       String currencyCode =  getCurrencyCode(locale);
        String currencySymbol = getCurrencySymbol(locale);
        Log.d("TAG", "onCreate:1+ "+currencyCode);
        Log.d("TAG", "onCreate:2+ "+currencySymbol);
//        btnDownload = findViewById(R.id.btnDownloa);Currency.getInstance(currency).getSymbol(Locale.getDefault())
//        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    public static void displayCurrencyInfoForLocale(Locale locale) {
        System.out.println("Locale: " + locale.getDisplayName());
        Currency currency = Currency.getInstance(locale);
        System.out.println("Currency Code: " + currency.getCurrencyCode());
        System.out.println("Symbol: " + currency.getSymbol());
        System.out.println("Default Fraction Digits: " + currency.getDefaultFractionDigits());
        System.out.println();
    }

    public static String getCurrencyCode(String countryCode) {
        return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
    }

    //to retrieve currency symbol
    public static String getCurrencySymbol(String countryCode) {
        return Currency.getInstance(new Locale("", countryCode)).getSymbol();
    }

    public boolean requestPermision()
    {
        Context context = One2OneChatAct.this;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, " Allow the Storage Permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_CONSTANT);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }
}