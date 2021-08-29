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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class DeliveryLoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button delivererLoginBtn;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login);

        mAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);

        emailInput=findViewById(R.id.deliverer_login_email);
        passwordInput=findViewById(R.id.deliverer_login_password);
        delivererLoginBtn=findViewById(R.id.deliverer_login_btn);

        delivererLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDeliverer();
            }
        });

    }

    private void loginDeliverer() {
        String email=emailInput.getText().toString();
        String password=passwordInput.getText().toString();

        if(!email.equals("") && !password.equals("")){
            loadingBar.setTitle("Deliverer Account Login");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        final DatabaseReference deliverersRef = FirebaseDatabase.getInstance().getReference().child("Deliverers");
                        deliverersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                                    loadingBar.dismiss();
                                    Toast.makeText(DeliveryLoginActivity.this, "You are logged in successfully.", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(DeliveryLoginActivity.this, DeliveryHomeActivity.class);
                                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    loadingBar.dismiss();
                                    Toast.makeText(DeliveryLoginActivity.this, "This account is not exist", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    }else {
                        loadingBar.dismiss();
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(DeliveryLoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(this, "Please complete the login form.", Toast.LENGTH_SHORT).show();
        }
    }
}