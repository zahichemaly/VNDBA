package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.tabs.VNTabsAdapter;
import com.booboot.vndbandroid.api.bean.ListType;

/**
 * Created by od on 13/03/2016.
 */
public class VNListFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    public final static String LIST_TYPE_ARG = "LIST_TYPE";
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.vn_list, container, false);

        final TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);
        int type = getArguments().getInt(LIST_TYPE_ARG);
        switch (type) {
            case ListType.VNLIST:
                tabLayout.addTab(tabLayout.newTab().setText("Playing"));
                tabLayout.addTab(tabLayout.newTab().setText("Finished"));
                tabLayout.addTab(tabLayout.newTab().setText("Stalled"));
                tabLayout.addTab(tabLayout.newTab().setText("Dropped"));
                tabLayout.addTab(tabLayout.newTab().setText("Unknown"));
                break;

            case ListType.VOTELIST:
                tabLayout.addTab(tabLayout.newTab().setText("10 - 9"));
                tabLayout.addTab(tabLayout.newTab().setText("8 - 7"));
                tabLayout.addTab(tabLayout.newTab().setText("6 - 5"));
                tabLayout.addTab(tabLayout.newTab().setText("4 - 3"));
                tabLayout.addTab(tabLayout.newTab().setText("2 - 1"));
                break;

            case ListType.WISHLIST:
                tabLayout.addTab(tabLayout.newTab().setText("High"));
                tabLayout.addTab(tabLayout.newTab().setText("Medium"));
                tabLayout.addTab(tabLayout.newTab().setText("Low"));
                tabLayout.addTab(tabLayout.newTab().setText("Blacklist"));
                break;
        }

        viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        viewPager.setAdapter(new VNTabsAdapter(getFragmentManager(), tabLayout.getTabCount(), type));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

        return inflatedView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
