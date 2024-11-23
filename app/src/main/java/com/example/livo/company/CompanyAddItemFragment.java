package com.example.livo.company;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class CompanyAddItemFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_MODEL_REQUEST = 2;

    private EditText productName, productPrice, productQuantity, productDescription;
    private ImageView productImageView;
    private Button upload3DModelButton, submitButton;
    private Uri imageUri, modelUri;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        // Initialize views
        productName = view.findViewById(R.id.p_name);
        productPrice = view.findViewById(R.id.p_price);
        productQuantity = view.findViewById(R.id.p_quantity);
        productDescription = view.findViewById(R.id.p_description); // Added description field
        productImageView = view.findViewById(R.id.p_normal_image);
        upload3DModelButton = view.findViewById(R.id.upload_3d_model);
        submitButton = view.findViewById(R.id.p_submit);

        // Initialize Firebase references
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("products");
        storageReference = FirebaseStorage.getInstance().getReference("product_images");

        // Set listeners for buttons and image views
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        upload3DModelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openModelChooser();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openModelChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Update this to the specific MIME type if needed for 3D models
        startActivityForResult(intent, PICK_MODEL_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            productImageView.setImageURI(imageUri);
        }

        if (requestCode == PICK_MODEL_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            modelUri = data.getData();
            Toast.makeText(getContext(), "3D Model Selected", Toast.LENGTH_SHORT).show();
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
                                    uploadModel(downloadUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            uploadModel(null);
        }
    }

    private void uploadModel(String imageUrl) {
        if (modelUri != null) {
            // Retrieve the email (companyID) from the session
            sessionClass session = sessionClass.getInstance(getActivity());
            String companyID = session.getEmail();

            if (companyID.isEmpty()) {
                Toast.makeText(getContext(), "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate productID
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    String productID = generateProductID((int) count);

                    // Define the structured path
                    StorageReference modelReference = storageReference.child("product_models")
                            .child(companyID)
                            .child(productID)
                            .child("model.glb");

                    modelReference.putFile(modelUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String modelUrl = uri.toString();
                                            saveProductData(imageUrl, modelUrl, companyID, productID);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to upload 3D model: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to generate product ID", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            saveProductData(imageUrl, null, null, null);
        }
    }


    private void saveProductData(String imageUrl, String modelUrl, String companyID, String productID) {
        String name = productName.getText().toString().trim();
        String price = productPrice.getText().toString().trim();
        String quantity = productQuantity.getText().toString().trim();
        String description = productDescription.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        if (companyID == null || productID == null) {
            Toast.makeText(getContext(), "Error: Product information is incomplete.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> productData = new HashMap<>();
        productData.put("productID", productID);
        productData.put("name", name);
        productData.put("description", description);
        productData.put("price", price);
        productData.put("quantity", quantity);
        productData.put("imageUrl", imageUrl);
        productData.put("modelUrl", modelUrl);
        productData.put("companyID", companyID);

        reference.child(productID).setValue(productData)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Product Data Submitted Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save product data", Toast.LENGTH_LONG).show());
    }


    // Helper function to generate productID based on the current product count
    private String generateProductID(int count) {
        return String.format("P%02d", count + 1); // "P01", "P02", "P03", etc.
    }

}
