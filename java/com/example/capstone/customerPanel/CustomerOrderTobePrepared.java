package com.example.capstone.customerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class CustomerOrderTobePrepared extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CustomerWaitingOrders1> customerWaitingOrders1List;
    private CustomerOrderTobePreparedAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_tobe_prepared);
        recyclerView = findViewById(R.id.Recycle_orderstobeprepared);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrderTobePrepared.this));
        customerWaitingOrders1List = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.Swipe1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        adapter = new CustomerOrderTobePreparedAdapter(CustomerOrderTobePrepared.this, customerWaitingOrders1List);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customerWaitingOrders1List.clear();
                recyclerView = findViewById(R.id.Recycle_orderstobeprepared);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrderTobePrepared.this));
                customerWaitingOrders1List = new ArrayList<>();
                customerorderstobePrepare();
            }
        });
        customerorderstobePrepare();

    }

    private void customerorderstobePrepare() {

        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    customerWaitingOrders1List.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                CustomerWaitingOrders1 customerWaitingOrders1 = dataSnapshot.getValue(CustomerWaitingOrders1.class);
                                customerWaitingOrders1List.add(customerWaitingOrders1);
                                adapter = new CustomerOrderTobePreparedAdapter(CustomerOrderTobePrepared.this, customerWaitingOrders1List);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
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