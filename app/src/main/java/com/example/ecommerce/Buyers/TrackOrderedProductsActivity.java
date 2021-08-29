package com.example.ecommerce.Buyers;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class TrackOrderedProductsActivity extends AppCompatActivity {

    private View view_order_placed,view_order_confirmed,view_order_processed,view_order_pickup,con_divider,ready_divider,placed_divider,view_order_delivered,ready_delivered;
    private ImageView img_orderconfirmed,orderprocessed,orderpickup,ordertrackbackbtn,img_orderplaced,orderdelivered;
    private TextView textorderpickup,text_confirmed,textorderprocessed,textorderplaced,orderInvoiceNumber,orderEstimateTime,textorderdelivered;

    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_ordered_products);


        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        view_order_placed=findViewById(R.id.view_order_placed);
        view_order_confirmed=findViewById(R.id.view_order_confirmed);
        view_order_processed=findViewById(R.id.view_order_processed);
        view_order_pickup=findViewById(R.id.view_order_pickup);
        placed_divider=findViewById(R.id.placed_divider);
        con_divider=findViewById(R.id.con_divider);
        ready_divider=findViewById(R.id.ready_divider);
        view_order_delivered=findViewById(R.id.view_order_delivered);
        ready_delivered=findViewById(R.id.ready_delivered);

        textorderplaced=findViewById(R.id.textorderplaced);
        textorderpickup=findViewById(R.id.textorderpickup);
        text_confirmed=findViewById(R.id.text_confirmed);
        textorderprocessed=findViewById(R.id.textorderprocessed);
        orderInvoiceNumber=findViewById(R.id.order_invoice_number);
        orderEstimateTime=findViewById(R.id.order_estimate_time);
        textorderdelivered=findViewById(R.id.textorderdelivered);

        img_orderplaced=findViewById(R.id.orderplaced);
        img_orderconfirmed=findViewById(R.id.img_orderconfirmed);
        orderprocessed=findViewById(R.id.orderprocessed);
        orderpickup=findViewById(R.id.orderpickup);
        ordertrackbackbtn=findViewById(R.id.order_track_backbtn);
        orderdelivered=findViewById(R.id.orderdelivered);

        ordertrackbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    orderInvoiceNumber.setText("#"+snapshot.child("invoiceNumber").getValue().toString());
                    orderInvoiceNumber.setAlpha((float)1);
                    orderEstimateTime.setAlpha((float)1);
                    orderEstimateTime.setText("72 Hours        ");
                    getOrderStatus("0");
                    if(snapshot.child("state").getValue().toString().equals("shipped")){
                        getOrderStatus("1");
                        orderEstimateTime.setText("48 Hours        ");
                    }
                    if(!snapshot.child("invoiceFileName").getValue().toString().equals("") && !snapshot.child("invoiceFileUrl").getValue().toString().equals("")){
                        getOrderStatus("2");
                        orderEstimateTime.setText("2 Hours         ");
                    }
                    if(snapshot.child("delivered").getValue().toString().equals("delivered")){
                        getOrderStatus("3");
                        orderEstimateTime.setAlpha((float)0.5);
                        orderEstimateTime.setText("0 Hour         ");
                        getOrderStatus("4");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void getOrderStatus(String orderStatus) {
        if (orderStatus.equals("0")){
            float alfa= (float) 1;
            setStatus(alfa);

        }else if (orderStatus.equals("1")){
            float alfa= (float) 1;
            setStatus(alfa);
            setStatus1(alfa);



        }else if (orderStatus.equals("2")){
            float alfa= (float) 1;
            setStatus(alfa);
            setStatus1(alfa);
            setStatus2(alfa);


        }else if (orderStatus.equals("3")){
            float alfa= (float) 1;
            setStatus(alfa);
            setStatus1(alfa);
            setStatus2(alfa);
            setStatus3(alfa);
        }else if (orderStatus.equals("4")){
            float alfa= (float) 1;
            setStatus(alfa);
            setStatus1(alfa);
            setStatus2(alfa);
            setStatus3(alfa);
            setStatus4(alfa);
        }
    }
    private void setStatus(float alfa) {
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        view_order_confirmed.setAlpha(alfa);
        placed_divider.setAlpha(alfa);
        textorderplaced.setAlpha(alfa);
        img_orderplaced.setAlpha(alfa);
    }

    private void setStatus1(float alfa) {
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        placed_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        text_confirmed.setAlpha(alfa);
        img_orderconfirmed.setAlpha(alfa);
        view_order_processed.setAlpha(alfa);
        con_divider.setAlpha(alfa);
    }

    private void setStatus2(float alfa) {
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        con_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        orderprocessed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);
        ready_divider.setAlpha(alfa);
        view_order_pickup.setAlpha(alfa);


    }

    private void setStatus3(float alfa) {
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_divider.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        textorderpickup.setAlpha(alfa);
        orderpickup.setAlpha(alfa);
        ready_delivered.setAlpha(alfa);
        view_order_delivered.setAlpha(alfa);
    }

    private void setStatus4(float alfa) {
        view_order_delivered.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        ready_delivered.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        orderdelivered.setAlpha(alfa);
        textorderdelivered.setAlpha(alfa);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}