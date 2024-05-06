package com.example.capstone.driverPanel;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.Driver;
import com.example.capstone.R;
import com.example.capstone.ReusableCodeForAll;
import com.example.capstone.SendNotification.APIService;
import com.example.capstone.SendNotification.Client;
import com.example.capstone.SendNotification.Data;
import com.example.capstone.SendNotification.MyResponse;
import com.example.capstone.SendNotification.NotificationSender;

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
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverCartFragment extends Fragment {

    RecyclerView recyclecart;
    private List<Cart> cartModelList;
    private DriverCartAdapter adapter;
    private LinearLayout TotalBtns;
    DatabaseReference databaseReference, data, reference, ref, getRef, dataa;
    public static TextView grandt;
    Button remove, placeorder;
    String address, Addnote;
    String OfferId, RandomUId, CustomerId;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Cart");
        View v = inflater.inflate(R.layout.fragment_drivercart, null);
        recyclecart = v.findViewById(R.id.recyclecart);
        recyclecart.setHasFixedSize(true);
        recyclecart.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        cartModelList = new ArrayList<>();
        grandt = v.findViewById(R.id.GT);
        remove = v.findViewById(R.id.RM);
        placeorder = v.findViewById(R.id.PO);
        TotalBtns = v.findViewById(R.id.TotalBtns);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        drivercart();
        return v;
    }

    private void drivercart() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cartModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cart cart = snapshot.getValue(Cart.class);

                    cartModelList.add(cart);
                }
                if (cartModelList.size() == 0) {
                    TotalBtns.setVisibility(View.INVISIBLE);
                } else {
                    TotalBtns.setVisibility(View.VISIBLE);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Are you sure you want to remove Offer");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    });


                    String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    data = FirebaseDatabase.getInstance().getReference("Driver").child(UserID);
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Driver driver = dataSnapshot.getValue(Driver.class);
                            placeorder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String ss = "";
                                            if (dataSnapshot.exists()) {
                                                ss = dataSnapshot.getValue(String.class);
                                            }

                                            if (ss.trim().equalsIgnoreCase("false") || ss.trim().equalsIgnoreCase("")) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Enter Experience");
                                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                                View view = inflater.inflate(R.layout.enteraddress, null);
                                                final EditText localaddress = (EditText) view.findViewById(R.id.LA);
                                                final EditText addnote = (EditText) view.findViewById(R.id.addnote);
                                                RadioGroup group = (RadioGroup) view.findViewById(R.id.grp);
                                                final RadioButton home = (RadioButton) view.findViewById(R.id.HA1);
                                                final RadioButton other = (RadioButton) view.findViewById(R.id.OA);
                                                builder.setView(view);
                                                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                        if (home.isChecked()) {

                                                            localaddress.getText().clear();
                                                            Toast.makeText(getContext(), "check", Toast.LENGTH_SHORT).show();
                                                        } else if (other.isChecked()) {
                                                            localaddress.getText().clear();
                                                            Toast.makeText(getContext(), "check", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                                        progressDialog.setMessage("Please wait...");
                                                        progressDialog.show();

                                                        reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                RandomUId = UUID.randomUUID().toString();
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    final Cart cart1 = dataSnapshot1.getValue(Cart.class);
                                                                    OfferId = cart1.getOfferID();
                                                                    address = localaddress.getText().toString().trim();
                                                                    Addnote = addnote.getText().toString().trim();
                                                                    final HashMap<String, String> hashMap = new HashMap<>();
                                                                    hashMap.put("CustomerId", cart1.getCustomerId());
                                                                    hashMap.put("OfferID", cart1.getOfferID());
                                                                    hashMap.put("OfferName", cart1.getOfferName());
                                                                    hashMap.put("OfferDays", cart1.getOfferQuantity());
                                                                    hashMap.put("Fee", cart1.getPrice());
                                                                    hashMap.put("TotalPrice", cart1.getTotalprice());
                                                                    if (RandomUId != null && OfferId != null) {
                                                                        FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Offers").child(OfferId).setValue(hashMap);
                                                                    } else {
                                                                        Log.e("DriverCartFragment", "RandomUId or OfferId is null");
                                                                    }
                                                                }
                                                                ref = FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal");
                                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        String grandtotal = dataSnapshot.getValue(String.class);
                                                                        HashMap<String, String> hashMap1 = new HashMap<>();
                                                                        hashMap1.put("Address", address);
                                                                        hashMap1.put("GrandTotalPrice", String.valueOf(grandtotal));
                                                                        hashMap1.put("MobileNumber", driver.getMobileNo());
                                                                        hashMap1.put("Name", driver.getFirstName() + " " + driver.getLastName());
                                                                        hashMap1.put("Note", Addnote);
                                                                        FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                getRef = FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Offers");
                                                                                                getRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                                                                            final DriverPendingOrders driverPendingOrders = dataSnapshot2.getValue(DriverPendingOrders.class);
                                                                                                            String d = driverPendingOrders.getOfferID();
                                                                                                            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                                                            CustomerId = driverPendingOrders.getCustomerId();
                                                                                                            final HashMap<String, String> hashMap2 = new HashMap<>();
                                                                                                            hashMap2.put("CustomerId", CustomerId);
                                                                                                            hashMap2.put("OfferId", driverPendingOrders.getOfferID());
                                                                                                            hashMap2.put("OfferName", driverPendingOrders.getOfferName());
                                                                                                            hashMap2.put("OfferQuantity", driverPendingOrders.getOfferQuantity());
                                                                                                            hashMap2.put("Fee", driverPendingOrders.getPrice());
                                                                                                            hashMap2.put("RandomUID", RandomUId);
                                                                                                            hashMap2.put("TotalPrice", driverPendingOrders.getTotalPrice());
                                                                                                            hashMap2.put("UserId", userid);

                                                                                                            FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(CustomerId).child(RandomUId).child("Offers").child(d).setValue(hashMap2);
                                                                                                        }
                                                                                                        dataa = FirebaseDatabase.getInstance().getReference("DriverPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation");
                                                                                                        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                DriverPendingOrders1 driverPendingOrders1 = dataSnapshot.getValue(DriverPendingOrders1.class);
                                                                                                                HashMap<String, String> hashMap3 = new HashMap<>();
                                                                                                                hashMap3.put("Address", driverPendingOrders1.getAddress());
                                                                                                                hashMap3.put("GrandTotalPrice", driverPendingOrders1.getGrandTotalPrice());
                                                                                                                hashMap3.put("MobileNumber", driverPendingOrders1.getMobileNumber());
                                                                                                                hashMap3.put("Name", driverPendingOrders1.getName());
                                                                                                                hashMap3.put("Note", driverPendingOrders1.getNote());
                                                                                                                hashMap3.put("RandomUID", RandomUId);
                                                                                                                if (CustomerId != null) {
                                                                                                                FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(CustomerId).child(RandomUId).child("OtherInformation").setValue(hashMap3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {

                                                                                                                        FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void aVoid) {

                                                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(CustomerId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                                                        sendNotifications(usertoken, "New Order", "You have a new Order", "Order");
                                                                                                                                        progressDialog.dismiss();
                                                                                                                                        ReusableCodeForAll.ShowAlert(getContext(), "", "Your Request has been shifted to Pending state, please wait until the Customer accept your Request.");
                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                                    }
                                                                                                                                });

                                                                                                                            }
                                                                                                                        });


                                                                                                                    }


                                                                                                                }); } else {
                                                                                                                    Log.e("DriverCartFragment", "CustomerId is null");
                                                                                                                }
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                            }
                                                                                                        });
//                                                                                                            }
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

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
//                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                AlertDialog aler = builder.create();
                                                aler.show();

                                            } else {
                                                ReusableCodeForAll.ShowAlert(getContext(), "Error", "It seems you have already placed the order, So you cannot place another order until the delivery of first order");
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
                adapter = new DriverCartAdapter(getContext(), cartModelList);
                recyclecart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}