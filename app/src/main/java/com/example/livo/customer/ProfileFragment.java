package com.example.livo.customer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livo.Database;
import com.example.livo.R;
import com.example.livo.databinding.FragmentCustomerProfileBinding;

import java.io.IOException;

public class ProfileFragment extends Fragment {
    private FragmentCustomerProfileBinding binding; // View binding for the fragment
    private Database database;
    private Bitmap profileImageBitmap;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCustomerProfileBinding.inflate(inflater, container, false);
        database = new Database(requireContext());

        // Get the user ID from session
        CustomerSession session = CustomerSession.getInstance(requireContext());
        String userEmail = session.getEmail();

        // Retrieve user ID based on email
        if (userEmail != null) {
            Cursor cursor = database.getReadableDatabase().rawQuery(
                    "SELECT user_id FROM userLogin_data WHERE email = ?",
                    new String[]{userEmail}
            );
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(0);
            }
            cursor.close();
        }

        // Load user data into UI
        loadUserData();

        // Set up the image picker
        binding.profileImage.setOnClickListener(view -> openImagePicker());

        // Set up the update button
        binding.updateProfileButton.setOnClickListener(view -> updateUserData());

        return binding.getRoot();
    }

    private void loadUserData() {
        Cursor cursor = database.getUserData(userId);
        if (cursor.moveToFirst()) {
            // Retrieve data from the cursor
            String name = cursor.getString(0);
            String address = cursor.getString(1);
            byte[] imageBytes = cursor.getBlob(2);
            String contact = cursor.getString(3);
            String email = cursor.getString(4);

            // Display data in the UI
            binding.profileName.setText(name);
            binding.profileAddress.setText(address);
            binding.profileContact.setText(contact);
            binding.profileEmail.setText(email);

            if (imageBytes != null) {
                profileImageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.profileImage.setImageBitmap(profileImageBitmap);
            }
        }
        cursor.close();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Profile Picture"));
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        profileImageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                        binding.profileImage.setImageBitmap(profileImageBitmap);
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void updateUserData() {
        String updatedName = binding.profileName.getText().toString();
        String updatedAddress = binding.profileAddress.getText().toString();
        String updatedContact = binding.profileContact.getText().toString();
        String updatedEmail = binding.profileEmail.getText().toString();

        if (updatedName.isEmpty() || updatedAddress.isEmpty() || updatedContact.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = database.updateUserData(userId, profileImageBitmap, updatedName, updatedEmail, updatedAddress, updatedContact);
        if (isUpdated) {
            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
