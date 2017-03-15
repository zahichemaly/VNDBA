package com.booboot.vndbandroid.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.booboot.vndbandroid.BuildConfig;
import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.model.vndb.Links;
import com.booboot.vndbandroid.util.Utils;

import java.util.Date;

/**
 * Created by od on 11/06/2016.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about, container, false);

        Utils.setTitle(getActivity(), "About");
        TextView appVersion = (TextView) rootView.findViewById(R.id.appVersion);
        appVersion.setText(BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        TextView thanksTextView = (TextView) rootView.findViewById(R.id.thanksTextView);
        Button feedbackButton = (Button) rootView.findViewById(R.id.feedbackButton);
        Button githubButton = (Button) rootView.findViewById(R.id.githubButton);
        Button vnstatButton = (Button) rootView.findViewById(R.id.vnstatButton);
        TextView aboutDescription = (TextView) rootView.findViewById(R.id.aboutDescription);

        Utils.setButtonColor(getActivity(), feedbackButton);
        Utils.setButtonColor(getActivity(), githubButton);
        Utils.setButtonColor(getActivity(), vnstatButton);
        int linkStart = aboutDescription.getText().toString().indexOf(Links.VNDB);
        Utils.setTextViewLink(getActivity(), aboutDescription, Links.VNDB, linkStart, linkStart + Links.VNDB.length());

        thanksTextView.setText(Html.fromHtml(Utils.convertLink(getActivity(), thanksTextView.getText().toString())));
        thanksTextView.setMovementMethod(LinkMovementMethod.getInstance());

        githubButton.setOnClickListener(this);
        feedbackButton.setOnClickListener(this);
        vnstatButton.setOnClickListener(this);

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
        menu.findItem(R.id.action_rate).setVisible(true);
        menu.findItem(R.id.action_share).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rate:
                Utils.openURL(getActivity(), Links.PLAY_STORE);
                break;

            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VNDB Android");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Links.PLAY_STORE);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedbackButton:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Links.EMAIL, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[VNDB Android] Feedback @ " + new Date().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n" + Utils.getDeviceInfo(getActivity()));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, Links.EMAIL); // Android 4.3 fix
                startActivity(Intent.createChooser(emailIntent, "Send a feedback with..."));
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;

            case R.id.githubButton:
                Utils.openURL(getActivity(), Links.GITHUB);
                break;

            case R.id.vnstatButton:
                Utils.openURL(getActivity(), Links.VNSTAT);
                break;
        }
    }
}
