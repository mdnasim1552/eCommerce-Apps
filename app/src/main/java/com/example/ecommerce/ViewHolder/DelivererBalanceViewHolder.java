package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import org.jetbrains.annotations.NotNull;

public class DelivererBalanceViewHolder extends RecyclerView.ViewHolder{
    public TextView date_Deliverer,amount_Deliverer,invoice_Deliverer;
    public DelivererBalanceViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        date_Deliverer=itemView.findViewById(R.id.balance_date_deliverer);
        amount_Deliverer=itemView.findViewById(R.id.balance_amount_deliverer);
        invoice_Deliverer=itemView.findViewById(R.id.balance_invoice_number_deliverer);
    }
}
