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
import com.booboot.vndbandroid.db.DB;
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
        DB.loadStats(getActivity(), new Callback() {
            @Override
            protected void config() {
                DoubleListElement[] elements = new DoubleListElement[]{
                        new DoubleListElement("Visual Novels", DB.dbstats.getVn() + "", false),
                        new DoubleListElement("Releases", DB.dbstats.getReleases() + "", false),
                        new DoubleListElement("Producers", DB.dbstats.getProducers() + "", false),
                        new DoubleListElement("Characters", DB.dbstats.getChars() + "", false),
                        new DoubleListElement("VN Tags", DB.dbstats.getTags() + "", false),
                        new DoubleListElement("Character Traits", DB.dbstats.getTraits() + "", false),
                        new DoubleListElement("Users", DB.dbstats.getUsers() + "", false),
                        new DoubleListElement("Threads", DB.dbstats.getThreads() + "", false),
                        new DoubleListElement("Posts", DB.dbstats.getPosts() + "", false)
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
