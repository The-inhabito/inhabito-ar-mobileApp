package com.example.livo.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.livo.R;
import com.example.livo.admin.adminFragments.AdminHomeFragment;
import com.example.livo.admin.adminFragments.AdminNotificationFragment;
import com.example.livo.admin.adminFragments.AdminViewCompanyFragment;
import com.example.livo.admin.adminFragments.AdminViewCustomerFragment;
import com.example.livo.company.CompanyHomeFragment;
import com.example.livo.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new AdminHomeFragment()); // Default fragment

        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            Fragment fragment = new Fragment();
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                fragment = new AdminHomeFragment();
            } else if (itemId == R.id.company) {
                fragment = new AdminViewCompanyFragment();
            } else if (itemId == R.id.customer) {
                fragment = new AdminViewCustomerFragment();
            } else if (itemId == R.id.notify) {
                fragment = new AdminNotificationFragment();
            }
            replaceFragment(fragment);
            return true;
        });
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
