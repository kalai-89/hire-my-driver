package com.example.capstone.customerPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.ReusableCodeForAll;
import com.example.capstone.SendNotification.APIService;
import com.example.capstone.SendNotification.Client;
import com.example.capstone.SendNotification.Data;
import com.example.capstone.SendNotification.MyResponse;
import com.example.capstone.SendNotification.NotificationSender;

import com.example.capstone.ReusableCodeForAll;
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

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerPendingOrdersAdapter extends RecyclerView.Adapter<CustomerPendingOrdersAdapter.ViewHolder> {

    private Context context;
    private List<CustomerPendingOrders1> customerPendingOrders1list;
    private APIService apiService;
    String userid, dishid;


    public CustomerPendingOrdersAdapter(Context context, List<CustomerPendingOrders1> customerPendingOrders1list) {
        this.customerPendingOrders1list = customerPendingOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_orders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new CustomerPendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CustomerPendingOrders1 customerPendingOrders1 = customerPendingOrders1list.get(position);
        holder.Address.setText(customerPendingOrders1.getAddress());
        holder.grandtotalprice.setText("Total: ₹ " + customerPendingOrders1.getGrandTotalPrice());
        final String random = customerPendingOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Customer_order_offers.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
            }
        });

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Offers");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final CustomerPendingOrders customerPendingOrders = snapshot.getValue(CustomerPendingOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String customerid = customerPendingOrders.getCustomerId();
                            String dishid = customerPendingOrders.getOfferId();
                            hashMap.put("CustomerId", customerPendingOrders.getCustomerId());
                            hashMap.put("OfferId", customerPendingOrders.getOfferId());
                            hashMap.put("OfferName", customerPendingOrders.getOfferName());
                            hashMap.put("OfferPrice", customerPendingOrders.getOfferPrice());
                            hashMap.put("OfferDays", customerPendingOrders.getOfferQuantity());
                            hashMap.put("RandomUID", random);
                            hashMap.put("TotalPrice", customerPendingOrders.getTotalPrice());
                            hashMap.put("UserId", customerPendingOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(customerid).child(random).child("Offers").child(dishid).setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                CustomerPendingOrders1 customerPendingOrders1 = dataSnapshot.getValue(CustomerPendingOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", customerPendingOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", customerPendingOrders1.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", customerPendingOrders1.getMobileNumber());
                                hashMap1.put("Name", customerPendingOrders1.getName());
                                hashMap1.put("Note",customerPendingOrders1.getNote());
                                hashMap1.put("RandomUID", random);
                                FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Offers");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final CustomerPendingOrders customerPendingOrders = snapshot.getValue(CustomerPendingOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    userid = customerPendingOrders.getUserId();
                                                    dishid = customerPendingOrders.getOfferId();
                                                    hashMap2.put("CustomerId", customerPendingOrders.getCustomerId());
                                                    hashMap2.put("OfferId", customerPendingOrders.getOfferId());
                                                    hashMap2.put("OfferName", customerPendingOrders.getOfferName());
                                                    hashMap2.put("OfferPrice", customerPendingOrders.getOfferPrice());
                                                    hashMap2.put("OfferDays", customerPendingOrders.getOfferQuantity());
                                                    hashMap2.put("RandomUID", random);
                                                    hashMap2.put("TotalPrice", customerPendingOrders.getTotalPrice());
                                                    hashMap2.put("UserId", customerPendingOrders.getUserId());
                                                    FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(userid).child(random).child("Offers").child(dishid).setValue(hashMap2);
                                                }
                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        CustomerPendingOrders1 customerPendingOrders1 = dataSnapshot.getValue(CustomerPendingOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", customerPendingOrders1.getAddress());
                                                        hashMap3.put("GrandTotalPrice", customerPendingOrders1.getGrandTotalPrice());
                                                        hashMap3.put("MobileNumber", customerPendingOrders1.getMobileNumber());
                                                        hashMap3.put("Name", customerPendingOrders1.getName());
                                                        hashMap3.put("Note",customerPendingOrders1.getNote());
                                                        hashMap3.put("RandomUID", random);
                                                        FirebaseDatabase.getInstance().getReference("DriverPaymentOrders").child(userid).child(random).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(userid).child(random).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                        sendNotifications(usertoken, "Order Accepted", "Your Order has been Accepted by the Customer, Now make Payment for Order", "Payment");
                                                                                                        ReusableCodeForAll.ShowAlert(context,"","Go to the payment page");

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

        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Offers");
                Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final CustomerPendingOrders customerPendingOrders = snapshot.getValue(CustomerPendingOrders.class);
                            userid = customerPendingOrders.getUserId();
                            dishid = customerPendingOrders.getOfferId();
                        }
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String usertoken = dataSnapshot.getValue(String.class);
                                sendNotifications(usertoken, "Order Rejected", "Your Order has been Rejected by the Customer due to some Circumstances", "Home");
                                FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(userid).child(random).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Offers").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return customerPendingOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder, Accept, Reject;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.AD);
            grandtotalprice = itemView.findViewById(R.id.TP);
            Vieworder = itemView.findViewById(R.id.vieww);
            Accept = itemView.findViewById(R.id.accept);
            Reject = itemView.findViewById(R.id.reject);


        }
    }
}