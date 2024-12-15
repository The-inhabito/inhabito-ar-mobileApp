package com.example.livo.customer;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomerSession {
    private static final String SESSION_PREFS = "UserSession";
    private static final String USER_EMAIL_KEY = "email";

    private static CustomerSession instance;
    private SharedPreferences sharedPreferences;

    // Private constructor for singleton
    private CustomerSession(Context context) {
        sharedPreferences = context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE);
    }

    // Singleton instance
    public static synchronized CustomerSession getInstance(Context context) {
        if (instance == null) {
            instance = new CustomerSession(context.getApplicationContext());
        }
        return instance;
    }

    // Get email from session
    public String getEmail() {
        if (sharedPreferences == null) {
            return null; // Fallback in case of failure
        }
        return sharedPreferences.getString(USER_EMAIL_KEY, null);
    }

    // Set email in session
    public void setEmail(String email) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_EMAIL_KEY, email);
            editor.apply();
        }
    }

    // Clear the session
    public void clearSession() {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    // Check if there is an active session
    public boolean hasActiveSession() {
        return getEmail() != null;
}
}