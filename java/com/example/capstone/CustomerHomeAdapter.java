package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.capstone.customerPanel.UpdateDelete;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcont;
    private List<UpdateModel> updateModelList;


    public CustomerHomeAdapter(Context context , List<UpdateModel>updateModelList)
    {
        this.updateModelList = updateModelList;
        this.mcont = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.customer_update_delete,parent,false);
        return new CustomerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {

        final UpdateModel updateModel = updateModelList.get(position);

        holder.type.setText(updateModel.getDescription());

        updateModel.getRandomUID();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcont, UpdateDelete.class);
                intent.putExtra("updatedeleteoffer",updateModel.getRandomUID());
                mcont.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("CustomerHomeAdapter", "getItemCount: " + updateModelList.size());
        return updateModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.offer_name);
        }
    }


}
