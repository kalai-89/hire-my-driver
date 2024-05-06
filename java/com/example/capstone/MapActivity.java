package com.example.capstone;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import androidx.appcompat.app.AppCompatActivity;
import com.example.capstone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Button btnPinLocation, back;
    private Marker marker;
    private static final String TAG = "MyFragmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        back = findViewById(R.id.button3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapview  = new Intent(MapActivity.this,driver_panel_bottom_navigation.class);
                startActivity(mapview);
                finish();
            }
        });
        btnPinLocation = findViewById(R.id.btnPinLocation);
        btnPinLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinLocation();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Clear existing marker
                if (marker != null) {
                    marker.remove();
                }

                // Add new marker at clicked location
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Pinned Location");
                marker = googleMap.addMarker(markerOptions);
            }
        });
    }

    private void pinLocation() {
        if (marker != null) {
            double latitude = marker.getPosition().latitude;
            double longitude = marker.getPosition().longitude;
            String address = getAddressFromLatLng(latitude, longitude); // Implement your logic to get address from latlng
            // Save the address to Firebase database or perform other actions

            Toast.makeText(this, "Address: " + address, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No location pinned", Toast.LENGTH_SHORT).show();
        }
    }

    private String getAddressFromLatLng(double latitude, double longitude) {
        // Create a Geocoder instance
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            // Get the list of addresses from latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                // Extract the address details
                String addressLine = address.getAddressLine(0); // Get the first address line
                String city = address.getLocality(); // Get the city
                String state = address.getAdminArea(); // Get the state
                String Suburban = address.getSubLocality(); // Get the country
                String postalCode = address.getPostalCode(); // Get the postal code
                String country = address.getCountryName();

                StringBuilder sb = new StringBuilder();
                sb.append(addressLine);
                sb.append(", ");
                sb.append(city);
                sb.append(", ");
                sb.append(state);
                sb.append(", ");
                sb.append(postalCode);
                sb.append(", ");
                sb.append(country);

                String addressString = sb.toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference driverRef = database.getReference("Driver");

                Map<String, Object> updates = new HashMap<>();

                updates.put("City", address.getLocality());
                updates.put("State", address.getAdminArea());
                updates.put("LocalAddress", address.getAddressLine(0) );
                updates.put("Suburban",  address.getSubLocality());

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();




                driverRef.child(uid).updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.e(TAG, "Update failed", databaseError.toException());
                        } else {
                            Log.d(TAG, "Update successful");
                        }
                    }
                });

            return addressString;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
