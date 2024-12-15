package com.example.livo.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.R;
import com.example.livo.customer.CustomerOrderModelClass;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<CustomerOrderModelClass> orderList;
    private Context context;
    private OnOrderActionListener actionListener;

    public OrderAdapter(Context context, List<CustomerOrderModelClass> orderList, OnOrderActionListener actionListener) {
        this.context = context;
        this.orderList = orderList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_cus, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        CustomerOrderModelClass order = orderList.get(position);

        // Bind order data to the view
        holder.orderID.setText(order.getOrderID());

        // Set up action for viewing details
        holder.orderIcon.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onViewDetails(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderID;
        ImageView orderIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderID);
            orderIcon = itemView.findViewById(R.id.moreDetail);
        }
    }

    public interface OnOrderActionListener {
        void onViewDetails(CustomerOrderModelClass order);
    }

    public void updateOrders(List<CustomerOrderModelClass> updatedOrders) {
        this.orderList = updatedOrders;
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }
}
