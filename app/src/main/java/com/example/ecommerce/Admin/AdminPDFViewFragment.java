package com.example.ecommerce.Admin;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ecommerce.R;

import java.net.URLEncoder;


public class AdminPDFViewFragment extends Fragment{

    private WebView webView;
    private String filename,fileurl,url="";


    public AdminPDFViewFragment(String filename,String fileurl) {
        this.filename=filename;
        this.fileurl=fileurl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_admin_p_d_f_view, container, false);

        try {
            url= URLEncoder.encode(fileurl,"UTF-8");
        }catch (Exception ex)
        {
            Toast.makeText(v.getContext(), "Error : "+ex, Toast.LENGTH_SHORT).show();
        }

        webView=v.findViewById(R.id.view_pdf_fragment);
        webView.getSettings().setJavaScriptEnabled(true);

        final ProgressDialog progressDialog=new ProgressDialog(v.getContext());
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

        return v;
    }
}