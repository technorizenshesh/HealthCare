package com.technorizen.healthcare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.models.SuccessResGetHiredWorker;
import com.technorizen.healthcare.util.BlockAddRatingInterface;

import java.util.ArrayList;

public class HiredWorkerAdapter extends RecyclerView.Adapter<HiredWorkerAdapter.SelectTimeViewHolder> {

    private ArrayList<SuccessResGetHiredWorker.Result> hiredWorkers;

    private Context context;

    private BlockAddRatingInterface blockAddRatingInterface;

    public HiredWorkerAdapter(Context context,ArrayList<SuccessResGetHiredWorker.Result> hiredWorkers,BlockAddRatingInterface blockAddRatingInterface)
    {
        this.context = context;
        this.hiredWorkers = hiredWorkers;
        this.blockAddRatingInterface = blockAddRatingInterface;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.worker_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {

        TextView tvWorkerName = holder.itemView.findViewById(R.id.tvWorkerName);
        TextView tvWorkerDesignation = holder.itemView.findViewById(R.id.tvWorkerDesignation);
        ImageView ivProfile = holder.itemView.findViewById(R.id.ivProfile);
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        AppCompatButton btnAddRating = holder.itemView.findViewById(R.id.btnRate);
        AppCompatButton btnBlockUnblock = holder.itemView.findViewById(R.id.btnBlock);
        AppCompatButton btnRehire = holder.itemView.findViewById(R.id.btnRehire);
        tvWorkerName.setText(hiredWorkers.get(position).getFirstName()+ " "+ hiredWorkers.get(position).getLastName());
        tvWorkerDesignation.setText(hiredWorkers.get(position).getDesignation());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(13));
        Glide.with(context)
                .load(hiredWorkers.get(position).getImage())
                .centerCrop()
                .apply(requestOptions)
                .into(ivProfile);
        if(hiredWorkers.get(position).getUserApproval().equalsIgnoreCase("Unblocked"))
        {
            btnBlockUnblock.setText(R.string.block);
            btnRehire.setOnClickListener(v ->
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("name",hiredWorkers.get(position).getFirstName() +" " +hiredWorkers.get(position).getLastName());
                        bundle.putString("id",hiredWorkers.get(position).getWorkerId());
                        bundle.putString("designation",hiredWorkers.get(position).getDesignation());
                        Navigation.findNavController(v).navigate(R.id.action_hiresWorkerFragment_to_postShiftsFragment,bundle);
                    }
            );
        }else
        {
            btnBlockUnblock.setText(R.string.unblock);
            btnRehire.setClickable(false);
        }
           btnBlockUnblock.setOnClickListener(v ->
                {
                    String status = btnBlockUnblock.getText().toString();
                    String newStatus="";
                    String newButtonText="";
                    if(status.equalsIgnoreCase("Block"))
                    {
                        newStatus = "Blocked";
                        newButtonText = "Unblock";
                        btnRehire.setClickable(false);
                    }
                    else
                    {
                        newStatus = "Unblocked";
                        newButtonText = "Block";
                        btnRehire.setOnClickListener(v1 ->
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name",hiredWorkers.get(position).getFirstName() +" " +hiredWorkers.get(position).getLastName());
                                    bundle.putString("id",hiredWorkers.get(position).getWorkerId());
                                    bundle.putString("designation",hiredWorkers.get(position).getDesignation());
                                    Navigation.findNavController(v1).navigate(R.id.action_hiresWorkerFragment_to_postShiftsFragment,bundle);
                                }
                        );
                    }
                    btnBlockUnblock.setText(newButtonText);
                    blockAddRatingInterface.block(hiredWorkers.get(position).getWorkerId(),newStatus);
                }
                );

        btnAddRating.setOnClickListener(v ->
                {
                    showImageSelection(hiredWorkers.get(position).getAvgRating(),hiredWorkers.get(position).getWorkerId());
                }
                );

        int lastPosition = hiredWorkers.size()-1;

        if(hiredWorkers.size() == 1)
        {
            if(position == 0)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(25, 30, 25, 100);
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

    String myRating = "";

    public void showImageSelection(String rating,String workerId) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.rating_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);

        if(rating!=null)
        {
            ratingBar.setRating(Float.parseFloat(String.valueOf(rating)));
        }

        AppCompatButton btnAddRating = dialog.findViewById(R.id.btnAddRating);

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    ratingBar.setRating(stars);

                    myRating = stars+"";

                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }

                return true;
            }});

        ImageView ivCancel = dialog.findViewById(R.id.cancel);

        btnAddRating.setOnClickListener(v ->
                {
                    blockAddRatingInterface.addRating(workerId,"",myRating);

                    dialog.dismiss();
                }
                );

        ivCancel.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
        );

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return hiredWorkers.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
