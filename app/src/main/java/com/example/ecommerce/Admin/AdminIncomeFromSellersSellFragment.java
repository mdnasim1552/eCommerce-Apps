package com.example.ecommerce.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AdminIncomeFromSellersSellFragment extends Fragment {

    private DatabaseReference sellersReference,deliverersReference, UsersReference, AdminReference;
    private TextView income,expenditure,benefit,sellingAmount,numberOfSellers,numberOfDeliveryMans,numberOfUsers;


    public AdminIncomeFromSellersSellFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_admin_income_from_sellers_sell, container, false);

        income=v.findViewById(R.id.income_from_sellers);
        expenditure=v.findViewById(R.id.expenditure_from_sellers);
        benefit=v.findViewById(R.id.benefit_from_sellers);
        sellingAmount=v.findViewById(R.id.selling_amount_from_selling_product);
        numberOfSellers=v.findViewById(R.id.number_of_sellers);
        numberOfDeliveryMans=v.findViewById(R.id.number_of_delivery_mans);
        numberOfUsers=v.findViewById(R.id.number_of_users);

        AdminReference= FirebaseDatabase.getInstance().getReference().child("Admins").child(Prevalent.currentOnlineUser.getPhone());
        sellersReference= FirebaseDatabase.getInstance().getReference().child("Sellers");
        deliverersReference= FirebaseDatabase.getInstance().getReference().child("Deliverers");
        UsersReference= FirebaseDatabase.getInstance().getReference().child("Users");
        sellersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                numberOfSellers.setText("5. Number of Sellers: "+snapshot.getChildrenCount());
                int i=0;
                //Toast.makeText(AdminIncomeFromSellersSellActivity.this, String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds: snapshot.getChildren()){
                    i=i+Integer.valueOf(ds.child("totalearning").getValue().toString());
                }
                i=(int)(i*0.1);
                income.setText("1. Income: "+String.valueOf(i));
                int finalI = i;
                deliverersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        numberOfDeliveryMans.setText("6. Number of Delivery Mans: "+snapshot.getChildrenCount());
                        //Toast.makeText(AdminIncomeFromSellersSellActivity.this, String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                        int e=0;
                        //Toast.makeText(AdminIncomeFromSellersSellActivity.this, String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            e=e+Integer.valueOf(ds.child("totalearning").getValue().toString());
                        }
                        expenditure.setText("2. Expenditure: "+String.valueOf(e));

                        benefit.setText("3. Benefit: "+String.valueOf(finalI -e));

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        UsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                numberOfUsers.setText("7. Number of Users: "+snapshot.getChildrenCount());
                //Toast.makeText(AdminIncomeFromSellersSellActivity.this, String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        AdminReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                sellingAmount.setText("4. Selling Amount: "+snapshot.child("totalearning").getValue().toString());
                //Toast.makeText(AdminIncomeFromSellersSellActivity.this, String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //benefit.setText(Integer.valueOf(income.getText().toString().replace("1. Income: ",""))-Integer.valueOf(expenditure.getText().toString().replace("2. Expenditure: ","")));





        return v;
    }
}