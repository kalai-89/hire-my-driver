package com.example.capstone.customerPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import java.util.List;

public class CustomerOrderTobePreparedAdapter extends RecyclerView.Adapter<CustomerOrderTobePreparedAdapter.ViewHolder> {

    private Context context;
    private List<CustomerWaitingOrders1> customerWaitingOrders1list;

    public CustomerOrderTobePreparedAdapter(Context context, List<CustomerWaitingOrders1> customerWaitingOrders1list) {
        this.customerWaitingOrders1list = customerWaitingOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_tobeprepared, parent, false);
        return new CustomerOrderTobePreparedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CustomerWaitingOrders1 customerWaitingOrders1 = customerWaitingOrders1list.get(position);
        holder.Address.setText(customerWaitingOrders1.getAddress());
        holder.grandtotalprice.setText("Total: ₹ " + customerWaitingOrders1.getGrandTotalPrice());
        final String random = customerWaitingOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerOrderTobePrepareView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
                ((CustomerOrderTobePrepared) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerWaitingOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.cust_address);
            grandtotalprice = itemView.findViewById(R.id.Grandtotalprice);
            Vieworder = itemView.findViewById(R.id.View_order);
        }
    }
}