package com.example.livo.company.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.R;
import com.example.livo.company.CompanyProductsModel;

import java.util.List;

public class DialogProductAdapter extends RecyclerView.Adapter<DialogProductAdapter.ViewHolder> {

    private Context context;
    private List<CompanyProductsModel> productList;

    public DialogProductAdapter(Context context, List<CompanyProductsModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dialog_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyProductsModel product = productList.get(position);

        // Bind product details
        holder.tvProductName.setText(product.getName());
        holder.tvProductQuantity.setText("Quantity: " + product.getQuantity());
        holder.tvProductPrice.setText("Price: " + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductQuantity, tvProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }
}
