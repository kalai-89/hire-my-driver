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

public class CustomerPreparedOrderAdapter extends RecyclerView.Adapter<CustomerPreparedOrderAdapter.ViewHolder> {

    private Context context;
    private List<CustomerFinalOrders1> customerFinalOrders1list;

    public CustomerPreparedOrderAdapter(Context context, List<CustomerFinalOrders1> customerFinalOrders1list) {
        this.customerFinalOrders1list = customerFinalOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_preparedorder, parent, false);
        return new CustomerPreparedOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CustomerFinalOrders1 customerFinalOrders1 = customerFinalOrders1list.get(position);
        holder.Address.setText(customerFinalOrders1.getAddress());
        holder.grandtotalprice.setText(" Total: ₹ " + customerFinalOrders1.getGrandTotalPrice());
        final String random = customerFinalOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerPreparedOrderView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
                ((CustomerPreparedOrder) context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerFinalOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.customer_address);
            grandtotalprice = itemView.findViewById(R.id.customer_totalprice);
            Vieworder = itemView.findViewById(R.id.View);
        }
    }
}