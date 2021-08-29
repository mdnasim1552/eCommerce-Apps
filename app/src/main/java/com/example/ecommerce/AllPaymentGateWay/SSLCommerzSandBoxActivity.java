package com.example.ecommerce.AllPaymentGateWay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Buyers.HomeActivity;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.PackingListRow;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class SSLCommerzSandBoxActivity extends AppCompatActivity implements SSLCTransactionResponseListener {

    private TextView giveMessageToTheUser,failedMessage,merchantValidationErrorMesage;
    private String name, phone, address, city,totalAmount;
    private String saveCurrentDate, saveCurrentTime,trnsID;
    private int securityCode,invoiceNumber;
    private ProgressBar progressBar;
    private ArrayList<String> productDetails ;
    private ArrayList<String> productSIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sslcommerz_sand_box);


        FirebaseDatabase.getInstance().getReference().child("Admins").child("01760091552").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    invoiceNumber=Integer.valueOf(snapshot.child("invoice").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        Bundle extras = getIntent().getExtras();
        totalAmount = extras.getString("totalAmount");
        name = extras.getString("name");
        phone = extras.getString("phone");
        address = extras.getString("address");
        city = extras.getString("city");
        productDetails = (ArrayList<String>) extras.getStringArrayList("ArrayList");
        productSIDList = (ArrayList<String>) extras.getStringArrayList("ArrayListForSellers");
        //Toast.makeText(this, productDetails.toString(), Toast.LENGTH_LONG).show();
       // Log.d(TAG,productDetails.toString());


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        Random rand=new Random();
        securityCode=rand.nextInt(90000000)+10000000;
        trnsID =String.valueOf(securityCode)+" "+ saveCurrentDate+" " + saveCurrentTime;


        giveMessageToTheUser =findViewById(R.id.give_message_to_the_user);
        failedMessage=findViewById(R.id.failed_messege);
        merchantValidationErrorMesage=findViewById(R.id.merchantValidationError_message);
        progressBar=findViewById(R.id.progressbar_id);

        final SSLCommerzInitialization sslCommerzInitialization = new SSLCommerzInitialization ("ecomm610054f3c7a27","ecomm610054f3c7a27@ssl", Integer.valueOf(totalAmount),
                SSLCCurrencyType.BDT,trnsID, "yourProductType", SSLCSdkType.TESTBOX);

        final SSLCCustomerInfoInitializer customerInfoInitializer = new SSLCCustomerInfoInitializer(name, "customer email",
                address, city, "1214", "Bangladesh", phone);



        IntegrateSSLCommerz
                .getInstance(SSLCommerzSandBoxActivity.this)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .addCustomerInfoInitializer(customerInfoInitializer)
                .buildApiCall(this);





    }

    @Override
    public void transactionSuccess(SSLCTransactionInfoModel sslcTransactionInfoModel) {
        giveMessageToTheUser.setVisibility(View.VISIBLE);
        giveMessageToTheUser.setText("Your transaction is successful. Please check your notification and collect your security code.");
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                FiveSecondProgresBar();
                ConfirmOrder();
            }
        });
        thread.start();
    }

    @Override
    public void transactionFail(String s) {
        giveMessageToTheUser.setVisibility(View.VISIBLE);
        failedMessage.setText(s);
    }

    @Override
    public void merchantValidationError(String s) {
        giveMessageToTheUser.setVisibility(View.VISIBLE);
        merchantValidationErrorMesage.setText(s);
    }


    private void ConfirmOrder()
    {
        invoiceNumber=invoiceNumber+1;

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", name);
        ordersMap.put("phone", phone);
        ordersMap.put("address", address);
        ordersMap.put("city", city);
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");
        ordersMap.put("delivered", "not delivered");
        ordersMap.put("uid", Prevalent.currentOnlineUser.getPhone());
        ordersMap.put("deliveryMansUserId", "anyone");
        ordersMap.put("securitycode", String.valueOf(securityCode));
        ordersMap.put("invoiceFileName", "");
        ordersMap.put("invoiceFileUrl", "");
        ordersMap.put("invoiceNumber", String.valueOf(invoiceNumber));

        for (int i = 0; i < productDetails.size(); i++) {
            ordersMap.put("productDetails"+String.valueOf(i), productDetails.get(i));
        }
        ordersMap.put("numberOfProducts", productDetails.size());


        ArrayList<PackingListRow> listSID=new ArrayList<>();
        for (int i = 0; i < productSIDList.size(); i++) {
            String pSID=productSIDList.get(i);
            String[] arr = pSID.split("\\s+");
            listSID.add(new PackingListRow(arr[0],Integer.valueOf(arr[1]),arr[2]));
        }
        ArrayList<PackingListRow> mergedList = new ArrayList<>();
        for (PackingListRow p : listSID) {
            int index = mergedList.indexOf(p);
            if (index != -1) {
                mergedList.set(index, mergedList.get(index).merge(p));
            } else {
                mergedList.add(p);
            }
        }
        for (int i = 0; i < mergedList.size(); i++) {
            ordersMap.put("seller"+String.valueOf(i), mergedList.get(i).toString());
        }
        ordersMap.put("numberOfSellers", mergedList.size());



        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Admins").child("01760091552").child("invoice")
                                                .setValue(String.valueOf(invoiceNumber)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(SSLCommerzSandBoxActivity.this, invoiceNumber+" is incremented as an invoice number", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        Toast.makeText(SSLCommerzSandBoxActivity.this, "your final order has been placed successfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SSLCommerzSandBoxActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }



    private void FiveSecondProgresBar() {
        for (int progress = 20; progress<=100; progress=progress+20) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}