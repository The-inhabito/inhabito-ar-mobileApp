package com.example.livo.customer;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomerSession {
    private static CustomerSession instance;
    private SharedPreferences sharedPreferences;

    private CustomerSession(Context context) {
        sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
    }

    public static synchronized CustomerSession getInstance(Context context) {
        if (instance == null) {
            instance = new CustomerSession(context.getApplicationContext());
        }
        return instance;
    }


    public String getEmail() {
        String email = sharedPreferences.getString("email", null);
        return (email != null && !email.isEmpty()) ? email : null;
    }


    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public void clearSession() {
        // Clear the user email from the session
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
