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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.SuccessResInvoiceSummaryUser;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;

public class InvoiceSummaryWorkerAdapter extends RecyclerView.Adapter<InvoiceSummaryWorkerAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;
    private Context context;
    private  ArrayList<SuccessResInvoiceSummaryUser.Result> summaryList;
    List<String> monthsList = new LinkedList<>();
    private boolean from ;
    private boolean showNotes = false;
    public InvoiceSummaryWorkerAdapter(Context context, ArrayList<SuccessResInvoiceSummaryUser.Result> summaryList)
    {
        this.context = context;
        this.summaryList = summaryList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.invoice_summary_worker_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        TextView sumaryCycleDate = holder.itemView.findViewById(R.id.sumaryCycleDate);
        TextView tvInvoiceNumber = holder.itemView.findViewById(R.id.tvInvoiceNumber);
        TextView tvInvoiceAmount = holder.itemView.findViewById(R.id.tvInvoiceAmount);
        TextView tvPayDate = holder.itemView.findViewById(R.id.tvPayDate);
        TextView tvPayStatus = holder.itemView.findViewById(R.id.tvPayStatus);
        TextView tvCancellationCharge = holder.itemView.findViewById(R.id.tvCancellationCharge);
        TextView tvFinalAmount = holder.itemView.findViewById(R.id.tvFinalAmount);
        RecyclerView rvTableItem = holder.itemView.findViewById(R.id.rvInvoiceTable);
        LinearLayout llInvoiceLayout = holder.itemView.findViewById(R.id.llInvoiceLayout);
        AppCompatButton btnDownload = holder.itemView.findViewById(R.id.btnDownload);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateFrom;
        String dateTo;
        String stringFromDate = "",stringToDate= "";
        try {
            dateFrom = summaryList.get(position).getFromDate();
            dateTo = summaryList.get(position).getToDate();
            Date myDateFrom = format.parse(dateFrom);
            Date  myDateSchedule = format.parse(dateTo);
            String pattern = "MMMM d, yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            stringFromDate = df.format(myDateFrom);
            stringToDate = df.format(myDateSchedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String text = "Summary of invoices from "+stringFromDate+" - "+stringToDate;
        sumaryCycleDate.setText(text);
        tvInvoiceNumber.setText(summaryList.get(position).getMainInvoiceNo());
        if(summaryList.get(position).getNoShowCancelCharge().equalsIgnoreCase("0"))
        {
            tvCancellationCharge.setText("Nil");
        }
        else
        {
            tvCancellationCharge.setText("$ "+summaryList.get(position).getNoShowCancelCharge());
        }
        btnDownload.setOnClickListener(v ->
                {
                    String userId =  SharedPreferenceUtility.getInstance(context).getString(USER_ID);
                    String data = "https://app.careshifts.net/pdf/home/download_worker_invoice_summary/"+summaryList.get(position).getId()+"/"+userId;
                    Intent defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
                    defaultBrowser.setData(Uri.parse(data));
                    context.startActivity(defaultBrowser);
                }
        );

        if(!summaryList.get(position).getTotalAmount().equalsIgnoreCase("0"))
        {
            tvFinalAmount.setText("$ "+summaryList.get(position).getFinalTotal());
            llInvoiceLayout.setVisibility(View.VISIBLE);
            rvTableItem.setVisibility(View.VISIBLE);
            rvTableItem.setHasFixedSize(true);
            rvTableItem.setLayoutManager(new LinearLayoutManager(context));
            rvTableItem.setAdapter(new InvoiceSummaryTableAdapter(context,summaryList.get(position).getInvoiceNos(),summaryList.get(position).getInvoiceamount()));
            SimpleDateFormat payDateFormate = new SimpleDateFormat("yyyy-MM-dd");
            String paydate = summaryList.get(position).getPayBefore();
            String myPayDate = "";
            try {
                Date  myDate = payDateFormate.parse(paydate);
                String pattern = "E, MMM d, yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                myPayDate = df.format(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvInvoiceAmount.setText("$ "+summaryList.get(position).getTotalAmount());
            if(summaryList.get(position).getTotalAmount().contains("-"))
            {
                tvPayStatus.setText("Nil");
                tvPayDate.setText("Nil");
            }
            else
            {
                tvPayStatus.setText(summaryList.get(position).getPayStatus());
                tvPayDate.setText(myPayDate);
            }
        }
        else
        {
            llInvoiceLayout.setVisibility(View.GONE);
            rvTableItem.setVisibility(View.GONE);
        }
        int lastPosition = summaryList.size()-1;
        if(summaryList.size() == 1)
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
        return summaryList.size();
    }
    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
