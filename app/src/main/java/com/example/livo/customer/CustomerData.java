package com.example.livo.customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livo.Database;
import com.example.livo.databinding.ActivityCustomerDataBinding;

import java.io.IOException;

public class CustomerData extends AppCompatActivity {
    ActivityCustomerDataBinding binding;
    Database database;
    private Bitmap imageToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize view binding
        binding = ActivityCustomerDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize database
        database = new Database(this);

        // Set click listener for the submit button
        binding.customerDataSubmitBtn.setOnClickListener(view -> handleFormSubmission());

        // Set click listener for image selection
        binding.customerImage.setOnClickListener(view -> openImagePicker());
    }

    // Handle form submission
    private void handleFormSubmission() {
        String name = binding.customerName.getText().toString().trim();
        String address = binding.customerAddress.getText().toString().trim();
        String contact = binding.customerContact.getText().toString().trim();

        // Retrieve email from session
        CustomerSession customerSession = CustomerSession.getInstance(this);
        String email = customerSession.getEmail();

        // Validate input fields
        if (name.isEmpty()) {
            binding.customerName.setError("Name is required");
            binding.customerName.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            binding.customerAddress.setError("Address is required");
            binding.customerAddress.requestFocus();
            return;
        }
        if (contact.isEmpty()) {
            binding.customerContact.setError("Contact is required");
            binding.customerContact.requestFocus();
            return;
        }
        if (imageToStore == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Invalid session. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert data into database
        boolean valueInserted = database.insertUserdata(name, address, imageToStore, contact, this);
        if (valueInserted) {
            Toast.makeText(CustomerData.this, "Data inserted successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), CustomerLogin.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(CustomerData.this, "Error inserting data", Toast.LENGTH_SHORT).show();
        }
    }

    // Open the image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    // Handle the result of the image picker
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageFilePath = result.getData().getData();
                    try {
                        // Convert selected image to Bitmap
                        imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                        binding.customerImage.setImageBitmap(imageToStore);
                    } catch (IOException e) {
                        Toast.makeText(this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}
