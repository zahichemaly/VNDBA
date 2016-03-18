package com.booboot.vndbandroid.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.bean.Item;
import com.booboot.vndbandroid.util.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class VNDetailsActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private Item vn;
    private ImageView cover;
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

        cover = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        supportingText = (TextView) findViewById(R.id.supportingText);

        actionBar = getSupportActionBar();
        actionBar.setTitle(vn.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);

        new Thread() {
            public void run() {
                cover.setImageDrawable(Bitmap.drawableFromUrl(vn.getImage()));
            }
        }.start();
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
