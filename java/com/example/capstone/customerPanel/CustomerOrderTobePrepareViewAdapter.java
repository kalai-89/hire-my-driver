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

public class CustomerOrderTobePrepareViewAdapter extends RecyclerView.Adapter<CustomerOrderTobePrepareViewAdapter.ViewHolder> {

    private Context mcontext;
    private List<CustomerWaitingOrders> customerWaitingOrderslist;

    public CustomerOrderTobePrepareViewAdapter(Context context, List<CustomerWaitingOrders> customerWaitingOrderslist) {
        this.customerWaitingOrderslist = customerWaitingOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_tobeprepared_view, parent, false);
        return new CustomerOrderTobePrepareViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CustomerWaitingOrders customerWaitingOrders = customerWaitingOrderslist.get(position);
        holder.dishname.setText(customerWaitingOrders.getOfferName());
        holder.price.setText("Fee: ₹ " + customerWaitingOrders.getOfferPrice());
        holder.quantity.setText("× " + customerWaitingOrders.getOfferQuantity());
        holder.totalprice.setText("Total: ₹ " + customerWaitingOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return customerWaitingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Dname);
            price = itemView.findViewById(R.id.Dprice);
            totalprice = itemView.findViewById(R.id.Tprice);
            quantity = itemView.findViewById(R.id.Dqty);
        }
    }
}