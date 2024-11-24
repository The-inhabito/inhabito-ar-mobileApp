package com.example.livo.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.livo.databinding.ActivityAdminLoginBinding;

public class AdminLogin extends AppCompatActivity {

    ActivityAdminLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hardcoded credentials
        final String hardcodedEmail = "admin@gmail.com";
        final String hardcodedPassword = "admin123";

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AdminLogin.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    // Check credentials with hardcoded values
                    if (email.equals(hardcodedEmail) && password.equals(hardcodedPassword)) {
                        Toast.makeText(AdminLogin.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AdminLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
