package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.doublelist.DoubleListAdapter;
import com.booboot.vndbandroid.adapter.doublelist.DoubleListElement;
import com.booboot.vndbandroid.db.Cache;
import com.booboot.vndbandroid.util.Callback;

/**
 * Created by od on 13/03/2016.
 */
public class DatabaseStatisticsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.db_stats, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        refresh(false);

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        return rootView;
    }

    public void refresh(boolean forceRefresh) {
        Cache.loadStats(getActivity(), new Callback() {
            @Override
            protected void config() {
                DoubleListElement[] elements = new DoubleListElement[]{
                        new DoubleListElement("Visual Novels", Cache.dbstats.getVn() + "", false),
                        new DoubleListElement("Releases", Cache.dbstats.getReleases() + "", false),
                        new DoubleListElement("Producers", Cache.dbstats.getProducers() + "", false),
                        new DoubleListElement("Characters", Cache.dbstats.getChars() + "", false),
                        new DoubleListElement("VN Tags", Cache.dbstats.getTags() + "", false),
                        new DoubleListElement("Character Traits", Cache.dbstats.getTraits() + "", false),
                        new DoubleListElement("Users", Cache.dbstats.getUsers() + "", false),
                        new DoubleListElement("Threads", Cache.dbstats.getThreads() + "", false),
                        new DoubleListElement("Posts", Cache.dbstats.getPosts() + "", false)
                };

                listView.setAdapter(new DoubleListAdapter(DatabaseStatisticsFragment.this.getActivity(), elements));
                refreshLayout.setRefreshing(false);
            }
        }, forceRefresh);
    }

    @Override
    public void onRefresh() {
        refresh(true);
    }
}
