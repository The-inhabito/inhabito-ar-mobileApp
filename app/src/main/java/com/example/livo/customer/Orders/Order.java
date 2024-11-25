package com.example.livo.customer.Orders;

public class Order {
    private String productName;
    private int quantity;
    private double totalPrice;
    private String orderStatus;

    public Order(String productName, int quantity, double totalPrice, String orderStatus) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
