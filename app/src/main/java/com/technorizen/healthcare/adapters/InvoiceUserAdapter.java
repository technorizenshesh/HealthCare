package com.technorizen.healthcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.SuccessResGetInvoices;
import com.technorizen.healthcare.util.DownloadInvoice;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;

public class InvoiceUserAdapter extends RecyclerView.Adapter<InvoiceUserAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;
    private Context context;
    private DownloadInvoice downloadInvoice;
    private  ArrayList<SuccessResGetInvoices.Result> invoicesList ;
    private boolean showNotes = false;
    public InvoiceUserAdapter(Context context, ArrayList<SuccessResGetInvoices.Result> invoicesList,DownloadInvoice downloadInvoice)
    {
        this.context = context;
        this.invoicesList = invoicesList;
        this.downloadInvoice=downloadInvoice;
    }
    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.invoice_user_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        String todayAsString = "";
        String dtStart = null;
        String scheduleString = "";
        String scheduleString1 = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dtStart = invoicesList.get(position).getInvoicedate();
            scheduleString1 = invoicesList.get(position).getSchedulePayDate();
            Date  myDate = format.parse(dtStart);
            Date  myDateSchedule = format.parse(scheduleString1);
            String pattern = "E, MMM d, yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            todayAsString = df.format(myDate);
            scheduleString = df.format(myDateSchedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView tvInvoiceNumber = holder.itemView.findViewById(R.id.tvInvoiceNumber);
        TextView tvDesc = holder.itemView.findViewById(R.id.tvDesc);
        TextView tvTimedWorked = holder.itemView.findViewById(R.id.tvTimedWorked);
        TextView tvRate = holder.itemView.findViewById(R.id.tvRate);
        TextView tvShiftAmount = holder.itemView.findViewById(R.id.tvShiftAmount);
        TextView tvTotal = holder.itemView.findViewById(R.id.tvTotal);
        TextView tvLateCancelationCharge = holder.itemView.findViewById(R.id.tvLateCancelationCharge);
        TextView tvInvoiceTotal = holder.itemView.findViewById(R.id.tvInvoiceTotal);
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        TextView tvTotalHr = holder.itemView.findViewById(R.id.tvTotalHours);
        TextView tvTransit = holder.itemView.findViewById(R.id.tvTransit);
        TextView tvPayStatus = holder.itemView.findViewById(R.id.tvPayStatus);
        TextView tvAdminFees = holder.itemView.findViewById(R.id.tvAdminFees);
        TextView tvHst = holder.itemView.findViewById(R.id.tvHst);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        AppCompatButton btnDownload = holder.itemView.findViewById(R.id.btnDownload);
        String strCompany = "";
        if(invoicesList.get(position).getShiftsdetail().get(0).getAccountType().equalsIgnoreCase("Individual"))
        {
            strCompany = invoicesList.get(position).getShiftsdetail().get(0).getUserName();
        }
        else
        {
            strCompany = invoicesList.get(position).getShiftsdetail().get(0).getCompany();
        }

        String text = "For a shift done by "+invoicesList.get(position).getShiftsdetail().get(0).getWorkerName()  +
                " as a "+invoicesList.get(position).getShiftsdetail().get(0).getJobPosition()+
                " ( "+invoicesList.get(position).getShiftsdetail().get(0).getDutyOfWorker()+")  @ " +
                strCompany+"  ON, on "+todayAsString+"( "+invoicesList.get(position).getStartTime()
                +" - "+invoicesList.get(position).getEndTime()+" )";
        tvDate.setText(scheduleString);

        Double d = Double.parseDouble(invoicesList.get(position).getTotalWorked());
        if(d < 2.0)
        {
            tvTimedWorked.setText(invoicesList.get(position).getTotalWorked()+" hr");
        }
        else
        {
            tvTimedWorked.setText(invoicesList.get(position).getTotalWorked()+" hrs");
        }

        tvRate.setText("$ "+invoicesList.get(position).getHourlyRate()+"/Hour");
        tvShiftAmount.setText("$ "+invoicesList.get(position).getTotalAmount()+"");
        tvTotal.setText("$ "+invoicesList.get(position).getUserTotalAmount()+"");
        tvInvoiceTotal.setText("$ "+invoicesList.get(position).getUserPaidAmout()+"");
        tvLateCancelationCharge.setText("$ "+invoicesList.get(position).getUserNoShowCancelCharge());
        tvPayStatus.setText(invoicesList.get(position).getPayementStatus());
        tvDesc.setText(text);
        btnDownload.setOnClickListener(v ->
                {
                    String userId =  SharedPreferenceUtility.getInstance(context).getString(USER_ID);
                    String data = "https://app.careshifts.net/pdf/home/download_user_shift_invoice/"+invoicesList.get(position).getId()+"/"+userId;
                    Intent defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
                    defaultBrowser.setData(Uri.parse(data));
                    context.startActivity(defaultBrowser);
                }
        );
        if(invoicesList.get(position).getShiftsdetail().get(0).getTransitAllowance().equalsIgnoreCase("None"))
        {
            tvTransit.setText(invoicesList.get(position).getShiftsdetail().get(0).getTransitAllowance());
        }
        else
        {
            tvTransit.setText(invoicesList.get(position).getShiftsdetail().get(0).getTransitAllowance()+" Hour");
        }

        Double d1 = Double.parseDouble(invoicesList.get(position).getHoursPurchased());
        if(d1 < 2.0)
        {
            tvTotalHr.setText(invoicesList.get(position).getHoursPurchased()+" hr");
        }
        else
        {
            tvTotalHr.setText(invoicesList.get(position).getHoursPurchased()+" hrs");
        }

        tvInvoiceNumber.setText(context.getString(R.string.invoice_no)+""+ invoicesList.get(position).getInvoiceNo());
        tvHst.setText("$ "+invoicesList.get(position).getHsttaxUser());
        tvAdminFees.setText("$ "+invoicesList.get(position).getAdminFeeUser());
        int lastPosition = invoicesList.size()-1;
        if(invoicesList.size() == 1)
        {
            if(position == 0)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 30, 25, 95);
                cv.setLayoutParams(params);
            }
        }
        else
        {
            if(position == lastPosition)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 0, 25, 100);
                cv.setLayoutParams(params);
            }
            if(position == 0)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 30, 25, 75);
                cv.setLayoutParams(params);
            }
        }
    }
    @Override
    public int getItemCount() {
        return invoicesList.size();
    }
    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
