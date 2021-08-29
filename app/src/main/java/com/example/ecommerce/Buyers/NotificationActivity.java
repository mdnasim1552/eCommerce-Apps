package com.example.ecommerce.Buyers;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;

public class NotificationActivity extends AppCompatActivity {

    private TextView securityTextField;
    private WebView webView;
    private String filename,fileurl,url="";
    private DatabaseReference ordersReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        securityTextField=findViewById(R.id.Security_code_id);
        getSecurityCode();

        ordersReff = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersReff.child(Prevalent.currentOnlineUser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    filename=snapshot.child("invoiceFileName").getValue().toString();
                    fileurl=snapshot.child("invoiceFileUrl").getValue().toString();

                    if(!filename.equals("") && !fileurl.equals("")){
                        try {
                            url= URLEncoder.encode(fileurl,"UTF-8");
                        }catch (Exception ex)
                        {
                            Toast.makeText(NotificationActivity.this, "Error : "+ex, Toast.LENGTH_SHORT).show();
                        }

                        webView=findViewById(R.id.view_invoice_pdf);
                        webView.getSettings().setJavaScriptEnabled(true);

                        final ProgressDialog progressDialog=new ProgressDialog(NotificationActivity.this);
                        progressDialog.setTitle(filename);
                        progressDialog.setMessage("Opening....");

                        webView.setWebViewClient(new WebViewClient(){
                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                                progressDialog.show();
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                progressDialog.dismiss();
                                if (view.getContentHeight() == 0) view.loadUrl(url);
                            }
                        });

                        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);

                    }else {
                        Toast.makeText(NotificationActivity.this, "Be patient,,,soon you will see your invoice.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(NotificationActivity.this, "Order something!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void getSecurityCode()
    {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderRef.child(Prevalent.currentOnlineUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String securitycode=dataSnapshot.child("securitycode").getValue().toString();
                    securityTextField.setText(securitycode);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}