package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;

import org.jetbrains.annotations.NotNull;

public class SellerProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtProductName, txtProductDescription, txtProductPrice, txtProductStatus;
    public ImageView imageView;
    public ItemClickListner listner;

    public SellerProductViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.seller_product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.seller_product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.seller_product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.seller_product_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.seller_product_state);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
