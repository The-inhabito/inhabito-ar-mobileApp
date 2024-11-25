package com.example.livo.customer.Products;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.livo.R;
import com.example.livo.company.CompanyProductsModel;
import com.example.livo.company.viewModel.ViewModelActivity;
import com.example.livo.customer.RecyclerViewInterfaceCus;
import com.example.livo.customer.ProductModel;


import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public List<ProductModel> productList;
    private Context context;
    private final RecyclerViewInterfaceCus recyclerViewInterface;

    public ProductAdapter(List<ProductModel> productList, Context context, RecyclerViewInterfaceCus recyclerViewInterface) {
        this.productList = productList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_product, parent, false);
        return new ProductViewHolder(itemview, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        ProductModel productModel = productList.get(position);
        holder.ProductName.setText(productModel.getName());
        holder.ProductPrice.setText("Rs " + productModel.getPrice());
        holder.ProductStatus.setText(productModel.getStatus());
        Glide.with(holder.itemView.getContext())
                .load(productModel.getImageUrl())
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.error_image)
                .into(holder.ProductImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView ProductImage;
        public TextView ProductName;
        public TextView ProductPrice;
        public TextView ProductStatus;
        public Button moreDetails;
        public ImageButton addCart;

        public ProductViewHolder(@NonNull View itemView, RecyclerViewInterfaceCus recyclerViewInterface) {
            super(itemView);
            ProductImage = itemView.findViewById(R.id.product_image);
            ProductName = itemView.findViewById(R.id.product_name);
            ProductPrice = itemView.findViewById(R.id.product_price);
            ProductStatus = itemView.findViewById(R.id.product_status);
            moreDetails = itemView.findViewById(R.id.btn_products);
            addCart = itemView.findViewById(R.id.add_cart);

            moreDetails.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.editProductData(position);
                    }
                }
            });

            // Add the product to cart when the "Add to Cart" button is clicked
            addCart.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ProductModel product = productList.get(position);
                    if (recyclerViewInterface != null) {
                        recyclerViewInterface.onAddToCartClick(product);
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.livo.company.ProductAdapter.ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        holder.threeD.setOnClickListener(v -> {
            Intent intentView = new Intent(context, ViewModelActivity.class);
            intentView.putExtra("modelUrl", product.getModelUrl());
            Log.d("ProductAdapter", "modelUrl: " + product.getModelUrl());

            context.startActivity(intentView);
        });

    }
}



