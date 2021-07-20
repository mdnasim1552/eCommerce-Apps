package com.example.ecommerce.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pagetitle, titleQuestions;
    private EditText phoneNumber,question1,question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        pagetitle=findViewById(R.id.page_title);
        titleQuestions=findViewById(R.id.title_questions);
        phoneNumber=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question_1);
        question2=findViewById(R.id.question_2);
        verifyButton=findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        if (check.equals("settings"))
        {
            pagetitle.setText("Set Questions");
            titleQuestions.setText("Please set Answers of the Following Security Questions?");
            phoneNumber.setVisibility(View.GONE);
            verifyButton.setText("Set");
            displayPreviousAnswer();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();
                }
            });

        }
        else if (check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });


        }
    }

    private void verifyUser() {
        String phone=phoneNumber.getText().toString();
        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals("")){

            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.hasChild("Security Questions")){
                            String ans1=dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2=dataSnapshot.child("Security Questions").child("answer2").getValue().toString();
                            if(!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this, "Your 1st answer is wrong", Toast.LENGTH_SHORT).show();
                            }else if(!ans2.equals(answer2)){
                                Toast.makeText(ResetPasswordActivity.this, "Your 2nd answer is wrong", Toast.LENGTH_SHORT).show();
                            }else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                builder.setCancelable(false);

                                EditText newPassword=new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write password here...");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPassword.getText().toString().equals("")){
                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                                Intent intent=new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).create();
                                builder.show();
                            }
                        }else {
                            Toast.makeText(ResetPasswordActivity.this, "You haven't set security questions answer", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ResetPasswordActivity.this, "This phone number doesn't exist.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }else{
            Toast.makeText(this, "Please complete the whole form", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAnswers() {
        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();
        if(answer1.equals("") && answer2.equals("")){
            Toast.makeText(ResetPasswordActivity.this, "Please answer both questions.", Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> userdataMap=new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);
            ref.child("Security Questions").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, "You have set security questions successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }
    private void displayPreviousAnswer() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.currentOnlineUser.getPhone());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ans1=dataSnapshot.child("answer1").getValue().toString();
                    String ans2=dataSnapshot.child("answer2").getValue().toString();
                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}