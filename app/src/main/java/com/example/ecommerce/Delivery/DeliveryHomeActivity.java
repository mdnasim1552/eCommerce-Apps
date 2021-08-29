package com.example.ecommerce.Delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.Model.CashOutRequest;
import com.example.ecommerce.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DeliveryHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView name,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_home);

        toolbar = findViewById(R.id.toolbar_deliverers);
        navigationView = findViewById(R.id.deliverer_side_navigation);
        drawerLayout = findViewById(R.id.deliverer_drawer);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delivery Orders List");

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        name = hView.findViewById(R.id.name_of_the_delivery_man);
        email = hView.findViewById(R.id.email_of_the_delivery_man);

        FirebaseDatabase.getInstance().getReference().child("Deliverers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            name.setText(snapshot.child("name").getValue().toString());
                            email.setText(snapshot.child("email").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        loadFragments(new DeliveryHomeFragment());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.navigation_home_deliverer:
                getSupportActionBar().setTitle("Delivery Orders List");
                loadFragments(new DeliveryHomeFragment());
                break;
            case R.id.my_order:
                Toast.makeText(DeliveryHomeActivity.this, "My order", Toast.LENGTH_SHORT).show();
//                Intent intent_my_order=new Intent(DeliveryHomeActivity.this,DeliveryMyPickedOrderActivity.class);
//                startActivity(intent_my_order);
                getSupportActionBar().setTitle("Picked Orders List");
                loadFragments(new DeliveryMyPickedOrderFragment());
                break;
            case R.id.navigation_logout_deliverer:

                final FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent_logout=new Intent(DeliveryHomeActivity.this, MainActivity.class);
                intent_logout.addFlags(intent_logout.FLAG_ACTIVITY_NEW_TASK | intent_logout.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                finish();
                break;
            case R.id.wallet:
                getSupportActionBar().setTitle("Deliverer Balance");
                loadFragments(new DelivererBalanceFragment());
                break;
            case R.id.cash_out_deliverer:
                cashOutRequest();
                break;
        }
        return true;
    }

    private void cashOutRequest() {
        FirebaseDatabase.getInstance().getReference().child("Deliverers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int balance=Integer.valueOf(snapshot.child("balance").getValue().toString());
                            if(balance>=500){
                                requestSuccess(String.valueOf(balance));
                                FirebaseDatabase.getInstance().getReference().child("Deliverers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");

                            }else {
                                Toast.makeText(DeliveryHomeActivity.this, "Your money is less than 500", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void requestSuccess(String balance) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("CashOutRequestDeliverer");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        String CurrentDateCurrentTime = saveCurrentDate +" "+ saveCurrentTime;

        String key=databaseReference.push().getKey();
        CashOutRequest obj=new CashOutRequest(CurrentDateCurrentTime,balance,FirebaseAuth.getInstance().getCurrentUser().getUid(),"",key);
        databaseReference.child(key).setValue(obj);
    }

    public void loadFragments(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.delivery_universal_frameLayout,fragment);
        fragmentTransaction.commit();
    }
}