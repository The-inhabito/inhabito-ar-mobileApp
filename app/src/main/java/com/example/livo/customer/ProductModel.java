package com.example.livo.customer;

public class ProductModel {
    private String productId, name, description, imageUrl, modelUrl;
    private double price;

    public ProductModel(String productId, String name, double price, String description, String imageUrl, String modelUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.modelUrl = modelUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
