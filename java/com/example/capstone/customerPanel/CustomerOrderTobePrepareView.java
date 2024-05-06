package com.example.capstone.customerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.R;
import com.example.capstone.SendNotification.APIService;
import com.example.capstone.SendNotification.Client;
import com.example.capstone.SendNotification.Data;
import com.example.capstone.SendNotification.MyResponse;
import com.example.capstone.SendNotification.NotificationSender;

import com.example.capstone.SendNotification.Client;
import com.example.capstone.SendNotification.Data;
import com.example.capstone.SendNotification.MyResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerOrderTobePrepareView extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<CustomerWaitingOrders> customerWaitingOrdersList;
    private CustomerOrderTobePrepareViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid;
    TextView grandtotal, note, address, name, number;
    LinearLayout l1;
    Button Preparing;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ordertobe_prepare_view);
        recyclerViewdish = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.rupees);
        note = findViewById(R.id.NOTE);
        address = findViewById(R.id.ad);
        name = findViewById(R.id.nm);
        number = findViewById(R.id.num);
        l1 = findViewById(R.id.button1);
        Preparing = findViewById(R.id.preparing);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(CustomerOrderTobePrepareView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(CustomerOrderTobePrepareView.this));
        customerWaitingOrdersList = new ArrayList<>();
        CustomerorderoffersView();
    }

    private void CustomerorderoffersView() {
        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerWaitingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerWaitingOrders customerWaitingOrders = snapshot.getValue(CustomerWaitingOrders.class);
                    customerWaitingOrdersList.add(customerWaitingOrders);
                }
                if (customerWaitingOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Preparing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        final CustomerWaitingOrders customerWaitingOrders = dataSnapshot1.getValue(CustomerWaitingOrders.class);
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        String dishid = customerWaitingOrders.getOfferId();
                                        userid = customerWaitingOrders.getUserId();
                                        hashMap.put("CustomerId", customerWaitingOrders.getCustomerId());
                                        hashMap.put("OfferId", customerWaitingOrders.getOfferId());
                                        hashMap.put("OfferName", customerWaitingOrders.getOfferName());
                                        hashMap.put("OfferPrice", customerWaitingOrders.getOfferPrice());
                                        hashMap.put("OfferQuantity", customerWaitingOrders.getOfferQuantity());
                                        hashMap.put("RandomUID", RandomUID);
                                        hashMap.put("TotalPrice", customerWaitingOrders.getTotalPrice());
                                        hashMap.put("UserId", customerWaitingOrders.getUserId());
                                        FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers").child(dishid).setValue(hashMap);
                                    }
                                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final CustomerWaitingOrders1 customerWaitingOrders1 = dataSnapshot.getValue(CustomerWaitingOrders1.class);
                                            HashMap<String, String> hashMap1 = new HashMap<>();
                                            hashMap1.put("Address", customerWaitingOrders1.getAddress());
                                            hashMap1.put("TotalPrice", customerWaitingOrders1.getGrandTotalPrice());
                                            hashMap1.put("MobileNumber", customerWaitingOrders1.getMobileNumber());
                                            hashMap1.put("Name", customerWaitingOrders1.getName());
                                            hashMap1.put("RandomUID", RandomUID);
                                            hashMap1.put("Status", "Customer is preparing your Order...");
                                            FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("Status").setValue("Customer is preparing your order...").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                                                    sendNotifications(usertoken, "Estimated Time", "Customer is Preparing your Order", "Preparing");
                                                                                    progressDialog.dismiss();
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerOrderTobePrepareView.this);
                                                                                    builder.setMessage("See Orders which are Prepared");
                                                                                    builder.setCancelable(false);
                                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                                            dialog.dismiss();
                                                                                            Intent b = new Intent(CustomerOrderTobePrepareView.this, CustomerOrderTobePrepared.class);
                                                                                            startActivity(b);
                                                                                            finish();


                                                                                        }
                                                                                    });
                                                                                    AlertDialog alert = builder.create();
                                                                                    alert.show();
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
                adapter = new CustomerOrderTobePrepareViewAdapter(CustomerOrderTobePrepareView.this, customerWaitingOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomerWaitingOrders1 customerWaitingOrders1 = dataSnapshot.getValue(CustomerWaitingOrders1.class);
                grandtotal.setText("₹ " + customerWaitingOrders1.getGrandTotalPrice());
                note.setText(customerWaitingOrders1.getNote());
                address.setText(customerWaitingOrders1.getAddress());
                name.setText(customerWaitingOrders1.getName());
                number.setText("+91" + customerWaitingOrders1.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String preparing) {
        Data data = new Data(title, message, preparing);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(CustomerOrderTobePrepareView.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}