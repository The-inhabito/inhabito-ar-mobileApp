package com.example.livo.customer.Products;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.customer.CustomerSession;
import com.example.livo.customer.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private Cart cart;
    private Button payButton;
    private TextView totalAmountTextView;
    private ImageView productImageView;
    private TextView emptyCartMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        Home activity = (Home) getActivity();
        cart = activity.getCart();  // Get the shared cart instance

        recyclerView = view.findViewById(R.id.recycler_view_cart);
        totalAmountTextView = view.findViewById(R.id.total_amount);
        emptyCartMessage = view.findViewById(R.id.empty_cart_message);
        payButton = view.findViewById(R.id.pay_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(cart.getItems(), totalAmountTextView);
        recyclerView.setAdapter(cartAdapter);

        payButton.setOnClickListener(view1 -> {
            if (cart.getItems().isEmpty()) {
                Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve session email
            CustomerSession session = CustomerSession.getInstance(getContext());
            String email = session.getEmail();

            if (email == null) {
                Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get user ID using email
            Database dbHelper = new Database(getContext());
            int userId = dbHelper.getUserIdByEmail(email);

            if (userId == -1) {
                Toast.makeText(getContext(), "User ID not found for email: " + email, Toast.LENGTH_SHORT).show();
                return;
            }

            // Save order
            double totalAmount = cart.calculateTotal();
            String orderDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

            long orderId = dbHelper.saveOrder(userId, totalAmount, orderDate, null); // Pass userId here

            if (orderId == -1) {
                Toast.makeText(getContext(), "Failed to create order!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save order items
            for (CartItem item : cart.getItems()) {
                fetchCompanyEmail(item.getProductName(), new OnCompanyEmailFetchedListener() {
                    @Override
                    public void onSuccess(String companyEmail) {
                        dbHelper.updateOrderWithCompanyEmail(orderId, companyEmail);
                        dbHelper.saveOrderItem(orderId, item);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
            cart.clear();
            updateCart();
        });
        return view;
    }
    private void fetchCompanyEmail(String productName, OnCompanyEmailFetchedListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.orderByChild("name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String companyId = snapshot.child("companyID").getValue(String.class);
                        if (companyId != null) {
                            Log.d("CartFragment", "CompanyId: " + companyId);
                            listener.onSuccess(companyId);

                            break;
                        } else {
                            listener.onError("Company ID not found");
                        }



                    }
                } else {
                    listener.onError("Product not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

//    private void fetchCompanyEmailById(String companyId, OnCompanyEmailFetchedListener listener) {
//        DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference("companies").child(companyId);
//        companyRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String companyEmail = snapshot.child("email").getValue(String.class);
//                    listener.onSuccess(companyEmail);
//                } else {
//                    listener.onError("Company not found");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                listener.onError(error.getMessage());
//            }
//        });
//    }

    // Interface to handle asynchronous Firebase calls
    interface OnCompanyEmailFetchedListener {
        void onSuccess(String companyEmail);
        void onError(String errorMessage);
    }

    public void updateCart() {
        if (cartAdapter != null) {
            cartAdapter.updateCartItems(cart.getItems());
        }

        if (cart.getItems().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            totalAmountTextView.setText("Total: Rs 0");
            payButton.setEnabled(false);
            emptyCartMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartMessage.setVisibility(View.GONE);
            payButton.setEnabled(true);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updateCart();
    }
}
