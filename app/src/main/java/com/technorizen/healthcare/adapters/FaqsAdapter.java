package com.technorizen.healthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.SuccessResGetFaqs;
import com.technorizen.healthcare.models.SuccessResGetInvoices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private  ArrayList<SuccessResGetFaqs.Result> faqsList ;
    List<String> monthsList = new LinkedList<>();

    private boolean from ;
    private boolean showAns = false;

    public FaqsAdapter(Context context, ArrayList<SuccessResGetFaqs.Result> faqsList)
    {
        this.context = context;
        this.faqsList = faqsList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.faqs_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        TextView tvQues = holder.itemView.findViewById(R.id.tvQs);
        TextView tvAnswer = holder.itemView.findViewById(R.id.tvAns);
        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);

        ImageView ivAdd =  holder.itemView.findViewById(R.id.plus);
        ImageView ivMinus =  holder.itemView.findViewById(R.id.minus);

        tvQues.setText(faqsList.get(position).getTitle());
        tvAnswer.setText(faqsList.get(position).getDescription());

        rlShiftsNote.setOnClickListener(v ->
                {
                    // showShiftsNotes(postedList.get(position).getShiftNotes());

                    showAns = !showAns;

                    if(showAns)
                    {

                        tvAnswer.setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        ivMinus.setVisibility(View.VISIBLE);

                    }
                    else
                    {

                        tvAnswer.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.VISIBLE);
                        ivMinus.setVisibility(View.GONE);
                    }
                }
        );

        int lastPosition = faqsList.size()-1;

        if(faqsList.size() == 1)
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
        return faqsList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
