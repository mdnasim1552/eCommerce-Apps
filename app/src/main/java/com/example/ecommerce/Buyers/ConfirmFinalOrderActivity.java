package com.example.ecommerce.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.AllPaymentGateWay.SSLCommerzSandBoxActivity;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;

    private String totalAmount = "";
    private DecimalFormat formatter;
    private Switch autoFillSwitch;
    private ArrayList<String> productDetails ;
    private ArrayList<String> productSIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        Bundle bundle = getIntent().getExtras();

        //totalAmount = getIntent().getStringExtra("Total Price");
        totalAmount = bundle .getString("Total Price");
        productDetails = (ArrayList<String>) bundle.getStringArrayList("ArrayList");
        productSIDList = (ArrayList<String>) bundle.getStringArrayList("ArrayListForSellers");


        formatter = new DecimalFormat("#,###");
        Toast.makeText(this, "Total Price =  Tk " + formatter.format(Integer.valueOf(totalAmount)), Toast.LENGTH_SHORT).show();//formatter.format(totalAmount)
       // Toast.makeText(this, productDetails.toString(), Toast.LENGTH_SHORT).show();

        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_name);
        phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
        addressEditText = (EditText) findViewById(R.id.shippment_address);
        cityEditText = (EditText) findViewById(R.id.shippment_city);
        autoFillSwitch=findViewById(R.id.auto_fill_final_order_form_switch);

        autoFillSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoFillSwitch.isChecked()){
                    userInfoDisplay(nameEditText, phoneEditText, addressEditText);
                }else {
                    nameEditText.setText("");
                    phoneEditText.setText("");
                    addressEditText.setText("");
                }
            }
        });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    private void userInfoDisplay(EditText nameEditText, EditText phoneEditText, EditText addressEditText) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String phone = dataSnapshot.child("phoneOrder").getValue().toString();
                    nameEditText.setText(name);
                    phoneEditText.setText(phone);
                    if (dataSnapshot.child("address").exists()){
                        String address = dataSnapshot.child("address").getValue().toString();
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void Check()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your city name.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(ConfirmFinalOrderActivity.this, SSLCommerzSandBoxActivity.class);
            intent.putExtra("totalAmount",totalAmount);
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("phone", phoneEditText.getText().toString());
            intent.putExtra("address", addressEditText.getText().toString());
            intent.putExtra("city", cityEditText.getText().toString());
            intent.putExtra("ArrayList",productDetails);
            intent.putExtra("ArrayListForSellers",productSIDList);
            startActivity(intent);

//            Intent intent = new Intent(ConfirmFinalOrderActivity.this, SSLCommerzSandBoxActivity.class);
//            Bundle extras = new Bundle();
//            extras.putString("totalAmount",totalAmount);
//            extras.putString("name", nameEditText.getText().toString());
//            extras.putString("phone", phoneEditText.getText().toString());
//            extras.putString("address", addressEditText.getText().toString());
//            extras.putString("city", cityEditText.getText().toString());
//            intent.putExtras(extras);
//            startActivity(intent);
        }
    }
}