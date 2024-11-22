package com.example.livo.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.databinding.ActivityCustomerLoginBinding;

public class CustomerLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        ActivityCustomerLoginBinding binding;
        Database database;
        binding = ActivityCustomerLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = new Database(this);

        binding.cusLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.cusLoginEmail.getText().toString()     ;
                String password = binding.cusLoginPass.getText().toString();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(CustomerLogin.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkCredentials = database.checkEmailPassword(email, password);

                    if(checkCredentials == true){
                        Toast.makeText(CustomerLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
                       startActivity(intent);
                    }else{
                        Toast.makeText(CustomerLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerLogin.this, CustomerSignup.class);
                startActivity(intent);
            }
        });













    }
}