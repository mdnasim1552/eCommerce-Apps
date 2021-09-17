package com.example.ecommerce.Delivery;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Admin.AdminViewPDFActivity;
import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.Model.Balance;
import com.example.ecommerce.Model.DelivererBalance;
import com.example.ecommerce.Model.Invoice;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.DeliveryOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class DeliveryMyPickedOrderFragment extends Fragment {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private DecimalFormat formatter;

    private Bitmap bmp, scaledbmp;
    private int pageHeight = 2010;
    private int pagewidth = 1200;

    private ArrayList<String> addAllProductDetails=new ArrayList<>();
    private String uID,invoiceNumber;

    private Uri FilePathUri;
    private StorageReference pdfFileStorageRef;
    private DatabaseReference pdfDatabaseRef;
    private String downloadPDFUrl="";


    public DeliveryMyPickedOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_delivery_my_picked_order, container, false);

        pdfFileStorageRef = FirebaseStorage.getInstance().getReference().child("Customers all invoice pdf");
        pdfDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Invoices");

        ActivityCompat.requestPermissions((Activity) v.getContext(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        formatter = new DecimalFormat("#,###");


        ordersList = v.findViewById(R.id.delivery_picked_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(v.getContext()));


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef.orderByChild("deliveryMansUserId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders, DeliveryOrdersViewHolder> adapter =new FirebaseRecyclerAdapter<AdminOrders, DeliveryOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull DeliveryOrdersViewHolder holder, int position, @NonNull @NotNull AdminOrders model) {

                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                holder.userTotalPrice.setText("Total Amount =  Tk " + formatter.format(Integer.valueOf(model.getTotalAmount())));//formatter.format(Integer.valueOf(model.getTotalAmount()))
                holder.userDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());

                holder.pickOrderBtn.setVisibility(View.GONE);


                //FirebaseDatabase.getInstance().getReference().child("Invoice").ch
                ordersRef.child(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("delivered").getValue().toString().equals("delivered")){
                                holder.SubmitSecurityCodeBtn.setVisibility(View.GONE);
                                holder.SecurityCode.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


                ordersRef.child(model.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String filename=snapshot.child("invoiceFileName").getValue().toString();
                            String fileurl=snapshot.child("invoiceFileUrl").getValue().toString();
                            if(!filename.equals("") && !fileurl.equals("")){
                                holder.uploadInvoiceBtn.setVisibility(View.GONE);
                            }else {
                                holder.uploadInvoiceBtn.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uID = getRef(position).getKey();

                        CharSequence options[] = new CharSequence[]
                                {
                                        "See invoice",
                                        "See picked product address",
                                        "Nothing"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Do you want to see invoice/picked product address ?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0) {
                                    ordersRef.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                String filename=snapshot.child("invoiceFileName").getValue().toString();
                                                String fileurl=snapshot.child("invoiceFileUrl").getValue().toString();
                                                if(!filename.equals("") && !fileurl.equals("")){
                                                    Intent intent=new Intent(v.getContext(), AdminViewPDFActivity.class);
                                                    intent.putExtra("filename",filename);
                                                    intent.putExtra("fileurl",fileurl);
                                                    startActivity(intent);
                                                }else {
                                                    Toast.makeText(v.getContext(), "First upload invoice pdf file", Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(v.getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                } else if(i==1){
                                    Toast.makeText(v.getContext(), "Picked Products address", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialogInterface.cancel();
                                    // finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });


                holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uID = getRef(position).getKey();
                        //Intent intent = new Intent(v.getContext(), AdminUserProductsActivity.class);
                        Intent intent = new Intent(v.getContext(), DelivererUserProductsActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });

                holder.SubmitSecurityCodeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        invoiceNumber=model.getInvoiceNumber();
                        uID = getRef(position).getKey();
                        String security_code=holder.SecurityCode.getText().toString();
                        holder.SecurityCode.setText("");
                        matchingSecurityCodeWithUser(security_code,uID);
                    }
                });

                holder.uploadInvoiceBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        invoiceNumber=model.getInvoiceNumber();
                        uID = getRef(position).getKey();
                        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.invoice_header);
                        scaledbmp = Bitmap.createScaledBitmap(bmp, 1200, 580, false);
                        generatePDF(model.getName(),model.getPhone(),model.getTotalAmount());
                        ChooseInvoicePDF();
                        Toast.makeText(v.getContext(), "Select "+invoiceNumber+".pdf file", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @NonNull
            @NotNull
            @Override
            public DeliveryOrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_orders_layout, parent, false);
                return new DeliveryOrdersViewHolder(view);
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();

    }

    private void ChooseInvoicePDF() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent();
                        intent.setType("application/pdf");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK)
        {
            FilePathUri=data.getData();
            processupload(FilePathUri);
        }
    }
    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference=pdfFileStorageRef.child(invoiceNumber+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        downloadPDFUrl = urlTask.getResult().toString();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                        String saveCurrentDate = currentDate.format(calendar.getTime());
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                        String saveCurrentTime = currentTime.format(calendar.getTime());
                        String CurrentDateCurrentTime = saveCurrentDate +" "+ saveCurrentTime;


                        Invoice obj=new Invoice(invoiceNumber+".pdf",downloadPDFUrl, FirebaseAuth.getInstance().getCurrentUser().getUid(),CurrentDateCurrentTime,uID);
                        pdfDatabaseRef.child(CurrentDateCurrentTime).setValue(obj);

                        ordersRef.child(uID).child("invoiceFileUrl").setValue(downloadPDFUrl);
                        ordersRef.child(uID).child("invoiceFileName").setValue(invoiceNumber+".pdf");

                        pd.dismiss();
                        Toast.makeText(getContext(),"File Uploaded",Toast.LENGTH_LONG).show();


                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });
    }



    private void matchingSecurityCodeWithUser(String security_code, String uID) {
        ordersRef.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String securitycode=dataSnapshot.child("securitycode").getValue().toString();
                    if(securitycode.equals(security_code)){
                        ordersRef.child(uID).child("delivered").setValue("delivered").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(getContext(), "Product is delivered successfully.And your payment is also attatched in your account.", Toast.LENGTH_SHORT).show();
                                GiveMoneyToSpecificSellersAndDeliverers(uID,FirebaseAuth.getInstance().getCurrentUser().getUid());
                            }
                        });
                        FirebaseDatabase.getInstance().getReference().child("Deliverers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    int totaldelivery=Integer.valueOf(snapshot.child("totaldelivery").getValue().toString());
                                    totaldelivery=totaldelivery+1;
                                    FirebaseDatabase.getInstance().getReference().child("Deliverers")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("totaldelivery").setValue(String.valueOf(totaldelivery));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference().child("Deliverers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                int currentPick=Integer.valueOf(snapshot.child("currentpick").getValue().toString());
                                currentPick=currentPick-1;
                                FirebaseDatabase.getInstance().getReference().child("Deliverers")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("currentpick").setValue(String.valueOf(currentPick));
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void generatePDF(String name, String phone, String TotalPriceOfProducts) {

        addAllProductDetails.clear();
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {

                if(list.size()>6){
                    pageHeight=pageHeight+500;
                }


                String saveCurrentDate, saveCurrentTime;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint title = new Paint();
                PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
                PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
                Canvas canvas = myPage.getCanvas();
                canvas.drawBitmap(scaledbmp, 0, 0, paint);

                title.setTextAlign(Paint.Align.CENTER);
                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                title.setTextSize(70);
                title.setColor(ContextCompat.getColor(getContext(), R.color.white));
                canvas.drawText("A portal for IT professionals.", pagewidth/2, 250, title);

                paint.setTextAlign(Paint.Align.RIGHT);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setTextSize(30f);
                paint.setColor(ContextCompat.getColor(getContext(), R.color.white));
                canvas.drawText("Cell: +8801760091552", 1160, 40, paint);
                canvas.drawText("      +8801760000000", 1160, 80, paint);


                title.setTextAlign(Paint.Align.CENTER);
                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
                title.setTextSize(70);
                title.setColor(ContextCompat.getColor(getContext(), R.color.design_default_color_error));
                canvas.drawText("Invoice", pagewidth/2, 500, title);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(35f);
                paint.setColor(ContextCompat.getColor(getContext(), R.color.design_default_color_error));
                canvas.drawText("Customer name: "+name, 20, 590, paint);
                canvas.drawText("Contact no: "+phone, 20, 640, paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("Invoice no: "+invoiceNumber, pagewidth-20, 590, paint);
                canvas.drawText("Date: "+saveCurrentDate, pagewidth-20, 640, paint);
                canvas.drawText("Time: "+saveCurrentTime, pagewidth-20, 690, paint);


                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                canvas.drawRect(20,780,pagewidth-20,860,paint);

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawText("Si. No.", 40, 830, paint);
                canvas.drawText("Item Description", 200, 830, paint);
                canvas.drawText("Price", 700, 830, paint);
                canvas.drawText("Qty", 900, 830, paint);
                canvas.drawText("Total", 1050, 830, paint);

                canvas.drawLine(180,790,180,840,paint);
                canvas.drawLine(680,790,680,840,paint);
                canvas.drawLine(880,790,880,840,paint);
                canvas.drawLine(1030,790,1030,840,paint);


                int yAxis=950;
                for (int i = 0; i < list.size(); i++) {

                    String eachProduct=list.get(i);
                    String[] arr = eachProduct.trim().split("\\s+");

                    canvas.drawText(String.valueOf(i+1)+".",40,yAxis,paint);
                    canvas.drawText(arr[0].replace("#"," "),200,yAxis,paint);
                    canvas.drawText(arr[1],700,yAxis,paint);
                    canvas.drawText(arr[2],900,yAxis,paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(arr[3],pagewidth-40,yAxis,paint);
                    paint.setTextAlign(Paint.Align.LEFT);
                    yAxis=yAxis+100;
                }
                int subTotalLine=1200;

                if(list.size()>3){
                    //Math.ceil()
                    for (double i = 1;  ; i++) {
                        if(Math.ceil(list.size()/3.0)==i){
                            break;
                        }else {
                            subTotalLine=subTotalLine+300;
                        }
                    }

                }

                canvas.drawLine(680,subTotalLine,pagewidth-20,subTotalLine,paint);
                canvas.drawText("Sub total",700,subTotalLine+50,paint);
                canvas.drawText(":",900,subTotalLine+50,paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(TotalPriceOfProducts,pagewidth-40,subTotalLine+50,paint);

                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Tax (0%)",700,subTotalLine+100,paint);
                canvas.drawText(":",900,subTotalLine+100,paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("0",pagewidth-40,subTotalLine+100,paint);
                paint.setTextAlign(Paint.Align.LEFT);

                paint.setColor(Color.rgb(247,147,30));
                canvas.drawRect(680,subTotalLine+150,pagewidth-20,subTotalLine+250,paint);

                paint.setColor(Color.BLACK);
                paint.setTextSize(50f);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Total",700,subTotalLine+215,paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(TotalPriceOfProducts,pagewidth-40,subTotalLine+215,paint);

                pdfDocument.finishPage(myPage);

                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

                    File dir = new File(path);
                    if(!dir.exists())
                        dir.mkdirs();

                    File file = new File(dir, invoiceNumber+".pdf");

                    pdfDocument.writeTo(new FileOutputStream(file));
                }catch (IOException e) {
                    e.printStackTrace();
                }
                pdfDocument.close();
            }
        });

    }



    private void readData(FirebaseCallback firebaseCallback){
        FirebaseDatabase.getInstance().getReference().child("Orders").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int numberOfProducts=Integer.valueOf(snapshot.child("numberOfProducts").getValue().toString());
                    for (int i = 0; i < numberOfProducts; i++) {
                        String pid=snapshot.child("productDetails"+String.valueOf(i)).getValue().toString();
                        addAllProductDetails.add(pid);
                    }
                }
                firebaseCallback.onCallback(addAllProductDetails);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private interface FirebaseCallback{
        void onCallback(ArrayList<String> list);
    }




    private ArrayList<String> sellerListArray=new ArrayList<>();
    private void readDataForStoreSeller(FirebaseCallbackForStoreSeller firebaseCallbackForStoreSeller){
        FirebaseDatabase.getInstance().getReference().child("Orders").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int numberOfSellers=Integer.valueOf(snapshot.child("numberOfSellers").getValue().toString());
                    for (int i = 0; i < numberOfSellers; i++) {
                        String sid=snapshot.child("seller"+String.valueOf(i)).getValue().toString();
                        sellerListArray.add(sid);
                    }
                }
                firebaseCallbackForStoreSeller.onCallbackForStoreSeller(sellerListArray);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallbackForStoreSeller{
        void onCallbackForStoreSeller(ArrayList<String> list);
    }

    private void GiveMoneyToTheDeliverer(String deliveryMansUserId) {
        if(!deliveryMansUserId.equals("anyone")){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("BalanceHistoryForDeliverer");

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            String saveCurrentDate = currentDate.format(calendar.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            String saveCurrentTime = currentTime.format(calendar.getTime());
            String CurrentDateCurrentTime = saveCurrentDate +" "+ saveCurrentTime;

            DelivererBalance obj =new DelivererBalance(deliveryMansUserId,CurrentDateCurrentTime,"100",invoiceNumber);
            databaseReference.push().setValue(obj);

            FirebaseDatabase.getInstance().getReference().child("Deliverers")
                    .child(deliveryMansUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        int balance=Integer.valueOf(snapshot.child("balance").getValue().toString());
                        balance=balance+100;

                        FirebaseDatabase.getInstance().getReference().child("Deliverers")
                                .child(deliveryMansUserId).child("balance")
                                .setValue(String.valueOf(balance)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Your money is added to your respective account. Please check for make sure.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("Deliverers")
                    .child(deliveryMansUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        int balance=Integer.valueOf(snapshot.child("totalearning").getValue().toString());
                        balance=balance+100;
                        FirebaseDatabase.getInstance().getReference().child("Deliverers")
                                .child(deliveryMansUserId).child("totalearning")
                                .setValue(String.valueOf(balance));
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }
    private void GiveMoneyToSpecificSellersAndDeliverers(String uID, String DeliveryMansUserId) {
        GiveMoneyToTheDeliverer(DeliveryMansUserId);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("BalanceHistoryForSellerAndAdmin");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        String CurrentDateCurrentTime = saveCurrentDate +" "+ saveCurrentTime;

        sellerListArray.clear();

        readDataForStoreSeller(new FirebaseCallbackForStoreSeller() {
            @Override
            public void onCallbackForStoreSeller(ArrayList<String> list) {
                for (int i = 0; i < list.size(); i++) {

                    String eachSeller=list.get(i);
                    String[] arr = eachSeller.split("\\s+");
                    GiveMoneyToTheSellers(arr[0],arr[1]);

                    Balance obj =new Balance(arr[0],CurrentDateCurrentTime,arr[1],invoiceNumber,arr[2]);
                    databaseReference.push().setValue(obj);

                }
            }
        });

    }


    private void GiveMoneyToTheSellers(String sid, String price) {
        if(!sid.equals("Admin")){
            FirebaseDatabase.getInstance().getReference().child("Sellers").child(sid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot Sellersnapshot) {
                            if(Sellersnapshot.exists()){
                                int balance=Integer.valueOf(Sellersnapshot.child("balance").getValue().toString());
                                balance=balance+Integer.valueOf(price);
                                FirebaseDatabase.getInstance().getReference().child("Sellers").child(sid).child("balance")
                                        .setValue(String.valueOf(balance)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getContext(), "Send money to specific sellers successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("Sellers").child(sid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot Sellersnapshot) {
                            if(Sellersnapshot.exists()){
                                int balance=Integer.valueOf(Sellersnapshot.child("totalearning").getValue().toString());
                                balance=balance+Integer.valueOf(price);
                                FirebaseDatabase.getInstance().getReference().child("Sellers").child(sid).child("totalearning")
                                        .setValue(String.valueOf(balance));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

        }else {
            FirebaseDatabase.getInstance().getReference().child("Admins").child("01760091552").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot Adminsnapshot) {
                    if(Adminsnapshot.exists()){
                        int balance=Integer.valueOf(Adminsnapshot.child("balance").getValue().toString());
                        balance=balance+Integer.valueOf(price);
                        FirebaseDatabase.getInstance().getReference().child("Admins").child("01760091552").child("balance")
                                .setValue(String.valueOf(balance)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Send money to specific Admins successfully.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("Admins").child("01760091552").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot Adminsnapshot) {
                    if(Adminsnapshot.exists()){
                        int balance=Integer.valueOf(Adminsnapshot.child("totalearning").getValue().toString());
                        balance=balance+Integer.valueOf(price);
                        FirebaseDatabase.getInstance().getReference().child("Admins").child("01760091552").child("totalearning")
                                .setValue(String.valueOf(balance));
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }
}