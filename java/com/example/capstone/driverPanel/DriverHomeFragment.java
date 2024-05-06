package com.example.capstone.driverPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.capstone.Driver;
import com.example.capstone.UpdateModel;

import com.example.capstone.Customer;

import com.example.capstone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriverHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    private List<UpdateModel> updateModelList;
    private DriverHomeAdapter adapter;
    String State, City, Sub, userid;
    DatabaseReference dataaa, databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driverhome, null);
        getActivity().setTitle("Hire My Driver");
        setHasOptionsMenu(true);
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateModelList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                     userid = user.getUid();
                }
                dataaa = FirebaseDatabase.getInstance().getReference("Driver").child(userid);
                dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Driver cust = dataSnapshot.getValue(Driver.class);
                        State  = cust.getState();
                        City = cust.getCity();
                        Sub = cust.getSuburban();
                        customermenu();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;
    }


    @Override
    public void onRefresh() {

        customermenu();
    }

    private void customermenu() {

        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("OfferDetails").child(State).child(City).child(Sub);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateModel updateModel = snapshot1.getValue(UpdateModel.class);
                        updateModelList.add(updateModel);
                    }
                }
                adapter = new DriverHomeAdapter(getContext(), updateModelList);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

    }

    private void search(final String searchtext) {

        ArrayList<UpdateModel> mylist = new ArrayList<>();
        for (UpdateModel object : updateModelList) {
            if (object.getOffers().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }
        adapter = new DriverHomeAdapter(getContext(), mylist);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.Searchdish);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");
    }
}