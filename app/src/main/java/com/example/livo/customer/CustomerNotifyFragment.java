package com.example.livo.customer;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livo.Database;
import com.example.livo.R;


import java.util.List;

public class CustomerNotifyFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private Database orderDbHelper; // Helper class to interact with the database

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_notify, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_orders_cus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the database helper
        orderDbHelper = new Database(getActivity());
        CustomerSession session = CustomerSession.getInstance(getActivity());
        String customerEmail = session.getEmail();

        // Fetch orders by customer email
        List<CustomerOrderModelClass> orders = orderDbHelper.getOrdersByCustomerEmail(customerEmail);

        // Set up the adapter and attach it to the RecyclerView
        orderAdapter = new OrderAdapter(getActivity(), orders, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onViewDetails(CustomerOrderModelClass order) {
                // Show the order details in a dialog
                showOrderDetailsDialog(order);
            }
        });

        recyclerView.setAdapter(orderAdapter);

        return view;
    }

    private void showOrderDetailsDialog(CustomerOrderModelClass order) {
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
//        DialogProductAdapter dialogProductAdapter = new DialogProductAdapter(getActivity(), order.getProducts());
//        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerViewProducts.setAdapter(dialogProductAdapter);

        // Set up the close button
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }
}
