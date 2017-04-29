package com.booboot.vndbandroid.activity.ranking;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.factory.ProgressiveResultLoader;
import com.booboot.vndbandroid.model.vndb.Options;
import com.booboot.vndbandroid.model.vndbandroid.ProgressiveResultLoaderOptions;
import com.booboot.vndbandroid.util.Utils;

public class RankingNewlyAddedFragment extends Fragment {
    public static ProgressiveResultLoaderOptions options;
    private ProgressiveResultLoader progressiveResultLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vn_card_list, container, false);
        Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.newly_added));

        progressiveResultLoader = new ProgressiveResultLoader();
        progressiveResultLoader.setActivity(getActivity());
        progressiveResultLoader.setRootView(rootView);
        progressiveResultLoader.setOptions(Options.create(1, 25, "id", true, false, 1));
        progressiveResultLoader.setShowFullDate(true);
        progressiveResultLoader.setShowRank(true);
        progressiveResultLoader.setFilters("(id > 1)");
        progressiveResultLoader.setCallback(new ProgressiveResultLoader.ProgressiveResultLoaderCallback() {
            @Override
            public void onResultsLoaded() {
                options = ProgressiveResultLoaderOptions.build(progressiveResultLoader);
            }
        });

        if (options == null || !options.isComplete()) {
            options = null;
            progressiveResultLoader.init();
            progressiveResultLoader.loadResults(true);
        } else {
            progressiveResultLoader.initFromExisting(options);
        }

        return rootView;
    }
}