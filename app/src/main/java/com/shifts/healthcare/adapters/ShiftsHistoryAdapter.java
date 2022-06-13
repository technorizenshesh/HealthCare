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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.shifts.healthcare.R;
import com.shifts.healthcare.models.SuccessResGetShiftInProgress;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class ShiftsHistoryAdapter extends RecyclerView.Adapter<ShiftsHistoryAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private  ArrayList<SuccessResGetShiftInProgress.Result> postedList ;
    List<String> monthsList = new LinkedList<>();

    private boolean from ;
    private boolean showNotes = false;


    public ShiftsHistoryAdapter(Context context, ArrayList<SuccessResGetShiftInProgress.Result> postedList, boolean from)
    {
        this.context = context;
        this.postedList = postedList;
        this.from = from;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.shift_in_progress_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        List<String> dates = new LinkedList<>();
        List<String> listStartTime = new LinkedList<>();
        List<String> listEndTime = new LinkedList<>();

        showNotes = false;

        List<SuccessResGetShiftInProgress.PostshiftTime> postshiftTimeList =  new LinkedList<>();

        postshiftTimeList = postedList.get(position).getPostshiftTime();

        TextView tvCompanyName = holder.itemView.findViewById(R.id.tvCompanyName);
        TextView tvJobPosition = holder.itemView.findViewById(R.id.jobPosition);
        TextView tvDuty = holder.itemView.findViewById(R.id.tvDuty);
        TextView tvBreak = holder.itemView.findViewById(R.id.tvBreak);
        TextView hrRate = holder.itemView.findViewById(R.id.hrRate);
        TextView tvCovid = holder.itemView.findViewById(R.id.tvCovid);
        TextView tvLocation = holder.itemView.findViewById(R.id.tvLocation);
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        TextView tvTime = holder.itemView.findViewById(R.id.tvSignleTime);
        TextView tvMultipleTime = holder.itemView.findViewById(R.id.tvMutlipleTime);
        TextView tvNumOfShifts = holder.itemView.findViewById(R.id.tvNumOfShifts);
        TextView tvDistance = holder.itemView.findViewById(R.id.tvDistance);
        TextView tvWorkerName = holder.itemView.findViewById(R.id.tvWorkerName);
        TextView tvWorkerDesignation = holder.itemView.findViewById(R.id.tvWorkerDesignation);
        TextView tvID = holder.itemView.findViewById(R.id.tvID);
        TextView tvShiftNotes = holder.itemView.findViewById(R.id.tvShiftsNotes);

        tvShiftNotes.setText(postedList.get(position).getShiftsdetail().get(0).getShiftNotes());

        ImageView ivAdd = holder.itemView.findViewById(R.id.plus);
        ImageView ivMinus = holder.itemView.findViewById(R.id.minus);

        AppCompatButton btnClock = holder.itemView.findViewById(R.id.btnClock);

        ImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);
        ImageView ivWorkerProfile = holder.itemView.findViewById(R.id.ivWorker);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(13));

        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);

        tvID.setText(postedList.get(position).getId());

        tvDistance.setText(context.getString(R.string.distance)+postedList.get(position).getShiftsdetail().get(0).getDistance()+" "+context.getString(R.string.miles));

        String date ="";
        String time ="";

        for (SuccessResGetShiftInProgress.PostshiftTime dateTime:postshiftTimeList)
        {
            date = date+ dateTime.getNewDate()+",";
            dates.add(dateTime.getNewDate());
            listStartTime.add(dateTime.getStartTime());
            listEndTime.add(dateTime.getEndTime());
        }

        if (date.endsWith(","))
        {
            date = date.substring(0, date.length() - 1);
        }

        if(postedList.get(position).getShiftsdetail().get(0).getTimeType().equalsIgnoreCase("Single"))
        {

            tvTime.setVisibility(View.VISIBLE);
            tvMultipleTime.setVisibility(View.GONE);
            String startTime = postshiftTimeList.get(0).getStartTime();
            String endTime = postshiftTimeList.get(0).getEndTime();
            String text = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+postshiftTimeList.get(0).getTotalHours()+" hrs)";

            //Set Total Time Here

            tvTime.setText(text);

        }
        else

        {
            tvTime.setVisibility(View.GONE);
            tvMultipleTime.setVisibility(View.VISIBLE);
        }

        tvMultipleTime.setOnClickListener(v ->
                {
                    showImageSelection(dates,listStartTime,listEndTime);
                }
                );

        if(postshiftTimeList.size()==1)
        {

            String dtStart = null;
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

            try {

                dtStart = postshiftTimeList.get(0).getShiftDate()+" 8:20";

                Date  myDate = format.parse(dtStart);

                String pattern = "EEE, MMM d, yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(myDate);

                tvDate.setText(context.getString(R.string.date_col)+" "+todayAsString);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else

        {
            for (SuccessResGetShiftInProgress.PostshiftTime dateTime:postshiftTimeList)
            {
                String month = dateTime.getNewMonth();
                if(!isExistMonth(month))
                {
                    monthsList.add(month);
                }
            }
            String myMultipleDate = "";
            for (String monthItem:monthsList)
            {
                myMultipleDate = myMultipleDate + monthItem+" ";

                for(SuccessResGetShiftInProgress.PostshiftTime dateTime:postshiftTimeList)
                {
                    if(monthItem.equalsIgnoreCase(dateTime.getNewMonth()))
                    {
                        myMultipleDate = myMultipleDate + dateTime.getNewDateSingle()+",";
                    }
                }

                if (myMultipleDate.endsWith(","))
                {
                    myMultipleDate = myMultipleDate.substring(0, myMultipleDate.length() - 1);
                }

                myMultipleDate = myMultipleDate +" | ";
            }

            if (myMultipleDate.endsWith(" | "))
            {
                myMultipleDate = myMultipleDate.substring(0, myMultipleDate.length() - 3);
            }
            tvDate.setText(context.getString(R.string.date_col)+" "+myMultipleDate);
        }

        tvDuty.setText("("+postedList.get(position).getShiftsdetail().get(0).getDutyOfWorker()+")");
        tvBreak.setText(context.getString(R.string.unpaid_break)+" "+postedList.get(position).getShiftsdetail().get(0).getUnpaidBreak());

        String pay = " $" + postshiftTimeList.get(0).getPayamount() +" @ $"+postedList.get(position).getShiftsdetail().get(0).getHourlyRate()+"/hr";

        hrRate.setText(context.getString(R.string.pay_col)+pay);
        tvCovid.setText(context.getString(R.string.covid_19_negative)+postedList.get(position).getShiftsdetail().get(0).getCovidStatus());
        tvLocation.setText(postedList.get(position).getShiftsdetail().get(0).getShiftLocation());

        tvJobPosition.setText(postedList.get(position).getShiftsdetail().get(0).getJobPosition());

        if(postedList.get(position).getShiftsdetail().get(0).getAccountType().equalsIgnoreCase("Company"))
        {
            tvCompanyName.setText(postedList.get(position).getShiftsdetail().get(0).getCompany());
        }else
        {
            tvCompanyName.setText(postedList.get(position).getShiftsdetail().get(0).getUserName());
        }

        Glide.with(context)
                .load(postedList.get(position).getShiftsdetail().get(0).getUserImage())
                 .centerCrop()
                .apply(requestOptions)
                .into(ivProfile);

        rlShiftsNote.setOnClickListener(v ->
                {
                    // showShiftsNotes(postedList.get(position).getShiftNotes());
                    showNotes = !showNotes;
                    if(showNotes)
                    {
                        tvShiftNotes.setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        ivMinus.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvShiftNotes.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.VISIBLE);
                        ivMinus.setVisibility(View.GONE);
                    }
                }
        );

        tvLocation.setOnClickListener(v ->
                {
                  /*  Uri gmmIntentUri = Uri.parse("geo:13.0698281,77.58960549999999");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
*/

                    String lat = postedList.get(position).getShiftsdetail().get(0).getLocationLat();
                    String lon = postedList.get(position).getShiftsdetail().get(0).getLocationLon();

                    //                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat,lon);

                    String uri = "http://maps.google.com/maps?q=loc:"+lat+","+lon;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);

                }
        );

        tvWorkerName.setText(postedList.get(position).getShiftsdetail().get(0).getWorkerName());
        tvWorkerDesignation.setText("( "+postedList.get(position).getShiftsdetail().get(0).getWorkerDesignation()+" )");

        Glide.with(context)
                .load(postedList.get(position).getShiftsdetail().get(0).getWorkerImage())
                .centerCrop()
                .apply(requestOptions)
                .into(ivWorkerProfile);

        if(!postedList.get(position).getTotalWorked().equalsIgnoreCase(""))
        {
            int seconds =  Integer.parseInt(postedList.get(position).getTotalWorked());
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;

            // Format the seconds into hours, minutes,
            // and seconds.
            String time21
                    = String
                    .format(Locale.getDefault(),
                            "%02d:%02d:%02d", hours,
                            minutes, secs);

            // Set the text view text.
            btnClock.setText(time21);
        }



    }

    public void showImageSelection(List<String> dates,List<String> startTimeList,List<String> endTimeList) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.multiple_date_recycler_view_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        RecyclerView rvDateTime = dialog.findViewById(R.id.rvDateTime);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        rvDateTime.setHasFixedSize(true);
        rvDateTime.setLayoutManager(new LinearLayoutManager(context));
        rvDateTime.setAdapter(new ShowDateTimeAdapter(context,dates,startTimeList,endTimeList));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public boolean isExistMonth(String month)
    {

        for (String myMonth:monthsList)
        {

            if(month.equalsIgnoreCase(myMonth))
            {
                return true;
            }

        }

        return false;
    }


    public void showShiftsNotes(String notes) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_shifts_notes);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView ivCancel = dialog.findViewById(R.id.cancel);


        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        TextView tvShiftsNotes = dialog.findViewById(R.id.tvShiftsNotes);

        tvShiftsNotes.setText(notes);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return postedList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public String modifyDateLayout(String inputDate) throws ParseException{
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(inputDate);
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }

}
