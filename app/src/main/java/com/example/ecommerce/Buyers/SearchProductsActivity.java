package com.example.ecommerce.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import java.util.ArrayList;

public class SearchProductsActivity extends AppCompatActivity {

    private ImageButton SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput="";
    private String type = "";
    private static final int RECOGNIZER_REQUEST_CODE=200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            type = getIntent().getExtras().get("Admin").toString();
        }

        inputText = findViewById(R.id.search_product_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().length() == 0) {
//
//                }
                controlSearchMethod();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //controlSearchMethod();
                openVoiceDialog();
            }
        });
        //the enter key of the keyboard, in a way than when the user press enter, the search activity begins. The code that I have used is like below:
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    controlSearchMethod();
                    return true;
                }
                return false;
            }
        });

    }

    private void controlSearchMethod() {
        SearchInput = inputText.getText().toString();
        if(!TextUtils.isEmpty(SearchInput)){
            //char a =Character.toUpperCase(SearchInput.charAt(0));
           // StringBuffer string = new StringBuffer(SearchInput);
           // string.setCharAt(0, a);
            SearchInput=SearchInput.toUpperCase();
        }
        onStart();
    }
    private void openVoiceDialog(){
        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        try {
            startActivityForResult(intent,RECOGNIZER_REQUEST_CODE);
        }catch (Exception e){
            Toast.makeText(this, "Error "+e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RECOGNIZER_REQUEST_CODE && resultCode==RESULT_OK){
            ArrayList<String> arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            inputText.setText(arrayList.get(0));
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(reference.orderByChild("pname").startAt(SearchInput).endAt(SearchInput+"\uf8ff"), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
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
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(SearchProductsActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
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

        searchList.setAdapter(adapter);
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