package com.shifts.healthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.shifts.healthcare.R;
import com.shifts.healthcare.models.SuccessResGetTransactionHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.SelectTimeViewHolder> {

    private Context context;

    private  ArrayList<SuccessResGetTransactionHistory.Result> transactionList ;

    public PaymentHistoryAdapter(Context context, ArrayList<SuccessResGetTransactionHistory.Result> transactionList)
    {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.payment_history_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        TextView tvInvoiceNum = holder.itemView.findViewById(R.id.tvInvoiceNum);
        TextView tvAmountPaid = holder.itemView.findViewById(R.id.tvAmountPaid);
        TextView tvDateOfPayment = holder.itemView.findViewById(R.id.tvDateOfPayment);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        tvInvoiceNum.setText(transactionList.get(position).getInvoiceNo());
        String dtStart = transactionList.get(position).getDateTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvAmountPaid.setText(transactionList.get(position).getTotal());
        String originalString = transactionList.get(position).getDateTime();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String newString = new SimpleDateFormat("dd/MM/yyyy").format(date);

        tvDateOfPayment.setText(newString);

        tvDateOfPayment.setText(transactionList.get(position).getTransectionDate());

        int lastPosition = transactionList.size()-1;

        if(transactionList.size() == 1)
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
        return transactionList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
