package com.example.ecommerce.Admin;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.Buyers.BuyersHomeFragment;
import com.example.ecommerce.Buyers.CategoryFragment;
import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.SellerBalanceFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView name,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Paper.init(this);

        toolbar = findViewById(R.id.toolbar_admin);
        navigationView = findViewById(R.id.admin_side_navigation);
        drawerLayout = findViewById(R.id.admin_drawer);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Home");

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        name = hView.findViewById(R.id.name_of_the_admin);
        phoneNumber = hView.findViewById(R.id.phone_number_of_the_admin);
        name.setText(Prevalent.currentOnlineUser.getName());
        phoneNumber.setText(Prevalent.currentOnlineUser.getPhone());
        loadFragments(new AdminAddNewProductFragment("admin"));
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            // Fragments back
            if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            } else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.navigation_add_new_product_admin:
                getSupportActionBar().setTitle("Admin Add New Product");
                loadFragments(new AdminAddNewProductFragment("admin"));
                break;
            case R.id.navigation_approve_new_product_admin:
                getSupportActionBar().setTitle("Approve New Products");
                loadFragments(new AdminApproveNewProductFragment());
                break;
            case R.id.navigation_maintain_product_admin:
                getSupportActionBar().setTitle("Maintain Products");
                alerDialogForMaintainProduct();
                break;
            case R.id.navigation_check_new_orders_admin:
//                Intent intent_check_orders = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
//                startActivity(intent_check_orders);
                getSupportActionBar().setTitle("New Orders");
                loadFragments(new AdminNewOrdersFragment());

                break;
            case R.id.navigation_see_all_the_invoice_admin:
                Intent intent_check_invoice=new Intent(AdminHomeActivity.this,AdminCheckInvoiceActivity.class);
                startActivity(intent_check_invoice);
                break;
            case R.id.navigation_wallet_admin:
                getSupportActionBar().setTitle("Balance");
                loadFragments(new SellerBalanceFragment("Admin"));
                break;
            case R.id.navigation_deliverer_cashout_request:
                //Intent intent_deliverer_cashout=new Intent(AdminHomeActivity.this,CashOutActivity.class);
               // startActivity(intent_deliverer_cashout);
                getSupportActionBar().setTitle("Deliverer Cashout Requests");
                loadFragments(new CashOutFragment("CashOutRequestDeliverer","Deliverer"));

                break;
            case R.id.navigation_seller_cashout_request:
                getSupportActionBar().setTitle("Seller Cashout Requests");
                loadFragments(new CashOutFragment("CashOutRequestSeller","Seller"));
                break;
            case R.id.navigation_logout_admin:
                Paper.book().destroy();
                Intent intent_logout = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent_logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                finish();
                break;
        }
        return true;
    }
    public void loadFragments(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_universal_frameLayout,fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void alerDialogForMaintainProduct(){
        CharSequence options[] = new CharSequence[]
                {
                        "All the Product at a glance",
                        "Category wise all the product"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeActivity.this);
        builder.setTitle("How do you maintain all the products ?");
        //builder.setCancelable(false);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (i == 0)
                {
                    loadFragments(new BuyersHomeFragment("Admin"));
                } else
                {
                    AppBarLayout appBarLayout=findViewById(R.id.appBarLayout_admin);
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(R.id.navigation_maintain_product_admin, "elevation", 0));
                    appBarLayout.setStateListAnimator(stateListAnimator);

                    loadFragments(new CategoryFragment("Admin"));
                    //dialogInterface.cancel();
                    // finish();
                }
            }
        });
        builder.show();
    }

}