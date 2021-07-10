package com.example.qrcodeapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

public class LoginAdapter extends FragmentStateAdapter {
    @NonNull
    @NotNull


    private Context context;
    int totalTabs;

    public LoginAdapter(FragmentActivity fa, Context context, int totalTabs){
        super(fa);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
    @Override

    public Fragment createFragment(int position){
        switch (position){
            case 0:
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
                return null;
        }
    }
}
