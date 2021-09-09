package com.technorizen.healthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.healthcare.R;

import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class SelectTimeStaticAdapter extends RecyclerView.Adapter<SelectTimeStaticAdapter.SelectTimeViewHolder> {
    ArrayAdapter ad;

    private List<String> dates;
    private Context context;


    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM","11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM","11:15 PM","11:30 PM","11:45 PM"};


    String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM","11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM","11:15 PM","11:30 PM","11:45 PM"};

    public SelectTimeStaticAdapter(List<String> dates, Context context)
    {
        this.dates = dates;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.time_static_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
