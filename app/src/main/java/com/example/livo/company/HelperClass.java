package com.example.livo.company;
//for authentication purpose only
public class HelperClass {
    String email, password, status;
    //inhere email is our username


    public HelperClass() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HelperClass(String email, String password, String status) {
        this.email = email;
        this.password = password;
        this.status = status;
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
