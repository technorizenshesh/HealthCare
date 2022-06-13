package com.shifts.healthcare.adapters;

import android.content.Context;
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
import com.shifts.healthcare.R;
import com.shifts.healthcare.models.SuccessResGetInvoices;
import com.shifts.healthcare.util.PayInvoiceInterface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class PayInvoiceAdapter extends RecyclerView.Adapter<PayInvoiceAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private PayInvoiceInterface payInvoiceInterface;

    private  ArrayList<SuccessResGetInvoices.Result> invoicesList ;

    List<String> monthsList = new LinkedList<>();

    private boolean from ;

    private boolean showNotes = false;

    public PayInvoiceAdapter(Context context, ArrayList<SuccessResGetInvoices.Result> invoicesList,PayInvoiceInterface payInvoiceInterface)
    {
        this.context = context;
        this.invoicesList = invoicesList;
        this.payInvoiceInterface = payInvoiceInterface;
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            dtStart = invoicesList.get(position).getShiftsdetail().get(0).getDateTime();
            scheduleString1 = invoicesList.get(position).getSchedulePayDate();
            Date  myDate = format.parse(dtStart);
            Date  myDateSchedule = format.parse(scheduleString1);
            String pattern = "E,MMM d, yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            todayAsString = df.format(myDate);
            scheduleString = df.format(myDateSchedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView tvCompanyName = holder.itemView.findViewById(R.id.tvCompanyName);
        TextView tvInvoiceNumber = holder.itemView.findViewById(R.id.tvInvoiceNumber);
        TextView tvDesc = holder.itemView.findViewById(R.id.tvDesc);
        TextView tvTimedWorked = holder.itemView.findViewById(R.id.tvTimedWorked);
        TextView tvRate = holder.itemView.findViewById(R.id.tvRate);
        TextView tvShiftAmount = holder.itemView.findViewById(R.id.tvShiftAmount);
        TextView tvTotal = holder.itemView.findViewById(R.id.tvTotal);
        TextView tvNoShowCharge = holder.itemView.findViewById(R.id.tvNoShowCharge);
        TextView tvInvoiceTotal = holder.itemView.findViewById(R.id.tvInvoiceTotal);
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        TextView tvTotalHr = holder.itemView.findViewById(R.id.tvTotalHours);
        TextView tvTransit = holder.itemView.findViewById(R.id.tvTransit);
        TextView tvLateCancelationCharge = holder.itemView.findViewById(R.id.tvLateCancelationCharge);
        TextView tvPayStatus = holder.itemView.findViewById(R.id.tvPayStatus);
        TextView tvAdminFees = holder.itemView.findViewById(R.id.tvAdminFees);
        TextView tvHst = holder.itemView.findViewById(R.id.tvHst);

        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);

        AppCompatButton btnDownload = holder.itemView.findViewById(R.id.btnDownload);

        AppCompatButton btnPay = holder.itemView.findViewById(R.id.btnPay);

        btnDownload.setVisibility(View.GONE);

        btnPay.setVisibility(View.VISIBLE);

        String strCompany = "";

        if(invoicesList.get(position).getShiftsdetail().get(0).getAccountType().equalsIgnoreCase("Individual"))
        {
            strCompany = invoicesList.get(position).getShiftsdetail().get(0).getUserName();
            tvCompanyName.setText(invoicesList.get(position).getShiftsdetail().get(0).getUserName());
        }
        else
        {
            strCompany = invoicesList.get(position).getShiftsdetail().get(0).getCompany();
            tvCompanyName.setText(invoicesList.get(position).getShiftsdetail().get(0).getCompany());
        }

        tvLateCancelationCharge.setText("$ "+invoicesList.get(position).getUserNoShowCancelCharge());

        String text = "For a shift done by "+invoicesList.get(position).getShiftsdetail().get(0).getWorkerName()  +
                " as a "+invoicesList.get(position).getShiftsdetail().get(0).getJobPosition()+
                " ( "+invoicesList.get(position).getShiftsdetail().get(0).getDutyOfWorker()+")  @ " +
                strCompany+"  ON, on "+todayAsString+"( "+invoicesList.get(position).getStartTime()
                +" - "+invoicesList.get(position).getEndTime()+" )";

        tvDate.setText(scheduleString);

        tvTimedWorked.setText(invoicesList.get(position).getTotalWorked());

        tvRate.setText(invoicesList.get(position).getHourlyRate()+"/Hour");

        tvShiftAmount.setText("$ "+invoicesList.get(position).getTotalAmount()+"");

        tvTotal.setText("$ "+invoicesList.get(position).getTotalAmount()+"");

        tvInvoiceTotal.setText("$ "+invoicesList.get(position).getUserPaidAmout()+"");

        tvPayStatus.setText(invoicesList.get(position).getPayementStatus());

        tvDesc.setText(text);

        if(invoicesList.get(position).getShiftsdetail().get(0).getCompany().equalsIgnoreCase(""))
        {
            tvCompanyName.setText(invoicesList.get(position).getShiftsdetail().get(0).getUserName());
        }
        else
        {
            tvCompanyName.setText(invoicesList.get(position).getShiftsdetail().get(0).getCompany());
        }

        if(invoicesList.get(position).getShiftsdetail().get(0).getTransitAllowance().equalsIgnoreCase("None"))
        {
            tvTransit.setText(invoicesList.get(position).getShiftsdetail().get(0).getTransitAllowance());
        }
        else
        {
            tvTransit.setText(invoicesList.get(position).getShiftsdetail().get(0).getTransitAllowance()+" Hour");
        }

        tvTotalHr.setText(invoicesList.get(position).getHoursPurchased());

        tvInvoiceNumber.setText(context.getString(R.string.invoice_no)+""+ invoicesList.get(position).getInvoiceNo());

        tvHst.setText("$ "+invoicesList.get(position).getHsttaxUser());

        tvAdminFees.setText("$ "+invoicesList.get(position).getAdminFeeUser());

        btnPay.setOnClickListener(v ->
                {
//                    payInvoiceInterface.payInvoice(v,invoicesList.get(position).getId(),invoicesList.get(position).getWorkerTotalEarning(),invoicesList.get(position).getUserId(),invoicesList.get(position).getWorkerId(),invoicesList.get(position).getShiftId());
                }
                );

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
