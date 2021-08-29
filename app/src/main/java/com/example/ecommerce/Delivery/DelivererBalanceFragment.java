package com.example.ecommerce.Delivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.DelivererBalance;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.DelivererBalanceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;


public class DelivererBalanceFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView currentBalancetxtView;
    private DecimalFormat formatter;

    public DelivererBalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_deliverer_balance, container, false);
        formatter = new DecimalFormat("#,###");
        currentBalancetxtView=v.findViewById(R.id.current_balance_deliverer);
        FirebaseDatabase.getInstance().getReference().child("Deliverers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            currentBalancetxtView.setText("Current Balance : "+formatter.format(Integer.valueOf(dataSnapshot.child("balance").getValue().toString()))+" Tk");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


        recyclerView=v.findViewById(R.id.recycler_balance_deliverer);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("BalanceHistoryForDeliverer");

        FirebaseRecyclerOptions<DelivererBalance> options =
                new FirebaseRecyclerOptions.Builder<DelivererBalance>()
                        .setQuery(reference.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), DelivererBalance.class)
                        .build();

        FirebaseRecyclerAdapter<DelivererBalance, DelivererBalanceViewHolder> adapter =
                new FirebaseRecyclerAdapter<DelivererBalance, DelivererBalanceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull DelivererBalanceViewHolder holder, int position, @NonNull final DelivererBalance model)
                    {
                        holder.date_Deliverer.setText(model.getDate());
                        holder.amount_Deliverer.setText(formatter.format(Integer.valueOf(model.getDeliveryAmount()))+"Tk");
                        holder.invoice_Deliverer.setText("#"+model.getInvoiceNumber());
                    }

                    @NonNull
                    @Override
                    public DelivererBalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_history_layout_for_deliverer, parent, false);
                        DelivererBalanceViewHolder holder = new DelivererBalanceViewHolder(view);
                        return holder;
                    }
                };



        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}