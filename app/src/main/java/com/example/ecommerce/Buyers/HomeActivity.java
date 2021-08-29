package com.example.ecommerce.Buyers;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Menu NotificationMenu;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setElevation(0);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        if (bundle != null)
//        {
//            type = getIntent().getExtras().get("Admin").toString();
//            Menu menu =navigationView.getMenu();
//            menu.findItem(R.id.nav_cart).setVisible(false);
//        }
        navigationView.setNavigationItemSelectedListener(this);

//        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
//        Menu menu =navigationView.getMenu();
//        MenuItem nav_dashboard = menu.findItem(R.id.nav_dashboard);
//        nav_dashboard.setVisible(true);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);
        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        loadFragments(new BuyersHomeFragment(""));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String securitycode=dataSnapshot.child("securitycode").getValue().toString();
                    if(!securitycode.equals("")){
                        if (NotificationMenu != null) {
                            MenuItem item = NotificationMenu.findItem(R.id.action_notification);
                            if (item != null) {
                                item.setIcon(R.drawable.notifications_change_icon);
                            }
                        }
                    }
                }else{
                    if (NotificationMenu != null) {
                        MenuItem item = NotificationMenu.findItem(R.id.action_notification);
                        if (item != null) {
                            item.setIcon(R.drawable.notification_action_start);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        NotificationMenu=menu;
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id==R.id.action_notification){
            item.getIcon().mutate().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        if(id==R.id.nav_home){
            toolbar.setTitle("Home");
            loadFragments(new BuyersHomeFragment(""));
        }
        if (id == R.id.nav_cart)
        {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_search)
        {
            Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_trackyourproduct)
        {
            Intent intent = new Intent(HomeActivity.this, TrackOrderedProductsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_categories)
        {
            AppBarLayout appBarLayout=findViewById(R.id.appbar_buyers);
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(R.id.nav_categories, "elevation", 0));
            appBarLayout.setStateListAnimator(stateListAnimator);

            toolbar.setTitle("Categories");
            loadFragments(new CategoryFragment("user"));
        }
        else if (id == R.id.nav_settings)
        {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return true;
    }
    public void loadFragments(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.buyers_universal_frameLayout,fragment);
        fragmentTransaction.commit();
    }

}