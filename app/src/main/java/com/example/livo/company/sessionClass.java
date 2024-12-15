package com.example.livo.company;

import android.content.Context;
import android.content.SharedPreferences;

public class sessionClass {
    private static sessionClass instance;
    private SharedPreferences sharedPreferences;

    public sessionClass(Context context) {
        sharedPreferences = context.getSharedPreferences("LivoUserSession", Context.MODE_PRIVATE);
    }

    public static synchronized sessionClass getInstance(Context context) {
        if (instance == null) {
            instance = new sessionClass(context.getApplicationContext());
        }
        return instance;
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data stored in SharedPreferences
        editor.apply(); // Apply changes

        // Nullify the instance to prevent reuse of the old session
        instance = null;
    }
}
