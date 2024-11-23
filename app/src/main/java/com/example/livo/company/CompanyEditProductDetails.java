package com.example.livo.company;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompanyEditProductDetails extends AppCompatActivity {

    private EditText productName, productDescription, productQuantity, productPrice;
    private Button updateProductButton;
    private DatabaseReference databaseReference;
    private CompanyProductsModel product;  // The product object to edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("products");



        // Initialize UI elements
        productName = findViewById(R.id.edit_product_name);
        productDescription = findViewById(R.id.edit_product_description);
        productQuantity = findViewById(R.id.edit_product_quantity);
        productPrice = findViewById(R.id.edit_product_price);
        updateProductButton = findViewById(R.id.btn_update_product);

        // Get the product data passed from the previous screen
        product = getIntent().getParcelableExtra("productData");
        String companyID = getIntent().getStringExtra("companyID");


        // Populate fields with the existing product data
        if (product != null) {
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productQuantity.setText(product.getQuantity());
            productPrice.setText(product.getPrice());
        }

        // Set up the update button click listener
        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductData();
            }
        });
    }

    private void updateProductData() {
        String name = productName.getText().toString();
        String description = productDescription.getText().toString();
        String quantity = productQuantity.getText().toString();
        String price = productPrice.getText().toString();

        String productID = product.getProductID();
        String companyID = getIntent().getStringExtra("companyID"); // Retrieve the companyID from intent

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) ||
                TextUtils.isEmpty(quantity) || TextUtils.isEmpty(price)) {
            Toast.makeText(CompanyEditProductDetails.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update product fields
        product.setName(name);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setPrice(price);

        // Ensure companyID is retained
        product.setCompanyID(companyID);

        // Update product in Firebase, including the companyID
        databaseReference.child(productID).setValue(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CompanyEditProductDetails.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                        finish();  // Close the activity after updating
                    } else {
                        Toast.makeText(CompanyEditProductDetails.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
