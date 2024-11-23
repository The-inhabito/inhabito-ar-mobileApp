package com.example.livo.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.livo.R;
import com.example.livo.databinding.ActivityCustomerHomeBinding;

public class Home extends AppCompatActivity {
    ActivityCustomerHomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = new Fragment();
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.cart) {
                //fragment = new CartFragment();
            } else if (itemId == R.id.profile) {
                fragment = new ProfileFragment();
            } else if (itemId == R.id.notify) {
//                Intent intent = new Intent(Home.this, ProfileFragment.class);
//                startActivity(intent);
            } else {
                fragment = new HomeFragment();
            }
            replaceFragment(fragment);
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
