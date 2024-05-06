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

public class DriverTrackAdapter extends RecyclerView.Adapter<DriverTrackAdapter.ViewHolder> {

    private Context context;
    private List<DriverFinalOrders> driverFinalOrderslist;

    public DriverTrackAdapter(Context context, List<DriverFinalOrders> driverFinalOrderslist) {
        this.driverFinalOrderslist = driverFinalOrderslist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.driver_trackorder, parent, false);
        return new DriverTrackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DriverFinalOrders driverFinalOrders = driverFinalOrderslist.get(position);
        holder.Offername.setText(driverFinalOrders.getOfferName());
        holder.Quantity.setText(driverFinalOrders.getOfferQuantity() + "× ");
        holder.Totalprice.setText("₹ " + driverFinalOrders.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return driverFinalOrderslist.size();
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