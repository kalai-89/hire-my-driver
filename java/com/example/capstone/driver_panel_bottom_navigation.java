package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.capstone.driverPanel.DriverCartFragment;
import com.example.capstone.driverPanel.DriverHomeFragment;
import com.example.capstone.driverPanel.DriverOrderFragment;
import com.example.capstone.driverPanel.DriverProfileFragment;
import com.example.capstone.driverPanel.DriverTrackFragment;
import com.example.capstone.MapActivity;
import com.example.capstone.R;
import com.example.capstone.SendNotification.Token;

import com.example.capstone.driverPanel.DriverCartFragment;
import com.example.capstone.driverPanel.DriverHomeFragment;
import com.example.capstone.driverPanel.DriverOrderFragment;
import com.example.capstone.driverPanel.DriverProfileFragment;
import com.example.capstone.driverPanel.DriverTrackFragment;
import com.example.capstone.SendNotification.Token;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.iid.FirebaseInstanceId;

public class driver_panel_bottom_navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_panel_bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        UpdateToken();
        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (name != null) {
            if (name.equalsIgnoreCase("Homepage")) {
                loadFragment(new DriverHomeFragment());
            } else if (name.equalsIgnoreCase("Preparingpage")) {
                loadFragment(new DriverTrackFragment());
            } else if (name.equalsIgnoreCase("Preparedpage")) {
                loadFragment(new DriverTrackFragment());
            } else if (name.equalsIgnoreCase("DeliverOrderpage")) {
                loadFragment(new DriverTrackFragment());
            } else if (name.equalsIgnoreCase("ThankYoupage")) {
                loadFragment(new DriverHomeFragment());
            }
        } else {
            loadFragment(new DriverHomeFragment());
        }
    }

    private void UpdateToken() {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isComplete()){
                    String token = task.getResult();
                    FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);



                }
            }
        });
//        String refreshToken = FirebaseInstanceId.getInstance().getToken();
//        Token token = new Token(refreshToken);
//        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.Home:
                fragment = new DriverHomeFragment();
                break;


            case R.id.Cart:
                fragment = new DriverCartFragment();
                break;

            case R.id.Order:
                fragment = new DriverOrderFragment();
                break;



            case R.id.Profile:
                fragment = new DriverProfileFragment();
                break;

        }
        return loadFragment(fragment);
    }
}