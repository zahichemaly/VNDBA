package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.booboot.vndbandroid.BuildConfig;
import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 11/06/2016.
 */
public class AboutFragment extends Fragment {
    private FloatingActionButton floatingSearchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about, container, false);

        Utils.setTitle(getActivity(), "About");
        TextView appVersion = (TextView) rootView.findViewById(R.id.appVersion);
        appVersion.setText(BuildConfig.VERSION_NAME + " / " + BuildConfig.VERSION_CODE);

        floatingSearchButton = (FloatingActionButton) getActivity().findViewById(R.id.floatingSearchButton);
        Button feedbackButton = (Button) rootView.findViewById(R.id.feedbackButton);
        Button githubButton = (Button) rootView.findViewById(R.id.githubButton);

        floatingSearchButton.setVisibility(View.GONE);
        Utils.setButtonColor(getActivity(), feedbackButton);
        Utils.setButtonColor(getActivity(), githubButton);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_filter).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
    }

    @Override
    public void onPause() {
        floatingSearchButton.setVisibility(View.VISIBLE);
        super.onPause();
    }

    @Override
    public void onResume() {
        floatingSearchButton.setVisibility(View.GONE);
        super.onResume();
    }
}
