package com.example.livo.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.livo.R;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {
    private List<CompanyModel> companyList;

    public CompanyAdapter(List<CompanyModel> companyList) {
        this.companyList = companyList;
    }

    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate your layout for the company item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompanyViewHolder holder, int position) {
        CompanyModel company = companyList.get(position);

        // Set company name and image
        holder.companyName.setText(company.getCompanyName());
        Glide.with(holder.itemView.getContext())
                .load(company.getImageUrl())
                .into(holder.companyImage);
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        ImageView companyImage;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyImage = itemView.findViewById(R.id.company_image);
        }
    }
}
