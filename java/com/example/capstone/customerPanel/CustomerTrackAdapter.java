package com.example.capstone.customerPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import java.util.List;

public class CustomerTrackAdapter extends RecyclerView.Adapter<CustomerTrackAdapter.ViewHolder> {

    private Context context;
    private List<CustomerFinalOrders> customerFinalOrderslist;

    public CustomerTrackAdapter(Context context, List<CustomerFinalOrders> customerFinalOrderslist) {
        this.customerFinalOrderslist = customerFinalOrderslist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_track, parent, false);
        return new CustomerTrackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CustomerFinalOrders customerFinalOrders = customerFinalOrderslist.get(position);
        holder.Offername.setText(customerFinalOrders.getOfferName());
        holder.Quantity.setText(customerFinalOrders.getOfferQuantity() + "× ");
        holder.Totalprice.setText("₹ " + customerFinalOrders.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return customerFinalOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Offername, Quantity, Totalprice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Offername = itemView.findViewById(R.id.dishnm);
            Quantity = itemView.findViewById(R.id.dishqty);
            Totalprice = itemView.findViewById(R.id.totRS);
        }
    }
}