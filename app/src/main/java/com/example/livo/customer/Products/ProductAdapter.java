package com.example.livo.customer.Products;

import android.content.Context;
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
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_product,parent, false );
        return new com.example.livo.customer.Products.ProductAdapter.ProductViewHolder(itemview, recyclerViewInterface);
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
    public class ProductViewHolder extends RecyclerView.ViewHolder{
        public ImageView ProductImage;
        public TextView ProductName;
        public TextView ProductPrice;
        public TextView ProductStatus;
        public Button moreDetails;
        public ImageButton addCart;

        public ProductViewHolder(@NonNull View itemview, RecyclerViewInterfaceCus recyclerViewInterface){
            super(itemview);
            ProductImage = itemview.findViewById(R.id.product_image);
            ProductName = itemview.findViewById(R.id.product_name);
            ProductPrice = itemview.findViewById(R.id.product_price);
            ProductStatus = itemview.findViewById(R.id.product_status);
            moreDetails = itemview.findViewById(R.id.btn_products);
            addCart = itemview.findViewById(R.id.add_cart);

            moreDetails.setOnClickListener(view -> {
                if (recyclerViewInterface != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.editProductData(position);
                    }
                }
            });

//            addCart.setOnClickListener(view -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    // Call your onAddToCartClick method with the product at the given position
//                    onAddToCartClick(position);
//            });


        }
    }
}


