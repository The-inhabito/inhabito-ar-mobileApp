package com.example.livo.company;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CompanyData extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText cName, cAddress, cContact;
    private ImageView ivImage;
    private Button dataBtn;
    private Uri imageUri;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_data);

        // Initialize views
        cName = findViewById(R.id.c_name);
        cAddress = findViewById(R.id.c_address);
        cContact = findViewById(R.id.c_contact);
        ivImage = findViewById(R.id.iv_image);
        dataBtn = findViewById(R.id.data_btn);

        // Initialize Firebase references
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("companyData");
        storageReference = FirebaseStorage.getInstance().getReference("company_images");

        // Set OnClickListener for the submit button
        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        // Set OnClickListener for the image view to select an image
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivImage.setImageURI(imageUri);
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    saveCompanyData(downloadUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CompanyData.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            saveCompanyData(null);
        }
    }


    private void saveCompanyData(String imageUrl) {
        String companyName = cName.getText().toString().trim();
        String companyAddress = cAddress.getText().toString().trim();
        String contactNumber = cContact.getText().toString().trim();

        if (companyName.isEmpty() || companyAddress.isEmpty() || contactNumber.isEmpty()) {
            Toast.makeText(CompanyData.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the email from the session
        sessionClass session = sessionClass.getInstance(CompanyData.this);
        String email = session.getEmail();

        if (email.isEmpty()) {
            Toast.makeText(CompanyData.this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new CompanyDataModel object
        CompanyDataModel companyData = new CompanyDataModel(companyName, companyAddress, contactNumber, email, imageUrl);

        // Use company name as the unique ID
        reference.child(companyName).setValue(companyData);

        Toast.makeText(CompanyData.this, "Company Data Submitted Successfully", Toast.LENGTH_SHORT).show();

        // Optionally, navigate to another activity
        Intent intent = new Intent(CompanyData.this, CompanyLogin.class);
        startActivity(intent);
    }
}
