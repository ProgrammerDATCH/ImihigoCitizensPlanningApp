package com.imihigocizitensplanning.app.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.imihigocizitensplanning.app.UserFragments.UserSuggestPlanFragment;
import com.imihigocizitensplanning.app.UserFragments.UserViewActionPlansFragment;
import com.imihigocizitensplanning.app.UserFragments.UserViewDeniedPlansFragment;
import com.imihigocizitensplanning.app.UserFragments.UserViewPlansFragment;

public class UserViewPagerAdapter extends FragmentPagerAdapter {


    public UserViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new UserViewPlansFragment();
            case 1: return new UserSuggestPlanFragment();
            case 2: return new UserViewActionPlansFragment();
            case 3: return new UserViewDeniedPlansFragment();
            default: return new UserViewPlansFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0)
        {
            title = "ALL PLANS";
        }
        if(position == 1)
        {
            title = "SUGGEST PLAN";
        }
        if(position == 2)
        {
            title = "ACTION PLANS";
        }
        if(position == 3)
        {
            title = "PROGRESS";
        }
        return title;
    }
}