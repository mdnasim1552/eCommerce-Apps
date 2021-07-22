package com.example.ecommerce.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartViewHolder extends ArrayAdapter<Cart> {

    public CartViewHolder(@NonNull Context context, ArrayList<Cart> objects) {
        super(context,R.layout.cart_items_layout, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        Cart itemView = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_items_layout, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_items_layout, parent, false);
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        TextView ProductName = (TextView) convertView.findViewById(R.id.cart_product_name);
        TextView ProductPrice  = (TextView) convertView.findViewById(R.id.cart_product_price);
        TextView ProductQuantity = (TextView) convertView.findViewById(R.id.cart_product_quantity);
        ImageView ProductImage=convertView.findViewById(R.id.cart_item_img);

        ArrayList<String> arrayList=CapitalizedEvery1stLetterOfEveryWord(itemView.getPname());
        String PnameString="";
        for (int i = 0; i < arrayList.size(); i++) {
            if(i!=(arrayList.size()-1)){
                PnameString=PnameString+arrayList.get(i)+" ";
            }else {
                PnameString=PnameString+arrayList.get(i);
            }
        }

        ProductName.setText(PnameString);
        ProductPrice.setText("Price " +formatter.format(Integer.valueOf(itemView.getPrice())) + " Tk");//formatter.format(Integer.valueOf(itemView.getPrice()))
        ProductQuantity.setText("Quantity = " + itemView.getQuantity());
        Picasso.get().load(itemView.getImage()).into(ProductImage);
        return convertView;
    }

    private ArrayList<String> CapitalizedEvery1stLetterOfEveryWord(String pname) {
        ArrayList<String> arr=new ArrayList<>();
        arr.clear();
        pname = pname.toLowerCase();
        String[] string_array_ = pname.trim().split("\\s+");

        for (int i = 0; i < string_array_.length; i++) {
            String splited_word = string_array_[i];
            char first_letter = Character.toUpperCase(splited_word.charAt(0));
            StringBuffer buffer_splited_word = new StringBuffer(splited_word);
            buffer_splited_word.setCharAt(0, first_letter);
            arr.add(buffer_splited_word.toString());
        }
        return arr;
    }

}
//private ItemClickListner itemClickListner;
//txtProductName = itemView.findViewById(R.id.cart_product_name);
//        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
//        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
