package com.shifts.healthcare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.shifts.healthcare.R;
import com.shifts.healthcare.models.SuccessResAccountDetails;
import com.shifts.healthcare.models.SuccessResInvoiceSummaryUser;
import com.shifts.healthcare.util.SharedPreferenceUtility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.shifts.healthcare.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 05,August,2021
 */

public class InvoiceSummaryAdapter extends RecyclerView.Adapter<InvoiceSummaryAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;
    private Context context;
    private  ArrayList<SuccessResInvoiceSummaryUser.Result> summaryList ;
    List<String> monthsList = new LinkedList<>();
    private boolean from ;
    private boolean showNotes = false;
    private SuccessResAccountDetails.Result accountDetail ;
    public InvoiceSummaryAdapter(Context context, ArrayList<SuccessResInvoiceSummaryUser.Result> summaryList,
                                 SuccessResAccountDetails.Result accountDetail)
    {
        this.context = context;
        this.summaryList = summaryList;
        this.accountDetail=accountDetail;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.invoice_summary_item, parent, false);
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
        TextView tvFinalAmount = holder.itemView.findViewById(R.id.tvFinalAmount);
        TextView tvCancellationCharge = holder.itemView.findViewById(R.id.tvCancellationCharge);
        RecyclerView rvTableItem = holder.itemView.findViewById(R.id.rvInvoiceTable);
        LinearLayout llInvoiceLayout = holder.itemView.findViewById(R.id.llInvoiceLayout);
        AppCompatButton btnPay = holder.itemView.findViewById(R.id.btnPay);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        AppCompatButton btnDownload = holder.itemView.findViewById(R.id.btnDownload);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateFrom;
        if(summaryList.get(position).getNoShowCancelCharge().equalsIgnoreCase("0"))
        {
            tvCancellationCharge.setText("Nil");
        }
        else
        {
            tvCancellationCharge.setText("$ "+summaryList.get(position).getNoShowCancelCharge());
        }
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
        btnPay.setOnClickListener(v ->
                {
                    showImageSelection();
                }
        );
        btnDownload.setOnClickListener(v ->
                {
                    String userId =  SharedPreferenceUtility.getInstance(context).getString(USER_ID);
                    String data = "https://app.careshifts.net/pdf/home/download_user_invoice_summary/"+summaryList.get(position).getId()+"/"+userId;
                    Intent defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
                    defaultBrowser.setData(Uri.parse(data));
                    context.startActivity(defaultBrowser);
                }
        );
        if(!summaryList.get(position).getInvoiceAmount().equalsIgnoreCase("0"))
        {
            tvFinalAmount.setText("$ "+summaryList.get(position).getFinalTotal());
            tvInvoiceAmount.setText("$ "+summaryList.get(position).getTotalAmount());
            tvPayStatus.setText(summaryList.get(position).getPayStatus());
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
                String pattern = "E,MMM d, yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                myPayDate = df.format(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvPayDate.setText(myPayDate);
            btnPay.setOnClickListener(v ->
                    {
                        showImageSelection();
                    }
            );
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

    public void showImageSelection() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.pay_invoice_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView tvInstitudeNumber = dialog.findViewById(R.id.tvInstitudeNumber);
        TextView tvTranferNum = dialog.findViewById(R.id.tvTranferNum);
        TextView tvAccNum = dialog.findViewById(R.id.tvAccNum);
        TextView tvNote = dialog.findViewById(R.id.tvNote);
        TextView tvAccName = dialog.findViewById(R.id.tvAccName);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);
        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );
        tvInstitudeNumber.setText(accountDetail.getInstitutionNumber());
        tvTranferNum.setText(accountDetail.getTransferNumber());
        tvAccNum.setText(accountDetail.getAccountNumber());
        tvNote.setText(accountDetail.getNote());
        tvAccName.setText(accountDetail.getName());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
