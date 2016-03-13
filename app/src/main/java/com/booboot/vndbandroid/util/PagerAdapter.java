package com.booboot.vndbandroid.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.booboot.vndbandroid.activity.PlayingFragment;

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

        switch (position) {
            case 0:
                PlayingFragment tab1 = new PlayingFragment();
                return tab1;
            case 1:
                PlayingFragment tab2 = new PlayingFragment();
                return tab2;
            case 2:
                PlayingFragment tab3 = new PlayingFragment();
                return tab3;
            case 3:
                PlayingFragment tab4 = new PlayingFragment();
                return tab4;
            case 4:
                PlayingFragment tab5 = new PlayingFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}