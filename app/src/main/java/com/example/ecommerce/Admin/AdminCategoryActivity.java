package com.example.ecommerce.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.R;

public class AdminCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView tShirts, sportsTShirts, femaleDresses, sweathers;
    private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
    private ImageView headPhonesHandFree, Laptops, watches, mobilePhones;

    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn, approveProductBtn;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            type = getIntent().getExtras().get("seller").toString();
        }

        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);
        approveProductBtn = (Button) findViewById(R.id.approve_new_product_btn);

        tShirts = (ImageView) findViewById(R.id.t_shirts);
        sportsTShirts = (ImageView) findViewById(R.id.sports_t_shirts);
        femaleDresses = (ImageView) findViewById(R.id.female_dresses);
        sweathers = (ImageView) findViewById(R.id.sweathers);

        glasses = (ImageView) findViewById(R.id.glasses);
        hatsCaps = (ImageView) findViewById(R.id.hats_caps);
        walletsBagsPurses = (ImageView) findViewById(R.id.purses_bags_wallets);
        shoes = (ImageView) findViewById(R.id.shoes);

        headPhonesHandFree = (ImageView) findViewById(R.id.headphones_handfree);
        Laptops = (ImageView) findViewById(R.id.laptop_pc);
        watches = (ImageView) findViewById(R.id.watches);
        mobilePhones = (ImageView) findViewById(R.id.mobilephones);

        if(type.equals("seller")){
            maintainProductsBtn.setVisibility(View.GONE);
            CheckOrdersBtn.setVisibility(View.GONE);
            approveProductBtn.setVisibility(View.GONE);
        }


        tShirts.setOnClickListener(this);
        sportsTShirts.setOnClickListener(this);
        femaleDresses.setOnClickListener(this);
        sweathers.setOnClickListener(this);
        glasses.setOnClickListener(this);
        hatsCaps.setOnClickListener(this);
        walletsBagsPurses.setOnClickListener(this);
        shoes.setOnClickListener(this);
        headPhonesHandFree.setOnClickListener(this);
        Laptops.setOnClickListener(this);
        watches.setOnClickListener(this);
        mobilePhones.setOnClickListener(this);
        maintainProductsBtn.setOnClickListener(this);
        LogoutBtn.setOnClickListener(this);
        CheckOrdersBtn.setOnClickListener(this);
        approveProductBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
        Bundle extras = new Bundle();
        switch (v.getId()){
            case R.id.t_shirts:
                extras.putString("category","tShirts");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.sports_t_shirts:
                extras.putString("category","Sports tShirts");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.female_dresses:
                extras.putString("category","Female Dresses");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.sweathers:
                extras.putString("category","Sweathers");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.glasses:
                extras.putString("category","Glasses");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.hats_caps:
                extras.putString("category","Hats Caps");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.purses_bags_wallets:
                extras.putString("category","Wallets Bags Purses");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.shoes:
                extras.putString("category","Shoes");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.headphones_handfree:
                extras.putString("category","HeadPhones HandFree");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.laptop_pc:
                extras.putString("category","Laptops");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.watches:
                extras.putString("category","Watches");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.mobilephones:
                extras.putString("category","Mobile Phones");
                if(type.equals("seller")){
                    extras.putString("seller","yes");
                }else {
                    extras.putString("seller","not");
                }
                intent.putExtras(extras);

                startActivity(intent);
                break;
            case R.id.approve_new_product_btn:
                Intent intent_approve=new Intent(AdminCategoryActivity.this,AdminCheckNewProductActivity.class);
                startActivity(intent_approve);
                break;
            case R.id.maintain_btn:
                Intent intent_maintain = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent_maintain.putExtra("Admin", "Admin");
                startActivity(intent_maintain);
                break;
            case R.id.check_orders_btn:
                Intent intent_check_orders = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent_check_orders);
                break;
            case R.id.admin_logout_btn:
                Intent intent_logout = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent_logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                finish();
                break;
            default:
                break;
        }
    }
}