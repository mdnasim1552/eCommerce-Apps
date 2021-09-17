package com.example.ecommerce.Delivery;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.DelivererCartViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DelivererUserProductsActivity extends AppCompatActivity {

    private String userID = "";
    private ListView productsList;
    private DelivererCartViewHolder adapter;
    private ArrayList<Cart> list;
    private Cart cart;
    private DatabaseReference cartListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverer_user_products);

        userID = getIntent().getStringExtra("uid");
        productsList = findViewById(R.id.deliverer_user_products_list);

        cart=new Cart();
        list=new ArrayList<Cart>();
        adapter=new DelivererCartViewHolder(this,list);

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View").child(userID).child("Products");

        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    cart=ds.getValue(Cart.class);
                    list.add(cart);
                }
                productsList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}