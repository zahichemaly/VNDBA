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
import com.booboot.vndbandroid.model.vndbandroid.Priority;
import com.booboot.vndbandroid.model.vndbandroid.Status;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 13/03/2016.
 */
public class VNListFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    public final static String LIST_TYPE_ARG = "LIST_TYPE";
    public final static int VNLIST = 1;
    public final static int VOTELIST = 2;
    public final static int WISHLIST = 3;

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
            case VNLIST:
                Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_visual_novel_list));
                int[] statusCount = Cache.getStatusCount();
                tabLayout.addTab(tabLayout.newTab().setText("Playing (" + statusCount[Status.PLAYING] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Finished (" + statusCount[Status.FINISHED] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Stalled (" + statusCount[Status.STALLED] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Dropped (" + statusCount[Status.DROPPED] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Unknown (" + statusCount[Status.UNKNOWN] + ")"));
                break;

            case VOTELIST:
                Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_votes));
                int[] voteCount = Cache.getVoteCount();
                tabLayout.addTab(tabLayout.newTab().setText("10 - 9 (" + voteCount[0] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("8 - 7 (" + voteCount[1] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("6 - 5 (" + voteCount[2] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("4 - 3 (" + voteCount[3] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("2 - 1 (" + voteCount[4] + ")"));
                break;

            case WISHLIST:
                Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.my_wishlist));
                int[] wishCount = Cache.getWishCount();
                tabLayout.addTab(tabLayout.newTab().setText("High (" + wishCount[Priority.HIGH] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Medium (" + wishCount[Priority.MEDIUM] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Low (" + wishCount[Priority.LOW] + ")"));
                tabLayout.addTab(tabLayout.newTab().setText("Blacklist (" + wishCount[Priority.BLACKLIST] + ")"));
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
            case VNLIST:
                if (tabLayout.getTabCount() < 5) return;
                int[] statusCount = Cache.getStatusCount();
                tabLayout.getTabAt(0).setText("Playing (" + statusCount[Status.PLAYING] + ")");
                tabLayout.getTabAt(1).setText("Finished (" + statusCount[Status.FINISHED] + ")");
                tabLayout.getTabAt(2).setText("Stalled (" + statusCount[Status.STALLED] + ")");
                tabLayout.getTabAt(3).setText("Dropped (" + statusCount[Status.DROPPED] + ")");
                tabLayout.getTabAt(4).setText("Unknown (" + statusCount[Status.UNKNOWN] + ")");
                break;

            case VOTELIST:
                if (tabLayout.getTabCount() < 5) return;
                int[] voteCount = Cache.getVoteCount();
                tabLayout.getTabAt(0).setText("10 - 9 (" + voteCount[0] + ")");
                tabLayout.getTabAt(1).setText("8 - 7 (" + voteCount[1] + ")");
                tabLayout.getTabAt(2).setText("6 - 5 (" + voteCount[2] + ")");
                tabLayout.getTabAt(3).setText("4 - 3 (" + voteCount[3] + ")");
                tabLayout.getTabAt(4).setText("2 - 1 (" + voteCount[4] + ")");
                break;

            case WISHLIST:
                if (tabLayout.getTabCount() < 4) return;
                int[] wishCount = Cache.getWishCount();
                tabLayout.getTabAt(0).setText("High (" + wishCount[Priority.HIGH] + ")");
                tabLayout.getTabAt(1).setText("Medium (" + wishCount[Priority.MEDIUM] + ")");
                tabLayout.getTabAt(2).setText("Low (" + wishCount[Priority.LOW] + ")");
                tabLayout.getTabAt(3).setText("Blacklist (" + wishCount[Priority.BLACKLIST] + ")");
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
