package com.example.livo.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.livo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompanySignup extends AppCompatActivity {

    EditText signupEmail, signupPassword, confirmPassword;
    TextView loginRedirectText;
    Button signupBtn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_signup);

        // Initialize the views
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_pass);
        confirmPassword = findViewById(R.id.signup_confirmpass);
        signupBtn = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("companies");

        // Set OnClickListener for the sign-up button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyEmail = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String confirmpassword = confirmPassword.getText().toString().trim();

                if (companyEmail.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
                    Toast.makeText(CompanySignup.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmpassword)) {
                    Toast.makeText(CompanySignup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Generate a unique ID and store user data
                    String companyId = reference.push().getKey();
                    HelperClass helperClass = new HelperClass(companyEmail, password);
                    reference.child(companyId).setValue(helperClass);

                    // Set session email
                    sessionClass companySession = sessionClass.getInstance(CompanySignup.this);
                    companySession.setEmail(companyEmail); // Save email in session after successful signup

                    Toast.makeText(CompanySignup.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CompanySignup.this, CompanyData.class);
                    startActivity(intent);
                    finish(); // Optionally finish the current activity
                }
            }
        });

        // Set OnClickListener for the login redirect text
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the login activity
                Intent intent = new Intent(CompanySignup.this, CompanyLogin.class);
                startActivity(intent);
                finish(); // Optionally finish the current activity
            }
        });
    }
}
