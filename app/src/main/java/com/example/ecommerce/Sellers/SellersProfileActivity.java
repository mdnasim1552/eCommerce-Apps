package com.example.ecommerce.Sellers;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellersProfileActivity extends AppCompatActivity {

    private String SellersUserId;
    private TextView name,address,phone,email,currentBalance,totalEarning;
    private CircleImageView circleImageView;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers_profile);

        Bundle bundle = getIntent().getExtras();

        //totalAmount = getIntent().getStringExtra("Total Price");
        SellersUserId = bundle .getString("SellersUserId");
        circleImageView=findViewById(R.id.profile_image_sellers_);
        name=findViewById(R.id.profile_name_sellers_);
        address=findViewById(R.id.address_sellers_);
        email=findViewById(R.id.mail_of_sellers_);
        phone=findViewById(R.id.phone_of_the_sellers_);
        currentBalance=findViewById(R.id.current_balance_sellers_);
        totalEarning=findViewById(R.id.total_earning_sellers_);
        backBtn=findViewById(R.id.back_btn_sellers_);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Sellers").child(SellersUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText(snapshot.child("name").getValue().toString());
                    address.setText(snapshot.child("address").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                    currentBalance.setText(snapshot.child("balance").getValue().toString()+" Tk");
                    totalEarning.setText(snapshot.child("totalearning").getValue().toString()+" Tk");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}