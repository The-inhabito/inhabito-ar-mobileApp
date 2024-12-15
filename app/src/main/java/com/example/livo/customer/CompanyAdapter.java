package com.example.livo.customer;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.livo.R;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {
    private List<CompanyModel> companyList;
    private OnCompanyClickListener clickListener; // Callback interface for handling clicks

    // Constructor accepting company list and click listener
    public CompanyAdapter(List<CompanyModel> companyList, OnCompanyClickListener clickListener) {
        this.companyList = companyList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each company item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        CompanyModel company = companyList.get(position);

        // Set company details
        holder.companyName.setText(company.getCompanyName());
        Glide.with(holder.itemView.getContext())
                .load(company.getImageUrl())
                .into(holder.companyImage);

        // Handle button click
        holder.viewProductsButton.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCompanyClick(company); // Trigger callback with company details
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    // ViewHolder class
    public class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        ImageView companyImage;
        Button viewProductsButton; // Add button reference

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyImage = itemView.findViewById(R.id.company_image);
            viewProductsButton = itemView.findViewById(R.id.btn_products); // Reference the button




        }
    }

    // Interface for click handling
    public interface OnCompanyClickListener {
        void onCompanyClick(CompanyModel company); // Pass company details to callback
    }
}
