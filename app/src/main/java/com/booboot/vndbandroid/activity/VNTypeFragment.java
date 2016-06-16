package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.ListType;
import com.booboot.vndbandroid.factory.FastScrollerFactory;
import com.booboot.vndbandroid.factory.VNCardFactory;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.Utils;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;

import java.util.LinkedHashMap;

/**
 * Created by od on 09/03/2016.
 */
public class VNTypeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public final static String TAB_VALUE_ARG = "STATUS";
    public final static String VN_ARG = "VN";

    public static boolean refreshOnInit;
    private MaterialListView materialListView;
    private SwipeRefreshLayout refreshLayout;
    private MainActivity activity;

    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vn_card_list_layout, container, false);

        int tabValue = getArguments().getInt(TAB_VALUE_ARG);
        type = getArguments().getInt(VNListFragment.LIST_TYPE_ARG);

        materialListView = (MaterialListView) rootView.findViewById(R.id.materialListView);

        for (final Item vn : getList().values()) {
            if (type == ListType.VNLIST && vn.getStatus() != tabValue) continue;
            if (type == ListType.VOTELIST && vn.getVote() / 10 != tabValue && vn.getVote() / 10 != tabValue - 1)
                continue;
            if (type == ListType.WISHLIST && vn.getPriority() != tabValue) continue;

            VNCardFactory.buildCard(getActivity(), vn, materialListView, false, false, false, false, false);
        }

        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(Card card, int position) {
                Intent intent = new Intent(getActivity(), VNDetailsActivity.class);
                intent.putExtra(VN_ARG, (Item) card.getTag());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }

            @Override
            public void onItemLongClick(Card card, int position) {
                Log.d("LONG_CLICK", card.getProvider().getTitle());
            }
        });

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

    public LinkedHashMap<Integer, Item> getList() {
        if (type == ListType.VNLIST) return Cache.vnlist;
        if (type == ListType.VOTELIST) return Cache.votelist;
        if (type == ListType.WISHLIST) return Cache.wishlist;
        return null;
    }

    private void initFilter() {
        activity = ((MainActivity) getActivity());

        activity.addActiveFragment(this);
        if (activity.getSearchView() != null)
            materialListView.getAdapter().getFilter().filter(activity.getSearchView().getQuery());
    }

    public MaterialListView getMaterialListView() {
        return materialListView;
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
                if (Cache.shouldRefreshView) {
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
