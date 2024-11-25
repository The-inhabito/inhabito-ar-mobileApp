package com.example.livo.customer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.customer.Products.customerProductFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private EditText searchEditText;
    private List<CompanyModel> itemList = new ArrayList<>(); // Full company list
    private List<CompanyModel> filteredList = new ArrayList<>(); // Filtered data list
    private RecyclerView recyclerView;
    private CompanyAdapter companyAdapter;
    private List<CompanyModel> companiesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout and set up RecyclerView
        View rootView = inflater.inflate(R.layout.fragment_customer_home, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewCompanies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize adapter with click listener
        companyAdapter = new CompanyAdapter(filteredList, company -> {
            // Navigate to CustomerProductFragment when an item is clicked
            customerProductFragment customerProductFragment = new customerProductFragment();
            Bundle args = new Bundle();
            args.putString("company_name", company.getCompanyName());
            args.putString("company_image", company.getImageUrl());
            customerProductFragment.setArguments(args);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, customerProductFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        recyclerView.setAdapter(companyAdapter);

        // Initialize the search EditText
        searchEditText = rootView.findViewById(R.id.search_cat);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("HomeFragment", "Search text: " + s.toString());
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });

        // Fetch companies and update the RecyclerView
        fetchCompanyData();

        return rootView;
    }

    private void filter(String text) {
        filteredList.clear(); // Clear the filtered list
        if (text.isEmpty()) {
            filteredList.addAll(itemList); // Show all items when search text is empty
        } else {
            for (CompanyModel item : itemList) {
                if (item.getCompanyName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        Log.d("HomeFragment", "Filtered list size: " + filteredList.size());
        companyAdapter.notifyDataSetChanged(); // Notify the adapter of changes
    }


    private void fetchCompanyData() {
        DatabaseReference companyDataRef = FirebaseDatabase.getInstance().getReference("companyData");

        companyDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String companyName = dataSnapshot.child("companyName").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String companyEmail = dataSnapshot.child("companyEmail").getValue(String.class);

                    if (companyName != null && imageUrl != null) {
                        CompanyModel companyObject = new CompanyModel(companyName, imageUrl, companyEmail);
                        itemList.add(companyObject);
                    }
                }
                filteredList.clear();
                filteredList.addAll(itemList);
                companyAdapter.notifyDataSetChanged();

                Log.d("HomeFragment", "Fetched " + itemList.size() + " companies");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching company data: " + error.getMessage());
            }
        });
    }


}
