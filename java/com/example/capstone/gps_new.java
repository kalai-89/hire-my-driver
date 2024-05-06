package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gps_new extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;
    Switch sw_locationupdates, sw_gps;

    Intent intent;

    boolean updateOn = false;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallBack;



    String state, city, sub;
    private static final String TAG = "MyFragmentActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dataaa, myRef = database.getReference("Customer");

    private static final int REQUEST_LOCATION_PERMISSION = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_new);

        Button driver12, maps;

         tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates=findViewById(R.id.sw_locationsupdates);

        intent = getIntent();

        driver12 = findViewById(R.id.button5);
        driver12.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent loginphonecust  = new Intent(gps_new.this,driver_panel_bottom_navigation.class);
                                            startActivity(loginphonecust);
                                            finish();
                                        }

                                    });

        maps = findViewById(R.id.button6);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapview  = new Intent(gps_new.this,MapActivity.class);
                startActivity(mapview);
                finish();
            }

        });
                locationRequest = new LocationRequest();
        locationRequest.setInterval(1000*30);
        locationRequest.setFastestInterval(1000*5);
        locationRequest.setPriority((LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference driverRef = database.getReference("Driver");
        // Create a new Driver object with updated information
        Driver driver = new Driver();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = database.getInstance().getReference("Driver").child(userid);







        locationCallBack = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };
        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(sw_gps.isChecked()){
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                tv_sensor.setText("Using GPS Sensors");
            }
            else{
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                tv_sensor.setText("Using towers + GPS");
            }
            }
        });

        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_locationupdates.isChecked()){
                    startLocationUpdates();
                }
                else{
                    stopLocationUpdates();
                }
            }
        });

        updateGPS();
    }// end onCreate method

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            tv_updates.setText("Location is being tracked");
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
            updateGPS();
        } else {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }



    private void stopLocationUpdates() {

        tv_lat.setText("Location is NOT tracked");
        tv_lon.setText("Location is NOT tracked");
        tv_altitude.setText("Location is NOT tracked");
        tv_accuracy.setText("Location is NOT tracked");
        tv_speed.setText("Location is NOT tracked");
        tv_sensor.setText("Location is NOT tracked");
        tv_updates.setText("Location is NOT tracked");
        tv_address.setText("Location is NOT tracked");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else {
                    Toast.makeText(this, "This app required permission to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                }
        }



    private void updateGPS(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(gps_new.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                updateUIValues(location);
                }
            });
        } else{
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
        }
    }
}

    private void updateUIValues(Location location) {



        // Update the driver's information in the Firebase database


        if (location != null) {
            tv_lat.setText(String.valueOf(location.getLatitude()));
            tv_lon.setText(String.valueOf(location.getLongitude()));
            tv_accuracy.setText(String.valueOf(location.getAccuracy()));

            if (location.hasAltitude()) {
                tv_altitude.setText((String.valueOf(location.getAltitude())));
            } else {
                tv_altitude.setText("Not Available");
            }
            if (location.hasSpeed()) {
                tv_speed.setText(String.valueOf(location.getSpeed()));
            } else {
                tv_speed.setText("Not Available");
            }


        }

        try {
            Geocoder geocoder = new Geocoder(gps_new.this);
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference driverRef = database.getReference("Driver");

            String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Create a new Driver object with updated information
            Driver driver = new Driver();




// Update the driver's information in the Firebase database

            Map<String, Object> updates = new HashMap<>();
            updates.put("Suburban", addresses.get(0).getSubLocality());
            updates.put("City", addresses.get(0).getLocality());
            updates.put("State", addresses.get(0).getSubAdminArea());
            updates.put("LocalAddress", addresses.get(0).getAddressLine(0));


            driverRef.child(driverId).updateChildren(updates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.e(TAG, "Update failed", databaseError.toException());
                    } else {
                        Log.d(TAG, "Update successful");
                    }
                }
            });
        } catch(Exception e) {
            tv_address.setText("Unable to get street address");
        }


    }



}