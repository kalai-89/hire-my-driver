package com.example.capstone.driverPanel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class DriverCartAdapter extends RecyclerView.Adapter<DriverCartAdapter.ViewHolder> {

    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;

    public DriverCartAdapter(Context context, List<Cart> cartModellist) {
        this.cartModellist = cartModellist;
        this.mcontext = context;
        total = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cart_placeorder, parent, false);
        return new DriverCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.Offername.setText(cart.getOfferName());
        holder.PriceRs.setText("Fee: ₹ " + cart.getPrice());
        holder.Qty.setText("× " + cart.getOfferQuantity());
        holder.Totalrs.setText("Total: ₹ " + cart.getTotalprice());
        String totalprice = cart.getTotalprice();
        if (totalprice != null) {
            total += Integer.parseInt(totalprice);
        }
        if (cart.getOfferQuantity() != null) {
            holder.elegantNumberButton.setNumber(cart.getOfferQuantity());
        }
        final int Offerprice = Integer.parseInt(cart.getPrice());

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int num = newValue;
                int totalprice = num * Offerprice;
                if (num != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("OfferID", cart.getOfferID());
                    hashMap.put("OfferName", cart.getOfferName());
                    hashMap.put("OfferQuantity", String.valueOf(num));
                    hashMap.put("Fee", String.valueOf(Offerprice));
                    hashMap.put("Totalprice", String.valueOf(totalprice));
                    hashMap.put("CustomerId",cart.getOfferID());
                    if (cart.getOfferID() != null) {
                        FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getOfferID()).setValue(hashMap);
                    } else {
                        Log.e("DriverCartAdapter", "Offer ID is null for cart item: " + cart.toString());
                    }
                } else if (cart.getOfferID() != null)  {
                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getOfferID()).removeValue();
                } else {
                    Log.e("DriverCartAdapter", "Offer ID is null for cart item: " + cart.toString());
                }
            }
        });
        DriverCartFragment.grandt.setText("Grand Total: ₹ " + total);
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Offername, PriceRs, Qty, Totalrs;
        ElegantNumberButton elegantNumberButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Offername = itemView.findViewById(R.id.Offername);
            PriceRs = itemView.findViewById(R.id.pricers);
            Qty = itemView.findViewById(R.id.qty);
            Totalrs = itemView.findViewById(R.id.totalrs);
            elegantNumberButton = itemView.findViewById(R.id.elegantbtn);
        }
    }
}