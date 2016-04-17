package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.materiallistview.MaterialListView;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.ListType;
import com.booboot.vndbandroid.api.bean.Priority;
import com.booboot.vndbandroid.api.bean.Status;
import com.booboot.vndbandroid.api.bean.Vote;
import com.booboot.vndbandroid.db.DB;
import com.booboot.vndbandroid.util.Callback;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by od on 09/03/2016.
 */
public class VNTypeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public final static String TAB_VALUE_ARG = "STATUS";
    public final static String VN_ARG = "VN";

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

            Date released;
            StringBuilder subtitle = new StringBuilder(), description = new StringBuilder();
            try {
                released = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(vn.getReleased());
                subtitle.append(new SimpleDateFormat("yyyy", Locale.US).format(released));
            } catch (ParseException e) {
            }

            subtitle.append(" • ").append(vn.getLengthString());

            if (DB.vnlist.get(vn.getId()) != null)
                description.append(Status.toString(DB.vnlist.get(vn.getId()).getStatus())).append("\n");
            else description.append("Not on your VN list\n");
            if (DB.wishlist.get(vn.getId()) != null)
                description.append(Priority.toString(DB.wishlist.get(vn.getId()).getPriority())).append("\n");
            else description.append("Not on your wishlist\n");
            if (DB.votelist.get(vn.getId()) != null) {
                int vote = DB.votelist.get(vn.getId()).getVote() / 10;
                description.append(vote).append(" (").append(Vote.getName(vote)).append(")\n");
            } else description.append("Not voted yet\n");

            Card card = new Card.Builder(getActivity())
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.vn_card_layout)
                    .setTitle(vn.getTitle())
                    .setSubtitle(subtitle.toString())
                    .setSubtitleColor(Color.GRAY)
                    .setTitleGravity(Gravity.CENTER)
                    .setSubtitleGravity(Gravity.CENTER)
                    .setDescription(description.toString())
                    .setDescriptionGravity(Gravity.CENTER)
                    // .setDescriptionColor(getActivity().getResources().getColor(R.color.dark_blue, getActivity().getTheme()))
                    .setDescriptionColor(MainActivity.getThemeColor(getActivity(), R.attr.colorPrimaryDark))
                    .setDrawable(vn.getImage())
                    .endConfig().build();
            card.setTag(vn);

            materialListView.getAdapter().add(card);
            materialListView.scrollToPosition(0);
        }

        materialListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(Card card, int position) {
                Intent intent = new Intent(getActivity(), VNDetailsActivity.class);
                intent.putExtra(VN_ARG, (Item) card.getTag());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Card card, int position) {
                Log.d("LONG_CLICK", card.getProvider().getTitle());
            }
        });

        initFilter();

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        return rootView;
    }

    public LinkedHashMap<Integer, Item> getList() {
        if (type == ListType.VNLIST) return DB.vnlist;
        if (type == ListType.VOTELIST) return DB.votelist;
        if (type == ListType.WISHLIST) return DB.wishlist;
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
        DB.loadData(getActivity(), new Callback() {
            @Override
            protected void config() {
                int currentPage = VNListFragment.currentPage;
                activity.goToFragment(MainActivity.selectedItem);
                VNListFragment.currentPage = currentPage;

                refreshLayout.setRefreshing(false);
            }
        });
    }
}
