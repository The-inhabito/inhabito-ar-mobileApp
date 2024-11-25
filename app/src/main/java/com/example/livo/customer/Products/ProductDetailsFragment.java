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

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.livo.R;

public class ProductDetailsFragment extends Fragment {
    private String productName, price, description, imageUrl, quantity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productName = getArguments().getString("productName");
            price = getArguments().getString("price");
            description = getArguments().getString("description");
            imageUrl = getArguments().getString("imageUrl");
            quantity = getArguments().getString("quantity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Set product details
        TextView productNameTextView = view.findViewById(R.id.productNameTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView quantityTextView = view.findViewById(R.id.quantityTextView);
        ImageView productImageView = view.findViewById(R.id.productImageView);
        Button threeD = view.findViewById(R.id.threeD);

        productNameTextView.setText(productName);
        priceTextView.setText(String.format("Price: Rs%s", price));
        descriptionTextView.setText(description);
        quantityTextView.setText(String.format("Quantity: %s", quantity));

        // Load image using a library like Glide or Picasso
        Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.baseline_image_24)
                .into(productImageView);

        return view;
    }
}
