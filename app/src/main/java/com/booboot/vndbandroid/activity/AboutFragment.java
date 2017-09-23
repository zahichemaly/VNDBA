package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
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
import com.booboot.vndbandroid.util.image.Pixels;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutFragment extends VNDBFragment {
    @BindView(R.id.scrollView)
    protected NestedScrollView scrollView;

    @BindView(R.id.thanksTextView)
    protected TextView thanksTextView;

    @BindView(R.id.appVersion)
    protected TextView appVersion;

    @BindView(R.id.feedbackButton)
    protected Button feedbackButton;

    @BindView(R.id.githubButton)
    protected Button githubButton;

    @BindView(R.id.aboutDescription)
    protected TextView aboutDescription;

    @BindView(R.id.vnstatButton)
    protected Button vnstatButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = R.layout.about;
        super.onCreateView(inflater, container, savedInstanceState);

        Utils.setTitle(getActivity(), "About");
        appVersion.setText(BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        Utils.setButtonColor(getActivity(), feedbackButton);
        Utils.setButtonColor(getActivity(), githubButton);
        Utils.setButtonColor(getActivity(), vnstatButton);
        int linkStart = aboutDescription.getText().toString().indexOf(Links.VNDB);
        Utils.setTextViewLink(getActivity(), aboutDescription, Links.VNDB, linkStart, linkStart + Links.VNDB.length());

        thanksTextView.setText(Html.fromHtml(Utils.convertLink(getActivity(), thanksTextView.getText().toString())));
        thanksTextView.setMovementMethod(LinkMovementMethod.getInstance());

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = v.getChildAt(v.getChildCount() - 1);
                int diff = (view.getBottom() - (v.getHeight() + v.getScrollY()));
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).toggleFloatingSearchButton(scrollY <= oldScrollY || diff > Pixels.px(35, v.getContext()));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem actionFilter = menu.findItem(R.id.action_filter);
        if (actionFilter == null) return;
        actionFilter.setVisible(false);
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

    @OnClick(R.id.feedbackButton)
    protected void feedbackButtonClicked() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Links.EMAIL, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[VNDB Android] Feedback @ " + new Date().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n" + Utils.getDeviceInfo(getActivity()));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, Links.EMAIL); // Android 4.3 fix
        startActivity(Intent.createChooser(emailIntent, "Send a feedback with..."));
        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.githubButton)
    protected void githubButtonClicked() {
        Utils.openURL(getActivity(), Links.GITHUB);
    }

    @OnClick(R.id.vnstatButton)
    protected void vnstatButtonClicked() {
        Utils.openURL(getActivity(), Links.VNSTAT);
    }
}
