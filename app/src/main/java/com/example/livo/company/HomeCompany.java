package com.example.livo.company;

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
import com.example.livo.databinding.ActivityHomeCompanyBinding;

public class HomeCompany extends AppCompatActivity {

    ActivityHomeCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CompanyHomeFragment());


        binding.bottomNavigationViewCompany.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.homefab) {
                replaceFragment(new CompanyHomeFragment());
            } else if (itemId == R.id.add_products) {
                replaceFragment(new CompanyAddItemFragment());
            } else if (itemId == R.id.setting) {
                replaceFragment(new CompanysettingFragment());
            } else if (itemId == R.id.chart) {
                replaceFragment(new CompanyChartFragment());
            } else if (itemId == R.id.notify) {
                replaceFragment(new CompanyNotifyFragment());
            }
            return true;
        });

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.company_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}