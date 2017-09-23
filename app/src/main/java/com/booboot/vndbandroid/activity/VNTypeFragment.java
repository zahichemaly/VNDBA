package com.booboot.vndbandroid.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.vncards.RecyclerItemClickListener;
import com.booboot.vndbandroid.adapter.vncards.VNCardsListView;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.factory.FastScrollerFactory;
import com.booboot.vndbandroid.factory.VNCardFactory;
import com.booboot.vndbandroid.model.vndbandroid.VNlistItem;
import com.booboot.vndbandroid.model.vndbandroid.VotelistItem;
import com.booboot.vndbandroid.model.vndbandroid.WishlistItem;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Utils;

import java.util.ArrayList;

import butterknife.BindView;

public class VNTypeFragment extends VNDBFragment implements SwipeRefreshLayout.OnRefreshListener {
    public final static String TAB_VALUE_ARG = "STATUS";
    public final static String VN_ARG = "VN";

    @BindView(R.id.materialListView)
    protected VNCardsListView materialListView;

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout refreshLayout;

    public static boolean refreshOnInit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = R.layout.vn_card_list;
        super.onCreateView(inflater, container, savedInstanceState);

        int tabValue = getArguments().getInt(TAB_VALUE_ARG);
        int type = getArguments().getInt(VNListFragment.LIST_TYPE_ARG);
        VNCardFactory.setupList(getActivity(), materialListView);

        switch (type) {
            case VNListFragment.VNLIST:
                for (final VNlistItem vn : new ArrayList<>(Cache.vnlist.values())) {
                    if (Cache.vns.get(vn.getVn()) == null) Cache.vnlist.remove(vn.getVn());
                    if (vn.getStatus() != tabValue || Cache.vns.get(vn.getVn()) == null) continue;
                    VNCardFactory.buildCard(getActivity(), Cache.vns.get(vn.getVn()), materialListView, false, false, false, false, false);
                }
                break;

            case VNListFragment.VOTELIST:
                for (final VotelistItem vn : new ArrayList<>(Cache.votelist.values())) {
                    if (Cache.vns.get(vn.getVn()) == null) Cache.votelist.remove(vn.getVn());
                    if (vn.getVote() / 10 != tabValue && vn.getVote() / 10 != tabValue - 1 || Cache.vns.get(vn.getVn()) == null)
                        continue;
                    VNCardFactory.buildCard(getActivity(), Cache.vns.get(vn.getVn()), materialListView, false, false, false, false, false);
                }
                break;

            case VNListFragment.WISHLIST:
                for (final WishlistItem vn : new ArrayList<>(Cache.wishlist.values())) {
                    if (Cache.vns.get(vn.getVn()) == null) Cache.wishlist.remove(vn.getVn());
                    if (vn.getPriority() != tabValue || Cache.vns.get(vn.getVn()) == null) continue;
                    VNCardFactory.buildCard(getActivity(), Cache.vns.get(vn.getVn()), materialListView, false, false, false, false, false);
                }
                break;
        }

        materialListView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardView cardView, int position) {
                Cache.openVNDetails(getActivity(), (int) cardView.getTag());
            }
        }));

        initFilter();

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Utils.getThemeColor(getActivity(), R.attr.colorAccent));
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
        MainActivity activity = ((MainActivity) getActivity());

        if (activity.getSearchView() != null)
            filter(activity.getSearchView().getQuery());
    }

    public void filter(CharSequence search) {
        materialListView.getAdapter().getFilter().filter(search);
    }

    @Override
    public void onRefresh() {
        Cache.loadData(getActivity(), new Callback() {
            @Override
            protected void config() {
                if (Cache.shouldRefreshView) {
                    if (getActivity() instanceof MainActivity && !getActivity().isDestroyed()) {
                        ((MainActivity) getActivity()).refreshVnlistFragment();
                    }
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
                if (Cache.countDownLatch != null) Cache.countDownLatch.countDown();
            }
        });
    }
}
