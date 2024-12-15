package com.example.livo.admin.adminFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.livo.R;
import com.example.livo.company.CompanyDataModel;
import com.example.livo.company.RecyclerViewInterface;

import java.util.List;

public class JoinRequestAdapter extends RecyclerView.Adapter<JoinRequestAdapter.CompanyViewHolder> {

    private final Context context;
    private final List<CompanyDataModel> companyList;
    private final RecyclerViewInterface recyclerViewInterface;

    public JoinRequestAdapter(Context context, List<CompanyDataModel> companyList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.companyList = companyList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_data, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        CompanyDataModel company = companyList.get(position);

        // Set company details
        holder.companyName.setText(company.getCompanyName());
        Glide.with(context)
                .load(company.getImageUrl())
                .placeholder(R.drawable.image) // Placeholder image
                .error(R.drawable.error_image) // Error fallback image
                .into(holder.companyImage);

        // Handle button clicks
        holder.acceptButton.setOnClickListener(v -> {
            if (recyclerViewInterface != null) {
                recyclerViewInterface.onItemClicked(position); // Accept action
            }
        });

        holder.rejectButton.setOnClickListener(v -> {
            if (recyclerViewInterface != null) {
                recyclerViewInterface.editProductData(position); // Reject action
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        ImageView companyImage;
        ImageButton acceptButton, rejectButton;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyImage = itemView.findViewById(R.id.company_image);
            companyName = itemView.findViewById(R.id.company_name);
            acceptButton = itemView.findViewById(R.id.accept);
            rejectButton = itemView.findViewById(R.id.reject);
        }
    }
}
