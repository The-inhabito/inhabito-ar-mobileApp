package com.example.livo.company.tabFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.company.order.OrderAdapter;
import com.example.livo.company.order.OrderModelClass;
import com.example.livo.company.sessionClass;

import java.util.ArrayList;
import java.util.List;

public class companyNotificationOrderSec extends Fragment {
    public RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<OrderModelClass> orderList;
    private Database database;
    private sessionClass sessionclass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_notification_order_sec, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = new Database(getContext());


        loadCompanyOrders();

        return view;
    }

    private void loadCompanyOrders() {
        String companyEmail = sessionclass.getEmail();
        orderList = new ArrayList<>();

        if (companyEmail != null) {
            orderList = database.getOrdersByCompanyEmail(companyEmail);
        }

        adapter = new OrderAdapter(getContext(), orderList, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onAccept(OrderModelClass order) {
                // Handle accept action
            }

            @Override
            public void onReject(OrderModelClass order) {
                // Handle reject action
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
