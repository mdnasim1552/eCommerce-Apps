package com.example.ecommerce.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.AdminViewPDFActivity;
import com.example.ecommerce.Model.Balance;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BalanceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class SellerBalanceFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView currentBalancetxtView,totalEarningtxtView;
    private DecimalFormat formatter;
    private String identity;


    public SellerBalanceFragment(String identity) {
        this.identity=identity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_seller_balance, container, false);

        formatter = new DecimalFormat("#,###");
        currentBalancetxtView=v.findViewById(R.id.current_balance);
        totalEarningtxtView=v.findViewById(R.id.total_earning);

        if(!identity.equals("Admin")){
            FirebaseDatabase.getInstance().getReference().child("Sellers")
                    .child(identity)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                currentBalancetxtView.setText("Current Balance by selling product : "+formatter.format(Integer.valueOf(dataSnapshot.child("balance").getValue().toString()))+" Tk");
                                totalEarningtxtView.setText("Total Earning by selling product : "+formatter.format(Integer.valueOf(dataSnapshot.child("totalearning").getValue().toString()))+" Tk");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }else {
            FirebaseDatabase.getInstance().getReference().child("Admins")
                    .child("01760091552")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                currentBalancetxtView.setText("Current Balance by selling product : "+formatter.format(Integer.valueOf(dataSnapshot.child("balance").getValue().toString()))+" Tk");
                                totalEarningtxtView.setText("Total Earning by selling product : "+formatter.format(Integer.valueOf(dataSnapshot.child("totalearning").getValue().toString()))+" Tk");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }



        recyclerView=v.findViewById(R.id.recycler_balance);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("BalanceHistoryForSellerAndAdmin");

        FirebaseRecyclerOptions<Balance> options =
                new FirebaseRecyclerOptions.Builder<Balance>()
                        .setQuery(reference.orderByChild("sid").equalTo(identity), Balance.class)
                        .build();

        FirebaseRecyclerAdapter<Balance, BalanceViewHolder> adapter =
                new FirebaseRecyclerAdapter<Balance, BalanceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BalanceViewHolder holder, int position, @NonNull final Balance model)
                    {
                        String products=model.getProducts().replace("@",", ");
                        products=products.replace("#"," ");

                        holder.dateB.setText(model.getDate());
                        holder.amountB.setText(formatter.format(Integer.valueOf(model.getSellingAmount()))+"Tk");
                        holder.invoiceB.setText("#"+model.getInvoiceNumber());
                        holder.productsB.setText(products.toLowerCase());
                        if(identity.equals("Admin")){
                            holder.invoiceB.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(v.getContext(), holder.invoiceB.getText(), Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance().getReference().child("Invoices").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds: snapshot.getChildren()) {
                                                String filename=ds.child("filename").getValue().toString();
                                                String fileurl=ds.child("fileurl").getValue().toString();
                                                if(filename.equals(model.getInvoiceNumber()+".pdf")){
                                                    Intent intent=new Intent(getActivity(), AdminViewPDFActivity.class);
                                                    intent.putExtra("filename",filename);
                                                    intent.putExtra("fileurl",fileurl);
                                                    startActivity(intent);
                                                    return;
//                                                    Fragment fragment = new AdminPDFViewFragment(filename,fileurl);
//                                                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
//                                                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                                                    fragmentTransaction.replace(R.id.admin_universal_frameLayout,fragment);
//                                                    fragmentTransaction.addToBackStack(null);
//                                                    fragmentTransaction.commit();
//                                                    return;

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
                    }

                    @NonNull
                    @Override
                    public BalanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_history_layout_for_recylerview, parent, false);
                        BalanceViewHolder holder = new BalanceViewHolder(view);
                        return holder;
                    }
                };



        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}