package com.example.capstone.customerPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CustomerPendingOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CustomerPendingOrders1> customerPendingOrders1List;
    private CustomerPendingOrdersAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Pending Offerss");
        v = inflater.inflate(R.layout.fragment_customer_pendingorders, null);
        recyclerView = v.findViewById(R.id.Recycle_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerPendingOrders1List = new ArrayList<>();
        swipeRefreshLayout = v.findViewById(R.id.Swipe_layoutt);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        adapter = new CustomerPendingOrdersAdapter(getContext(),customerPendingOrders1List);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customerPendingOrders1List.clear();
                recyclerView = v.findViewById(R.id.Recycle_orders);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                customerPendingOrders1List = new ArrayList<>();
                customerorders();
            }
        });
        customerorders();
        return v;
    }

    private void customerorders() {
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    customerPendingOrders1List.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                CustomerPendingOrders1 customerPendingOrders1 = dataSnapshot.getValue(CustomerPendingOrders1.class);
                                customerPendingOrders1List.add(customerPendingOrders1);
                                adapter = new CustomerPendingOrdersAdapter(getContext(),customerPendingOrders1List);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}