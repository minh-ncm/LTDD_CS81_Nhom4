package com.news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class HomePageViewPagerAdapter extends FragmentStatePagerAdapter {
    public HomePageViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TheGioiFragment();
            case 1:
                return new GiaiTriFragment();
            case 2 :
                return new TheThaoFragment();
            default:
                return new TheGioiFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Thế Giới";
            case 1:
                return "Giải Trí";
            case 2:
                return "Thể Thao";
            default:return "Thế Giới";
        }

    }
}
