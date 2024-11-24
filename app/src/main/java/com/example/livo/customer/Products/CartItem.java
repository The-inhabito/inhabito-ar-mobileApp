package com.example.livo.customer.Products;

public class CartItem {
    private String name;
    private String price;
    private int quantity;
    private byte[] imageUrl;

    public CartItem(String name, String price, int quantity, byte[] imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public byte[] getImage() {
        return imageUrl;
    }

    public void setImage(byte[] image) {
        this.imageUrl = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}