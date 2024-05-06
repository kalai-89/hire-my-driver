package com.example.capstone.customerPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone.R;
import com.example.capstone.driverPanel.PayableOrders;

public class CustomerOrderFragment extends Fragment {

    TextView Payableorder1;
    TextView OrdertobePrepare, Preparedorders;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_customer_order,null);
        getActivity().setTitle("New Orders");
        Payableorder1 = (TextView) v.findViewById(R.id.payableorder);
        OrdertobePrepare=(TextView)v.findViewById(R.id.ordertobe);
        Preparedorders=(TextView)v.findViewById(R.id.prepareorder);

        OrdertobePrepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),CustomerOrderTobePrepared.class);
                startActivity(i);
            }
        });

        Preparedorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),CustomerPreparedOrder.class);
                startActivity(intent);
            }
        });
        Payableorder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PayableOrders.class);
                startActivity(i);
            }
        });


        return v;


    }
}