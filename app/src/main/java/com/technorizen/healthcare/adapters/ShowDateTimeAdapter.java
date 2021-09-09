package com.technorizen.healthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.Date;
import com.technorizen.healthcare.util.StartTimeAndTimeInterface;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class ShowDateTimeAdapter extends RecyclerView.Adapter<ShowDateTimeAdapter.SelectTimeViewHolder> {
    ArrayAdapter ad;

    private List<String> dates;
    private List<String> startTime;
    private List<String> endTime;

    private Context context;


    private List<Date> dateList = new LinkedList<>();


    public ShowDateTimeAdapter(Context context,List<String> dates,List<String> startTime,List<String> endTime )
    {
        this.dates = dates;
        this.context = context;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.show_date_time_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {


        int myPosition = position;

        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        TextView tvDay = holder.itemView.findViewById(R.id.tvDay);
        TextView tvStart = holder.itemView.findViewById(R.id.tvStart);
        TextView tvEnd = holder.itemView.findViewById(R.id.tvEnd);

        tvDate.setText(dates.get(position));
        tvStart.setText(startTime.get(position));
        tvEnd.setText(endTime.get(position));

        myPosition = position+1;

        tvDay.setText("Day "+myPosition);



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
