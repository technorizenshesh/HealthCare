package com.shifts.healthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shifts.healthcare.R;
import com.shifts.healthcare.models.Date;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class InvoiceSummaryTableAdapter extends RecyclerView.Adapter<InvoiceSummaryTableAdapter.SelectTimeViewHolder> {
    ArrayAdapter ad;

    private List<String> invoiceNum;
    private List<String> invoiceAmount;

    private Context context;

    private List<Date> dateList = new LinkedList<>();


    public InvoiceSummaryTableAdapter(Context context, List<String> invoiceNum, List<String> invoiceAmount )
    {

        this.context = context;
        this.invoiceNum = invoiceNum;
        this.invoiceAmount = invoiceAmount;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.summary_table_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {


        TextView tvInvoiceNum = holder.itemView.findViewById(R.id.tvInvoiceNum);
        TextView tvAmount = holder.itemView.findViewById(R.id.tvAmount);

        tvInvoiceNum.setText(invoiceNum.get(position));
        tvAmount.setText(invoiceAmount.get(position));

    }

    @Override
    public int getItemCount() {
        return invoiceNum.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
