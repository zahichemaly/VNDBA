package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.content.Intent;
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
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.api.bean.Links;
import com.booboot.vndbandroid.util.Utils;

/**
 * Created by od on 11/06/2016.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
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
        TextView myVNDBProfile = (TextView) rootView.findViewById(R.id.myVNDBProfile);
        TextView aboutDescription = (TextView) rootView.findViewById(R.id.aboutDescription);

        floatingSearchButton.setVisibility(View.GONE);
        Utils.setButtonColor(getActivity(), feedbackButton);
        Utils.setButtonColor(getActivity(), githubButton);
        Utils.setTextViewLink(getActivity(), myVNDBProfile, Links.MY_VNDB_PROFILE, 0, myVNDBProfile.getText().toString().length());
        int linkStart = aboutDescription.getText().toString().indexOf(Links.VNDB);
        Utils.setTextViewLink(getActivity(), aboutDescription, Links.VNDB, linkStart, linkStart + Links.VNDB.length());

        githubButton.setOnClickListener(this);
        feedbackButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedbackButton:
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;

            case R.id.githubButton:
                Utils.openURL(getActivity(), Links.GITHUB);
                break;
        }
    }
}
