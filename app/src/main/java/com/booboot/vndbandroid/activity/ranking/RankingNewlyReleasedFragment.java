package com.booboot.vndbandroid.activity.ranking;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.bean.Options;
import com.booboot.vndbandroid.factory.ProgressiveResultLoader;
import com.booboot.vndbandroid.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RankingNewlyReleasedFragment extends Fragment {
    private static ProgressiveResultLoader progressiveResultLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vn_card_list_layout, container, false);
        Utils.setTitle(getActivity(), getActivity().getResources().getString(R.string.newly_released));

        if (progressiveResultLoader == null) {
            progressiveResultLoader = new ProgressiveResultLoader();
            progressiveResultLoader.setActivity(getActivity());
            progressiveResultLoader.setRootView(rootView);
            progressiveResultLoader.setOptions(Options.create(1, 25, "released", true, false, false, 1));
            progressiveResultLoader.setShowFullDate(true);
            progressiveResultLoader.setShowRank(true);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
            progressiveResultLoader.setFilters("(released <= \"" + currentDate + "\")");
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