package com.example.capstone.driverPanel;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.example.capstone.UpdateModel;
import com.example.capstone.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class DriverHomeAdapter extends RecyclerView.Adapter<DriverHomeAdapter.ViewHolder> {


    private Context mcontext;
    private List<UpdateModel>updateModellist;
    DatabaseReference databaseReference;

    public DriverHomeAdapter(Context context,List<UpdateModel>updateModellist)
    {
        this.updateModellist=updateModellist;
        this.mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.driver_menu,parent,false);
        return new DriverHomeAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final DriverHomeAdapter.ViewHolder holder, int position) {


        final UpdateModel updateModel=updateModellist.get(position);
        Glide.with(mcontext).load(updateModel.getImageURL()).into(holder.imageView);



        updateModel.getRandomUID();
        updateModel.getCustomerId();
        holder.price.setText("Fee: ₹ " + updateModel.getPrice());
        holder.Typename.setText("Required License: "+updateModel.getOffers());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mcontext,OrderOffer.class);
                intent.putExtra("OfferMenu",updateModel.getRandomUID());
                intent.putExtra("CustomerId",updateModel.getCustomerId());


                mcontext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return updateModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView Offername,price,Typename;
        ElegantNumberButton additem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.menu_image);
            Offername=itemView.findViewById(R.id.offer_name);
            price=itemView.findViewById(R.id.dishprice);
            additem=itemView.findViewById(R.id.number_btn);
            Typename=itemView.findViewById(R.id.typename);

        }
    }
}