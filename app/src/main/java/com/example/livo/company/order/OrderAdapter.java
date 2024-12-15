package com.example.livo.company.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderModelClass> orderList;
    private Context context;
    private OnOrderActionListener actionListener;

    public OrderAdapter(Context context, List<OrderModelClass> orderList, OnOrderActionListener actionListener) {
        this.context = context;
        this.orderList = orderList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        OrderModelClass order = orderList.get(position);

        // Bind order data to the view
        holder.orderID.setText(order.getOrderID());

        // Set up action buttons
        holder.acceptButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAccept(order);
            }
        });

        holder.rejectButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onReject(order);
            }
        });

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
        ImageButton acceptButton, rejectButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderID);
            orderIcon = itemView.findViewById(R.id.moreDetail);
            acceptButton = itemView.findViewById(R.id.accept);
            rejectButton = itemView.findViewById(R.id.reject);
        }
    }
    public void updateOrders(List<OrderModelClass> updatedOrders) {
        this.orderList = updatedOrders;
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }
    public interface OnOrderActionListener {
        void onAccept(OrderModelClass order);
        void onReject(OrderModelClass order);
        void onViewDetails(OrderModelClass order);
    }
}
