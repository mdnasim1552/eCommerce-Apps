package com.example.ecommerce.Delivery;

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

public class DeliveryMansProfileActivity extends AppCompatActivity {

    private String DeliveryMansUserId;
    private TextView name,address,phone,email,currentBalance,totalEarning,numberOfDelivery,currentPick;
    private CircleImageView circleImageView;
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_mans_profile);

        Bundle bundle = getIntent().getExtras();

        //totalAmount = getIntent().getStringExtra("Total Price");
        DeliveryMansUserId = bundle .getString("DeliveryMansUserId");
        circleImageView=findViewById(R.id.profile_image_deliverymans_);
        name=findViewById(R.id.profile_name_deliverymans_);
        address=findViewById(R.id.address_deliverymans_);
        numberOfDelivery=findViewById(R.id.delivery_number_of_deliverymans_);
        currentPick=findViewById(R.id.current_pick_of_deliverymans_);
        email=findViewById(R.id.mail_of_deliverymans_);
        phone=findViewById(R.id.phone_of_the_deliverymans_);
        currentBalance=findViewById(R.id.current_balance_deliverymans_);
        totalEarning=findViewById(R.id.total_earning_deliverymans_);
        backBtn=findViewById(R.id.back_btn_deliverymans_);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Deliverers").child(DeliveryMansUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    name.setText(snapshot.child("name").getValue().toString());
                    address.setText(snapshot.child("address").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    phone.setText(snapshot.child("phone").getValue().toString());
                    currentBalance.setText(snapshot.child("balance").getValue().toString()+" Tk");
                    totalEarning.setText(snapshot.child("totalearning").getValue().toString()+" Tk");
                    numberOfDelivery.setText(snapshot.child("totaldelivery").getValue().toString());
                    currentPick.setText(snapshot.child("currentpick").getValue().toString());
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