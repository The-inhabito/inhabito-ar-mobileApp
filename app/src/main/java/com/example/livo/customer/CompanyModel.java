package com.example.livo.customer;

public class CompanyModel {
    private String companyName;
    private String imageUrl;
    private String companyEmail;

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    // Constructor, getters and setters
    public CompanyModel(String companyName, String imageUrl, String companyEmail) {
        this.companyName = companyName;
        this.imageUrl = imageUrl;
        this.companyEmail = companyEmail;
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
