package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.capstone.customerPanel.CustomerHomeFragment;
import com.example.capstone.customerPanel.CustomerOrderFragment;
import com.example.capstone.customerPanel.CustomerPendingOrderFragment;
import com.example.capstone.customerPanel.CustomerProfileFragment;

public class customer_panel_bottom_navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.customer_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.customerHome:
                fragment=new CustomerHomeFragment();
                break;
            case R.id.PendingOrders:
                fragment=new CustomerPendingOrderFragment();
                break;
            case R.id.Orders:
                fragment=new CustomerOrderFragment();
                break;
            case R.id.customerProfile:
                fragment=new CustomerProfileFragment();
                break;
        }
        return loadcustomerfragment(fragment);
    }

    private boolean loadcustomerfragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}