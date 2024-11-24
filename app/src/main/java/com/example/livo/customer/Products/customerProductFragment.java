package com.example.livo.customer.Products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.R;
import com.example.livo.customer.CompanyModel;
import com.example.livo.customer.ProductModel;
import com.example.livo.customer.RecyclerViewInterfaceCus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class customerProductFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductModel> productList;
    private String companyName;
    CompanyModel companyModel;
    private String productName, price, imageUrl, status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            companyName = getArguments().getString("company_name");
        } else {
            companyName = ""; // Default to avoid null values
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_product, container, false);

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productList = new ArrayList<>();

        // Initialize RecyclerViewInterface
        RecyclerViewInterfaceCus recyclerViewInterface = position -> {
            ProductModel selectedProduct = productList.get(position);
            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();

            Bundle args = new Bundle();
            args.putString("productName", selectedProduct.getName());
            args.putString("price", selectedProduct.getPrice());
            args.putString("status", selectedProduct.getStatus());
            args.putString("imageUrl", selectedProduct.getImageUrl());
            args.putString("quantity", selectedProduct.getQuantity());
            args.putString("description", selectedProduct.getDescription());

            productDetailsFragment.setArguments(args);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, productDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };

        // Initialize ProductAdapter
        productAdapter = new ProductAdapter(productList, getContext(), recyclerViewInterface);
        recyclerView.setAdapter(productAdapter);

        // Display the company name
        TextView companyNameTextView = view.findViewById(R.id.companyNameTextView);
        companyNameTextView.setText(companyName);

        // Load products from Firebase after the company name is set
        if (!companyName.isEmpty()) {
            loadProductsForCompany();
        }

        return view;
    }


    private void loadProductsForCompany() {
        // Ensure the companyName is not null or empty
        if (companyName == null || companyName.isEmpty()) {
            Toast.makeText(getContext(), "Company Name is not set", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference the companyData table to fetch companyEmail (companyID) based on companyName
        DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference("companyData");
        companyRef.orderByChild("companyName").equalTo(companyName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the company exists in the companyData table
                if (dataSnapshot.exists()) {
                    String companyEmail = null;

                    // Fetch the first matching companyEmail (assuming unique company names)
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        companyEmail = snapshot.child("companyEmail").getValue(String.class);
                        break;
                    }

                    if (companyEmail != null) {
                        // Now fetch products for the company based on companyEmail
                        fetchProducts(companyEmail);
                    } else {
                        Toast.makeText(getContext(), "Company Email not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Company not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error fetching company data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProducts(String companyEmail) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("products");
        productRef.orderByChild("companyID").equalTo(companyEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear(); // Clear any existing data
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        ProductModel product = productSnapshot.getValue(ProductModel.class);
                        if (product != null) {
                            productList.add(product);
                        }
                    }
                    productAdapter.notifyDataSetChanged(); // Notify adapter after all data is added
                    Log.d("customerProductFragment", "Products loaded: " + productList.size());
                } else {
                    Toast.makeText(getContext(), "No products found for this company.", Toast.LENGTH_SHORT).show();
                    Log.d("customerProductFragment", "No products found for companyID: " + companyEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load products: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("customerProductFragment", "Firebase error: " + databaseError.getMessage());
            }
        });
    }



//    public void onAddToCartClick(int pos) {
//        // Get the menu item (product) from the filtered list at the position
//        ProductModel product = filteredList.get(pos); // Assuming filteredList contains the list of products
//
//        // Add the item to the cart
//        Cart.addItem(product); // Assuming cart is an object that holds the items
//
//        // Show a confirmation toast
//        Toast.makeText(getContext(), "Added to cart successfully", Toast.LENGTH_SHORT).show();
//
//        // Notify the CartFragment of the change if it's visible
//        Home activity = (Home) getActivity();  // Ensure the activity is of type Home
//        CartFragment cartFragment = (CartFragment) activity.getSupportFragmentManager().findFragmentById(R.id.cart);
//
//        // Check if the CartFragment is visible
//        if (cartFragment != null && cartFragment.isVisible()) {
//            cartFragment.updateCart();  // Update the cart in the fragment
//        }
//    }



}
