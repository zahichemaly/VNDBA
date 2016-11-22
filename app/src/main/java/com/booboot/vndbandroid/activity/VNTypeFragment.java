package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vncards.RecyclerItemClickListener;
import com.booboot.vndbandroid.adapter.vncards.VNCardsListView;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.bean.vndbandroid.ListType;
import com.booboot.vndbandroid.bean.vndbandroid.VNlistItem;
import com.booboot.vndbandroid.bean.vndbandroid.VotelistItem;
import com.booboot.vndbandroid.bean.vndbandroid.WishlistItem;
import com.booboot.vndbandroid.factory.FastScrollerFactory;
import com.booboot.vndbandroid.factory.VNCardFactory;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Utils;

import java.util.ArrayList;

/**
 * Created by od on 09/03/2016.
 */
public class VNTypeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public final static String TAB_VALUE_ARG = "STATUS";
    public final static String VN_ARG = "VN";

    public static boolean refreshOnInit;
    private VNCardsListView materialListView;
    private SwipeRefreshLayout refreshLayout;
    private MainActivity activity;

    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vn_card_list, container, false);

        int tabValue = getArguments().getInt(TAB_VALUE_ARG);
        type = getArguments().getInt(VNListFragment.LIST_TYPE_ARG);

        materialListView = (VNCardsListView) rootView.findViewById(R.id.materialListView);
        VNCardFactory.setupList(getActivity(), materialListView);

        switch (type) {
            case ListType.VNLIST:
                for (final VNlistItem vn : new ArrayList<>(Cache.vnlist.values())) {
                    if (Cache.vns.get(vn.getVn()) == null) Cache.vnlist.remove(vn.getVn());
                    if (vn.getStatus() != tabValue || Cache.vns.get(vn.getVn()) == null) continue;
                    VNCardFactory.buildCard(getActivity(), Cache.vns.get(vn.getVn()), materialListView, false, false, false, false, false);
                }
                break;

            case ListType.VOTELIST:
                for (final VotelistItem vn : new ArrayList<>(Cache.votelist.values())) {
                    if (Cache.vns.get(vn.getVn()) == null) Cache.votelist.remove(vn.getVn());
                    if (type == ListType.VOTELIST && vn.getVote() / 10 != tabValue && vn.getVote() / 10 != tabValue - 1 || Cache.vns.get(vn.getVn()) == null)
                        continue;
                    VNCardFactory.buildCard(getActivity(), Cache.vns.get(vn.getVn()), materialListView, false, false, false, false, false);
                }
                break;

            case ListType.WISHLIST:
                for (final WishlistItem vn : new ArrayList<>(Cache.wishlist.values())) {
                    if (Cache.vns.get(vn.getVn()) == null) Cache.wishlist.remove(vn.getVn());
                    if (type == ListType.WISHLIST && vn.getPriority() != tabValue || Cache.vns.get(vn.getVn()) == null) continue;
                    VNCardFactory.buildCard(getActivity(), Cache.vns.get(vn.getVn()), materialListView, false, false, false, false, false);
                }
                break;
        }

        materialListView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull CardView cardView, int position) {
                Cache.openVNDetails(getActivity(), (int) cardView.getTag());
            }
        }));

        initFilter();

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Utils.getThemeColor(activity, R.attr.colorAccent));
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (refreshOnInit) {
                    refreshOnInit = false;
                    refreshLayout.setRefreshing(true);
                    onRefresh();
                }
            }
        });

        FastScrollerFactory.get(getActivity(), rootView, materialListView, refreshLayout);

        return rootView;
    }

    private void initFilter() {
        activity = ((MainActivity) getActivity());

        activity.addActiveFragment(this);
        if (activity.getSearchView() != null)
            filter(activity.getSearchView().getQuery());
    }

    public void filter(CharSequence search) {
        materialListView.getAdapter().getFilter().filter(search);
    }

    @Override
    public void onDestroy() {
        activity.removeActiveFragment(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        Cache.loadData(getActivity(), new Callback() {
            @Override
            protected void config() {
                if (Cache.shouldRefreshView && MainActivity.instance != null && !MainActivity.instance.isDestroyed()) {
                    MainActivity.instance.refreshVnlistFragment();
                }
                refreshLayout.setRefreshing(false);
            }
        }, new Callback() {
            @Override
            protected void config() {
                if (Cache.pipeliningError) return;
                Cache.pipeliningError = true;
                Callback.showToast(getActivity(), message);
                refreshLayout.setRefreshing(false);
                if (countDownLatch != null) countDownLatch.countDown();
            }
        });
    }
}
