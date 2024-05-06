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

public class CustomerPreparedOrderViewAdapter extends RecyclerView.Adapter<CustomerPreparedOrderViewAdapter.ViewHolder> {

    private Context mcontext;
    private List<CustomerFinalOrders> customerFinalOrderslist;

    public CustomerPreparedOrderViewAdapter(Context context, List<CustomerFinalOrders> customerFinalOrderslist) {
        this.customerFinalOrderslist = customerFinalOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_preparedorderview, parent, false);
        return new CustomerPreparedOrderViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CustomerFinalOrders customerFinalOrders=customerFinalOrderslist.get(position);
        holder.dishname.setText(customerFinalOrders.getOfferName());
        holder.price.setText("Offer Fee : ₹ " + customerFinalOrders.getOfferPrice());
        holder.quantity.setText("× " + customerFinalOrders.getOfferQuantity());
        holder.totalprice.setText("Total: ₹ " + customerFinalOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return customerFinalOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.Cdishname);
            price = itemView.findViewById(R.id.Cdishprice);
            totalprice = itemView.findViewById(R.id.Ctotalprice);
            quantity = itemView.findViewById(R.id.Cdishqty);
        }
    }
}