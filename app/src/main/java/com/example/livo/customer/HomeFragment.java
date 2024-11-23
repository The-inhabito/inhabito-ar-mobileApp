package com.example.livo.customer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.Database;
import com.example.livo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CompanyAdapter companyAdapter;
    private List<CompanyModel> companiesList = new ArrayList<>();

    // onCreateView or onViewCreated method
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout and set up RecyclerView
        View rootView = inflater.inflate(R.layout.fragment_customer_home, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewCompanies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        companyAdapter = new CompanyAdapter(companiesList);
        recyclerView.setAdapter(companyAdapter);

        // Fetch companies and update the RecyclerView
        fetchCompanyData();

        return rootView;
    }

    private void fetchCompanyData() {
        DatabaseReference companyDataRef = FirebaseDatabase.getInstance().getReference("companyData");

        // Use a listener to fetch all company data
        companyDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Loop through all entries in the 'companyData' table
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Get companyName and imageUrl from the current snapshot
                    String companyName = dataSnapshot.child("companyName").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                    if (companyName != null && imageUrl != null) {
                        // Create a new CompanyModel object
                        CompanyModel companyObject = new CompanyModel(companyName, imageUrl);

                        // Add the company to the list
                        companiesList.add(companyObject);
                    }
                }
                // Notify the adapter after all data has been added
                companyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching company data: " + error.getMessage());
            }
        });
    }

}
