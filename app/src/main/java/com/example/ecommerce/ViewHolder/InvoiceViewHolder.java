package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import org.jetbrains.annotations.NotNull;

public class InvoiceViewHolder extends RecyclerView.ViewHolder{

    public TextView customersID,deliveryMansID,invoiceFileNameID,invoiceDateTimeID;



    public InvoiceViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        customersID=itemView.findViewById(R.id.invoice_customer_id);
        deliveryMansID=itemView.findViewById(R.id.invoice_delivery_mans_id);
        invoiceFileNameID=itemView.findViewById(R.id.invoice_file_name_id);
        invoiceDateTimeID=itemView.findViewById(R.id.invoice_date_time_id);
    }
}
