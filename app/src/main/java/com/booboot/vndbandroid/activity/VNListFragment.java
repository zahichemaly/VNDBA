package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.tabs.VNTabsAdapter;
import com.booboot.vndbandroid.api.bean.ListType;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.db.DB;

/**
 * Created by od on 13/03/2016.
 */
public class VNListFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    public final static String LIST_TYPE_ARG = "LIST_TYPE";
    private ViewPager viewPager;
    public static int currentPage = -1;
    private TabLayout tabLayout;
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.vn_list, container, false);

        tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);
        type = getArguments().getInt(LIST_TYPE_ARG);
        switch (type) {
            case ListType.VNLIST:
                tabLayout.addTab(tabLayout.newTab().setText("Playing (" + DB.getStatusNumber(Status.PLAYING) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Finished (" + DB.getStatusNumber(Status.FINISHED) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Stalled (" + DB.getStatusNumber(Status.STALLED) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Dropped (" + DB.getStatusNumber(Status.DROPPED) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Unknown (" + DB.getStatusNumber(Status.UNKNOWN) + ")"));
                break;

            case ListType.VOTELIST:
                tabLayout.addTab(tabLayout.newTab().setText("10 - 9 (" + DB.getVoteNumber(10) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("8 - 7 (" + DB.getVoteNumber(8) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("6 - 5 (" + DB.getVoteNumber(6) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("4 - 3 (" + DB.getVoteNumber(4) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("2 - 1 (" + DB.getVoteNumber(2) + ")"));
                break;

            case ListType.WISHLIST:
                tabLayout.addTab(tabLayout.newTab().setText("High (" + DB.getWishNumber(Priority.HIGH) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Medium (" + DB.getWishNumber(Priority.MEDIUM) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Low (" + DB.getWishNumber(Priority.LOW) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Blacklist (" + DB.getWishNumber(Priority.BLACKLIST) + ")"));
                break;
        }

        viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        viewPager.setAdapter(new VNTabsAdapter(getFragmentManager(), tabLayout.getTabCount(), type));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

        if (currentPage >= 0) viewPager.setCurrentItem(currentPage);
        else currentPage = 0;

        return inflatedView;
    }

    /**
     * Refresh the view pager with a little trick : setting the adapter to null then back to its value.
     * Don't forget to remember the previous page.
     */
    public void refresh() {
        int currentPage = viewPager.getCurrentItem();
        PagerAdapter adapter = viewPager.getAdapter();
        viewPager.setAdapter(null);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPage);
    }

    @SuppressWarnings("ConstantConditions")
    /**
     * @SuppressWarnings because Android Studio keeps saying tabLayou.getTabAt(i) may be null although we checked that with tabLayout.getTabCount() < x
     */
    public void refreshTitles() {
        switch (type) {
            case ListType.VNLIST:
                if (tabLayout.getTabCount() < 5) return;
                tabLayout.getTabAt(0).setText("Playing (" + DB.getStatusNumber(Status.PLAYING) + ")");
                tabLayout.getTabAt(1).setText("Finished (" + DB.getStatusNumber(Status.FINISHED) + ")");
                tabLayout.getTabAt(2).setText("Stalled (" + DB.getStatusNumber(Status.STALLED) + ")");
                tabLayout.getTabAt(3).setText("Dropped (" + DB.getStatusNumber(Status.DROPPED) + ")");
                tabLayout.getTabAt(4).setText("Unknown (" + DB.getStatusNumber(Status.UNKNOWN) + ")");
                break;

            case ListType.VOTELIST:
                if (tabLayout.getTabCount() < 5) return;
                tabLayout.getTabAt(0).setText("10 - 9 (" + DB.getVoteNumber(10) + ")");
                tabLayout.getTabAt(1).setText("8 - 7 (" + DB.getVoteNumber(8) + ")");
                tabLayout.getTabAt(2).setText("6 - 5 (" + DB.getVoteNumber(6) + ")");
                tabLayout.getTabAt(3).setText("4 - 3 (" + DB.getVoteNumber(4) + ")");
                tabLayout.getTabAt(4).setText("2 - 1 (" + DB.getVoteNumber(2) + ")");
                break;

            case ListType.WISHLIST:
                if (tabLayout.getTabCount() < 4) return;
                tabLayout.getTabAt(0).setText("High (" + DB.getWishNumber(Priority.HIGH) + ")");
                tabLayout.getTabAt(1).setText("Medium (" + DB.getWishNumber(Priority.MEDIUM) + ")");
                tabLayout.getTabAt(2).setText("Low (" + DB.getWishNumber(Priority.LOW) + ")");
                tabLayout.getTabAt(3).setText("Blacklist (" + DB.getWishNumber(Priority.BLACKLIST) + ")");
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        currentPage = tab.getPosition();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
