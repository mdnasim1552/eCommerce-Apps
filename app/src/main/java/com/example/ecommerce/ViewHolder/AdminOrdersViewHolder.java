package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import org.jetbrains.annotations.NotNull;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder{
    public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
    public Button ShowOrdersBtn;
    public AdminOrdersViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userShippingAddress = itemView.findViewById(R.id.order_address_city);
        ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);
    }
}
