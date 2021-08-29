package com.example.ecommerce.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.AdminMaintainProductsActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BuyersHomeFragment extends Fragment {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DecimalFormat formatter;
    private String type = "";

    public BuyersHomeFragment() {
        // Required empty public constructor
    }

    public BuyersHomeFragment(String t) {
        // Required empty public constructor
        type=t;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_buyers_home, container, false);


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        formatter = new DecimalFormat("#,###");
        recyclerView = v.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        layoutManager = new GridLayoutManager(v.getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("productState").equalTo("Approved"), Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        ArrayList<String> arrayList=CapitalizedEvery1stLetterOfEveryWord(model.getPname());
                        String PnameString="";
                        for (int i = 0; i < arrayList.size(); i++) {
                            if(i!=(arrayList.size()-1)){
                                PnameString=PnameString+arrayList.get(i)+" ";
                            }else {
                                PnameString=PnameString+arrayList.get(i);
                            }
                        }

                        holder.txtProductName.setText(PnameString);
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + formatter.format(Integer.valueOf(model.getPrice())) + " Tk");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(v.getContext(), AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private ArrayList<String> CapitalizedEvery1stLetterOfEveryWord(String pname) {
        ArrayList<String> arr=new ArrayList<>();
        arr.clear();
        pname = pname.toLowerCase();
        String[] string_array_ = pname.trim().split("\\s+");

        for (int i = 0; i < string_array_.length; i++) {
            String splited_word = string_array_[i];
            char first_letter = Character.toUpperCase(splited_word.charAt(0));
            StringBuffer buffer_splited_word = new StringBuffer(splited_word);
            buffer_splited_word.setCharAt(0, first_letter);
            arr.add(buffer_splited_word.toString());
        }
        return arr;
    }

}