package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOne extends AppCompatActivity {

    Button Driver, Customer;
    Intent intent;
    String type;
    ConstraintLayout bgimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);



        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();


        Driver = (Button)findViewById(R.id.driver);
        Customer = (Button)findViewById(R.id.customer);




        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){


                    Intent gpsIntent = new Intent(ChooseOne.this, Customerlogin.class);
                    startActivity(gpsIntent);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphonecust  = new Intent(ChooseOne.this,Customerloginphone.class);
                    startActivity(loginphonecust);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Registercust  = new Intent(ChooseOne.this,customerregistration.class);
                    startActivity(Registercust);
                }

            }
        });

        Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("Email")){
                    Intent loginemailcust  = new Intent(ChooseOne.this,Login.class);
                    startActivity(loginemailcust);
                    finish();
                }
                if(type.equals("Phone")){
                    Intent loginphonecust  = new Intent(ChooseOne.this,Loginphone.class);
                    startActivity(loginphonecust);
                    finish();
                }
                if(type.equals("SignUp")){
                    Intent Registercust  = new Intent(ChooseOne.this,Registration.class);
                    startActivity(Registercust);
                }

            }
        });

    }
}