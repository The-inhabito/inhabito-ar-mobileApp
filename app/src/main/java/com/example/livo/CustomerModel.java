package com.example.livo;

import android.graphics.Bitmap;

public class CustomerModel {
    private String name;
    private String address;
    private String contact;
    private String email;
    private Bitmap profilePic;

    public CustomerModel(String name, String address, String contact, String email, Bitmap profilePic) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.profilePic = profilePic;
    }

    // Getters
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
    public Bitmap getProfilePic() { return profilePic; }
}
