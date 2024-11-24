package com.example.livo.admin;

public class CompanyDataModel {
    private String companyName;
    private String imageUrl;
    private String companyEmail;
    private String companyAddress;
    private String companyContactNo;

    public CompanyDataModel(String companyAddress, String companyContactNo, String companyEmail, String companyName, String imageUrl) {
        this.companyAddress = companyAddress;
        this.companyContactNo = companyContactNo;
        this.companyEmail = companyEmail;
        this.companyName = companyName;
        this.imageUrl = imageUrl;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyContactNo() {
        return companyContactNo;
    }

    public void setCompanyContactNo(String companyContactNo) {
        this.companyContactNo = companyContactNo;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
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
