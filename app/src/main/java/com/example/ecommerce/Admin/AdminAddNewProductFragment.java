package com.example.ecommerce.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.ecommerce.R;

public class AdminAddNewProductFragment extends Fragment implements View.OnClickListener{

    private ImageView tShirts, sportsTShirts, femaleDresses, sweathers;
    private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
    private ImageView headPhonesHandFree, Laptops, watches, mobilePhones;
    private String type = "";

    public AdminAddNewProductFragment(String t) {
        // Required empty public constructor
        type=t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_add_new_product, container, false);
        tShirts = (ImageView) v.findViewById(R.id.t_shirts);
        sportsTShirts = (ImageView) v.findViewById(R.id.sports_t_shirts);
        femaleDresses = (ImageView) v.findViewById(R.id.female_dresses);
        sweathers = (ImageView) v.findViewById(R.id.sweathers);

        glasses = (ImageView) v.findViewById(R.id.glasses);
        hatsCaps = (ImageView) v.findViewById(R.id.hats_caps);
        walletsBagsPurses = (ImageView) v.findViewById(R.id.purses_bags_wallets);
        shoes = (ImageView) v.findViewById(R.id.shoes);

        headPhonesHandFree = (ImageView) v.findViewById(R.id.headphones_handfree);
        Laptops = (ImageView) v.findViewById(R.id.laptop_pc);
        watches = (ImageView) v.findViewById(R.id.watches);
        mobilePhones = (ImageView) v.findViewById(R.id.mobilephones);

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

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), AdminAddNewProductActivity.class);
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
            default:
                break;
        }
    }
}