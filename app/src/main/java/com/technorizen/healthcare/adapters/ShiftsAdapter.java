package com.technorizen.healthcare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.util.DeleteShifts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.technorizen.healthcare.fragments.PostShiftsFragment.isNumber;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;
    private Context context;
    private ArrayList<SuccessResGetPost.Result> postedList ;
    List<String> monthsList;
    private boolean from;
    private DeleteShifts shifts;
    private boolean showNotes = false;
    public void addList(ArrayList<SuccessResGetPost.Result> postedList)
    {
        this.postedList = postedList;
    }
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
        monthsList = new LinkedList<>();
        List<String> dates = new LinkedList<>();
        List<String> listStartTime = new LinkedList<>();
        List<String> listEndTime = new LinkedList<>();
        showNotes = false;
        List<SuccessResGetPost.PostshiftTime> postshiftTimeList =  new LinkedList<>();
        postshiftTimeList = postedList.get(position).getPostshiftTime();
        Collections.sort(postshiftTimeList, new Comparator<SuccessResGetPost.PostshiftTime>(){
            public int compare(SuccessResGetPost.PostshiftTime obj1, SuccessResGetPost.PostshiftTime obj2) {
                // ## Ascending order
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                String  dtDate1 = obj1.getShiftDate()+" 8:20";
                String  dtDate2 = obj2.getShiftDate()+" 8:20";
                Date  date1 = null;
                Date  date2 = null;
                try {
                    date1 = format.parse(dtDate1);
                    date2 = format.parse(dtDate2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date1.compareTo(date2);
            }
        });
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
        TextView tvID = holder.itemView.findViewById(R.id.tvID);
        TextView tvShiftNumber = holder.itemView.findViewById(R.id.tvShiftNumber);
        TextView tvShiftNotes = holder.itemView.findViewById(R.id.tvShiftsNotes);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        TextView tvShiftType = holder.itemView.findViewById(R.id.tvShiftType);
        TextView tvTransit = holder.itemView.findViewById(R.id.tvTransit);
        LinearLayout llRehire = holder.itemView.findViewById(R.id.llRehire);
        LinearLayout llRecruitment = holder.itemView.findViewById(R.id.llRecruitment);
        LinearLayout llRehiredShift = holder.itemView.findViewById(R.id.llRehiredShift);
        AppCompatButton btnPost = holder.itemView.findViewById(R.id.btnPost);
        AppCompatButton btnCancelRehiredShift = holder.itemView.findViewById(R.id.btnCancelRehiredShift);
        tvShiftNotes.setText(postedList.get(position).getShiftNotes());
        ImageView ivAdd = holder.itemView.findViewById(R.id.plus);
        ImageView ivMinus = holder.itemView.findViewById(R.id.minus);
        tvID.setText(postedList.get(position).getRemainingVacancies());
        tvShiftNumber.setText(postedList.get(position).getShiftNo());
        AppCompatButton btnDelete = holder.itemView.findViewById(R.id.btnDelete);
        AppCompatButton btnAccept = holder.itemView.findViewById(R.id.btnAccept);
        AppCompatButton btnReject = holder.itemView.findViewById(R.id.btnReject);
        AppCompatButton btnShiftAccepted = holder.itemView.findViewById(R.id.btnShiftAccepted);
        ImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);
        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);
        if(postedList.get(position).getNoVacancies().equalsIgnoreCase("1"))
        {
            tvID.setVisibility(View.GONE);
        }
        else
        {
            tvID.setVisibility(View.VISIBLE);
        }
        if(postedList.get(position).getType().equalsIgnoreCase("Directshift"))
        {
            tvShiftType.setVisibility(View.GONE);
            btnAccept.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
            btnDelete.setBackground(context.getResources().getDrawable(R.drawable.button_bg));
        }
        else if (postedList.get(position).getType().equalsIgnoreCase("Recruitmentshift"))
        {
            tvShiftType.setVisibility(View.VISIBLE);
            tvShiftType.setText(R.string.recruitment);
            btnDelete.setBackground(context.getResources().getDrawable(R.drawable.bright_button_bg));
            btnAccept.setBackground(context.getResources().getDrawable(R.drawable.bright_button_bg));
        }
        if(from)
        {
            if(postedList.get(position).getStatus().equalsIgnoreCase("Rehire_Rejected"))
            {
                btnDelete.setVisibility(View.GONE);
                btnAccept.setVisibility(View.GONE);
                llRehiredShift.setVisibility(View.VISIBLE);
            }else
            {
                btnDelete.setVisibility(View.VISIBLE);
                btnAccept.setVisibility(View.GONE);
                llRehiredShift.setVisibility(View.GONE);
            }
        }else
        {
            if(postedList.get(position).getType().equalsIgnoreCase("Rehire"))
            {
                llRehire.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
                btnAccept.setVisibility(View.GONE);
            }
            else
            {
                llRehire.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnAccept.setVisibility(View.VISIBLE);
            }
        }
        if (from)
        {
            tvDistance.setVisibility(View.GONE);
        }
        else
        {
            tvDistance.setVisibility(View.VISIBLE);
            tvDistance.setText(context.getString(R.string.distance)+postedList.get(position).getDistance()+" "+context.getString(R.string.miles));
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
            String text;
            String totalHours =  getTime24Difference(postshiftTimeList.get(0).getStartTimeNew(),postshiftTimeList.get(0).getEndTimeNew(),postedList.get(position).getTransitAllowance());
            Double d = Double.parseDouble(totalHours);
            if(d < 2.0)
            {
                text  = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+totalHours+" hr)";
            }
            else
            {
                text = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+totalHours+" hrs)";
            }


//            Double d = Double.parseDouble(postshiftTimeList.get(0).getTotalHours());
//            if(d < 2.0)
//            {
//               text  = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+postshiftTimeList.get(0).getTotalHours()+" hr)";
//            }
//            else
//            {
//                text = context.getString(R.string.time_col)+" "+startTime+" - "+endTime+"("+postshiftTimeList.get(0).getTotalHours()+" hrs)";
//            }

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
                tvDate.setText(context.getString(R.string.date_col)+todayAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else
        {
            for (SuccessResGetPost.PostshiftTime dateTime:postshiftTimeList)
            {
                String month = dateTime.getNewMonth();
                Log.d(TAG, "onBindViewHolder: "+month);
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
        if(postedList.get(position).getUnpaidBreak().equalsIgnoreCase("None"))
        {
            tvBreak.setText(context.getString(R.string.unpaid_break)+postedList.get(position).getUnpaidBreak());
        }
        else
        {
            tvBreak.setText(context.getString(R.string.unpaid_break)+postedList.get(position).getUnpaidBreak()+" Minutes");
        }

        if(postedList.get(position).getTransitAllowance().equalsIgnoreCase("None"))
        {
            tvTransit.setText(context.getString(R.string.transit_allowance)+postedList.get(position).getTransitAllowance());
        }
        else
        {
            tvTransit.setText(context.getString(R.string.transit_allowance)+postedList.get(position).getTransitAllowance()+" Hour");
        }

        String pay = " $" + postshiftTimeList.get(0).getPayamount() +" @ $"+postedList.get(position).getHourlyRate()+"/hr";

        hrRate.setText(context.getString(R.string.pay_col)+pay);
        tvCovid.setText(context.getString(R.string.covid_19_negative)+postedList.get(position).getCovidStatus());
        tvLocation.setText(postedList.get(position).getShiftLocation());

      //  tvNumOfShifts.setText(context.getString(R.string.number_of_shifts_col)+postedList.get(position).getTotalShift());

        tvJobPosition.setText(postedList.get(position).getJobPosition());

        if(postedList.get(position).getAccountType().equalsIgnoreCase("Company"))
        {
            tvCompanyName.setText(postedList.get(position).getCompany());
        }else
        {
            tvCompanyName.setText(postedList.get(position).getUserName());
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(13));
        Glide.with(context)
                .load(postedList.get(position).getUserImage())
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

        btnDelete.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Delete Shift")
                            .setMessage("Are you sure you want to delete shift?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    try {
                                        shifts.onClick(postedList.get(position).getShiftId(),"",postedList.get(position).getPostshiftTime(),"");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();
                }
                );

        btnCancelRehiredShift.setOnClickListener(v ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Delete Shift")
                            .setMessage("Are you sure you want to delete shifts?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        shifts.onClick(postedList.get(position).getId(),"",postedList.get(position).getPostshiftTime(),"");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();

                }
        );

        btnPost.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Post Shift")
                            .setMessage("Are you sure you want to post shift?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    shifts.shiftConfimation(postedList.get(position).getShiftId(),"","","");

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();
                }
        );

        btnAccept.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Accept Shift")
                            .setMessage("Are you sure you want to accept shift?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        shifts.onClick(postedList.get(position).getId(),postedList.get(position).getUserId(),postedList.get(position).getPostshiftTime(),postedList.get(position).getType());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();
                }
        );

        btnReject.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Decline Shift")
                            .setMessage("Are you sure you want to decline shift?")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    shifts.rejectSHift(postedList.get(position).getId(),postedList.get(position).getUserId(),postedList.get(position).getPostshiftTime(),postedList.get(position).getType());
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();

                }
                );

        btnShiftAccepted.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Accept Shift")
                            .setMessage("Are you sure you want to accept shift?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    try {
                                        shifts.onClick(postedList.get(position).getId(),postedList.get(position).getUserId(),postedList.get(position).getPostshiftTime(),postedList.get(position).getType());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();
                }
        );

        tvLocation.setOnClickListener(v ->
                {
                  /*  Uri gmmIntentUri = Uri.parse("geo:13.0698281,77.58960549999999");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
*/
//
//                    Geocoder coder = new Geocoder(context);
//                    List<Address> address;
//
//
//                    try {
//                        // May throw an IOException
//                        address = coder.getFromLocationName(postedList.get(position).getShiftsdetail().get(0).getShiftLocation(), 5);
//                        if (address == null) {
//
//                        }
//
//                        Address location = address.get(0);
//
//                        String lat = String.valueOf(location.getLatitude());
//                        String lon = String.valueOf(location.getLongitude());
//
//                        String uri = "http://maps.google.com/maps?q=loc:"+lat+","+lon;
//
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                        context.startActivity(intent);
//
//                    } catch (IOException ex) {
//
//                        ex.printStackTrace();
//                    }


                    String lat = postedList.get(position).getLocationLat();
                    String lon = postedList.get(position).getLocationLon();

                    //                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat,lon);
                    String uri = "http://maps.google.com/maps?q=loc:"+lat+","+lon;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
                }
        );

        int lastPosition = postedList.size()-1;

        if(postedList.size() == 1)
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
    public static String getTime24Difference(String time1,String time2,String transit)
    {
        long myTime1=0;
        long myTime2=0;
        String namepass[] = time1.split(":");
        myTime1 = Long.parseLong(namepass[0]);
        myTime1 = myTime1*60;
        myTime1 = myTime1+Long.parseLong(namepass[1]);
        Log.d(TAG, "getTime24DifferenceOne: "+myTime1);
        String namepass1[] = time2.split(":");
        myTime2 = Long.parseLong(namepass1[0]);
        myTime2 = myTime2*60;
        myTime2 = myTime2+Long.parseLong(namepass1[1]);
        Log.d(TAG, "getTime24DifferenceTwo: "+myTime2);
        long remainingTime = 0;
        float timeDifference;
        if(myTime1>myTime2)
        {
           remainingTime = 1440 - myTime1 ;
           timeDifference = remainingTime+myTime2;
           Log.d(TAG, "Time Difference: "+timeDifference);
        }
        else
        {
            if(myTime1==myTime2)
            {
               timeDifference = 1440;
            }
            else
            {
                timeDifference = myTime2 - myTime1;
            }
        }
        float totalHours = timeDifference / 60;
        float totalMinutes = timeDifference % 60;
        Log.d(TAG, "Time Difference: "+totalHours);
        float total = totalMinutes / 60;
        Log.d(TAG, "Time DifferenceMinutes: "+totalMinutes);
        Log.d(TAG, "Toal: "+total);
        if(!transit.equalsIgnoreCase("None"))
        {
//            long hrs = Long.parseLong(transit);
//            totalHours = totalHours+hrs;
        }
        Float mySum = totalHours+total;
        String totalMyHours = String.valueOf(totalHours);
        Log.d(TAG, "Time DifferenceMinutes: "+mySum);
        return  totalMyHours;
    }

}
