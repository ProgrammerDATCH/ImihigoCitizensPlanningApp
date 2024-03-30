package com.imihigocizitensplanning.app.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.imihigocizitensplanning.app.DistrictFragments.DistrictReportFragment;
import com.imihigocizitensplanning.app.DistrictFragments.DistrictSetActionPlansFragment;
import com.imihigocizitensplanning.app.DistrictFragments.DistrictSetPlansFragment;
import com.imihigocizitensplanning.app.DistrictFragments.DistrictSuggestedPlansFragment;
import com.imihigocizitensplanning.app.DistrictFragments.DistrictViewDeniedPlansFragment;
import com.imihigocizitensplanning.app.DistrictFragments.DistrictViewPlansFragment;

public class DistrictViewPagerAdapter extends FragmentPagerAdapter {


    public DistrictViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new DistrictViewPlansFragment();
            case 1: return new DistrictSetPlansFragment();
            case 2: return new DistrictSuggestedPlansFragment();
            case 3: return new DistrictSetActionPlansFragment();
            case 4: return new DistrictReportFragment();
            case 5: return new DistrictViewDeniedPlansFragment();
            default: return new DistrictViewPlansFragment();
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0)
        {
            title = "ALL";
        }
        if(position == 1)
        {
            title = "ADD";
        }
        if(position == 2)
        {
            title = "SUGGESTED";
        }
        if(position == 3)
        {
            title = "ACTION";
        }
        if(position == 4)
        {
            title = "REPORT";
        }
        if(position == 5)
        {
            title = "DENIED";
        }
        return title;
    }
}
