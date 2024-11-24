package com.example.livo.admin.adminFragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.livo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCompanyDetails extends AppCompatActivity {

    private String companyName;
    private String companyEmail;
    private String phoneNumber;
    private String companyAddress;
    private String companyImageUrl;
    private TextView companyNameTextView, companyEmailTextView, companyAddressTextView, companyContactTextView;
    private ImageView companyProfileImageView;
    private Button btnViewProducts, btnContact, btnReviews, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_company_details2);

        // Initialize views
        companyNameTextView = findViewById(R.id.CompanyName);
        companyEmailTextView = findViewById(R.id.companyEmail);
        companyAddressTextView = findViewById(R.id.companyAddress);
        companyContactTextView = findViewById(R.id.phoneNumber);
        companyProfileImageView = findViewById(R.id.companyProfile);
        btnViewProducts = findViewById(R.id.btnProduct);
        btnContact = findViewById(R.id.btnContact);
        btnReviews = findViewById(R.id.btnReviews);
        btnDelete = findViewById(R.id.deleteBtn);

        // Retrieve the data passed from AdminViewCompanyFragment
        Intent intent = getIntent();
        companyName = intent.getStringExtra("companyName");
        companyEmail = intent.getStringExtra("companyEmail");
        companyAddress = intent.getStringExtra("companyAddress");
        phoneNumber = intent.getStringExtra("companyContactNo");
        companyImageUrl = intent.getStringExtra("companyImageUrl");

        // Set the company data in the views
        companyNameTextView.setText(companyName);
        companyEmailTextView.setText(companyEmail);
        companyAddressTextView.setText(companyAddress);
        companyContactTextView.setText(phoneNumber);
        Glide.with(this).load(companyImageUrl).into(companyProfileImageView);

        // Set up button clicks
        btnViewProducts.setOnClickListener(v -> {
//            // Handle "View Products" button click
//            Intent productsIntent = new Intent(AdminCompanyDetails.this, AdminViewProductsActivity.class);
//            productsIntent.putExtra("companyEmail", companyEmail); // Pass the company email to view products
//            startActivity(productsIntent);
        });

        btnContact.setOnClickListener(v -> {
            // Handle "Contact" button click
            Toast.makeText(this, "Contact company: " + companyEmail, Toast.LENGTH_SHORT).show();
        });

        btnReviews.setOnClickListener(v -> {
            // Handle "View Reviews" button click
            Toast.makeText(this, "Viewing reviews for: " + companyName, Toast.LENGTH_SHORT).show();
        });

        btnDelete.setOnClickListener(v -> {
            // Handle "Delete" button click (remove company from listing)
            deleteCompany();
        });
    }

    private void deleteCompany() {
        DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference("companyData").child(companyEmail);
        companyRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminCompanyDetails.this, "Company deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after successful deletion
            } else {
                Toast.makeText(AdminCompanyDetails.this, "Failed to delete company", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
