package com.example.livo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.livo.admin.AdminLogin;
import com.example.livo.company.CompanySignup;
import com.example.livo.customer.CustomerSignup;
import com.example.livo.databinding.ActivityCommonInterfaceBinding;

public class commonInterface extends AppCompatActivity {
ActivityCommonInterfaceBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommonInterfaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //redirecting to the users
        binding.users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_u = new Intent(commonInterface.this, CustomerSignup.class);
                startActivity(intent_u);
            }
        });
        //redirecting to the admin
        binding.admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_a = new Intent(commonInterface.this, AdminLogin.class);
                startActivity(intent_a);
            }
        });
        //redirecting to the company
        binding.company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(commonInterface.this, CompanySignup.class);
                startActivity(intent);
            }
        });
    }
}