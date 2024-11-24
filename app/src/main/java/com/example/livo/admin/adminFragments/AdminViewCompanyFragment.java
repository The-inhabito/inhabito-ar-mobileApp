package com.example.livo.admin.adminFragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.livo.R;
import com.example.livo.admin.CompanyAdapter;
import com.example.livo.admin.RecyclerViewInterface;
import com.example.livo.customer.CompanyModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminViewCompanyFragment extends Fragment implements RecyclerViewInterface {

    private List<CompanyModel> itemList = new ArrayList<>(); // Full company list
    private RecyclerView recyclerView;
    private CompanyAdapter companyAdapter;
    private List<CompanyModel> companiesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_view_company, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewCompanies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize the adapter, passing this fragment as the click listener
        companyAdapter = new CompanyAdapter(companiesList, getContext(), this);
        recyclerView.setAdapter(companyAdapter);

        fetchCompanyData();

        return rootView;
    }

    private void fetchCompanyData() {
        DatabaseReference companyDataRef = FirebaseDatabase.getInstance().getReference("companyData");

        companyDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear(); // Clear any existing data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String companyName = dataSnapshot.child("companyName").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String companyEmail = dataSnapshot.child("companyEmail").getValue(String.class);

                    if (companyName != null && imageUrl != null) {
                        CompanyModel companyObject = new CompanyModel(companyName, imageUrl, companyEmail);
                        itemList.add(companyObject); // Add to full list
                    }
                }
                companiesList.clear();
                companiesList.addAll(itemList); // Update the companiesList with the new data
                companyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching company data: " + error.getMessage());
            }
        });
    }
    @Override
    public void viewProductData(int position) {
        // Get the company data for the selected position
        CompanyModel selectedCompany = companiesList.get(position);

        // Log company details for debugging
        Log.d("ViewCompany", "Selected Company: " + selectedCompany.getCompanyName());

        // Use an Intent to pass company data for viewing products or managing the company
        Intent viewIntent = new Intent(getContext(), AdminCompanyDetails.class);
        viewIntent.putExtra("companyName", selectedCompany.getCompanyName());
        viewIntent.putExtra("companyEmail", selectedCompany.getCompanyEmail());
        viewIntent.putExtra("companyImageUrl", selectedCompany.getImageUrl());

        startActivity(viewIntent); // Start the new activity to view company products
    }
}
