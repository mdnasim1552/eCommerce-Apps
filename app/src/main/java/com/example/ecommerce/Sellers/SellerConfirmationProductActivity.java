package com.example.ecommerce.Sellers;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.SellerCartViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SellerConfirmationProductActivity extends AppCompatActivity {

    private String userID = "";
    private ListView productsList;
    private SellerCartViewHolder adapter;
    private ArrayList<Cart> list;
    private Cart cart;
    private Products products;
    private DatabaseReference cartListRef;
    private ImageView confirmProductsBtn,sellerUserBackbtn;
    private ArrayList<String> addAllProductPID=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_confirmation_product);

        userID = getIntent().getStringExtra("uid");
        productsList = findViewById(R.id.seller_user_products_list);
        confirmProductsBtn=findViewById(R.id.seller_confirm_products);
        sellerUserBackbtn=findViewById(R.id.seller_user_backbtn);
        sellerUserBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirmProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmProductMethod();
            }
        });

        cart=new Cart();
        products=new Products();
        list=new ArrayList<Cart>();
        adapter=new SellerCartViewHolder(this,list);

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View");//.child("01760091552").child("Products")
        addAllProductPID.clear();
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<String> listp) {
                cartListRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            for(int i=0;i<listp.size();i++){
                                if(ds.child("Products").child(listp.get(i)).exists()){
                                    if(ds.child("Products").child(listp.get(i)).child("sid").getValue().toString().equals(userID)
                                            && ds.child("Products").child(listp.get(i)).child("confirmationOfSellers").getValue().toString().equals("not confirm")){
                                        cart=ds.child("Products").child(listp.get(i)).getValue(Cart.class);
                                        list.add(cart);
                                    }
                                }
                            }

                            //cart=ds.getValue(Cart.class);
                            //list.add(cart);
                        }
                        productsList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void confirmProductMethod() {
        addAllProductPID.clear();
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<String> listp) {
                cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            for(int i=0;i<listp.size();i++){
                                if(ds.child("Products").child(listp.get(i)).exists()){
                                    if(ds.child("Products").child(listp.get(i)).child("sid").getValue().toString().equals(userID)
                                            && ds.child("Products").child(listp.get(i)).child("confirmationOfSellers").getValue().toString().equals("not confirm")){
                                        //cartListRef.child(ds).child()//&& ds.child("Products").child(listp.get(i)).child("confirmationOfSellers").getValue().toString().equals("")
                                        cartListRef.child(ds.getKey()).child("Products").child(listp.get(i)).child("confirmationOfSellers").setValue("confirm");
                                        //Toast.makeText(SellerConfirmationProductActivity.this, ds.getKey().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void readData(FirebaseCallback firebaseCallback){
        FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String pid=ds.child("pid").getValue().toString();
                    addAllProductPID.add(pid);
                }
                firebaseCallback.onCallback(addAllProductPID);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private interface FirebaseCallback{
        void onCallback(ArrayList<String> listp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}