package com.example.ecommerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.AdminCategoryActivity;
import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.SellerProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class SellerHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference ProductsRef;
    private BottomNavigationView navView;
    private DecimalFormat formatter;
    private StorageReference photoRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        formatter = new DecimalFormat("#,###");
        recyclerView = findViewById(R.id.seller_home_recyclerView);
        recyclerView.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.navigation_home:
                return true;
            case R.id.navigation_add:
                Toast.makeText(this, "Clicked navigation_add", Toast.LENGTH_SHORT).show();
                Intent intent_add=new Intent(SellerHomeActivity.this, AdminCategoryActivity.class);
                intent_add.putExtra("seller","seller");
                startActivity(intent_add);
                return true;
            case R.id.navigation_logout:
                Toast.makeText(this, "Clicked navigation_notifications", Toast.LENGTH_SHORT).show();

                final FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent_logout=new Intent(SellerHomeActivity.this, MainActivity.class);
                intent_logout.addFlags(intent_logout.FLAG_ACTIVITY_NEW_TASK | intent_logout.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_logout);
                finish();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, SellerProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, SellerProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + formatter.format(Integer.valueOf(model.getPrice())) + " Tk");
                        holder.txtProductStatus.setText(model.getProductState());
                        if(model.getProductState().equals("Approved")){
                            holder.txtProductStatus.setTextColor(Color.parseColor("#1C8051"));
                        }
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to delete this products ?");
                                builder.setCancelable(false);

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if (i == 0)
                                        {
                                            String productID = model.getPid();
                                            //RemoverOrder(uID);
                                            deleteProduct(productID,model.getImage());
                                        }
                                        else
                                        {
                                            dialogInterface.cancel();
                                            // finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_product_items_layout, parent, false);
                        SellerProductViewHolder holder = new SellerProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private void deleteProduct(String productId, String productImgId)
    {
        photoRef= FirebaseStorage.getInstance().getReferenceFromUrl(productImgId);

        ProductsRef.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SellerHomeActivity.this, "The Product Is deleted successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}