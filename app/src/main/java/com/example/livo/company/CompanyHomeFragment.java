package com.example.livo.company;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.livo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyHomeFragment extends Fragment implements RecyclerViewInterface {
    private DatabaseReference databaseReference;
    private ProductAdapter adapter;
    RecyclerView recyclerView;
    List<CompanyProductsModel> productList;
    ImageButton editButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_home, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        productList = new ArrayList<>();

        // Initialize adapter with context and recyclerViewInterface
        adapter = new ProductAdapter(productList, getContext(), this);
        recyclerView.setAdapter(adapter);
        // Retrieve the email from the session to serve as companyID
        sessionClass session = sessionClass.getInstance(getActivity());
        String Companyemail = session.getEmail();

        // Fetch products from Firebase
        fetchProductsFromFirebase(Companyemail);

        return view;

    }



    private void fetchProductsFromFirebase(String email) {
        databaseReference.orderByChild("companyID").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();  // Clear the old data
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    CompanyProductsModel product = postSnapshot.getValue(CompanyProductsModel.class);
                    productList.add(product);  // Add the new data
                }
                adapter.notifyDataSetChanged();  // Notify adapter to refresh the list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error here
            }
        });
    }

    @Override
    public void onItemClicked(int position) {


    }
    public void editProductData(int position) {
        Intent editIntent = new Intent(getContext(), CompanyEditProductDetails.class);
        CompanyProductsModel selectedProduct = productList.get(position);
        sessionClass session = sessionClass.getInstance(getActivity());
        String Companyemail = session.getEmail();
        // Get the product ID from the selected product
        String productID = selectedProduct.getProductID();



        // Log the product ID for debugging
        Log.d("EditProduct", "Selected Product ID: " + productID);

        editIntent.putExtra("productData", selectedProduct);  // Pass product data to the edit screen
        editIntent.putExtra("productID", productID);
        editIntent.putExtra("companyID",Companyemail );

        startActivity(editIntent);
    }


}
