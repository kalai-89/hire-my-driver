package com.example.capstone.driverPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.capstone.R;
import com.example.capstone.driverPanel.DriverPaymentOrders;
import com.example.capstone.driverPanel.DriverPaymentOrders1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PayableOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<DriverPaymentOrders> driverPaymentOrdersList;
    private PayableOrderAdapter adapter;
    DatabaseReference databaseReference;
    private LinearLayout pay;
    Button payment;
    TextView grandtotal,phonenum;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payable_orders);
        recyclerView = findViewById(R.id.recyclepayableorder);
        pay = findViewById(R.id.btn);
        grandtotal = findViewById(R.id.rs);
        phonenum = findViewById(R.id.textView11);
        payment = (Button) findViewById(R.id.paymentmethod);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PayableOrders.this));
        driverPaymentOrdersList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.Swipe2);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        adapter = new PayableOrderAdapter(PayableOrders.this, driverPaymentOrdersList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView = findViewById(R.id.recyclepayableorder);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(PayableOrders.this));
                driverPaymentOrdersList = new ArrayList<>();
                DriverpayableOrders();
            }
        });
        DriverpayableOrders();

    }

    private void DriverpayableOrders() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    driverPaymentOrdersList.clear();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String randomuid = snapshot.getKey();
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Offers");
                        data.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    DriverPaymentOrders driverPaymentOrders = snapshot1.getValue(DriverPaymentOrders.class);
                                    driverPaymentOrdersList.add(driverPaymentOrders);
                                }
                                if (driverPaymentOrdersList.size() == 0) {
                                    pay.setVisibility(View.INVISIBLE);
                                } else {
                                    pay.setVisibility(View.VISIBLE);
                                    payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(PayableOrders.this, DriverPayment.class);
                                            intent.putExtra("RandomUID", randomuid);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                                adapter = new PayableOrderAdapter(PayableOrders.this, driverPaymentOrdersList);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("OtherInformation");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    DriverPaymentOrders1 driverPaymentOrders1 = dataSnapshot.getValue(DriverPaymentOrders1.class);
                                    grandtotal.setText("₹ " + driverPaymentOrders1.getGrandTotalPrice());
                                    phonenum.setText("Contact Number" + driverPaymentOrders1.getMobileNumber());
                                    swipeRefreshLayout.setRefreshing(false);

                                } else {
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}