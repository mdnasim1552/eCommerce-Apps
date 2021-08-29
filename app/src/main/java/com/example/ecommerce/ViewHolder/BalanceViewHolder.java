package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import org.jetbrains.annotations.NotNull;

public class BalanceViewHolder extends RecyclerView.ViewHolder{
    public TextView dateB,amountB,productsB,invoiceB;
    public BalanceViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        dateB=itemView.findViewById(R.id.balance_date);
        amountB=itemView.findViewById(R.id.balance_amount);
        productsB=itemView.findViewById(R.id.balance_products);
        invoiceB=itemView.findViewById(R.id.balance_invoice_number);
    }
}
