package com.example.livo.company;
import com.bumptech.glide.Glide;
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

import com.example.livo.R;
import com.example.livo.company.viewModel.ViewModelActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public List<CompanyProductsModel> productList;
    private Context context;
    private final RecyclerViewInterface recyclerViewInterface;

    public ProductAdapter(List<CompanyProductsModel> productList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.productList = productList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    //populate the product item xml
    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent, false );
       return new ProductViewHolder(itemview, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        CompanyProductsModel product = productList.get(position);
        //validate and set image
        holder.ProductName.setText(product.getName());
        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())  // Image URL
                .placeholder(R.drawable.image)  // Placeholder while loading
                .error(R.drawable.error_image)  // Error image if loading fails
                .into(holder.ProductImage);  // ImageView

//        // Set AR button functionality
//        holder.Ar.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ARActivity.class);
//            intent.putExtra("modelUrl", product.getModelUrl());  // Pass model URL to ARActivity
//            context.startActivity(intent);
//        });

        holder.threeD.setOnClickListener(v -> {
            Intent intentView = new Intent(context, ViewModelActivity.class);
            intentView.putExtra("modelUrl", product.getModelUrl());
//            Log.d("ProductAdapter", "modelUrl: " + product.getModelUrl());

            context.startActivity(intentView);
        });



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public class ProductViewHolder extends RecyclerView.ViewHolder{
        public ImageView ProductImage;
        public TextView ProductName;
        public Button Ar;
        public Button threeD;
        public ImageButton editButton;

        public ProductViewHolder(@NonNull View itemview, RecyclerViewInterface recyclerViewInterface){
            super(itemview);
            ProductImage = itemview.findViewById(R.id.product_image);
            ProductName = itemview.findViewById(R.id.product_name);
//            Ar = itemview.findViewById(R.id.btn_view_ar);
            threeD = itemview.findViewById(R.id.btn3d);
            editButton = itemview.findViewById(R.id.btn_edit);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.editProductData(position);
                        }
                    }
                }
            });

        }
    }
}
