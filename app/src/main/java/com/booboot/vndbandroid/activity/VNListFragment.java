package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.tabs.VNTabsAdapter;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.bean.vndbandroid.ListType;
import com.booboot.vndbandroid.bean.vndbandroid.Priority;
import com.booboot.vndbandroid.bean.vndbandroid.Status;
import com.booboot.vndbandroid.util.Utils;

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
                Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_visual_novel_list));
                tabLayout.addTab(tabLayout.newTab().setText("Playing (" + Cache.getStatusNumber(Status.PLAYING) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Finished (" + Cache.getStatusNumber(Status.FINISHED) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Stalled (" + Cache.getStatusNumber(Status.STALLED) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Dropped (" + Cache.getStatusNumber(Status.DROPPED) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Unknown (" + Cache.getStatusNumber(Status.UNKNOWN) + ")"));
                break;

            case ListType.VOTELIST:
                Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_votes));
                tabLayout.addTab(tabLayout.newTab().setText("10 - 9 (" + Cache.getVoteNumber(10) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("8 - 7 (" + Cache.getVoteNumber(8) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("6 - 5 (" + Cache.getVoteNumber(6) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("4 - 3 (" + Cache.getVoteNumber(4) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("2 - 1 (" + Cache.getVoteNumber(2) + ")"));
                break;

            case ListType.WISHLIST:
                Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_wishlist));
                tabLayout.addTab(tabLayout.newTab().setText("High (" + Cache.getWishNumber(Priority.HIGH) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Medium (" + Cache.getWishNumber(Priority.MEDIUM) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Low (" + Cache.getWishNumber(Priority.LOW) + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Blacklist (" + Cache.getWishNumber(Priority.BLACKLIST) + ")"));
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_filter).setVisible(true);
        menu.findItem(R.id.action_sort).setVisible(true);
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
        refreshTitles();
    }

    @SuppressWarnings("ConstantConditions")
    /**
     * @SuppressWarnings because Android Studio keeps saying tabLayou.getTabAt(i) may be null although we checked that with tabLayout.getTabCount() < x
     */
    public void refreshTitles() {
        switch (type) {
            case ListType.VNLIST:
                if (tabLayout.getTabCount() < 5) return;
                tabLayout.getTabAt(0).setText("Playing (" + Cache.getStatusNumber(Status.PLAYING) + ")");
                tabLayout.getTabAt(1).setText("Finished (" + Cache.getStatusNumber(Status.FINISHED) + ")");
                tabLayout.getTabAt(2).setText("Stalled (" + Cache.getStatusNumber(Status.STALLED) + ")");
                tabLayout.getTabAt(3).setText("Dropped (" + Cache.getStatusNumber(Status.DROPPED) + ")");
                tabLayout.getTabAt(4).setText("Unknown (" + Cache.getStatusNumber(Status.UNKNOWN) + ")");
                break;

            case ListType.VOTELIST:
                if (tabLayout.getTabCount() < 5) return;
                tabLayout.getTabAt(0).setText("10 - 9 (" + Cache.getVoteNumber(10) + ")");
                tabLayout.getTabAt(1).setText("8 - 7 (" + Cache.getVoteNumber(8) + ")");
                tabLayout.getTabAt(2).setText("6 - 5 (" + Cache.getVoteNumber(6) + ")");
                tabLayout.getTabAt(3).setText("4 - 3 (" + Cache.getVoteNumber(4) + ")");
                tabLayout.getTabAt(4).setText("2 - 1 (" + Cache.getVoteNumber(2) + ")");
                break;

            case ListType.WISHLIST:
                if (tabLayout.getTabCount() < 4) return;
                tabLayout.getTabAt(0).setText("High (" + Cache.getWishNumber(Priority.HIGH) + ")");
                tabLayout.getTabAt(1).setText("Medium (" + Cache.getWishNumber(Priority.MEDIUM) + ")");
                tabLayout.getTabAt(2).setText("Low (" + Cache.getWishNumber(Priority.LOW) + ")");
                tabLayout.getTabAt(3).setText("Blacklist (" + Cache.getWishNumber(Priority.BLACKLIST) + ")");
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
