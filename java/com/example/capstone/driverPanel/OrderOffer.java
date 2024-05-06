package com.example.capstone.driverPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.capstone.Customer;
import com.example.capstone.Driver;
import com.example.capstone.UpdateModel;

import com.example.capstone.customer_panel_bottom_navigation;

import com.example.capstone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderOffer extends AppCompatActivity {


    String RandomId, CustomerID;
    ImageView imageView;
    ElegantNumberButton additem;
    TextView Typename, CustomerName, CustomerLoaction, TypeQuantity, TypePrice, TypeDescription;
    DatabaseReference databaseReference, dataaa, customerdata, reference, data, dataref;
    String State, City, Sub, dishname;
    int dishprice;
    String custID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_offer);


        Typename = (TextView) findViewById(R.id.food_name);
        CustomerName = (TextView) findViewById(R.id.customer_name);
        CustomerLoaction = (TextView) findViewById(R.id.customer_location);
        TypeQuantity = (TextView) findViewById(R.id.food_quantity);
        TypePrice = (TextView) findViewById(R.id.food_price);
        TypeDescription = (TextView) findViewById(R.id.food_description);
        imageView = (ImageView) findViewById(R.id.image);
        additem = (ElegantNumberButton) findViewById(R.id.number_btn);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Driver").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Driver cust = dataSnapshot.getValue(Driver.class);
                State = cust.getState();
                City = cust.getCity();
                Sub = cust.getSuburban();

                RandomId = getIntent().getStringExtra("OfferMenu");
                CustomerID = getIntent().getStringExtra("CustomerId");

                databaseReference = FirebaseDatabase.getInstance().getReference("OfferDetails").child(State).child(City).child(Sub).child(CustomerID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateModel updateModel = dataSnapshot.getValue(UpdateModel.class);
                        Typename.setText(updateModel.getOffers());
                        String qua = "<b>" + "Quantity: " + "</b>" + updateModel.getQuantity();
                        TypeQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Description: " + "</b>" + updateModel.getDescription();
                        TypeDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Fee: ₹ " + "</b>" + updateModel.getPrice();
                        TypePrice.setText(Html.fromHtml(pri));
                        Glide.with(OrderOffer.this).load(updateModel.getImageURL()).into(imageView);

                        customerdata = FirebaseDatabase.getInstance().getReference("Customer").child(CustomerID);
                        customerdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Customer customer = dataSnapshot.getValue(Customer.class);

                                String name = "<b>" + "Customer Name: " + "</b>" + customer.getFname() + " " + customer.getLname();
                                CustomerName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Location: " + "</b>" + customer.getSuburban();
                                CustomerLoaction.setText(Html.fromHtml(loc));
                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Cart cart = dataSnapshot.getValue(Cart.class);

                                         if (dataSnapshot.exists()) {
                                            additem.setNumber(cart.getOfferQuantity());
                                        }
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart cart1=null;
                                if (dataSnapshot.exists()) {
                                    int totalcount=0;
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        totalcount++;
                                    }
                                    int i=0;
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        i++;
                                        if(i==totalcount){
                                            cart1= snapshot.getValue(Cart.class);
                                        }
                                    }

                                    if (CustomerID.equals(cart1.getCustomerId())) {
                                        data = FirebaseDatabase.getInstance().getReference("OfferDetails").child(State).child(City).child(Sub).child(CustomerID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UpdateModel update = dataSnapshot.getValue(UpdateModel.class);
                                                dishname = update.getOffers();
                                                dishprice = Integer.parseInt(update.getPrice());

                                                int num = Integer.parseInt(additem.getNumber());
                                                int totalprice = num * dishprice;
                                                if (num != 0) {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("Name", dishname);
                                                    hashMap.put("ID", RandomId);
                                                    hashMap.put("No of Days", String.valueOf(num));
                                                    hashMap.put("Fee", String.valueOf(dishprice));
                                                    hashMap.put("Total fee", String.valueOf(totalprice));
                                                    hashMap.put("CustomerId", CustomerID);
                                                    custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Toast.makeText(OrderOffer.this, "Added to confirm state", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {

                                                    firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderOffer.this);
                                        builder.setMessage("You can't add multiple customer at a time. Try to add items of same customer");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                                Intent intent = new Intent(OrderOffer.this, customer_panel_bottom_navigation.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    data = FirebaseDatabase.getInstance().getReference("OfferDetails").child(State).child(City).child(Sub).child(CustomerID).child(RandomId);
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UpdateModel update = dataSnapshot.getValue(UpdateModel.class);
                                            dishname = update.getOffers();
                                            dishprice = Integer.parseInt(update.getPrice());
                                            int num = Integer.parseInt(additem.getNumber());
                                            int totalprice = num * dishprice;
                                            if (num != 0) {
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("OfferName", dishname);
                                                hashMap.put("OfferID", RandomId);
                                                hashMap.put("OfferQuantity", String.valueOf(num));
                                                hashMap.put("Price", String.valueOf(dishprice));
                                                hashMap.put("Totalprice", String.valueOf(totalprice));
                                                hashMap.put("CustomerId", CustomerID);
                                                custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                                reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(OrderOffer.this, "Confirm Application in the next layout", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {

                                                firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
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
}
