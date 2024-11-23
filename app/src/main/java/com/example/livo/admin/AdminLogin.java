package com.example.livo.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.livo.LoginActivity;
import com.example.livo.MainActivity;
import com.example.livo.R;
import com.example.livo.databinding.ActivityLoginBinding;

public class AdminLogin extends AppCompatActivity {

    ActivityLoginBinding binding;
    admin_dbHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        databaseHelper = new admin_dbHelper(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.equals("") || password.equals(""))
                    Toast.makeText(AdminLogin.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);

                    if (checkCredentials == true) {
                        Toast.makeText(AdminLogin.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AdminLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
//        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminLogin.this, adminsignup.class);
//                startActivity(intent);
//            }
//        });


    }

}