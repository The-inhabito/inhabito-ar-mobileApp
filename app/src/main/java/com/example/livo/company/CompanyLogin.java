package com.example.livo.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.livo.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CompanyLogin extends AppCompatActivity {
    EditText loginEmail, loginPass;
    Button loginBtn;
    TextView SignupRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_pass);
        SignupRedirect = findViewById(R.id.signupRedirect);
        loginBtn = findViewById(R.id.login_button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !validatePassword() | !validateUsername() ){

                }else{
                    checkCompany();
                }
            }
        });

        SignupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(CompanyLogin.this, CompanySignup.class);
                startActivity(intent);
            }
        });


    }
    public Boolean validateUsername(){
        String val = loginEmail.getText().toString();
        if(val.isEmpty()){
            loginEmail.setError("Email cannot be empty!");
            return false;
        }else{
            loginEmail.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = loginPass.getText().toString();
        if(val.isEmpty()){
            loginPass.setError("Password cannot be empty!");
            return false;
        }else{
            loginPass.setError(null);
            return true;
        }
    }
    public void checkCompany() {
        String CompanyEmail = loginEmail.getText().toString().trim();
        String CompanyPass = loginPass.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("companies");
        Query CheckDatabase = reference.orderByChild("email").equalTo(CompanyEmail);

        CheckDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean isValid = false;
                    String status = "";
                    for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                        String passwordFromDb = companySnapshot.child("password").getValue(String.class);
                        status = companySnapshot.child("status").getValue(String.class);

                        if (passwordFromDb != null && passwordFromDb.equals(CompanyPass)) {
                            isValid = true;
                            break;
                        }
                    }

                    if (isValid) {
                        if ("approved".equals(status)) {
                            // Store the email in session
                            sessionClass session = sessionClass.getInstance(getApplicationContext());
                            session.setEmail(CompanyEmail);

                            // Proceed to HomeCompany activity
                            Intent intent = new Intent(CompanyLogin.this, HomeCompany.class);
                            startActivity(intent);
                            finish();
                        } else if ("pending".equals(status)) {
                            Toast.makeText(CompanyLogin.this, "Your account is under review. Please wait for admin approval.", Toast.LENGTH_LONG).show();
                        } else if ("rejected".equals(status)) {
                            Toast.makeText(CompanyLogin.this, "Your account has been rejected. Please contact support.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CompanyLogin.this, "Account status unknown. Contact support.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        loginPass.setError("Invalid Credentials");
                        loginPass.requestFocus();
                    }
                } else {
                    loginEmail.setError("Company does not exist");
                    loginPass.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CompanyLogin.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}