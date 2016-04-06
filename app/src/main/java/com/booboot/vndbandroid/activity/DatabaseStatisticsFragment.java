package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.adapter.dbstats.DatabaseStatisticsAdapter;
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
                String[] leftText = new String[]{
                        "Visual Novels",
                        "Releases",
                        "Producers",
                        "Characters",
                        "VN Tags",
                        "Character Traits",
                        "Users",
                        "Threads",
                        "Posts"
                };
                String[] rightText = new String[]{
                        DB.dbstats.getVn() + "",
                        DB.dbstats.getReleases() + "",
                        DB.dbstats.getProducers() + "",
                        DB.dbstats.getChars() + "",
                        DB.dbstats.getTags() + "",
                        DB.dbstats.getTraits() + "",
                        DB.dbstats.getUsers() + "",
                        DB.dbstats.getThreads() + "",
                        DB.dbstats.getPosts() + ""
                };

                listView.setAdapter(new DatabaseStatisticsAdapter(DatabaseStatisticsFragment.this.getActivity(), leftText, rightText));
                refreshLayout.setRefreshing(false);
            }
        }, forceRefresh);
    }

    @Override
    public void onRefresh() {
        refresh(true);
    }
}
