package com.example.capstone.customerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Customer_order_offers extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<CustomerPendingOrders> customerPendingOrdersList;
    private CustomerOrderOffersAdapter adapter;
    DatabaseReference reference;
    String RandomUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_offers);
        recyclerViewdish = findViewById(R.id.Recycle_orders_offers);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(this));
        customerPendingOrdersList = new ArrayList<>();
        Customerorderoffers();

    }

    private void Customerorderoffers() {

        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Offers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerPendingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CustomerPendingOrders customerPendingOrders = snapshot.getValue(CustomerPendingOrders.class);
                    customerPendingOrdersList.add(customerPendingOrders);
                }
                adapter = new CustomerOrderOffersAdapter(Customer_order_offers.this, customerPendingOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}