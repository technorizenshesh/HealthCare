//package com.technorizen.healthcare.adapters;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.technorizen.healthcare.R;
//import com.technorizen.healthcare.models.Date;
//import com.technorizen.healthcare.util.StartTimeAndTimeInterface;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import static com.technorizen.healthcare.util.DataManager.subArray;
//public class SelectEditTimeAdapter extends RecyclerView.Adapter<SelectEditTimeAdapter.SelectTimeViewHolder> {
//
//    ArrayAdapter ad;
//    private List<String> dates;
//    private Context context;
//    private List<Date> dateList = new LinkedList<>();
//    private Map<String,String> startTime ;
//    private Map<String,String> endTime ;
//    private StartTimeAndTimeInterface startTimeAndTimeInterface;
//    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//            "11:15 AM","11:30 AM","11:45 AM",
//            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//            "11:15 PM","11:30 PM","11:45 PM"
//    };
//
//
//
//    public SelectEditTimeAdapter(List<String> dates, Context context, StartTimeAndTimeInterface startTimeAndTimeInterface,Map<String,String> startTime,Map<String,String> endTime)
//    {
//        this.dates = dates;
//        this.context = context;
//        this.startTime= startTime;
//        this.endTime = endTime;
//        this.startTimeAndTimeInterface = startTimeAndTimeInterface;
//    }
//
//    @NonNull
//    @Override
//    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.time_item, parent, false);
//        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
//        return viewHolder;
//    }
//    @Override
//    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
//        int myPosition = position;
//        Spinner spinnerStart,spinnerEnd;
//        spinnerStart = holder.itemView.findViewById(R.id.spinnerStartTime);
//        spinnerEnd = holder.itemView.findViewById(R.id.spinnerEndTime);
//        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
//        TextView tvDay = holder.itemView.findViewById(R.id.tvDay);
//        tvDate.setText(dates.get(position));
//        tvDay.setText(position+1+"");
//
//        ad = new ArrayAdapter(
//                context,
//                android.R.layout.simple_spinner_item,
//                start);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        spinnerStart.setAdapter(ad);
//        spinnerStart.setSelection(getStartDatePosition(startTime.get(dates.get(position))));
//
//        int endEndPosition = start.length-1;
//
//        String myEndTime = endTime.get(dates.get(position));
//        int myPosition1 = getStartDatePosition(myEndTime);
//        holder.end = subArray(start, myPosition1, endEndPosition);
//
//        ad = new ArrayAdapter(
//                context,
//                android.R.layout.simple_spinner_item,
//                holder.end);
//        ad.setDropDownViewResource(
//                android.R.layout
//                        .simple_spinner_dropdown_item);
//        spinnerEnd.setAdapter(ad);
//        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                startTimeAndTimeInterface.endTime(dates.get(myPosition),spinnerEnd.getSelectedItem().toString());
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                startTimeAndTimeInterface.startTime(dates.get(myPosition),spinnerStart.getSelectedItem().toString());
//                   if(holder.firstTime)
//                {
//                    int subEnd = start.length-1;
//                    int myPosition1 = position + 1;
//                    String[] subarray = subArray(start, myPosition1, subEnd);
//                    ad = new ArrayAdapter(
//                            context,
//                            android.R.layout.simple_spinner_item,
//                            subarray);
//                    ad.setDropDownViewResource(
//                            android.R.layout
//                                    .simple_spinner_dropdown_item);
//                    spinnerEnd.setAdapter(ad);
//                    if(subarray.length==0)
//                    {
//                        startTimeAndTimeInterface.endTime(dates.get(myPosition),"");
//                    }
//                }
//                holder.firstTime = true;
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        String endTimePOsition = endTime.get(dates.get(position));
//        spinnerEnd.setSelection(getEndDatePosition(endTimePOsition,holder.end));
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return dates.size();
//    }
//
//    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
//        private boolean firstTime = false;
//
//        String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
//                "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
//                "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
//                "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
//                "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
//                "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
//                "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
//                "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
//                "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
//                "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
//                "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
//                "11:15 AM","11:30 AM","11:45 AM",
//                "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
//                "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
//                "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
//                "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
//                "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
//                "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
//                "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
//                "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
//                "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
//                "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
//                "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
//                "11:15 PM","11:30 PM","11:45 PM"
//        };
//
//        public SelectTimeViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//
//    public int getStartDatePosition(String code)
//    {
//        int i=0;
//        while (i<start.length)
//        {
//            if(start[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//        return 0;
//    }
//
//    public int getEndDatePosition(String code,String[] end)
//    {
//        int i=0;
//        while (i<end.length)
//        {
//            if(end[i].equalsIgnoreCase(code))
//            {
//                int z = i;
//                return i;
//            }
//            i++;
//        }
//        return 0;
//    }
//}

