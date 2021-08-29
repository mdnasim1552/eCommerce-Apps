package com.example.ecommerce.Delivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DeliveryRegistrationActivity extends AppCompatActivity {

    private Button deliveryLoginBegin, deliveryRegisterButton;
    private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registration);

        mAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);

        deliveryLoginBegin =findViewById(R.id.delivery_mans_already_have_account_btn);
        deliveryRegisterButton=findViewById(R.id.delivery_mans_register_btn);

        nameInput=findViewById(R.id.delivery_mans_name);
        phoneInput=findViewById(R.id.delivery_mans_phone);
        emailInput=findViewById(R.id.delivery_mans_email);
        passwordInput=findViewById(R.id.delivery_mans_password);
        addressInput=findViewById(R.id.delivery_mans_address);


        deliveryLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DeliveryRegistrationActivity.this, DeliveryLoginActivity.class);
                startActivity(intent);
            }
        });

        deliveryRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDeliverer();
            }
        });

    }

    private void registerDeliverer() {

        String name, phone, email, password, address;
        name=nameInput.getText().toString();
        phone=phoneInput.getText().toString();
        email=emailInput.getText().toString();
        password=passwordInput.getText().toString();
        address=addressInput.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("")){

            loadingBar.setTitle("Creating Deliverer Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        final DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();
                        String sid=mAuth.getCurrentUser().getUid();
                        HashMap<String,Object> sellerMap=new HashMap<>();
                        sellerMap.put("sid",sid);
                        sellerMap.put("name",name);
                        sellerMap.put("phone",phone);
                        sellerMap.put("email",email);
                        sellerMap.put("password",password);
                        sellerMap.put("address",address);
                        sellerMap.put("balance","0");
                        sellerMap.put("totalearning","0");
                        sellerMap.put("totaldelivery","0");
                        sellerMap.put("currentpick","0");

                        rootRef.child("Deliverers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    loadingBar.dismiss();
                                    Toast.makeText(DeliveryRegistrationActivity.this, "You are Resistered successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(DeliveryRegistrationActivity.this, DeliveryHomeActivity.class);
                                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }else {
                        loadingBar.dismiss();
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(DeliveryRegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Please complete the registration form.", Toast.LENGTH_SHORT).show();
        }

    }
}