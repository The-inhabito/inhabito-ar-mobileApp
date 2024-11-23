package com.example.livo.company;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.livo.company.tabFragments.companyNotificationAdminSec;
import com.example.livo.company.tabFragments.companyNotificationOrderSec;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull CompanyNotifyFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new companyNotificationAdminSec();
            case 1: return new companyNotificationOrderSec();
            default: return new companyNotificationOrderSec();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
