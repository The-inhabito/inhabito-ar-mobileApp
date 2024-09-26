package com.example.livo.customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
    private Uri imageFilePath;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerDataBinding.inflate(getLayoutInflater());  // Initialize binding
        setContentView(binding.getRoot());  // Use binding.getRoot() after initialization

        database = new Database(this);

        binding.customerDataSubmitBtn.setOnClickListener(view -> {
            String name = binding.customerName.getText().toString();
            String address = binding.customerAddress.getText().toString();
            String contact = binding.customerContact.getText().toString();

            CustomerSession customerSession = CustomerSession.getInstance(CustomerData.this);
            String email = customerSession.getEmail();

            if (name.equals("") || address.equals("") || contact.equals("")) {
                Toast.makeText(CustomerData.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (imageToStore == null) {
                Toast.makeText(CustomerData.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }else {
                boolean valueInserted = database.insertUserdata(
                        name,
                        address,
                        imageToStore,
                        contact,
                        email
                );

                if (valueInserted) {
                    Toast.makeText(CustomerData.this, "Data inserted successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CustomerLogin.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CustomerData.this, "Error inserting data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.customerImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        });
    }
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageFilePath = result.getData().getData();
                    try {
                        imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                        binding.customerImage.setImageBitmap(imageToStore);
                    } catch (IOException e) {
                        Toast.makeText(CustomerData.this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}
