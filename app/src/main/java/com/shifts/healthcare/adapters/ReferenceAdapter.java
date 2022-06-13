package com.shifts.healthcare.adapters;

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
import com.shifts.healthcare.R;
import com.shifts.healthcare.models.SuccessResGetReference;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.SelectTimeViewHolder> {

    ArrayAdapter ad;

    private Context context;

    private  ArrayList<SuccessResGetReference.Result> referenceList ;
    List<String> monthsList = new LinkedList<>();

    private boolean from ;
    private boolean showNotes = false;

    public ReferenceAdapter(Context context, ArrayList<SuccessResGetReference.Result> referenceList)
    {
        this.context = context;
        this.referenceList = referenceList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reference_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {


        TextView tvCompanyName = holder.itemView.findViewById(R.id.tvCompanyName);
        TextView tvContactPerson = holder.itemView.findViewById(R.id.tvContactPerson);
        TextView tvEmailAdd = holder.itemView.findViewById(R.id.tvEmailAdd);
        TextView tvStartDate = holder.itemView.findViewById(R.id.tvStartDate);
        TextView tvEndDate = holder.itemView.findViewById(R.id.tvEndDate);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);

        tvCompanyName.setText(referenceList.get(position).getCompanyName());
        tvContactPerson.setText(referenceList.get(position).getContactPerson());
        tvEmailAdd.setText(referenceList.get(position).getCompanyEmail());
        tvStartDate.setText(referenceList.get(position).getStartDate());
        tvEndDate.setText(referenceList.get(position).getEndDate());

        int lastPosition = referenceList.size()-1;

        if(referenceList.size() == 1)
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
        return referenceList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



}
