package com.example.ecommerce.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        formatter = new DecimalFormat("#,###");


        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef, AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull AdminOrdersViewHolder holder, int position, @NonNull @NotNull AdminOrders model) {
                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                holder.userTotalPrice.setText("Total Amount =  Tk " + formatter.format(Integer.valueOf(model.getTotalAmount())));//formatter.format(Integer.valueOf(model.getTotalAmount()))
                holder.userDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());

                holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "Sent Shipped Notification",
                                        "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped this order products ?");
                        builder.setCancelable(false);

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0)
                                {
                                    String uID = getRef(position).getKey();
                                    RemoverOrder(uID);
                                }else if(i==1){
                                    String uID = getRef(position).getKey();
                                    SentShippedNotification(uID);
                                }
                                else
                                {
                                    dialogInterface.cancel();
                                   // finish();
                                }
                            }
                        });
                        builder.show();

                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void SentShippedNotification(String uID) {
        ordersRef.child(uID).child("state").setValue("shipped").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminNewOrdersActivity.this, "Shipped notification is sent to the users..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void RemoverOrder(String uID)
    {
        //ordersRef.child(uID).removeValue();

        ordersRef.child(uID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("Admin View")
                            .child(uID)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(AdminNewOrdersActivity.this, "order has been deleted successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }


}