//Original
package com.shifts.healthcare.adapters;
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

import com.shifts.healthcare.R;
import com.shifts.healthcare.models.Date;
import com.shifts.healthcare.util.StartTimeAndTimeInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SelectEditTimeAdapter extends RecyclerView.Adapter<SelectEditTimeAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private List<String> dates;

    private Context context;

    private List<Date> dateList = new LinkedList<>();

    private Map<String,String> startTime ;

    private Map<String,String> endTime ;

    private StartTimeAndTimeInterface startTimeAndTimeInterface;

    String[] start = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM"
    };

    String[] end = {"12:00 AM","12:15 AM","12:30 AM","12:45 AM","01:00 AM",
            "01:15 AM","01:30 AM","01:45 AM","02:00 AM",
            "02:15 AM","02:30 AM","02:45 AM","03:00 AM",
            "03:15 AM","03:30 AM","03:45 AM","04:00 AM",
            "04:15 AM","04:30 AM","04:45 AM","05:00 AM",
            "05:15 AM","05:30 AM","05:45 AM","06:00 AM",
            "06:15 AM","06:30 AM","06:45 AM","07:00 AM",
            "07:15 AM","07:30 AM","07:45 AM","08:00 AM",
            "08:15 AM","08:30 AM","08:45 AM","09:00 AM",
            "09:15 AM","09:30 AM","09:45 AM","10:00 AM",
            "10:15 AM","10:30 AM","10:45 AM","11:00 AM",
            "11:15 AM","11:30 AM","11:45 AM",
            "12:00 PM","12:15 PM","12:30 PM","12:45 PM","01:00 PM",
            "01:15 PM","01:30 PM","01:45 PM","02:00 PM",
            "02:15 PM","02:30 PM","02:45 PM","03:00 PM",
            "03:15 PM","03:30 PM","03:45 PM","04:00 PM",
            "04:15 PM","04:30 PM","04:45 PM","05:00 PM",
            "05:15 PM","05:30 PM","05:45 PM","06:00 PM",
            "06:15 PM","06:30 PM","06:45 PM","07:00 PM",
            "07:15 PM","07:30 PM","07:45 PM","08:00 PM",
            "08:15 PM","08:30 PM","08:45 PM","09:00 PM",
            "09:15 PM","09:30 PM","09:45 PM","10:00 PM",
            "10:15 PM","10:30 PM","10:45 PM","11:00 PM",
            "11:15 PM","11:30 PM","11:45 PM"
    };

    public SelectEditTimeAdapter(List<String> dates, Context context, StartTimeAndTimeInterface startTimeAndTimeInterface,Map<String,String> startTime,Map<String,String> endTime)
    {
        this.dates = dates;
        this.context = context;
        this.startTime= startTime;
        this.endTime = endTime;
        this.startTimeAndTimeInterface = startTimeAndTimeInterface;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.time_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        int myPosition = position;
        Spinner spinnerStart,spinnerEnd;
        spinnerStart = holder.itemView.findViewById(R.id.spinnerStartTime);
        spinnerEnd = holder.itemView.findViewById(R.id.spinnerEndTime);
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        TextView tvDay = holder.itemView.findViewById(R.id.tvDay);

        tvDate.setText(dates.get(position));

        tvDay.setText(position+1+"");

        ad = new ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                start);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinnerStart.setAdapter(ad);
        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startTimeAndTimeInterface.startTime(dates.get(myPosition),spinnerStart.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerStart.setSelection(getStartDatePosition(startTime.get(dates.get(position))));

        ad = new ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                end);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spinnerEnd.setAdapter(ad);

        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startTimeAndTimeInterface.endTime(dates.get(myPosition),spinnerEnd.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerEnd.setSelection(getStartDatePosition(endTime.get(dates.get(position))));

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

    public int getStartDatePosition(String code)
    {
        int i=0;

        while (i<start.length)
        {
            if(start[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }

        return 0;
    }

    public int getEndDatePosition(String code)
    {
        int i=0;
        while (i<end.length)
        {
            if(end[i].equalsIgnoreCase(code))
            {
                int z = i;
                return i;
            }
            i++;
        }
        return 0;
    }
}