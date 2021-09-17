package com.example.ecommerce.Delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.AdminUserProductsActivity;
import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.DeliveryOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class DeliveryHomeFragment extends Fragment {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private DecimalFormat formatter;

    public DeliveryHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_delivery_home, container, false);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        formatter = new DecimalFormat("#,###");


        ordersList = v.findViewById(R.id.delivery_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(v.getContext()));


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef.orderByChild("delivered").equalTo("not delivered"), AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, DeliveryOrdersViewHolder> adapter =new FirebaseRecyclerAdapter<AdminOrders, DeliveryOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull DeliveryOrdersViewHolder holder, int position, @NonNull @NotNull AdminOrders model) {

                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                holder.userTotalPrice.setText("Total Amount =  Tk " + formatter.format(Integer.valueOf(model.getTotalAmount())));//formatter.format(Integer.valueOf(model.getTotalAmount()))
                holder.userDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());

                FirebaseDatabase.getInstance().getReference().child("Orders").child(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("state").getValue().toString().equals("shipped")){
                                holder.itemView.setVisibility(View.GONE);
                                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                params.height = 0;
                                params.width = 0;
                                holder.itemView.setLayoutParams(params);
                            }else{
                                holder.itemView.setVisibility(View.VISIBLE);
                                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                holder.itemView.setLayoutParams(params);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


                holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        Intent intent = new Intent(v.getContext(), AdminUserProductsActivity.class);
                        //Intent intent = new Intent(v.getContext(), DelivererUserProductsActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                holder.SubmitSecurityCodeBtn.setVisibility(View.GONE);
                holder.SecurityCode.setVisibility(View.GONE);
                holder.uploadInvoiceBtn.setVisibility(View.GONE);

                holder.pickOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        pickOrderedProduct(uID, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }
                });




            }

            @NonNull
            @NotNull
            @Override
            public DeliveryOrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_orders_layout, parent, false);
                return new DeliveryOrdersViewHolder(view);
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();

    }

    private void pickOrderedProduct(String uID, String deliveryMansid) {

        FirebaseDatabase.getInstance().getReference().child("Deliverers").child(deliveryMansid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int currentPick=Integer.valueOf(snapshot.child("currentpick").getValue().toString());
                currentPick=currentPick+1;
                FirebaseDatabase.getInstance().getReference().child("Deliverers").child(deliveryMansid).child("currentpick").setValue(String.valueOf(currentPick));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        ordersRef.child(uID).child("state").setValue("shipped").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    ordersRef.child(uID).child("deliveryMansUserId").setValue(deliveryMansid).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Toast.makeText(getContext(), "Shipped notification is sent to the users..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}