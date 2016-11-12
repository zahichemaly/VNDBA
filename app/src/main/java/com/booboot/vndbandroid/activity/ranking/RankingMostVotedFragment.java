package com.booboot.vndbandroid.activity.ranking;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.bean.vndb.Options;
import com.booboot.vndbandroid.factory.ProgressiveResultLoader;
import com.booboot.vndbandroid.util.Utils;

public class RankingMostVotedFragment extends Fragment {
    public static ProgressiveResultLoader progressiveResultLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vn_card_list_layout, container, false);
        Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.most_voted));

        if (progressiveResultLoader == null) {
            progressiveResultLoader = new ProgressiveResultLoader();
            progressiveResultLoader.setActivity(getActivity());
            progressiveResultLoader.setRootView(rootView);
            progressiveResultLoader.setOptions(Options.create(1, 25, "votecount", true, false, false, 1));
            progressiveResultLoader.setShowRank(true);
            progressiveResultLoader.setShowVoteCount(true);
            progressiveResultLoader.setFilters("(id > 1)");
            progressiveResultLoader.init();
            progressiveResultLoader.loadResults(true);
        } else {
            progressiveResultLoader.setActivity(getActivity());
            progressiveResultLoader.setRootView(rootView);
            progressiveResultLoader.initFromExisting();
        }

        return rootView;
    }
}