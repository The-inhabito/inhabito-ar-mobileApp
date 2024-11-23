package com.example.livo.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.livo.R;
import com.example.livo.admin.adminFragments.AdminHomeFragment;
import com.example.livo.admin.adminFragments.AdminNotificationFragment;
import com.example.livo.admin.adminFragments.AdminViewCompanyFragment;
import com.example.livo.admin.adminFragments.AdminViewCustomerFragment;
import com.example.livo.customer.HomeFragment;
import com.example.livo.customer.ProfileFragment;
import com.example.livo.databinding.ActivityAdminMainBinding;
import com.example.livo.databinding.ActivityCustomerHomeBinding;

public class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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