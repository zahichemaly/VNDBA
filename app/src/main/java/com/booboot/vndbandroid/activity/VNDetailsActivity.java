package com.booboot.vndbandroid.activity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.util.Bitmap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class VNDetailsActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Item vn;
    private ImageButton image;
    private TextView title;
    private TextView subtitle;
    private TextView supportingText;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vn_details);

        vn = (Item) getIntent().getSerializableExtra(PlayingFragment.VN_ARG);
        initExpandableListView();

        image = (ImageButton) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        supportingText = (TextView) findViewById(R.id.supportingText);

        actionBar = getSupportActionBar();
        actionBar.setTitle(vn.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);

        new Thread() {
            public void run() {
                image.setImageDrawable(Bitmap.drawableFromUrl(vn.getImage()));
            }
        }.start();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(VNDetailsActivity.this);
                dialog.setContentView(R.layout.act_lightbox);
                dialog.setCancelable(true);

                final ImageView lightbox = (ImageView) dialog.findViewById(R.id.lightboxView);
                new Thread() {
                    public void run() {
                        lightbox.setImageDrawable(Bitmap.drawableFromUrl(vn.getImage()));
                    }
                }.start();

                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                lightbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        title.setText(vn.getTitle());
        subtitle.setText(vn.getOriginal());
        supportingText.setText(vn.getReleased());

    }

    private void initExpandableListView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = getExpandableListData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.addHeaderView(getLayoutInflater().inflate(R.layout.vn_details_header, null));
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public LinkedHashMap<String, List<String>> getExpandableListData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        List<String> description = new ArrayList<>();
        description.add(vn.getDescription());

        List<String> platforms = new ArrayList<>();
        for (String platform : vn.getPlatforms())
            platforms.add(platform);

        List<String> languages = new ArrayList<>();
        for (String language : vn.getLanguages())
            languages.add(language);

        expandableListDetail.put("Description", description);
        expandableListDetail.put("Platforms", platforms);
        expandableListDetail.put("Languages", languages);

        return expandableListDetail;
    }
}
