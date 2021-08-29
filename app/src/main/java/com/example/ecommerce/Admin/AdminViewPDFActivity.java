package com.example.ecommerce.Admin;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;

import java.net.URLEncoder;

public class AdminViewPDFActivity extends AppCompatActivity {
    private WebView webView;
    private String filename,fileurl,url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_pdfactivity);

        filename=getIntent().getStringExtra("filename");
        fileurl=getIntent().getStringExtra("fileurl");

        try {
            url= URLEncoder.encode(fileurl,"UTF-8");
        }catch (Exception ex)
        {
            Toast.makeText(this, "Error : "+ex, Toast.LENGTH_SHORT).show();
        }

        webView=findViewById(R.id.view_pdf);
        webView.getSettings().setJavaScriptEnabled(true);

        final ProgressDialog progressDialog=new ProgressDialog(this);
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

    }
}