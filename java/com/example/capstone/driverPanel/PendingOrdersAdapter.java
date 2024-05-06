package com.example.capstone.driverPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import java.util.List;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {

    private Context context;
    private List<DriverPendingOrders> customerPendingOrderslist;

    public PendingOrdersAdapter(Context context, List<DriverPendingOrders> customerPendingOrderslist) {
        this.customerPendingOrderslist = customerPendingOrderslist;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_order, parent, false);
        return new PendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DriverPendingOrders customerPendingOrders = customerPendingOrderslist.get(position);
        holder.Offername.setText(customerPendingOrders.getOfferName());
        holder.Price.setText("Price: ₹ " + customerPendingOrders.getPrice());
        holder.Quantity.setText("× " + customerPendingOrders.getOfferQuantity());
        holder.Totalprice.setText("Total: ₹ " + customerPendingOrders.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return customerPendingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Offername, Price, Quantity, Totalprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Offername = itemView.findViewById(R.id.Offerh);
            Price = itemView.findViewById(R.id.pricee);
            Quantity = itemView.findViewById(R.id.qtyy);
            Totalprice = itemView.findViewById(R.id.total);

        }
    }
}