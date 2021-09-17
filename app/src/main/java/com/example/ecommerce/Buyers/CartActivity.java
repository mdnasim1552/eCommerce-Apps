package com.example.ecommerce.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView listView;

    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;

    private CartViewHolder adapter;
    private ArrayList<Cart> list;
    private Cart cart;
    private int overTotalPrice = 0;

    private DatabaseReference cartListRef;

    private RelativeLayout relativeLayout;
    private ArrayList<String> productDetails = new ArrayList<String>();
    private ArrayList<String> productSIDList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = findViewById(R.id.cart_list);


        NextProcessBtn = (Button) findViewById(R.id.next_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        txtMsg1 = (TextView) findViewById(R.id.msg1);
        relativeLayout=findViewById(R.id.rl11);

        cart=new Cart();
        list=new ArrayList<Cart>();
        adapter=new CartViewHolder(this,list);
        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products");

        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    overTotalPrice = 0;
                    list.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        cart=ds.getValue(Cart.class);
                        list.add(cart);
                        overTotalPrice=overTotalPrice+(Integer.valueOf(cart.getPrice()))*(Integer.valueOf(cart.getQuantity()));
                        int individualProductPrice=(Integer.valueOf(cart.getPrice()))*(Integer.valueOf(cart.getQuantity()));
                        String removeSpaceFromPname=cart.getPname().trim().replaceAll("\\s+","#");
                        productDetails.add(removeSpaceFromPname+" "+cart.getPrice()+" "+cart.getQuantity()+" "+String.valueOf(individualProductPrice)+" "+cart.getSid());
                        productSIDList.add(cart.getSid()+" "+String.valueOf(individualProductPrice)+" "+removeSpaceFromPname);
                    }
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    txtTotalAmount.setText("Total Price = Tk " + formatter.format(overTotalPrice));//formatter.format(overTotalPrice)

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Cart c = adapter.getItem(position);
                            setAlertDialogForEachItem(c.getPid());
                            adapter.notifyDataSetChanged();// notify the changed
                            //Toast.makeText(CartActivity.this, "You clicked " + c.getPname() + "\n " + c.getPid(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    listView.setAdapter(adapter);
                    //CheckOrderState();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("TAG", "onCancelled", error.toException());
            }
        });



        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(overTotalPrice==0){
                    Toast.makeText(CartActivity.this, "please add product to your cart.", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    intent.putExtra("ArrayList",productDetails);
                    intent.putExtra("ArrayListForSellers",productSIDList);
                    intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }


    private void setAlertDialogForEachItem(String pid) {
        CharSequence options[] = new CharSequence[]
                {
                        "Edit",
                        "Remove"
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("Cart Options:");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0)
                {
                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("pid", pid);
                    startActivity(intent);
                }

                if (i == 1)
                {
                    cartListRef.child(pid)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Cart List")
                                                .child("Admin View")
                                                .child(Prevalent.currentOnlineUser.getPhone())
                                                .child("Products")
                                                .child(pid).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        Toast.makeText(CartActivity.this, "Item removed successfully.", Toast.LENGTH_SHORT).show();
                                        //Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                        //startActivity(intent);
                                        //finish();
                                    }
                                }
                            });
                }
            }
        });
        builder.show();

    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {

                        txtTotalAmount.setText("Dear " + userName + "\n order is shipped successfully.");
                        relativeLayout.getLayoutParams().height = 220;
                        listView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, your final order has been Shipped successfully. Soon you will received your order at your door step.");
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    } else if(shippingState.equals("not shipped"))
                    {
                        txtTotalAmount.setText("Shipping State = Not Shipped");
                        relativeLayout.getLayoutParams().height = 140;
                        listView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, your final order has been placed successfully. Soon it will be verified.");
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    }
                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}