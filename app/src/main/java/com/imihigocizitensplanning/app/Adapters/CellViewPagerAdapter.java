package com.imihigocizitensplanning.app.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.imihigocizitensplanning.app.Cell.CellViewPlansFragment;
import com.imihigocizitensplanning.app.Cell.DeniedImihigoCellFragment;
import com.imihigocizitensplanning.app.Cell.PendingImihigoCellFragment;

public class CellViewPagerAdapter extends FragmentPagerAdapter {


    public CellViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new CellViewPlansFragment();
            case 1: return new PendingImihigoCellFragment();
            case 2: return new DeniedImihigoCellFragment();
            default: return new CellViewPlansFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
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
            title = "PENDING PLANS";
        }
        if(position == 2)
        {
            title = "PROGRESS";
        }
        return title;
    }
}