package com.example.livo.customer;

public class CompanyModel {
    private String companyName;
    private String imageUrl;

    // Constructor, getters and setters
    public CompanyModel(String companyName, String imageUrl) {
        this.companyName = companyName;
        this.imageUrl = imageUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    }
