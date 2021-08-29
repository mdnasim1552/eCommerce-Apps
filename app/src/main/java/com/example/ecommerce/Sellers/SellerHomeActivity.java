package com.example.ecommerce.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

import com.example.ecommerce.Admin.AdminAddNewProductFragment;
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

public class SellerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);


        toolbar = findViewById(R.id.toolbar_seller);
        navigationView = findViewById(R.id.seller_side_navigation);
        drawerLayout = findViewById(R.id.seller_drawer);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sellers Home");

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        name = hView.findViewById(R.id.name_of_the_sellers);
        email = hView.findViewById(R.id.email_of_the_sellers);

        FirebaseDatabase.getInstance().getReference().child("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
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

        loadFragments(new SellerHomeFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_product_menu_for_seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_add_product){
            getSupportActionBar().setTitle("Add Product");
            loadFragments(new AdminAddNewProductFragment("seller"));
        }
        return super.onOptionsItemSelected(item);
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
            case R.id.navigation_home_seller:
//                startActivity(getIntent());
//                overridePendingTransition(0, 0);
//                finish();
                getSupportActionBar().setTitle("Sellers Home");
                loadFragments(new SellerHomeFragment());
                break;
            case R.id.navigation_logout_seller:
                final FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent_logout=new Intent(SellerHomeActivity.this, MainActivity.class);
                intent_logout.addFlags(intent_logout.FLAG_ACTIVITY_NEW_TASK | intent_logout.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.add_product_seller:
                getSupportActionBar().setTitle("Add Product");
                loadFragments(new AdminAddNewProductFragment("seller"));

                //overridePendingTransition(0, 0);
                break;
            case R.id.wallet_seller:
                getSupportActionBar().setTitle("Seller Balance");
                loadFragments(new SellerBalanceFragment(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                break;
            case R.id.cash_out_seller:
                cashOutRequest();
                break;
        }
        return true;
    }

    private void cashOutRequest() {
        FirebaseDatabase.getInstance().getReference().child("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int balance=Integer.valueOf(snapshot.child("balance").getValue().toString());
                            if(balance>=500){
                                requestSuccess(String.valueOf(balance));
                                FirebaseDatabase.getInstance().getReference().child("Sellers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue("0");

                            }else {
                                Toast.makeText(SellerHomeActivity.this, "Your money is less than 500", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void requestSuccess(String balance) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("CashOutRequestSeller");

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
        fragmentTransaction.replace(R.id.seller_universal_frameLayout,fragment);
        fragmentTransaction.commit();
    }
}