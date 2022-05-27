package com.technorizen.healthcare.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.maxpilotto.creditcardview.CreditCardView;
import com.maxpilotto.creditcardview.models.Brand;
import com.maxpilotto.creditcardview.models.CreditCard;
import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.CardItemBinding;
import com.technorizen.healthcare.models.SuccessResGetCardDetails;
import com.technorizen.healthcare.util.CardInterface;

import java.text.ParseException;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CategoryViewHolder> {

    private Context context;
    CardItemBinding binding;
    private List<SuccessResGetCardDetails.Result> cardList;
    private CardInterface cardInterface;
    private int myPosition;
    private int selectedPosition = -1;
    private boolean from;

    public CardAdapter(Context context, List<SuccessResGetCardDetails.Result> cardList,boolean from,CardInterface cardInterface)
    {
        this.context = context;
        this.cardList= cardList;
        this.from = from;
        this.cardInterface = cardInterface;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CardItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        myPosition = position;
        TextView tvAddress,tvName,tvPhone;
        AppCompatButton btnEdit;
        ImageView ivDelete,ivChecked;
        ivChecked = holder.itemView.findViewById(R.id.checked);
        ivDelete = holder.itemView.findViewById(R.id.delete);
        CreditCardView llParent = holder.itemView.findViewById(R.id.card1);
        String cardNumber = cardList.get(position).getCardNo();
        String card = cardList.get(position).getCardNo().substring(cardList.get(position).getCardNo().length()-3);
        llParent.setCardData(cardList.get(position).getCardHolderName(),"XXXXXXXXXXXXX"+card,"",cardList.get(position).getExpMonth()+""+cardList.get(position).getExpYear());
        ivDelete.setOnClickListener(v ->
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Card")
                            .setMessage("Are you sure you want to delete card?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                        if(cardList.get(position).getSetDefault().equalsIgnoreCase("1"))
                                        {
                                            cardInterface.cardSelectdPosition(-1);
                                        }
                                        cardInterface.deleteCard(cardList.get(position).getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_noti)
                            .show();
                }
                );

        llParent.setStyle(Brand.GENERIC,R.style.MyCardStyle);

        if(cardList.get(position).getSetDefault().equalsIgnoreCase("1"))
        {
            cardInterface.cardSelectdPosition(position);
            ivChecked.setVisibility(View.VISIBLE);
        }
        else
        {
            ivChecked.setVisibility(View.GONE);
        }
        if (from)
        {
            llParent.setOnClickListener(v ->
                    {
                        new AlertDialog.Builder(context)
                                .setTitle("Default Card")
                                .setMessage("Are you sure you want to update card as default?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        cardInterface.updateDefault(cardList.get(position).getId());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(R.drawable.ic_noti)
                                .show();
                    }
            );
        }

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        public CategoryViewHolder(CardItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
