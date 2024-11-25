package com.example.livo.customer.Products;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.livo.R;
import com.example.livo.customer.Home;

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
            } else {
                Intent intent = new Intent(getActivity(), paymentActivity.class);
                intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cart.getItems())); // Pass cart items
                intent.putExtra("totalAmount", cart.calculateTotalAmount());
                startActivity(intent);
            }
        });

        updateCart();
        return view;
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
