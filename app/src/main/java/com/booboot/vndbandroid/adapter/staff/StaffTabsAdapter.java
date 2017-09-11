package com.booboot.vndbandroid.adapter.staff;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.booboot.vndbandroid.activity.AboutFragment;

/**
 * Created by od on 13/03/2016.
 */
public class StaffTabsAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public StaffTabsAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment tab;

        switch (position) {
            case 0:
                tab = new AboutFragment();
                break;
            default:
                tab = new AboutFragment();
        }

        return tab;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}