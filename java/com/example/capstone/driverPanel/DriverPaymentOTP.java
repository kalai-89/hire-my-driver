package com.example.capstone.driverPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstone.R;
import com.example.capstone.SendNotification.APIService;
import com.example.capstone.SendNotification.Client;
import com.example.capstone.SendNotification.Data;
import com.example.capstone.SendNotification.MyResponse;
import com.example.capstone.SendNotification.NotificationSender;


import com.example.capstone.driverPanel.DriverPaymentOrders;
import com.example.capstone.driver_panel_bottom_navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverPaymentOTP extends AppCompatActivity {

    EditText otp;
    Button place;
    String ot, RandomUID, CustomerID;
    private APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment_otp);
        otp = (EditText) findViewById(R.id.OTP);
        place = (Button) findViewById(R.id.place);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        RandomUID = getIntent().getStringExtra("RandomUID");

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ot = otp.getText().toString().trim();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final DriverPaymentOrders driverPaymentOrders = dataSnapshot1.getValue(DriverPaymentOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String dishid = driverPaymentOrders.getOfferId();
                            hashMap.put("CustomerId", driverPaymentOrders.getCustomerId());
                            hashMap.put("OfferId", driverPaymentOrders.getOfferId());
                            hashMap.put("OfferName", driverPaymentOrders.getOfferName());
                            hashMap.put("OfferOffer Fee ", driverPaymentOrders.getOfferPrice());
                            hashMap.put("OfferDays", driverPaymentOrders.getOfferQuantity());
                            hashMap.put("RandomUID", RandomUID);
                            hashMap.put("TotalOffer Fee ", driverPaymentOrders.getTotalPrice ());
                            hashMap.put("UserId", driverPaymentOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("DriverFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers").child(dishid).setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final DriverPaymentOrders1 driverPaymentOrders1 = dataSnapshot.getValue(DriverPaymentOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", driverPaymentOrders1.getAddress());
                                hashMap1.put("TotalOffer Fee ", driverPaymentOrders1.getGrandTotalPrice ());
                                hashMap1.put("MobileNumber", driverPaymentOrders1.getMobileNumber());
                                hashMap1.put("Name", driverPaymentOrders1.getName());
                                hashMap1.put("Note", driverPaymentOrders1.getNote());
                                hashMap1.put("RandomUID", RandomUID);
                                hashMap1.put("Status", "Your order is waiting to be prepared by Customer...");
                                FirebaseDatabase.getInstance().getReference("DriverFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final DriverPaymentOrders driverPaymentOrderss = snapshot.getValue(DriverPaymentOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    String dishid = driverPaymentOrderss.getOfferId();
                                                    CustomerID = driverPaymentOrderss.getCustomerId();
                                                    hashMap2.put("CustomerId", driverPaymentOrderss.getCustomerId());
                                                    hashMap2.put("OfferId", driverPaymentOrderss.getOfferId());
                                                    hashMap2.put("OfferName", driverPaymentOrderss.getOfferName());
                                                    hashMap2.put("OfferOffer Fee ", driverPaymentOrderss.getOfferPrice());
                                                    hashMap2.put("OfferDays", driverPaymentOrderss.getOfferQuantity());
                                                    hashMap2.put("RandomUID", RandomUID);
                                                    hashMap2.put("TotalOffer Fee ", driverPaymentOrderss.getTotalPrice ());
                                                    hashMap2.put("UserId", driverPaymentOrderss.getUserId());
                                                    FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(CustomerID).child(RandomUID).child("Offers").child(dishid).setValue(hashMap2);
                                                }
                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        DriverPaymentOrders1 driverPaymentOrders11 = dataSnapshot.getValue(DriverPaymentOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", driverPaymentOrders11.getAddress());
                                                        hashMap3.put("TotalOffer Fee ", driverPaymentOrders11.getGrandTotalPrice());
                                                        hashMap3.put("MobileNumber", driverPaymentOrders11.getMobileNumber());
                                                        hashMap3.put("Name", driverPaymentOrders11.getName());
                                                        hashMap3.put("Note", driverPaymentOrders11.getNote());
                                                        hashMap3.put("RandomUID", RandomUID);
                                                        hashMap3.put("Status", "Your order is waiting to be prepared by Customer...");
                                                        FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(CustomerID).child(RandomUID).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(CustomerID).child(RandomUID).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(CustomerID).child(RandomUID).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(CustomerID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                        sendNotifications(usertoken, "Order Confirmed", "Payment mode is confirmed by user, Now you can start the order", "Confirm");
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                AlertDialog.Builder builder = new AlertDialog.Builder(DriverPaymentOTP.this);
                                                                                                builder.setMessage("Payment mode confirmed, Now you can track your order.");
                                                                                                builder.setCancelable(false);
                                                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                                                        dialog.dismiss();
                                                                                                        Intent b = new Intent(DriverPaymentOTP.this, driver_panel_bottom_navigation.class);
                                                                                                        b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                        startActivity(b);
                                                                                                        finish();

                                                                                                    }
                                                                                                });
                                                                                                AlertDialog alert = builder.create();
                                                                                                alert.show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String confirm) {

        Data data = new Data(title, message, confirm);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(DriverPaymentOTP.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


}