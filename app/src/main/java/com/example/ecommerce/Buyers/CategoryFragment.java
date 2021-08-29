package com.example.ecommerce.Buyers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ecommerce.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;


public class CategoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    private String type="";

    public CategoryFragment() {
    }

    public CategoryFragment(String t) {
        // Required empty public constructor
        type=t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_category, container, false);

        tabLayout=v.findViewById(R.id.fragment_tab_layout_id);
        viewPager=v.findViewById(R.id.fragment_view_pager_id);
        myPagerAdapter=new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return v;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        String[] txt={"T-Shirt","Laptops","Sports-Tshirt","Sweaters","Glasses","Hats Caps","Wallets Bags Purses","Shoes","HeadPhones HandFree","Watches","Mobile Phones","Female Dresses"};

        public MyPagerAdapter(@NonNull @NotNull FragmentManager fm) {
            super(fm);
        }


        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0: return new BoxFragment("tShirts",type);
                case 1: return new BoxFragment("Laptops",type);
                case 2: return new BoxFragment("Sports tShirts",type);
                case 3: return new BoxFragment("Sweathers",type);
                case 4: return new BoxFragment("Glasses",type);
                case 5: return new BoxFragment("Hats Caps",type);
                case 6: return new BoxFragment("Wallets Bags Purses",type);
                case 7: return new BoxFragment("Shoes",type);
                case 8: return new BoxFragment("HeadPhones HandFree",type);
                case 9: return new BoxFragment("Watches",type);
                case 10: return new BoxFragment("Mobile Phones",type);
                case 11: return new BoxFragment("Female Dresses",type);
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return txt.length;
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return txt[position];
        }
    }
}