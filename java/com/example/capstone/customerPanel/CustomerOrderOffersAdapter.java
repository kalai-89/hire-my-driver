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

public class CustomerOrderOffersAdapter extends RecyclerView.Adapter<CustomerOrderOffersAdapter.ViewHolder> {


    private Context mcontext;
    private List<CustomerPendingOrders> customerPendingOrderslist;

    public CustomerOrderOffersAdapter(Context context, List<CustomerPendingOrders> customerPendingOrderslist) {
        this.customerPendingOrderslist = customerPendingOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_order_offers, parent, false);
        return new CustomerOrderOffersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CustomerPendingOrders customerPendingOrders = customerPendingOrderslist.get(position);
        holder.dishname.setText(customerPendingOrders.getOfferName());
        holder.price.setText("Offer Fee : ₹ " + customerPendingOrders.getOfferPrice());
        holder.quantity.setText("× " + customerPendingOrders.getOfferQuantity());
        holder.totalprice.setText("Total: ₹ " + customerPendingOrders.getTotalPrice());


    }

    @Override
    public int getItemCount() {
        return customerPendingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.DN);
            price = itemView.findViewById(R.id.PR);
            totalprice = itemView.findViewById(R.id.TR);
            quantity = itemView.findViewById(R.id.QY);
        }
    }
}