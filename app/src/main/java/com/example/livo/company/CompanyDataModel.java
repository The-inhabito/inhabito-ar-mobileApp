package com.example.livo.company;
public class CompanyDataModel {
    String CompanyName;
    String CompanyAddress;
    String CompanyContactNo;
    String CompanyEmail;
    String ImageUrl; // Add this field

    public CompanyDataModel() {
    }

    public CompanyDataModel(String companyName, String companyAddress, String companyContactNo, String companyEmail, String imageUrl) {
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        CompanyContactNo = companyContactNo;
        CompanyEmail = companyEmail;
        ImageUrl = imageUrl;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public String getCompanyContactNo() {
        return CompanyContactNo;
    }

    public void setCompanyContactNo(String companyContactNo) {
        CompanyContactNo = companyContactNo;
    }

    public String getCompanyEmail() {
        return CompanyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        CompanyEmail = companyEmail;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}

