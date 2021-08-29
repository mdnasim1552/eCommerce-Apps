package com.example.ecommerce.Admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminApproveNewProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;
    private BottomNavigationView navView;
    private DecimalFormat formatter;
    private StorageReference photoRef;

    public AdminApproveNewProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_approve_new_product, container, false);
        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        formatter = new DecimalFormat("#,###");
        recyclerView = v.findViewById(R.id.admin_approve_new_product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(v.getContext());
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        //layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProductsRef.orderByChild("productState").equalTo("Not Approved"), Products.class)
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
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "Delete",
                                                "No"
                                        };

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("Do you want to Approve this products ?");
                                builder.setCancelable(false);

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        String productID = model.getPid();
                                        if (i == 0)
                                        {
                                            ChangeProductState(productID);
                                            // String productID = model.getPid();
                                            //RemoverOrder(uID);
                                            // deleteProduct(productID,model.getImage());
                                        } else if(i==1){
                                            deleteProduct(productID,model.getImage());
                                        } else
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

    private void deleteProduct(String productID, String image) {
        photoRef= FirebaseStorage.getInstance().getReferenceFromUrl(image);
        unverifiedProductsRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "The Product Is deleted successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void ChangeProductState(String productID) {
        unverifiedProductsRef.child(productID)
                .child("productState")
                .setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "That item has been approved and is available for sale from the seller...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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