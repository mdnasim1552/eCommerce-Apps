package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import org.jetbrains.annotations.NotNull;

public class DeliveryOrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
    public Button ShowOrdersBtn,SubmitSecurityCodeBtn;
    public EditText SecurityCode;
    public Button pickOrderBtn,uploadInvoiceBtn;

    public DeliveryOrdersViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.delivery_order_user_name);
        userPhoneNumber = itemView.findViewById(R.id.delivery_order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.delivery_order_total_price);
        userDateTime = itemView.findViewById(R.id.delivery_order_date_time);
        userShippingAddress = itemView.findViewById(R.id.delivery_order_address_city);
        ShowOrdersBtn = itemView.findViewById(R.id.delivery_show_all_products_btn);
        SecurityCode=itemView.findViewById(R.id.delivery_users_security_code);
        SubmitSecurityCodeBtn=itemView.findViewById(R.id.delivery_submit_btn);
        pickOrderBtn=itemView.findViewById(R.id.pick_order_switch);
        uploadInvoiceBtn=itemView.findViewById(R.id.upload_invoice_btn);
    }
}
