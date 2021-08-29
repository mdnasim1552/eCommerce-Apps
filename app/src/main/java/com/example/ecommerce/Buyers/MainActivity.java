package com.example.ecommerce.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.Delivery.DeliveryHomeActivity;
import com.example.ecommerce.Delivery.DeliveryRegistrationActivity;
import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.SellerHomeActivity;
import com.example.ecommerce.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button  joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private TextView sellerbegin,deliveryBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = findViewById(R.id.main_join_now_btn);
        loginButton =  findViewById(R.id.main_login_btn);
        sellerbegin = findViewById(R.id.seller_begin);
        deliveryBegin = findViewById(R.id.delivery_begin);

        loadingBar = new ProgressDialog(this);


        Paper.init(this);

        sellerbegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
        deliveryBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DeliveryRegistrationActivity.class);
                startActivity(intent);
            }
        });




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        String UserParentDbKey = Paper.book().read(Prevalent.UserParentDbKey);

        if (UserPhoneKey != "" && UserPasswordKey != "" && UserParentDbKey !="")
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey, UserPasswordKey, UserParentDbKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this, firebaseUser+"", Toast.LENGTH_SHORT).show();
        if(firebaseUser!=null){
            ProgressDialog loadingBarDemo;
            loadingBarDemo=new ProgressDialog(this);
            loadingBarDemo.setTitle("Account Login");
            loadingBarDemo.setMessage("Please wait, while we are checking the credentials.");
            loadingBarDemo.setCanceledOnTouchOutside(false);
            loadingBarDemo.show();
            String firebase_users_uid=firebaseUser.getUid();
                //checkSellerIdentity();
               // checkDeliveryMansIdentity();

            final DatabaseReference deliverersRef = FirebaseDatabase.getInstance().getReference().child("Deliverers");
            deliverersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(firebase_users_uid).exists()){
                        loadingBarDemo.dismiss();
                        Toast.makeText(MainActivity.this, "You are logged in successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this, DeliveryHomeActivity.class);
                        //intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();

                    }else {
                        loadingBarDemo.dismiss();
                        Toast.makeText(MainActivity.this, "You are logged in successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this, SellerHomeActivity.class);
                        //intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }

    }

    private void AllowAccess(final String phone, final String password,final String parentDbKey)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbKey).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbKey).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                            if(parentDbKey.equals("Users")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                                finish();
                            }else if(parentDbKey.equals("Admins")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                                finish();
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}