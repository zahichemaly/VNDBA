package com.booboot.vndbandroid.adapter.tabs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.booboot.vndbandroid.activity.VNListFragment;
import com.booboot.vndbandroid.activity.VNTypeFragment;
import com.booboot.vndbandroid.bean.vndbandroid.ListType;
import com.booboot.vndbandroid.bean.vndbandroid.Priority;
import com.booboot.vndbandroid.bean.vndbandroid.Status;

/**
 * Created by od on 13/03/2016.
 */
public class VNTabsAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;
    private int type;

    public VNTabsAdapter(FragmentManager fm, int numOfTabs, int type) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        VNTypeFragment tab = new VNTypeFragment();

        switch (position) {
            case 0:
                args.putInt(VNTypeFragment.TAB_VALUE_ARG, getTabValue(Status.PLAYING, 10, Priority.HIGH));
                break;
            case 1:
                args.putInt(VNTypeFragment.TAB_VALUE_ARG, getTabValue(Status.FINISHED, 8, Priority.MEDIUM));
                break;
            case 2:
                args.putInt(VNTypeFragment.TAB_VALUE_ARG, getTabValue(Status.STALLED, 6, Priority.LOW));
                break;
            case 3:
                args.putInt(VNTypeFragment.TAB_VALUE_ARG, getTabValue(Status.DROPPED, 4, Priority.BLACKLIST));
                break;
            case 4:
                args.putInt(VNTypeFragment.TAB_VALUE_ARG, getTabValue(Status.UNKNOWN, 2, -1));
                break;
        }

        args.putInt(VNListFragment.LIST_TYPE_ARG, type);
        tab.setArguments(args);
        return tab;
    }

    private int getTabValue(int vnlistValue, int votelistValue, int wishlistValue) {
        if (type == ListType.VNLIST) return vnlistValue;
        if (type == ListType.VOTELIST) return votelistValue;
        if (type == ListType.WISHLIST) return wishlistValue;
        return -1;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}