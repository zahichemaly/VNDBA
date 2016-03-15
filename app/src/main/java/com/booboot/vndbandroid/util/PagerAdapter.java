package com.booboot.vndbandroid.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.booboot.vndbandroid.activity.PlayingFragment;
import com.booboot.vndbandroid.api.bean.Status;

/**
 * Created by od on 13/03/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        PlayingFragment tab = null;

        switch (position) {
            case 0:
                tab = new PlayingFragment();
                args.putInt(PlayingFragment.STATUS_ARG, Status.PLAYING);
                break;
            case 1:
                tab = new PlayingFragment();
                args.putInt(PlayingFragment.STATUS_ARG, Status.FINISHED);
                break;
            case 2:
                tab = new PlayingFragment();
                args.putInt(PlayingFragment.STATUS_ARG, Status.STALLED);
                break;
            case 3:
                tab = new PlayingFragment();
                args.putInt(PlayingFragment.STATUS_ARG, Status.DROPPED);
                break;
            case 4:
                tab = new PlayingFragment();
                args.putInt(PlayingFragment.STATUS_ARG, Status.UNKNOWN);
                break;
        }

        tab.setArguments(args);
        return tab;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}