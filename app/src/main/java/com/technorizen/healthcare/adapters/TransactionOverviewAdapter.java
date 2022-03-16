package com.technorizen.healthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.SuccessResGetTransactionHistory;
import com.technorizen.healthcare.models.SuccessResGetTransactionOverview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class TransactionOverviewAdapter extends RecyclerView.Adapter<TransactionOverviewAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private  ArrayList<SuccessResGetTransactionOverview.Result> transactionList ;
    List<String> monthsList = new LinkedList<>();

    private boolean from ;
    private boolean showNotes = false;

    public TransactionOverviewAdapter(Context context, ArrayList<SuccessResGetTransactionOverview.Result> transactionList)
    {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.transaction_overview, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        TextView tvInvoiceAmount = holder.itemView.findViewById(R.id.tvInvoiceAmount);
        TextView tvPayamount = holder.itemView.findViewById(R.id.tvPayamount);
        TextView tvAmountBalance = holder.itemView.findViewById(R.id.tvAmountBalance);
        TextView tvWalletBalance = holder.itemView.findViewById(R.id.tvWalletBalance);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);

        tvInvoiceAmount.setText(transactionList.get(position).getTotalAmount());
        tvPayamount.setText(transactionList.get(position).getTotalAmount());
        tvWalletBalance.setText(transactionList.get(position).getWalletAmount());

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
