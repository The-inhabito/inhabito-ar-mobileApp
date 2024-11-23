package com.example.livo.company;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.livo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class CompanysettingFragment extends Fragment {
    private TextView companyName, companyAddress, phoneNumber, companyEmail;
    private DatabaseReference databaseReference;
    private ImageView companyProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_companysetting, container, false);

        companyName = view.findViewById(R.id.CompanyName);
        companyAddress = view.findViewById(R.id.companyAddress);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        companyEmail = view.findViewById(R.id.companyEmail);
        companyProfile = view.findViewById(R.id.companyProfile);

        sessionClass session = sessionClass.getInstance(getActivity());
        String email = session.getEmail();



        databaseReference = FirebaseDatabase.getInstance().getReference("companyData");
        Query query = databaseReference.orderByChild("companyEmail").equalTo(email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot companySnapshot : snapshot.getChildren()) {
                        String name = companySnapshot.child("companyName").getValue(String.class);
                        String address = companySnapshot.child("companyAddress").getValue(String.class);
                        String phone = companySnapshot.child("companyContactNo").getValue(String.class);
                        String email = companySnapshot.child("companyEmail").getValue(String.class);
                        String imageUrl = companySnapshot.child("imageUrl").getValue(String.class);

                        companyName.setText(name);
                        companyAddress.setText(address);
                        phoneNumber.setText(phone);
                        companyEmail.setText(email);

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(getContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.baseline_image_24)
                                    .error(R.drawable.baseline_image_24)
                                    .into(companyProfile);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "No data found for this company", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
