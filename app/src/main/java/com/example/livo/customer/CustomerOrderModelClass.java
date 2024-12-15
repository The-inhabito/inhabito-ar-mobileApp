package com.example.livo.customer;


import com.example.livo.company.CompanyProductsModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderModelClass {
    private String orderID;
    private String orderDate;
    private String orderStatus;
    private double totalPrice;
    private List<CompanyProductsModel> products = new ArrayList<>();

    public CustomerOrderModelClass(String orderID, String orderDate, double totalPrice, String orderStatus) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.products = new ArrayList<>(); // Initialize products list
    }

    public List<CompanyProductsModel> getProducts() {
        return products;
    }

    public void setProducts(List<CompanyProductsModel> products) {
        this.products = products;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}