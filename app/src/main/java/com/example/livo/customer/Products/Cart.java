package com.example.livo.customer.Products;

import com.example.livo.customer.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items; // List holding items in the cart

    public Cart() {
        items = new ArrayList<>();
    }

    public boolean calculateTotalAmount() {
        if (items.isEmpty()) {
            return false; // Return false if cart is empty
        }

        double total = 0.0;
        for (CartItem item : items) {
            // Parse the price as a double and then multiply by quantity
            total += Double.parseDouble(String.valueOf(item.getPrice())) * item.getQuantity();
        }

        System.out.println("Total Amount: " + total); // Log the total for debugging
        return true; // Return true if calculation is successful
    }


    // Add a getter for items
    public List<CartItem> getItems() {
        return items;
    }

    // Add item to the cart
    public void addItem(CartItem item) {
        items.add(item);
    }

    // Remove item from the cart
    public void removeItem(CartItem item) {
        items.remove(item);
    }
}

