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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.Customer;

import com.example.capstone.R;
import com.example.capstone.SendNotification.APIService;
import com.example.capstone.SendNotification.Client;
import com.example.capstone.SendNotification.Data;
import com.example.capstone.SendNotification.MyResponse;
import com.example.capstone.SendNotification.NotificationSender;

import com.example.capstone.customerPanel.CustomerFinalOrders;
import com.example.capstone.Customer;
import com.example.capstone.SendNotification.Client;
import com.example.capstone.SendNotification.Data;
import com.example.capstone.SendNotification.MyResponse;
import com.example.capstone.customer_panel_bottom_navigation;
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

public class CustomerPreparedOrderView extends AppCompatActivity {


    RecyclerView recyclerViewdish;
    private List<CustomerFinalOrders> customerFinalOrdersList;
    private CustomerPreparedOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid;
    TextView grandtotal, address, name, number;
    LinearLayout l1;
    Button Prepared;
    private ProgressDialog progressDialog;
    private APIService apiService;
    Spinner Shipper;
    String  deliveryId = "oCpc4SwLVFbKO0fPdtp4R6bmDmI3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_prepared_order_view);


        recyclerViewdish = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.Gtotal);
        address = findViewById(R.id.Cadress);
        name = findViewById(R.id.Cname);
        number = findViewById(R.id.Cnumber);
        l1 = findViewById(R.id.linear);
        Shipper = findViewById(R.id.shipper);
        Prepared = findViewById(R.id.prepared);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(CustomerPreparedOrderView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(CustomerPreparedOrderView.this));
        customerFinalOrdersList = new ArrayList<>();


        CustomerorderoffersView();
    }

    private void CustomerorderoffersView() {


        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerFinalOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerFinalOrders customerFinalOrders = snapshot.getValue(CustomerFinalOrders.class);
                    customerFinalOrdersList.add(customerFinalOrders);
                    userid = customerFinalOrders.getUserId();
                }
                if (customerFinalOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Prepared.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            FirebaseDatabase.getInstance().getReference("DriverFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("Status").setValue("Your Order is delivered").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String usertoken = dataSnapshot.getValue(String.class);
                                            sendNotifications(usertoken, "Home Chef", "Thank you for Ordering", "ThankYou");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String usertoken = dataSnapshot.getValue(String.class);
                                            sendNotifications(usertoken, "Order Placed", "Your food has been delivered to Driver's Doorstep", "Delivered");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerPreparedOrderView.this);
                                    builder.setMessage("Offer has been accepted already");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                            Intent intent = new Intent(CustomerPreparedOrderView.this, customer_panel_bottom_navigation.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();


                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReference("DriverFinalOrders").child(userid).child(RandomUID).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseDatabase.getInstance().getReference("DriverFinalOrders").child(userid).child(RandomUID).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("DeliveryShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false");
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
                    });




            };




    }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CustomerFinalOrders1 customerFinalOrders1 = dataSnapshot.getValue(CustomerFinalOrders1.class);
                grandtotal.setText("₹ " + customerFinalOrders1.getGrandTotalPrice());
                address.setText(customerFinalOrders1.getAddress());
                name.setText(customerFinalOrders1.getName());
                number.setText("+91" + customerFinalOrders1.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String prepared) {

        Data data = new Data(title, message, prepared);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(CustomerPreparedOrderView.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}