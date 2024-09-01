package com.example.livo.company;
//for authentication purpose only
public class HelperClass {
    String email, password;
    //inhere email is our username


    public HelperClass() {
    }

    public HelperClass(String email, String password) {
        this.email = email;
        this.password = password;
    }
//Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
