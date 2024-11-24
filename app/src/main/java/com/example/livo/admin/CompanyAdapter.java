package com.example.livo.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.livo.R;

import com.example.livo.admin.adminFragments.AdminCompanyDetails;
import com.example.livo.customer.CompanyModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {
    private List<CompanyModel> companyList;
    private Context context;
    RecyclerViewInterface recyclerViewInterface;


    public CompanyAdapter(List<CompanyModel> companyList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.companyList = companyList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_company_item, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        CompanyModel company = companyList.get(position);

        holder.companyName.setText(company.getCompanyName());
        Glide.with(holder.itemView.getContext())
                .load(company.getImageUrl())
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_image_24)
                .into(holder.companyImage);


    }


    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        ShapeableImageView companyImage;
        Button detailsButton;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name);
            companyImage = itemView.findViewById(R.id.company_image);
            detailsButton = itemView.findViewById(R.id.btn_details);

            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.viewProductData(position);
                        }
                    }

                }
            });
        }
    }
    public interface onCompanyClickListener {
        void onCompanyClick(CompanyModel company);
    }

}

