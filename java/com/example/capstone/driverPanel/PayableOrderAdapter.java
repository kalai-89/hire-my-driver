package com.example.capstone.driverPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.driverPanel.DriverPaymentOrders;

import java.util.List;

public class PayableOrderAdapter extends RecyclerView.Adapter<PayableOrderAdapter.ViewHolder> {

    private Context context;
    private List<DriverPaymentOrders> driverPaymentOrderslist;

    public PayableOrderAdapter(Context context, List<DriverPaymentOrders> driverPendingOrderslist) {
        this.driverPaymentOrderslist = driverPendingOrderslist;
        this.context = context;
    }

    @NonNull
    @Override
    public PayableOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.driver_payableorder, parent, false);
        return new PayableOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayableOrderAdapter.ViewHolder holder, int position) {

        final DriverPaymentOrders driverPaymentOrders = driverPaymentOrderslist.get(position);
        holder.Offername.setText(driverPaymentOrders.getOfferName());
        holder.Price.setText("Fee: ₹ " + driverPaymentOrders.getOfferPrice());
        holder.Quantity.setText("× " + driverPaymentOrders.getOfferQuantity());
        holder.Totalprice.setText("Total: ₹ " + driverPaymentOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return driverPaymentOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Offername, Price, Quantity, Totalprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Offername = itemView.findViewById(R.id.dish);
            Price = itemView.findViewById(R.id.pri);
            Quantity = itemView.findViewById(R.id.qt);
            Totalprice = itemView.findViewById(R.id.Tot);
        }
    }
}