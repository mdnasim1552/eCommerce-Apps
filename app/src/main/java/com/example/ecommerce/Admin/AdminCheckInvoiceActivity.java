package com.example.ecommerce.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
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

import com.example.ecommerce.Model.Invoice;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.InvoiceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminCheckInvoiceActivity extends AppCompatActivity {

    private EditText inputText;
    private ImageButton SearchBtn;
    private RecyclerView searchList;
    private String SearchInput="";
    private String type = "";
    private static final int RECOGNIZER_REQUEST_CODE=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_invoice);

        inputText = findViewById(R.id.search_customers_invoice_edittext);
        SearchBtn = findViewById(R.id.search_invoice_speaker_btn);
        searchList = findViewById(R.id.search_invoice_list);
        searchList.setLayoutManager(new LinearLayoutManager(AdminCheckInvoiceActivity.this));



        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Invoices");

        FirebaseRecyclerOptions<Invoice> options =
                new FirebaseRecyclerOptions.Builder<Invoice>()
                        .setQuery(reference.orderByChild("customersUserID").startAt(SearchInput).endAt(SearchInput+"\uf8ff"), Invoice.class)
                        .build();

        FirebaseRecyclerAdapter<Invoice, InvoiceViewHolder> adapter =
                new FirebaseRecyclerAdapter<Invoice, InvoiceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position, @NonNull final Invoice model)
                    {
                        holder.customersID.setText(model.getCustomersUserID());
                        holder.deliveryMansID.setText(model.getDeliveryMansUserId());
                        holder.invoiceFileNameID.setText(model.getFilename());
                        holder.invoiceDateTimeID.setText(model.getCurrentDateCurrentTime());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(AdminCheckInvoiceActivity.this,AdminViewPDFActivity.class);
                                intent.putExtra("filename",model.getFilename());
                                intent.putExtra("fileurl",model.getFileurl());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_invoice_items_layout, parent, false);
                        InvoiceViewHolder holder = new InvoiceViewHolder(view);
                        return holder;
                    }
                };

        searchList.setAdapter(adapter);
        adapter.startListening();

    }
}