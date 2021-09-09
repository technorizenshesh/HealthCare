package com.technorizen.healthcare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.util.DeleteShifts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.SelectTimeViewHolder> {
    ArrayAdapter ad;

    private Context context;

    private ArrayList<SuccessResGetPost.Result> postedList ;
    List<String> monthsList = new LinkedList<>();

    private boolean from ;

    private DeleteShifts shifts;

    public ShiftsAdapter(Context context, ArrayList<SuccessResGetPost.Result> postedList,boolean from,DeleteShifts shifts)
    {
        this.context = context;
        this.postedList = postedList;
        this.from = from;
        this.shifts =shifts;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.shifts_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        List<String> dates = new LinkedList<>();
        List<String> listStartTime = new LinkedList<>();
        List<String> listEndTime = new LinkedList<>();

        List<SuccessResGetPost.PostshiftTime> postshiftTimeList =  new LinkedList<>();

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

        AppCompatButton btnDelete = holder.itemView.findViewById(R.id.btnDelete);
        AppCompatButton btnAccept = holder.itemView.findViewById(R.id.btnAccept);

        ImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);

        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);

        if(from)
        {

            btnDelete.setVisibility(View.VISIBLE);
            btnAccept.setVisibility(View.GONE);

        }else

        {
            btnDelete.setVisibility(View.GONE);
            btnAccept.setVisibility(View.VISIBLE);
        }

        String date ="";
        String time ="";

        for (SuccessResGetPost.PostshiftTime dateTime:postshiftTimeList)
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

        if(postedList.get(position).getTimeType().equalsIgnoreCase("Single"))
        {

            tvTime.setVisibility(View.VISIBLE);
            tvMultipleTime.setVisibility(View.GONE);
            String startTime = postshiftTimeList.get(0).getStartTime();
            String endTime = postshiftTimeList.get(0).getEndTime();
            String text = context.getString(R.string.time_col)+" "+startTime+" - "+endTime;
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

                String pattern = "EEE, d MMM yyyy";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
                DateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
                String todayAsString = df.format(myDate);

                tvDate.setText(context.getString(R.string.date_col)+todayAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else

        {


            for (SuccessResGetPost.PostshiftTime dateTime:postshiftTimeList)

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

                for(SuccessResGetPost.PostshiftTime dateTime:postshiftTimeList)
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

            tvDate.setText(context.getString(R.string.date_col)+myMultipleDate);

        }

        tvDuty.setText("("+postedList.get(position).getDutyOfWorker()+")");
        tvBreak.setText(context.getString(R.string.unpaid_break)+postedList.get(position).getUnpaidBreak());
        hrRate.setText(context.getString(R.string.pay_col)+postedList.get(position).getHourlyRate());
        tvCovid.setText(context.getString(R.string.covid_19_negative)+postedList.get(position).getCovidStatus());
        tvLocation.setText(postedList.get(position).getShiftLocation());
        tvNumOfShifts.setText(context.getString(R.string.number_of_shifts_col)+postedList.get(position).getTotalShift());

        tvJobPosition.setText(postedList.get(position).getJobPosition());

        if(postedList.get(position).getAccountType().equalsIgnoreCase("Company"))
        {
            tvCompanyName.setText(postedList.get(position).getCompany());
        }else
        {
            tvCompanyName.setText(postedList.get(position).getUserName());
        }

        Glide.with(context)
                .load(postedList.get(position).getUserImage())
                 .centerCrop()
                .into(ivProfile);

        rlShiftsNote.setOnClickListener(v ->
                {
                    showShiftsNotes(postedList.get(position).getShiftNotes());
                }
                );

        btnDelete.setOnClickListener(v ->

                {


                    new AlertDialog.Builder(context)
                            .setTitle("Delete Shift")
                            .setMessage("Are you sure you want to delete shifts?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    shifts.onClick(postedList.get(position).getId());

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
                );

        btnAccept.setOnClickListener(v ->
                {



                }
                );


        tvLocation.setOnClickListener(v ->
                {
                  /*  Uri gmmIntentUri = Uri.parse("geo:13.0698281,77.58960549999999");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
*/

                    String lat = postedList.get(position).getLocationLat();
                    String lon = postedList.get(position).getLocationLon();

                    //                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat,lon);

                    String uri = "http://maps.google.com/maps?q=loc:"+lat+","+lon;


                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);

                }
        );



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
