package com.example.livo.customer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.livo.R;
import com.example.livo.customer.Products.Cart;
import com.example.livo.customer.Products.CartFragment;
import com.example.livo.databinding.ActivityCustomerHomeBinding;

public class Home extends AppCompatActivity {
    private ActivityCustomerHomeBinding binding;
    private Cart cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cart = new Cart();

        // Load HomeFragment by default when the activity starts for the first time
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.home); // Highlight the Home button
        }

        // Handle bottom navigation view item selection
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = getSelectedFragment(item.getItemId());
            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return true;
        });
    }

    /**
     * Determines the fragment to load based on the selected menu item ID.
     *
     * @param itemId The selected menu item's ID.
     * @return The fragment to load.
     */
    @Nullable
    private Fragment getSelectedFragment(int itemId) {
        if (itemId == R.id.home) {
            return new HomeFragment();
        } else if (itemId == R.id.cart) {
            return new CartFragment();
        } else if (itemId == R.id.profile) {
            return new ProfileFragment();
//        } else if (itemId == R.id.notify) {
//            return new OrderFragment();
//        }
        } else {
            return null;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public Cart getCart() {
        return cart;
    }
}
