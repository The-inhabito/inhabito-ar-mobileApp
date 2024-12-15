package com.example.livo.admin.adminFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.R;
import com.example.livo.company.CompanyDataModel;
import com.example.livo.company.RecyclerViewInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminNotificationFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private JoinRequestAdapter adapter;

    private final List<CompanyDataModel> pendingCompanies = new ArrayList<>();
    private DatabaseReference companyRef, companyDataRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        companyRef = FirebaseDatabase.getInstance().getReference("companies");
        companyDataRef = FirebaseDatabase.getInstance().getReference("companyData");

        adapter = new JoinRequestAdapter(getContext(), pendingCompanies, this);
        recyclerView.setAdapter(adapter);

        fetchCompaniesByStatus("pending");

        return view;
    }

    private void fetchCompaniesByStatus(String status) {
        // Query to fetch companies with a specific status (approved or pending)
        companyRef.orderByChild("status").equalTo(status).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear previous data
                pendingCompanies.clear();

                // Loop through each company and fetch their details
                for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                    String companyKey = companySnapshot.getKey(); // Get the company key

                    // Fetch the email from the company node using the companyKey
                    String companyEmail = companySnapshot.child("email").getValue(String.class);

                    // Now pass the email to fetchCompanyDetails method
                    if (companyEmail != null) {
                        fetchCompanyDetails(companyEmail); // Fetch company details using email
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error cases
            }
        });
    }

    private void fetchCompanyDetails(String companyEmail) {
        // Fetch company details from the companyData node using the companyEmail
        companyDataRef.orderByChild("companyEmail").equalTo(companyEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Loop through each company data and retrieve the details
                for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                    // Retrieve company details from snapshot
                    CompanyDataModel company = companySnapshot.getValue(CompanyDataModel.class);

                    if (company != null) {
                        // Add the company data to the list for display
                        pendingCompanies.add(company);
                        adapter.notifyDataSetChanged(); // Notify the adapter to update the view
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error cases
            }
        });
    }


    @Override
    public void onItemClicked(int position) {
        String companyEmail = pendingCompanies.get(position).getCompanyEmail();

        companyRef.orderByChild("email").equalTo(companyEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                    String companyKey = companySnapshot.getKey();
                    companyRef.child(companyKey).child("status").setValue("accepted");

                    pendingCompanies.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminNotification", "Error updating status: " + error.getMessage());
            }
        });
    }

    @Override
    public void editProductData(int position) {
        String companyEmail = pendingCompanies.get(position).getCompanyEmail();

        companyRef.orderByChild("email").equalTo(companyEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                    String companyKey = companySnapshot.getKey();
                    companyRef.child(companyKey).child("status").setValue("rejected");

                    pendingCompanies.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminNotification", "Error updating status: " + error.getMessage());
            }
        });
    }

}
