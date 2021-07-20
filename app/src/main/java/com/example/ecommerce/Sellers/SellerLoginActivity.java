package com.example.ecommerce.Sellers;

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

import org.jetbrains.annotations.NotNull;

public class SellerLoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button sellerLoginBtn;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        setContentView(R.layout.activity_seller_login);

        mAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);

        emailInput=findViewById(R.id.seller_login_email);
        passwordInput=findViewById(R.id.seller_login_password);
        sellerLoginBtn=findViewById(R.id.seller_login_btn);

        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
    }

    private void loginSeller() {
        String email=emailInput.getText().toString();
        String password=passwordInput.getText().toString();

        if(!email.equals("") && !password.equals("")){
            loadingBar.setTitle("Seller Account Login");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        loadingBar.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "You are logged in successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        loadingBar.dismiss();
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(SellerLoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(this, "Please complete the login form.", Toast.LENGTH_SHORT).show();
        }
    }
}