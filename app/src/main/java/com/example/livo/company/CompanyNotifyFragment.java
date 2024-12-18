package com.example.livo.company;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.company.order.DialogProductAdapter;
import com.example.livo.company.order.OrderAdapter;
import com.example.livo.company.order.OrderModelClass;

import java.util.List;

public class CompanyNotifyFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private Database orderDbHelper; // Helper class to interact with the database

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_notify, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the database helper
        orderDbHelper = new Database(getActivity());
        sessionClass session = sessionClass.getInstance(getActivity());
        String companyEmail = session.getEmail();

        // Fetch orders by company email
        List<OrderModelClass> orders = orderDbHelper.getOrdersByCompanyEmail(companyEmail);

        // Set up the adapter and attach it to the RecyclerView
        orderAdapter = new OrderAdapter(getActivity(), orders, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onAccept(OrderModelClass order) {
                // Update the order status to "Accepted"
                orderDbHelper.updateOrderStatus(order.getOrderID(), "Accepted");

                // Optionally, refresh the orders list
                List<OrderModelClass> updatedOrders = orderDbHelper.getOrdersByCompanyEmail(companyEmail);
                orderAdapter.updateOrders(updatedOrders);

                // Optionally, show a message to the user (Toast)
                Toast.makeText(getActivity(), "Order Accepted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onReject(OrderModelClass order) {
                // Update the order status to "Rejected"
                orderDbHelper.updateOrderStatus(order.getOrderID(), "Rejected");

                // Optionally, refresh the orders list
                List<OrderModelClass> updatedOrders = orderDbHelper.getOrdersByCompanyEmail(companyEmail);
                orderAdapter.updateOrders(updatedOrders);

                // Optionally, show a message to the user (Toast)
                Toast.makeText(getActivity(), "Order Rejected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onViewDetails(OrderModelClass order) {
                // Show the order details in a dialog
                showOrderDetailsDialog(order);
            }
        });

        recyclerView.setAdapter(orderAdapter);

        return view;
    }
    private void showOrderDetailsDialog(OrderModelClass order) {
        // Create a dialog
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_order_details);
        dialog.setCancelable(true);

        // Initialize the views in the dialog
        TextView tvOrderID = dialog.findViewById(R.id.tv_order_id);
        TextView tvOrderDate = dialog.findViewById(R.id.tv_order_date);
        TextView tvOrderStatus = dialog.findViewById(R.id.tv_order_status);
        RecyclerView recyclerViewProducts = dialog.findViewById(R.id.recycler_view_products);
        Button btnClose = dialog.findViewById(R.id.btn_close);

        // Set order data
        tvOrderID.setText("Order ID: " + order.getOrderID());
        tvOrderDate.setText("Order Date: " + order.getOrderDate());
        tvOrderStatus.setText("Order Status: " + order.getOrderStatus());

        // Set up RecyclerView with the new DialogProductAdapter
        DialogProductAdapter dialogProductAdapter = new DialogProductAdapter(getActivity(), order.getProducts());
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewProducts.setAdapter(dialogProductAdapter);

        // Set up the close button
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }



}
