package com.example.livo.customer.Products;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private String imageUrl;  // Changed to String for image URL

    // Constructor
    public CartItem(String productId, String productName, int quantity, double price, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;  // Store image as URL
    }

    // Constructor for creating CartItem from Parcel (required for Parcelable)
    protected CartItem(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
        imageUrl = in.readString();  // Read image URL
    }

    // Getter and Setter methods
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to calculate the total price of the cart item
    public double getTotalPrice() {
        return price * quantity;
    }

    // Parcelable implementation
    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeInt(quantity);
        dest.writeDouble(price);
        dest.writeString(imageUrl);  // Write image URL
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
