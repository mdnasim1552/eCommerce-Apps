package com.example.ecommerce.Admin;

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

import com.example.ecommerce.Delivery.DeliveryMansProfileActivity;
import com.example.ecommerce.Model.CashOutRequest;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CashOutViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class CashOutFragment extends Fragment {

    private DatabaseReference cashoutRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DecimalFormat formatter;
    private String CashOutRequest,type;

    public CashOutFragment() {

    }
    public CashOutFragment(String CashOutRequest,String type) {
        this.CashOutRequest=CashOutRequest;
        this.type=type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_cash_out, container, false);

        cashoutRef= FirebaseDatabase.getInstance().getReference().child(CashOutRequest);
        formatter = new DecimalFormat("#,###");
        recyclerView=v.findViewById(R.id.cash_out_list_fragment);
        layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<CashOutRequest> options =
                new FirebaseRecyclerOptions.Builder<CashOutRequest>()
                        .setQuery(cashoutRef.orderByChild("transactionid").equalTo(""), CashOutRequest.class)
                        .build();
        FirebaseRecyclerAdapter<CashOutRequest, CashOutViewHolder> adapter=new FirebaseRecyclerAdapter<CashOutRequest, CashOutViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull CashOutViewHolder holder, int position, @NonNull @NotNull CashOutRequest model) {
                holder.amountCashOut.setText("Amount: "+formatter.format(Integer.valueOf(model.getAmount()))+"Tk");//formatter.format(Integer.valueOf(model.getAmount()))
                holder.dateTimeCashOut.setText("D/T: "+model.getDate());
                holder.seeProfileCashOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("Deliverer")){
                            Intent intent=new Intent(v.getContext(), DeliveryMansProfileActivity.class);
                            intent.putExtra("DeliveryMansUserId",model.getSid());
                            startActivity(intent);
                        }else if(type.equals("Seller")){
                            Toast.makeText(v.getContext(), "Commimg soon", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                holder.submitBtnCashOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String trsnID =holder.trsnIdCashOut.getText().toString();
                        if(!trsnID.equals("")){
                            //cashoutRef.child(model.getSid()).child("transactionid").setValue(trsnID);
                            cashoutRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    for(DataSnapshot ds: snapshot.getChildren()){
                                        String key=ds.child("key").getValue().toString();
                                        if(key.equals(model.getKey())){
                                            FirebaseDatabase.getInstance().getReference().child(CashOutRequest)
                                                    .child(model.getKey()).child("transactionid").setValue(trsnID);
                                            return;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }else {
                            Toast.makeText(v.getContext(), "You don't input any transaction ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @NonNull
            @NotNull
            @Override
            public CashOutViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cash_out_items_layout, parent, false);
                CashOutViewHolder holder = new CashOutViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}